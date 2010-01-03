/*
 * File Name: OracleAutoCreateSql.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-28
 * Grant: open source to everybody
 */
package com.china.center.annosql.adapter;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.annosql.AutoCreateSql;
import com.china.center.annosql.ErrorConstant;
import com.china.center.annosql.InnerBean;
import com.china.center.annosql.MYSqlException;
import com.china.center.annosql.tools.BaseTools;
import com.china.center.annosql.tools.BeanTools;
import com.china.center.annosql.wrap.AnnoSQLWrap;
import com.china.center.annotation.Entity;
import com.china.center.annotation.Join;
import com.china.center.annotation.Relationship;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.JoinType;


/**
 * 基本sql的自动生成实现
 * 
 * @author zhuzhu
 * @version 2007-9-28
 * @see
 * @since
 */
public abstract class BaseAutoCreateSql implements AutoCreateSql
{
    protected final Log _logger = LogFactory.getLog(getClass());

    protected Map<Class, AnnoSQLWrap> sqlBufferMap = new HashMap(MAX_BUFFER_SQL);

    /**
     * default constructor
     */
    public BaseAutoCreateSql()
    {}

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.AutoCreateSql#delSql(java.lang.Object)
     */
    public String delSql(Class claz)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        return this.delSql(claz, BeanTools.getIdColumn(claz));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.AutoCreateSql#delSql(java.lang.Object, java.lang.String)
     */
    public String delSql(Class claz, String columnName)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        if (columnName == null)
        {
            throwsLogger(ErrorConstant.MISSING_ID);
        }

        // 是实体表
        checkEntry(claz);

        StringBuffer buffer = new StringBuffer();

        boolean hasDelSql = false;

        if (sqlBufferMap.containsKey(claz))
        {
            AnnoSQLWrap wrap = sqlBufferMap.get(claz);

            if (wrap != null && !BaseTools.isNullOrNone(wrap.getDeleteSQL()))
            {
                buffer.append(wrap.getDeleteSQL());

                hasDelSql = true;
            }
        }

        if ( !hasDelSql)
        {
            String tableName = BeanTools.getTableName(claz);

            buffer.append("DELETE FROM ");

            buffer.append(tableName).append(" ");

            cacheDeleteSQL(claz, buffer);
        }

        buffer.append(" WHERE ").append(columnName).append(" = ?");

        String sql = buffer.toString();

        if (_logger.isDebugEnabled())
        {
            _logger.debug(sql);
        }

        return sql;
    }

    /**
     * @param claz
     * @param buffer
     */
    protected void cacheDeleteSQL(Class claz, StringBuffer buffer)
    {
        AnnoSQLWrap wrap = getWrap(claz);

        wrap.setDeleteSQL(buffer.toString());

        addSqlBufferMap(claz, wrap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.AutoCreateSql#insertSql(java.lang.Object)
     */
    public String insertSql(Class claz)
        throws MYSqlException
    {
        return insertSqlInner(claz, true);
    }

    /**
     * 构建插入语句(内部方法)
     * 
     * @param claz
     * @param processIdAutoIncrement
     *            是否处理自增类型
     * @return add sql
     * @throws MYSqlException
     */
    protected String insertSqlInner(Class claz, boolean processIdAutoIncrement)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        if (sqlBufferMap.containsKey(claz))
        {
            AnnoSQLWrap wrap = sqlBufferMap.get(claz);

            if (wrap != null && !BaseTools.isNullOrNone(wrap.getInsertSQL()))
            {
                return wrap.getInsertSQL();
            }
        }

        // 是实体表
        checkEntry(claz);

        String tableName = BeanTools.getTableName(claz);

        // 获得列名
        List<InnerBean> columns = BeanTools.getClassFieldsInsert(claz, processIdAutoIncrement);

        if (columns.size() == 0)
        {
            throwsLogger(ErrorConstant.COLUMN_EMPTY);
        }

        StringBuffer buffer = new StringBuffer("INSERT INTO ");
        StringBuffer endBuffer = new StringBuffer("VALUES (");

        buffer.append(tableName).append("(");

        String end1 = ", ";
        String end2 = "#, ";

        for (InnerBean filed : columns)
        {
            buffer.append(filed.getColumnName()).append(end1);

            endBuffer.append("#").append(filed.getFieldName()).append(end2);
        }

        buffer.delete(buffer.length() - end1.length(), buffer.length());

        endBuffer.delete(endBuffer.length() - end2.length() + 1, endBuffer.length());

        buffer.append(") ");
        endBuffer.append(')');

        String sql = buffer.append(endBuffer.toString()).toString();

        if (_logger.isDebugEnabled())
        {
            _logger.debug(sql);
        }

        cacheInsertSQL(claz, sql);

        return sql;
    }

    /**
     * cacheInsertSQL
     * 
     * @param claz
     * @param sql
     */
    protected void cacheInsertSQL(Class claz, String sql)
    {
        AnnoSQLWrap wrap = getWrap(claz);

        wrap.setInsertSQL(sql);

        addSqlBufferMap(claz, wrap);
    }

    /**
     * 根据主键和类型查询单体的自动sql
     * 
     * @param <T>
     * @param id
     * @param claz
     * @return
     * @throws MYSqlException
     */
    public String querySql(String columnName, Class<?> claz)
        throws MYSqlException
    {
        if (columnName == null)
        {
            throwsLogger(ErrorConstant.MISSING_ID);
        }

        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        String pfix = prefix(claz) + '.';

        return this.queryByCondtionSql("WHERE " + pfix + columnName + " = ? ", claz);

    }

    /**
     * 取得查询sql
     * 
     * @param claz
     * @return
     * @throws MYSqlException
     */
    public String querySql(Class<?> claz)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        String id = BeanTools.getIdColumn(claz);

        return this.querySql(id, claz);
    }

    protected boolean containQuerySql(Class claz)
    {
        if (sqlBufferMap.containsKey(claz))
        {
            AnnoSQLWrap wrap = sqlBufferMap.get(claz);

            if (wrap != null && !BaseTools.isNullOrNone(wrap.getQuerySQL()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * base
     */
    public String queryByCondtionSql(String condition, Class<?> claz)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        StringBuffer buffer = new StringBuffer();

        // process condition WHERE 1=1 and ...
        condition = condition.trim().substring("WHERE".length());

        if (containQuerySql(claz))
        {
            AnnoSQLWrap wrap = getWrap(claz);

            buffer.append(wrap.getQuerySQL());
        }
        else
        {
            // 是实体表
            checkEntry(claz);

            StringBuffer euqalBuffer = new StringBuffer();

            String tableName = BeanTools.getTableName(claz);

            buffer.append("select ");

            List<InnerBean> columns = BeanTools.getClassFieldsInner(claz);

            // 处理join
            String end = ", ";

            String pfix = prefix(claz);

            String pfix1 = pfix + '.';

            StringBuffer tableBuffer = new StringBuffer();

            Map<String, String> joinClass = new HashMap<String, String>();
            Map<String, String> joinClassAndField = new HashMap<String, String>();

            Map<String, StringBuffer> joinMap = new HashMap<String, StringBuffer>();

            for (InnerBean field : columns)
            {
                // 处理Relationship
                if (field.isRelationship())
                {
                    Relationship relationship = BeanTools.getRelationship(field.getField());

                    String fieldName = relationship.relationField();

                    // relationField
                    Field relationField = BeanTools.getFieldIgnoreCase(fieldName, claz);

                    if (relationField == null)
                    {
                        throwsLogger(ErrorConstant.JOIN_NULL, claz.getName() + "." + fieldName
                                                              + " is not exist in "
                                                              + claz.getName());
                    }

                    Join join = BeanTools.getJoin(relationField);

                    if (join == null)
                    {
                        throwsLogger(ErrorConstant.JOIN_NULL, claz.getName() + "."
                                                              + relationField.getName()
                                                              + " miss join");
                    }

                    // get tag class in join
                    Class tagClass = join.tagClass();

                    String tagTableName = BeanTools.getTableName(tagClass);

                    String key = join.alias();

                    if (BaseTools.isNullOrNone(key))
                    {
                        key = prefix(tagClass);
                    }

                    String tagPfix = key;

                    String jointagField = join.tagField();

                    if (jointagField == null || "".equals(jointagField.trim()))
                    {
                        jointagField = BeanTools.getIdFieldName(tagClass);
                    }

                    if (jointagField == null || "".equals(jointagField.trim()))
                    {
                        throwsLogger(ErrorConstant.JOIN_NULL, tagClass.getName() + " miss id");
                    }

                    Field ffx = BeanTools.getFieldIgnoreCase(jointagField, tagClass);

                    if (join.type() == JoinType.LEFT)
                    {
                        if ( !joinClass.containsKey(key))
                        {
                            joinMap.put(key, new StringBuffer());

                            // left join t_center_location t4 on (t1.locationId
                            // = t4.id and t1.kkkk = t4.bbbb)
                            joinMap.get(key).append(" LEFT OUTER JOIN ").append(tagTableName).append(
                                " ").append(tagPfix).append(" on (");
                        }

                        // 此字段是否关联
                        if ( !joinClassAndField.containsKey(key + '$' + jointagField))
                        {
                            joinMap.get(key).append(pfix1).append(
                                BeanTools.getColumnName(relationField)).append(" = ").append(
                                tagPfix + ".").append(BeanTools.getColumnName(ffx)).append(" ");
                        }
                    }
                    else if (join.type() == JoinType.EQUAL)
                    {
                        if ( !joinClass.containsKey(key))
                        {
                            tableBuffer.append(tagTableName).append(" ").append(tagPfix).append(
                                end);
                        }

                        if ( !joinClassAndField.containsKey(key + '$' + jointagField))
                        {
                            // where t1.locationId = t4.id
                            euqalBuffer.append(pfix1).append(
                                BeanTools.getColumnName(relationField)).append(" = ").append(
                                tagPfix + ".").append(BeanTools.getColumnName(ffx)).append(" and ");
                        }
                    }
                    else if (join.type() == JoinType.RIGHT)
                    {
                        // right
                        if ( !joinClass.containsKey(key))
                        {
                            joinMap.put(key, new StringBuffer());

                            // left join t_center_location t4 on (t1.locationId
                            // = t4.id and t1.kkkk = t4.bbbb)
                            joinMap.get(key).append(" RIGHT OUTER JOIN ").append(tagTableName).append(
                                " ").append(tagPfix).append(" on (");
                        }

                        // 此字段是否关联
                        if ( !joinClassAndField.containsKey(key + jointagField))
                        {
                            joinMap.get(key).append(pfix1).append(
                                BeanTools.getColumnName(relationField)).append(" = ").append(
                                tagPfix + ".").append(BeanTools.getColumnName(ffx)).append(" ");
                        }
                    }
                    else if (join.type() == JoinType.FULL)
                    {
                        // full join
                        if ( !joinClass.containsKey(key))
                        {
                            joinMap.put(key, new StringBuffer());

                            // left join t_center_location t4 on (t1.locationId
                            // = t4.id and t1.kkkk = t4.bbbb)
                            joinMap.get(key).append(" FULL OUTER JOIN ").append(tagTableName).append(
                                " ").append(tagPfix).append(" on (");
                        }

                        // 此字段是否关联
                        if ( !joinClassAndField.containsKey(key + jointagField))
                        {
                            joinMap.get(key).append(pfix1).append(
                                BeanTools.getColumnName(relationField)).append(" = ").append(
                                tagPfix + ".").append(BeanTools.getColumnName(ffx)).append(" ");
                        }
                    }
                    else
                    {
                        // 暂时无其他
                    }

                    // 存放 join的对象 字段
                    joinClass.put(key, tagClass.getName());

                    joinClassAndField.put(key + '$' + jointagField, jointagField);

                    String tagField = relationship.tagField();

                    if (tagField == null || "".equals(tagField.trim()))
                    {
                        tagField = field.getFieldName();
                    }

                    Field ff1 = BeanTools.getFieldIgnoreCase(tagField, tagClass);

                    if (ff1 == null)
                    {
                        throwsLogger(ErrorConstant.PARAMETER_NULL, tagClass.getName()
                                                                   + " miss field:" + tagField);
                    }

                    buffer.append(tagPfix + ".").append(BeanTools.getColumnName(ff1).toUpperCase()).append(
                        " as ").append(field.getFieldName()).append(end);
                }
                // process alias
                else if (field.isAlias())
                {
                    buffer.append(pfix1).append(field.getAliasField()).append(" as ").append(
                        field.getColumnName().toUpperCase()).append(end);
                }
                else
                {
                    buffer.append(pfix1).append(field.getColumnName().toUpperCase()).append(end);
                }
            }

            buffer.delete(buffer.length() - end.length(), buffer.length());

            if (tableBuffer.length() > 0)
            {
                tableBuffer.delete(tableBuffer.length() - end.length(), tableBuffer.length());

                buffer.append(" FROM ").append(tableName).append(" ").append(pfix).append(" ").append(
                    getMapSql(joinMap)).append(" , ").append(tableBuffer.toString()).append(" ");
            }
            else
            {
                buffer.append(" FROM ").append(tableName).append(" ").append(pfix).append(" ").append(
                    getMapSql(joinMap)).append(" ");
            }

            buffer.append(" WHERE ").append(euqalBuffer.toString()).append(" ");

            cacheQuerySQL(claz, buffer);
        }

        buffer.append(condition);

        if (_logger.isDebugEnabled())
        {
            _logger.debug(buffer.toString());
        }

        return buffer.toString();
    }

    /**
     * cacheQuerySQL
     * 
     * @param claz
     * @param buffer
     */
    protected void cacheQuerySQL(Class<?> claz, StringBuffer buffer)
    {
        AnnoSQLWrap wrap = getWrap(claz);

        wrap.setQuerySQL(buffer.toString());

        addSqlBufferMap(claz, wrap);
    }

    protected String getMapSql(Map<String, StringBuffer> joinMap)
    {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, StringBuffer> entry : joinMap.entrySet())
        {
            buffer.append(entry.getValue().toString()).append(") ");
        }

        return buffer.toString();
    }

    /**
     * updateByConditionSql
     * 
     * @param condition
     * @param claz
     * @return
     * @throws MYSqlException
     */
    protected String updateByConditionSql(String condition, Class claz)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        StringBuffer buffer = new StringBuffer();

        boolean hasUpdateSQL = false;

        if (sqlBufferMap.containsKey(claz))
        {
            AnnoSQLWrap wrap = sqlBufferMap.get(claz);

            if (wrap != null && !BaseTools.isNullOrNone(wrap.getUpdateSQL()))
            {
                hasUpdateSQL = true;

                buffer.append(wrap.getUpdateSQL());
            }
        }

        if ( !hasUpdateSQL)
        {
            // 是实体表
            checkEntry(claz);

            String tableName = BeanTools.getTableName(claz);

            List<InnerBean> columns = BeanTools.getClassFieldsInner(claz);

            if (columns.size() == 0)
            {
                throwsLogger(ErrorConstant.COLUMN_EMPTY);
            }

            buffer.append("UPDATE ");

            buffer.append(tableName).append(" SET ");

            String end = "#, ";

            for (InnerBean filed : columns)
            {
                // update filter autoIncrement,because in access update AutoIncrement id is error
                if ( !filed.isAutoIncrement())
                {
                    buffer.append(filed.getColumnName()).append(" = ");

                    buffer.append("#").append(filed.getFieldName()).append(end);
                }
            }

            buffer.delete(buffer.length() - 2, buffer.length());

            // cache update sql
            cacheUpdateSQL(claz, buffer);
        }

        buffer.append(" WHERE ").append(condition);

        String sql = buffer.toString();

        if (_logger.isDebugEnabled())
        {
            _logger.debug(sql);
        }

        return sql;

    }

    /**
     * cacheUpdateSQL
     * 
     * @param claz
     * @param buffer
     */
    protected void cacheUpdateSQL(Class claz, StringBuffer buffer)
    {
        AnnoSQLWrap wrap = getWrap(claz);

        wrap.setUpdateSQL(buffer.toString());

        addSqlBufferMap(claz, wrap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.AutoCreateSql#updateSql(java.lang.Object)
     */
    public String updateSql(Class claz)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        return this.updateSql(claz, BeanTools.getIdColumn(claz));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.AutoCreateSql#updateSql(java.lang.Object, java.lang.String)
     */
    public String updateSql(Class claz, String columnName)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        String condition = "";

        if ( !BaseTools.isNullOrNone(columnName))
        {
            condition = columnName + " = #" + columnName + "#";
        }

        return updateByConditionSql(condition, claz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.AutoCreateSql#updateFieldSql(java.lang.Class, java.lang.String)
     */
    public String updateFieldSql(Class claz, String fieldName)
        throws MYSqlException
    {
        if (claz == null)
        {
            throwsLogger(ErrorConstant.PARAMETER_NULL);
        }

        Field field = BeanTools.getFieldIgnoreCase(fieldName, claz);

        String fieldColumnName = BeanTools.getColumnName(field);

        String idColumn = BeanTools.getIdColumn(claz);

        String id = BeanTools.getIdFieldName(claz);

        String tableName = BeanTools.getTableName(claz);

        return "update " + tableName + " set " + fieldColumnName + " = #" + fieldName + "# where "
               + idColumn + "= #" + id + "#";
    }

    protected void checkEntry(Class<?> claz)
        throws MYSqlException
    {
        // 是实体表
        if (claz.getAnnotation(Entity.class) == null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug(claz + " is not Entry");
            }

            throwsLogger(ErrorConstant.OBJECT_NOT_ENTITY);
        }
    }

    /**
     * debug日志，并且抛出异常
     * 
     * @param errorNo
     * @throws MYSqlException
     */
    protected void throwsLogger(String errorNo)
        throws MYSqlException
    {
        if (_logger.isDebugEnabled())
        {
            _logger.debug(BeanTools.getErrorMessage(errorNo));
        }

        throw new MYSqlException(errorNo);
    }

    /**
     * debug日志，并且抛出异常
     * 
     * @param errorNo
     * @throws MYSqlException
     */
    protected void throwsLogger(String errorNo, String message)
        throws MYSqlException
    {
        if (_logger.isDebugEnabled())
        {
            _logger.debug(BeanTools.getErrorMessage(errorNo));
        }

        throw new MYSqlException(errorNo, message);
    }

    public String prefix(Class<?> claz)
    {
        Table table = claz.getAnnotation(Table.class);

        if (table == null)
        {
            if (BeanTools.isInherit(claz))
            {
                return prefix(claz.getSuperclass());
            }
        }

        String name = claz.getName();

        if (name.indexOf(".") == -1)
        {
            return name;
        }

        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * 增加到sql缓存里面
     * 
     * @param claz
     * @param wrap
     */
    protected void addSqlBufferMap(Class claz, AnnoSQLWrap wrap)
    {
        if (sqlBufferMap.size() <= MAX_BUFFER_SQL)
        {
            sqlBufferMap.put(claz, wrap);
        }
        else
        {
            sqlBufferMap.clear();

            sqlBufferMap.put(claz, wrap);
        }
    }

    protected AnnoSQLWrap getWrap(Class key)
    {
        AnnoSQLWrap wrap = sqlBufferMap.get(key);

        if (wrap == null)
        {
            wrap = new AnnoSQLWrap(key);
        }

        return wrap;
    }
}
