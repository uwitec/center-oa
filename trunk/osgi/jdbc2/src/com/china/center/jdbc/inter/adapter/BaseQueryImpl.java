/**
 * File Name: MySqlQuery.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-2<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.adapter;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.annosql.tools.BeanUtils;
import com.china.center.jdbc.inter.AdapterCache;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.inter.Query;
import com.china.center.jdbc.util.JDBCCommonTools;


/**
 * BaseQueryImpl的实现
 * 
 * @author ZHUZHU
 * @version 2008-3-2
 * @see
 * @since
 */
public abstract class BaseQueryImpl implements Query
{
    protected final Log _logger = LogFactory.getLog(getClass());

    protected JdbcOperation jdbcOperation = null;

    protected String sqlString = "";

    protected String prefix = "";

    protected String idColumn = "id";

    protected boolean cache = false;

    protected Object[] paramters = null;

    protected AdapterCache adapterCache = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.Query#list(java.lang.Class)
     */
    public <T> List<T> list(Class<T> claz)
    {
        List<T> result = null;

        if (cache)
        {
            result = adapterCache.query(claz, getLastSql(), paramters);

            if ( !JDBCCommonTools.isEmptyOrNull(result))
            {
                return result;
            }
        }

        List<Map> list = list();

        try
        {
            result = BeanTools.getBeans(list, claz);

            if (cache)
            {
                adapterCache.addMore(claz, result, getLastSql(), paramters);
            }

            return result;
        }
        catch (InstantiationException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }
    }

    public List list()
    {
        return this.jdbcOperation.queryForList(getLastSql(), paramters);
    }

    public int getCount()
    {
        return this.jdbcOperation.queryForInt(getCountSql(), paramters);
    }

    private String getCountSql()
    {
        String sql = this.sqlString.trim();

        String tem = sql.toLowerCase();

        int index = tem.indexOf(FROM);

        // real is error
        if (index == -1)
        {
            return "select count(1) from (" + this.sqlString + ") t_count_query ";
        }

        return "select count(1) from " + sql.substring(index + FROM.length());
    }

    public void setIdColumn(String idColumn)
    {
        if (idColumn != null && !"".equals(idColumn.trim()))
        {
            this.idColumn = idColumn;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.Query#setFirstResult(int)
     */
    public abstract Query setFirstResult(int firstResult);

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.Query#setMaxResults(int)
     */
    public abstract Query setMaxResults(int maxResults);

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.Query#setResultsRange(int, int)
     */
    public abstract Query setResultsRange(int begin, int end);

    public abstract String getLastSql();

    public Map uniqueResult()
    {
        List<Map> list = this.jdbcOperation.queryForList(getLastSql(), paramters);

        if (list.size() == 0)
        {
            return null;
        }

        if (list.size() > 1)
        {
            throw new RuntimeException("the key is duplicate!");
        }

        return list.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.Query#uniqueResult(java.lang.Class)
     */
    public <T> T uniqueResult(Class<T> claz)
    {
        Map result = uniqueResult();

        if (result == null)
        {
            return null;
        }

        T obj = null;

        try
        {
            obj = claz.newInstance();
        }
        catch (InstantiationException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }

        try
        {
            BeanUtils.populateSqlFieldIgnorCase(obj, result);
        }
        catch (IllegalAccessException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e)
        {
            _logger.warn(e, e);
            throw new RuntimeException(e);
        }

        return obj;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * @return the sqlString
     */
    public String getSqlString()
    {
        return sqlString;
    }

    /**
     * @return the paramters
     */
    public Object[] getParamters()
    {
        return paramters;
    }

    /**
     * @param jdbcOperation
     *            the jdbcOperation to set
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }

    /**
     * @param sqlString
     *            the sqlString to set
     */
    public void setSqlString(String sqlString)
    {
        this.sqlString = sqlString;
    }

    /**
     * @param paramters
     *            the paramters to set
     */
    public void setParamters(Object... paramters)
    {
        this.paramters = paramters;
    }

    public void setAdapterCache(AdapterCache adapterCache)
    {
        this.adapterCache = adapterCache;
    }

    public void setCache(boolean cache)
    {
        this.cache = cache;
    }
}
