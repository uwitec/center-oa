/*
 * File Name: EhcacheManager.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.manager;


import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.tools.ResourceLocator;


/**
 * 缓存设置
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
public class EhcacheManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private Cache cache_local = null;

    public void init()
    {
        try
        {
            InputStream in = ResourceLocator.getResource("classpath:config/template_ehcache.xml");

            CacheManager.create(in);

            cache_local = CacheManager.getInstance().getCache("CENTER_CHINA_CACHE_LOCAL");

            if (cache_local != null)
            {
                cache_local.flush();
            }

            System.out.println("系统加载缓存...");
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
    }

    public void destory()
    {
        System.out.println("shutdown CacheManager");
        CacheManager.getInstance().shutdown();
    }

    /**
     * @return the cache_local
     */
    public Cache getCache_local()
    {
        return cache_local;
    }

    /**
     * @param cache_local
     *            the cache_local to set
     */
    public void setCache_local(Cache cache_local)
    {
        this.cache_local = cache_local;
    }
}
