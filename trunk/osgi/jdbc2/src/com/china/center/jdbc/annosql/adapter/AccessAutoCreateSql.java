/*
 * File Name: OracleAutoCreateSql.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-28
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annosql.adapter;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.jdbc.annosql.ErrorConstant;
import com.china.center.jdbc.annosql.InnerBean;
import com.china.center.jdbc.annosql.MYSqlException;
import com.china.center.jdbc.annosql.tools.BaseTools;
import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.annosql.wrap.AnnoSQLWrap;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.jdbc.annotation.enums.JoinType;


/**
 * access的自动生成sql的实现<br>
 * access不知处full join
 * 
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
public class AccessAutoCreateSql extends BaseAutoCreateSql
{
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

            String tableName = BeanTools.getTableName(claz);

            StringBuffer euqalBuffer = new StringBuffer();

            buffer.append("select ");

            List<InnerBean> columns = BeanTools.getClassFieldsInner(claz);

            // 处理join
            String end = ", ";

            String pfix = prefix(claz);

            String pfix1 = pfix + '.';

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

                    Join join = BeanTools.getJoin(relationField);

                    if (join == null)
                    {
                        throwsLogger(ErrorConstant.JOIN_NULL, relationField.getName()
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
                        jointagField = BeanTools.getIdColumn(tagClass);
                    }

                    if (jointagField == null || "".equals(jointagField.trim()))
                    {
                        throwsLogger(ErrorConstant.JOIN_NULL, tagClass.getName() + " miss id");
                    }

                    Field ffx = BeanTools.getFieldIgnoreCase(jointagField, tagClass);

                    // count(1) from (select ExamTypeBean.NAME as typeName,
                    // LocationBean.NAME as locationName,
                    // dirLocation.NAME as dirLocationName, ExamBean.ID,
                    // ExamBean.NAME, ExamBean.DESCRIPTION, ExamBean.TYPE,
                    // ExamBean.LOGTIME, ExamBean.TOTALPOINT,
                    // ExamBean.DIFFICULTY, ExamBean.BASELINE,
                    // ExamBean.EXAMTIME,
                    // ExamBean.LOCATIONID, ExamBean.DIRLOCATIONID
                    // FROM (((T_CENTER_EXAM ExamBean LEFT OUTER JOIN
                    // T_CENTER_LOCATION dirLocation on
                    // (ExamBean.dirLocationId = dirLocation.id )) LEFT OUTER
                    // JOIN T_CENTER_LOCATION LocationBean on
                    // (ExamBean.locationId = LocationBean.id )) inner join
                    // T_CENTER_EXAMTYPE ExamTypeBean on
                    // (ExamBean.type = ExamTypeBean.id)) WHERE 1=1 order by
                    // ExamBean.id desc) t_count_query

                    // 由于access对=和join不能混合使用,所以屏蔽了=的使用,用inner join代替

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
                            joinMap.put(key, new StringBuffer());

                            // left join t_center_location t4 on (t1.locationId
                            // = t4.id and t1.kkkk = t4.bbbb)
                            joinMap.get(key).append(" INNER JOIN ").append(tagTableName).append(
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
                        throw new MYSqlException("access do not support full join");
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

            // (((
            String beg = getString("(", joinMap.size());

            buffer.append(" FROM ").append(beg).append(tableName).append(" ").append(pfix).append(
                " ").append(getMapSql(joinMap)).append(" ");

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

    protected String getMapSql(Map<String, StringBuffer> joinMap)
    {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, StringBuffer> entry : joinMap.entrySet())
        {
            buffer.append(entry.getValue().toString()).append(")) ");
        }

        return buffer.toString();
    }

    private String getString(String str, int count)
    {
        String result = "";

        for (int i = 0; i < count; i++ )
        {
            result += str;
        }

        return result;
    }
}
