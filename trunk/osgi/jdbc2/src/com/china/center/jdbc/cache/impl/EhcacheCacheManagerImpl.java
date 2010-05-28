/**
 *
 */
package com.china.center.jdbc.cache.impl;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.jdbc.annosql.tools.BaseTools;
import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.cache.CacheBootstrap;
import com.china.center.jdbc.cache.CacheManager;
import com.china.center.jdbc.cache.bean.InnerMoreCacheBean;
import com.china.center.jdbc.cache.bean.InnerSingleCacheBean;
import com.china.center.jdbc.cache.bean.StatEfficiency;
import com.china.center.jdbc.cache.notify.MulticastNotifyReceiver;
import com.china.center.jdbc.cache.notify.MulticastNotifySender;


/**
 * use ehcache impl CacheManager
 * 
 * @author Administrator
 */
public class EhcacheCacheManagerImpl implements CacheManager
{
    private EhcacheBootstrapImpl bootstrap = null;

    private static final Log _logger = LogFactory.getLog(EhcacheCacheManagerImpl.class.getName());

    private boolean cluster = false;

    private String group = "230.0.0.1";

    private int mprot = 10221;

    private int timeToLive = 32;

    private Map<Class, Boolean> classCacheConfig = new HashMap<Class, Boolean>(1000);

    private MulticastNotifyReceiver receiver = null;

    public <T> boolean addMoreCache(Class<T> claz, InnerMoreCacheBean<T> moreCache)
    {
        if ( !isClassCache(claz))
        {
            return false;
        }

        Cache cache = bootstrap.findMoreCache(claz);

        StatEfficiency eff = bootstrap.moreCacheEfficiency(claz);

        if (cache != null)
        {
            InnerMoreCacheBean<T> putCache = (InnerMoreCacheBean<T>)BaseTools.deepCopy(moreCache);

            Element element = new Element(putCache.getKey(), putCache);

            cache.put(element);

            cache.flush();

            eff.addCache_incr();

            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " put element:" + moreCache.getKey()
                              + ";Element:" + BaseTools.toStrings(moreCache));
            }

            return true;
        }
        else
        {
            _logger.error(claz.getName() + " cache is not exist");
        }

        return false;
    }

    /**
     * class是否缓存
     * 
     * @param claz
     * @return
     */
    private boolean isClassCache(Class claz)
    {
        Boolean result = classCacheConfig.get(claz);

        if (result == null)
        {
            boolean ca = BeanTools.isCache(claz);

            classCacheConfig.put(claz, ca);

            return ca;
        }

        return result;
    }

    public <T> boolean addSingleCache(Class<T> claz, InnerSingleCacheBean<T> singleCache)
    {
        if ( !isClassCache(claz))
        {
            return false;
        }

        Cache cache = bootstrap.findSingleCache(claz);

        StatEfficiency eff = bootstrap.singleCacheEfficiency(claz);

        if (cache != null)
        {
            InnerSingleCacheBean<T> putCache = (InnerSingleCacheBean<T>)BaseTools.deepCopy(singleCache);

            Element element = new Element(putCache.getKey(), putCache);

            cache.put(element);

            cache.flush();

            eff.addCache_incr();

            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " put element:" + singleCache.getKey()
                              + ";Element:" + BaseTools.toStrings(singleCache));
            }

            return true;
        }
        else
        {
            _logger.error(claz.getName() + " cache is not exist");
        }

        return false;
    }

    public <T> List<T> findMoreCaches(Class<T> claz, String key, String condtion, Object... args)
    {
        if ( !isClassCache(claz))
        {
            return null;
        }

        Cache cache = bootstrap.findMoreCache(claz);

        if (cache == null)
        {
            // 注册
            bootstrap.registerCache(claz);

            cache = bootstrap.findMoreCache(claz);

            if (cache == null)
            {
                return null;
            }
        }

        StatEfficiency eff = bootstrap.moreCacheEfficiency(claz);

        Element element = cache.get(key);

        if (element == null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " can not find:" + key);
            }

            return null;
        }

        // 过期的返回null
        if (element.isExpired())
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " can not find(expired):" + key);
            }

            return null;
        }

        Object oo = element.getValue();

        if (oo == null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " can not find(element is null):" + key);
            }
            return null;
        }

        if ( ! (oo instanceof InnerMoreCacheBean))
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName()
                              + " can not find(element is not instanceof InnerMoreCacheBean):"
                              + key);
            }

            cache.remove(key);

            return null;
        }

        InnerMoreCacheBean<T> more = (InnerMoreCacheBean)oo;

        if ( !BaseTools.isNullOrNone(more.getSql()))
        {
            if ( !more.getSql().equals(condtion))
            {
                if (_logger.isDebugEnabled())
                {
                    _logger.debug("in " + cache.getName()
                                  + " can not find(condtition is not equal):" + key);
                }

                return null;
            }
        }

        Object[] par = more.getParameters();

        if ( !BaseTools.equals(par, args))
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " can not find(parameters is not equal):"
                              + key);
            }

            return null;
        }

        eff.useCache_incr();

        if (_logger.isDebugEnabled())
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " success find:" + key + ";Result:"
                              + BaseTools.toStrings(more.getResult()));
            }
        }

        return (List<T>)BaseTools.deepCopy(more.getResult());
    }

    public <T> T findSingleCache(Class<T> claz, String key)
    {
        if ( !isClassCache(claz))
        {
            return null;
        }

        Cache cache = bootstrap.findSingleCache(claz);

        if (cache == null)
        {
            // 注册
            bootstrap.registerCache(claz);

            cache = bootstrap.findSingleCache(claz);

            if (cache == null)
            {
                return null;
            }
        }

        StatEfficiency eff = bootstrap.singleCacheEfficiency(claz);

        Element element = cache.get(key);

        if (element == null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " can not find:" + key);
            }

            return null;
        }

        // 过期的返回null
        if (element.isExpired())
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " can not find(expired):" + key);
            }

            return null;
        }

        Object oo = element.getValue();

        if (oo == null)
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName() + " can not find(element is null):" + key);
            }
            return null;
        }

        if ( ! (oo instanceof InnerSingleCacheBean))
        {
            if (_logger.isDebugEnabled())
            {
                _logger.debug("in " + cache.getName()
                              + " can not find(element is not instanceof InnerSingleCacheBean):"
                              + key);
            }

            cache.remove(key);

            return null;
        }

        InnerSingleCacheBean<T> single = (InnerSingleCacheBean)oo;

        if (_logger.isDebugEnabled())
        {
            _logger.debug("in " + cache.getName() + " success find:" + key + ";Result:"
                          + BaseTools.toStrings(single.getResult()));
        }

        eff.useCache_incr();

        return (T)BaseTools.deepCopy(single.getResult());
    }

    /**
     * insert 通知<br>
     * 清除相关class和单体和more,并且清空本身class的more
     */
    public void noteInsert(Class claz)
    {
        if (this.cluster)
        {
            // 通知集群的缓存清除 采用MulticastSocket通知
            try
            {
                MulticastNotifySender sender = new MulticastNotifySender(
                    InetAddress.getByName(this.group), this.mprot, this.timeToLive);

                sender.sendNotify(NOTIFY_INSERT + ":" + claz.getName());

                // 测试发现有时本机不能接受Multicast的消息，所以自行调用一次
                noteInsertInner(claz);
            }
            catch (UnknownHostException e)
            {
                _logger.error(e, e);
            }
        }
        else
        {
            noteInsertInner(claz);
        }
    }

    /**
     * 插入通知的内部实现
     * 
     * @param claz
     */
    private void noteInsertInner(Class claz)
    {
        List<Class> ref = bootstrap.refClass(claz);

        if (ref == null)
        {
            ref = new ArrayList<Class>();

            ref.add(claz);
        }
        else
        {
            if ( !ref.contains(claz))
            {
                ref.add(claz);
            }
        }

        this.removeMoreCache(claz);

        if ( !BaseTools.isEmptyOrNull(ref))
        {
            for (Class item : ref)
            {
                this.removeMoreCache(item);

                this.removeSingelCache(item);
            }
        }
    }

    /**
     * update 通知<br>
     * 清除相关class和单体和more,并且清空本身class的more和single
     */
    public void noteUpdate(Class claz)
    {
        if (this.cluster)
        {
            // 通知集群的缓存清除 采用MulticastSocket通知
            try
            {
                MulticastNotifySender sender = new MulticastNotifySender(
                    InetAddress.getByName(this.group), this.mprot, this.timeToLive);

                sender.sendNotify(NOTIFY_UPDATE + ":" + claz.getName());

                // 测试发现有时本机不能接受Multicast的消息，所以自行调用一次
                noteUpdateInner(claz);
            }
            catch (UnknownHostException e)
            {
                _logger.error(e, e);
            }
        }
        else
        {
            noteUpdateInner(claz);
        }
    }

    /**
     * 更新通知的内部实现
     * 
     * @param claz
     */
    private void noteUpdateInner(Class claz)
    {
        List<Class> ref = bootstrap.refClass(claz);

        if (ref == null)
        {
            ref = new ArrayList<Class>();

            ref.add(claz);
        }
        else
        {
            if ( !ref.contains(claz))
            {
                ref.add(claz);
            }
        }

        for (Class item : ref)
        {
            this.removeMoreCache(item);

            this.removeSingelCache(item);
        }
    }

    /**
     * delete 通知<br>
     * 清除相关class和单体和more,并且清空本身class的more和single
     */
    public void noteDelete(Class claz)
    {
        noteUpdate(claz);
    }

    public void removeCache(Class claz)
    {
        removeSingelCache(claz);

        removeMoreCache(claz);
    }

    public void removeMoreCache(Class claz)
    {
        Cache cache = bootstrap.findMoreCache(claz);

        StatEfficiency eff = bootstrap.moreCacheEfficiency(claz);

        if (cache != null)
        {
            cache.removeAll();

            cache.flush();

            eff.clear_incr();
        }
    }

    public void removeMoreCache(Class claz, String key)
    {
        Cache cache = bootstrap.findMoreCache(claz);

        if (cache != null)
        {
            cache.remove(key);
        }
    }

    public void removeSingelCache(Class claz)
    {
        Cache cache = bootstrap.findSingleCache(claz);

        StatEfficiency eff = bootstrap.singleCacheEfficiency(claz);

        if (cache != null)
        {
            cache.removeAll();
            cache.flush();

            eff.clear_incr();
        }
    }

    public void removeSingleCache(Class claz, String key)
    {
        Cache cache = bootstrap.findSingleCache(claz);

        if (cache != null)
        {
            cache.remove(key);
        }
    }

    public void setBootstrap(CacheBootstrap manager)
    {
        this.bootstrap = (EhcacheBootstrapImpl)manager;
    }

    public void setCluster(boolean cluster)
    {
        this.cluster = cluster;
    }

    /**
     * 集群通知
     */
    public void noyify(String notify)
    {
        if (_logger.isDebugEnabled())
        {
            _logger.debug("process notify:" + notify);
        }

        if (BaseTools.isNullOrNone(notify))
        {
            return;
        }

        if (notify.startsWith(NOTIFY_INSERT + ":"))
        {
            String className = notify.substring(2);

            if (_logger.isDebugEnabled())
            {
                _logger.debug("notify:" + className);
            }

            try
            {
                Class claz = Class.forName(className);

                noteInsertInner(claz);

                if (_logger.isDebugEnabled())
                {
                    _logger.debug("noteInsertInner:" + className);
                }
            }
            catch (ClassNotFoundException e)
            {
                _logger.error(e, e);
            }
        }

        if (notify.startsWith(NOTIFY_UPDATE + ":"))
        {
            String className = notify.substring(2);

            if (_logger.isDebugEnabled())
            {
                _logger.debug("notify:" + className);
            }

            try
            {
                Class claz = Class.forName(className);

                noteUpdateInner(claz);

                if (_logger.isDebugEnabled())
                {
                    _logger.debug("noteUpdateInner:" + className);
                }
            }
            catch (ClassNotFoundException e)
            {
                _logger.error(e, e);
            }
        }
    }

    /**
     * @return the group
     */
    public String getGroup()
    {
        return group;
    }

    /**
     * @param group
     *            the group to set
     */
    public void setGroup(String group)
    {
        this.group = group;
    }

    /**
     * @return the mprot
     */
    public int getMprot()
    {
        return mprot;
    }

    /**
     * @param mprot
     *            the mprot to set
     */
    public void setMprot(int mprot)
    {
        this.mprot = mprot;
    }

    /**
     * @return the timeToLive
     */
    public int getTimeToLive()
    {
        return timeToLive;
    }

    /**
     * @param timeToLive
     *            the timeToLive to set
     */
    public void setTimeToLive(int timeToLive)
    {
        this.timeToLive = timeToLive;
    }

    public void destory()
    {
        if (this.cluster)
        {
            if (receiver != null)
            {
                receiver.close();

                if (_logger.isDebugEnabled())
                {
                    _logger.debug("destory MulticastNotifyReceiver success");
                }
            }
        }
    }

    public void init()
    {
        if (this.cluster)
        {
            try
            {
                receiver = new MulticastNotifyReceiver(InetAddress.getByName(this.group),
                    this.mprot, this);

                receiver.start();
            }
            catch (UnknownHostException e)
            {
                _logger.error(e, e);

                this.cluster = false;

                _logger.error("new MulticastNotifyReceiver faild, so cluster set false");
            }
        }
    }

}
