/**
 *
 */
package com.china.center.jdbc.cache.impl;


import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.jdbc.annosql.InnerBean;
import com.china.center.jdbc.annosql.MYSqlException;
import com.china.center.jdbc.annosql.tools.BaseTools;
import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.annotation.CacheRelation;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.jdbc.cache.CacheBootstrap;
import com.china.center.jdbc.cache.bean.MoreHashMap;
import com.china.center.jdbc.cache.bean.StatEfficiency;


/**
 * use ehcache impl Bootstrap
 * 
 * @author ZHUZHU
 */
public class EhcacheBootstrapImpl implements CacheBootstrap<net.sf.ehcache.Cache>
{
    private final static String CACHE_DEFAULT_TEMPLATE = "CACHE_DEFAULT_TEMPLATE";

    private List<Class> allClass = new ArrayList<Class>(500);

    private Map<Class, List<Class>> refClass = new HashMap<Class, List<Class>>(500);

    private Map<String, Class> tableMap = new HashMap<String, Class>(500);

    private static final Log _logger = LogFactory.getLog(EhcacheBootstrapImpl.class.getName());

    /**
     * 模板cache
     */
    private Cache templateCache = null;

    /**
     * 缓存是否有用
     */
    private boolean enable = true;

    private Object lock = new Object();

    /**
     * 过滤条件
     */
    private String filtrate = "";

    private MoreHashMap<Class, Cache, Cache> moreHash = new MoreHashMap<Class, Cache, Cache>(500);

    private MoreHashMap<Class, StatEfficiency, StatEfficiency> efficiencyHash = new MoreHashMap<Class, StatEfficiency, StatEfficiency>(
        500);

    public void bootstrap(InputStream in)
    {
        if (_logger.isDebugEnabled())
        {
            _logger.debug("bootstrap cache begin.....");

            _logger.debug("loading inputStream");
        }

        // 使用ehcache作为引导程序
        CacheManager.create(in);

        templateCache = CacheManager.getInstance().getCache(CACHE_DEFAULT_TEMPLATE);

        if (templateCache == null)
        {
            _logger.fatal("load tempalte cache failed");
            this.enable = false;
        }
        else
        {
            _logger.info("load tempalte cache success");
            this.enable = true;
        }

        if (_logger.isDebugEnabled())
        {
            _logger.debug("bootstrap cache end.....");
        }
    }

    /**
     * 注册缓存
     */
    public boolean registerCache(Class claz)
    {
        if (filtrateClass(claz))
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug(claz.getName() + " is filtrated by " + this.filtrate
                              + ", register failed");
            }
            return false;
        }

        // 必须是entry
        if ( !BeanTools.isEntry(claz))
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug(claz.getName() + " is not a entry bean, so registerCache fault");
            }
            return false;
        }

        boolean contains = false;

        synchronized (lock)
        {
            if ( !allClass.contains(claz))
            {
                addClass(claz);

                contains = false;
            }
            else
            {
                contains = true;
            }

            // 只有缓存的entry才建立缓存实例
            if ( !contains && BeanTools.isCache(claz))
            {
                // 注入缓存对象
                moreHash.put(claz, coloneCache(claz.getName() + "_SINGLE"),
                    coloneCache(claz.getName() + "_MORE"));

                if (_logger.isDebugEnabled())
                {
                    _logger.debug("create cache:" + claz.getName() + "_SINGLE");

                    _logger.debug("create cache:" + claz.getName() + "_MORE");
                }

                efficiencyHash.put(claz, new StatEfficiency(), new StatEfficiency());

                if (_logger.isDebugEnabled())
                {
                    _logger.debug("create efficiency:" + claz.getName());
                }
            }
        }

        return true;
    }

    /**
     * 加入class到缓存
     * 
     * @param claz
     */
    private synchronized void addClass(Class claz)
    {
        allClass.add(claz);

        tableMap.put(BeanTools.getTableName(claz).toUpperCase(), claz);

        try
        {
            analyseClassRef(claz);
        }
        catch (MYSqlException e)
        {
            _logger.error(e, e);
        }
    }

    /**
     * 分析类之间的关系
     * 
     * @param claz
     * @throws MYSqlException
     */
    private void analyseClassRef(Class claz)
        throws MYSqlException
    {
        if (refClass.containsKey(claz))
        {
            return;
        }

        recursion(claz, claz);

        List<InnerBean> columns = BeanTools.getClassFieldsInner(claz);

        for (InnerBean field : columns)
        {
            if (field.isRelationship())
            {
                Relationship relationship = BeanTools.getRelationship(field.getField());

                String fieldName = relationship.relationField();

                Field relationField = BeanTools.getFieldIgnoreCase(fieldName, claz);

                if (relationField == null)
                {
                    continue;
                }

                Join join = BeanTools.getJoin(relationField);

                if (join == null)
                {
                    continue;
                }

                Class tagClass = join.tagClass();

                addRef(tagClass, claz);
            }
        }
    }

    /**
     * 增加对于关系
     * 
     * @param main
     * @param ref
     */
    private void addRef(Class main, Class ref)
    {
        List<Class> list = null;

        if (refClass.containsKey(main))
        {
            list = refClass.get(main);
        }
        else
        {
            list = new ArrayList<Class>();

            refClass.put(main, list);
        }

        if ( !list.contains(ref))
        {
            list.add(ref);
        }
    }

    /**
     * 递归获得组织结构
     * 
     * @param list
     * @param claz
     */
    private void recursion(Class claz, Class orgClass)
    {
        CacheRelation cacheRelation = BeanTools.getCacheRelation(claz);

        if (cacheRelation != null)
        {
            Class[] arr = cacheRelation.relation();

            for (Class eachItem : arr)
            {
                addRef(claz, eachItem);

                addRef(orgClass, eachItem);
            }
        }

        if (BeanTools.isInherit(claz))
        {
            Class sup = claz.getSuperclass();

            if (sup != Object.class)
            {
                // 相互关联的
                addRef(sup, claz);

                addRef(claz, sup);

                recursion(sup, orgClass);
            }
        }
    }

    /**
     * 过滤出class
     * 
     * @param claz
     * @return
     */
    private boolean filtrateClass(Class claz)
    {
        if (BaseTools.isNullOrNone(filtrate))
        {
            return false;
        }

        return false;
    }

    private Cache coloneCache(String name)
    {
        // String name,
        // int maxElementsInMemory,
        // boolean overflowToDisk,
        // boolean eternal,
        // long timeToLiveSeconds,
        // long timeToIdleSeconds,
        // boolean diskPersistent,
        // long diskExpiryThreadIntervalSeconds

        CacheConfiguration config = templateCache.getCacheConfiguration();

        Cache cache = new Cache(name, config.getMaxElementsInMemory(), config.isOverflowToDisk(),
            config.isEternal(), config.getTimeToLiveSeconds(), config.getTimeToIdleSeconds(),
            config.isDiskPersistent(), config.getDiskExpiryThreadIntervalSeconds());

        // init cache isntance
        cache.initialise();

        cache.removeAll();

        return cache;
    }

    public Cache findMoreCache(Class claz)
    {
        return moreHash.getValue2(claz);
    }

    public Cache findSingleCache(Class claz)
    {
        return moreHash.getValue1(claz);
    }

    public void logOutCahce(Class claz)
    {
        synchronized (lock)
        {
            if ( !allClass.contains(claz))
            {
                allClass.remove(claz);

                Cache cache = moreHash.getValue1(claz);

                if (cache != null)
                {
                    cache.removeAll();

                    cache.flush();
                }

                cache = moreHash.getValue2(claz);

                if (cache != null)
                {
                    cache.removeAll();

                    cache.flush();
                }

                moreHash.remove(claz);

                efficiencyHash.remove(claz);
            }
        }
    }

    public StatEfficiency moreCacheEfficiency(Class claz)
    {
        return efficiencyHash.getValue2(claz);
    }

    public List<Class> refClass(Class claz)
    {
        if (refClass.containsKey(claz))
        {
            return refClass.get(claz);
        }

        // 注册
        registerCache(claz);

        return refClass.get(claz);
    }

    public StatEfficiency singleCacheEfficiency(Class claz)
    {
        return efficiencyHash.getValue1(claz);
    }

    public void setFiltrate(String filtrate)
    {
        this.filtrate = filtrate;
    }

    public boolean enable()
    {
        return this.enable;
    }

    public Set<Class> cacheKeySet()
    {
        return moreHash.keySet();
    }

    public Class getClassByTableName(String tableName)
    {
        return tableMap.get(tableName);
    }
}
