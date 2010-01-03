/**
 *
 */
package com.china.center.jdbc.inter.adapter;


import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.cache.CacheBootstrap;
import com.china.center.cache.CacheManager;
import com.china.center.cache.bean.InnerMoreCacheBean;
import com.china.center.cache.bean.InnerSingleCacheBean;
import com.china.center.jdbc.inter.AdapterCache;
import com.china.center.tools.ResourceLocator;
import com.china.center.tools.StringTools;


/**
 * @author Administrator
 */
public class AdapterCacheImpl implements AdapterCache
{
    private CacheBootstrap cacheBootstrap = null;

    private CacheManager cacheManager = null;

    private String cacheConfig = "";

    private static final Log _logger = LogFactory.getLog(AdapterCacheImpl.class.getName());

    public <T> void addMore(Class<T> claz, List<T> result, String condtion, Object... args)
    {
        InnerMoreCacheBean<T> bean = new InnerMoreCacheBean<T>();

        if (args != null)
        {
            bean.setKey(String.valueOf(condtion.hashCode()) + '~' + arrayHash(args));
        }
        else
        {
            bean.setKey(String.valueOf(condtion.hashCode()) + '~' + "null");
        }

        bean.setSql(condtion);

        bean.setParameters(args);

        bean.setResult(result);

        cacheManager.addMoreCache(claz, bean);
    }

    private String arrayHash(Object... args)
    {
        if (args.length == 0)
        {
            return "0";
        }

        String result = "";
        for (Object object : args)
        {
            result += String.valueOf(object.hashCode());
        }

        return result;
    }

    public <T> void addSingle(Class<T> claz, Object id, T object)
    {
        InnerSingleCacheBean<T> bean = new InnerSingleCacheBean<T>();

        bean.setKey(id.toString());

        bean.setResult(object);

        cacheManager.addSingleCache(claz, bean);
    }

    public void deleteNote(Class claz)
    {
        cacheManager.noteDelete(claz);
    }

    public <T> T find(Class<T> claz, Object id)
    {
        return cacheManager.findSingleCache(claz, id.toString());
    }

    public void insertNote(Class claz)
    {
        cacheManager.noteInsert(claz);
    }

    public <T> List<T> query(Class<T> claz, String condtion, Object... args)
    {
        String key = "";

        if (args != null)
        {
            key = String.valueOf(condtion.hashCode()) + '~' + arrayHash(args);
        }
        else
        {
            key = String.valueOf(condtion.hashCode()) + '~' + "null";
        }

        return cacheManager.findMoreCaches(claz, key, condtion, args);
    }

    public void updateNote(Class claz)
    {
        cacheManager.noteUpdate(claz);
    }

    /**
     * @return the cacheBootstrap
     */
    public CacheBootstrap getCacheBootstrap()
    {
        return cacheBootstrap;
    }

    /**
     * @param cacheBootstrap
     *            the cacheBootstrap to set
     */
    public void setCacheBootstrap(CacheBootstrap cacheBootstrap)
    {
        this.cacheBootstrap = cacheBootstrap;
    }

    /**
     * @return the cacheManager
     */
    public CacheManager getCacheManager()
    {
        return cacheManager;
    }

    /**
     * @param cacheManager
     *            the cacheManager to set
     */
    public void setCacheManager(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    public void bootstrap()
    {
        _logger.info("begin bootstrap adapterCache...");
        try
        {
            this.cacheBootstrap.bootstrap(ResourceLocator.getResource("classpath:"
                                                                      + this.cacheConfig));

            cacheManager.setBootstrap(this.cacheBootstrap);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("bootstrap adapterCache error");
        }

        _logger.info("bootstrap adapterCache end");
    }

    /**
     * @return the cacheConfig
     */
    public String getCacheConfig()
    {
        return cacheConfig;
    }

    /**
     * @param cacheConfig
     *            the cacheConfig to set
     */
    public void setCacheConfig(String cacheConfig)
    {
        this.cacheConfig = cacheConfig;
    }

    public void cacheNote(String sql)
    {
        // 这里的sql是一个完整的sql 从标准sql看update delte insert 都只能操作一张表的数据
        // 而且这个表都在关键字之后
        if (StringTools.isNullOrNone(sql))
        {
            return;
        }

        sql = sql.toLowerCase().trim();

        // insert into table() values
        if (sql.startsWith(INSERT))
        {
            proceeInsert(sql);

            return;
        }

        // update table set aa = ?
        if (sql.startsWith(UPDATE))
        {
            proceeUpdate(sql);
            return;
        }

        // delete from table where
        if (sql.startsWith(DELETE))
        {
            proceeDelete(sql);
            return;
        }
    }

    /**
     * @param sql
     */
    private void proceeInsert(String sql)
    {
        String temp = sql.substring(INSERT.length()).trim();

        if ( !temp.startsWith(INTO))
        {
            return;
        }

        temp = temp.substring(INTO.length()).trim();

        int tableNameLength = 0;

        for (int i = 0; i < temp.length(); i++ )
        {
            if (temp.charAt(i) == ' ' || temp.charAt(i) == '(')
            {
                tableNameLength = i;

                break;
            }
        }

        String tableName = temp.substring(0, tableNameLength);

        Class claz = cacheBootstrap.getClassByTableName(tableName.toUpperCase());

        if (claz != null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("insert note:" + claz.getName());
            }

            this.insertNote(claz);
        }
    }

    /**
     * @param sql
     */
    private void proceeUpdate(String sql)
    {
        String temp = sql.substring(UPDATE.length()).trim();

        int tableNameLength = 0;

        for (int i = 0; i < temp.length(); i++ )
        {
            if (temp.charAt(i) == ' ' || temp.charAt(i) == '(')
            {
                tableNameLength = i;

                break;
            }
        }

        String tableName = temp.substring(0, tableNameLength);

        Class claz = cacheBootstrap.getClassByTableName(tableName.toUpperCase());

        if (claz != null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("update note:" + claz.getName());
            }

            this.updateNote(claz);
        }
    }

    /**
     * @param sql
     */
    private void proceeDelete(String sql)
    {
        String temp = sql.substring(DELETE.length()).trim();

        if ( !temp.startsWith(FROM))
        {
            return;
        }

        temp = temp.substring(FROM.length()).trim();

        int tableNameLength = 0;

        for (int i = 0; i < temp.length(); i++ )
        {
            if (temp.charAt(i) == ' ')
            {
                tableNameLength = i;

                break;
            }
        }

        String tableName = temp.substring(0, tableNameLength);

        Class claz = cacheBootstrap.getClassByTableName(tableName.toUpperCase());

        if (claz != null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("delete note:" + claz.getName());
            }

            this.deleteNote(claz);
        }
    }
}
