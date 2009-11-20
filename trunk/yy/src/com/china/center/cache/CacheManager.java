/**
 * File Name: CacheManager.java
 * CopyRight: Copyright by www.centerchina.com
 * Description:
 * Creater: zhuzhu
 * CreateTime: 2008-7-9
 * Grant: open source to everybody
 */
package com.china.center.cache;


import java.util.List;

import com.china.center.cache.bean.InnerMoreCacheBean;
import com.china.center.cache.bean.InnerSingleCacheBean;


/**
 * manage the cache<br>
 * in jdbc cache,we will cache two operations: find and query.<br>
 * In cache,the base cache object is bean or VO,and the cache contain two modes:<br>
 * 1:sigle bean 2:more beans<br>
 * Recomment use ehcache
 * 
 * @author zhuzhu
 */
public interface CacheManager
{
    String NOTIFY_INSERT = "0";

    String NOTIFY_UPDATE = "1";

    /**
     * 增加find的缓存
     * 
     * @param <T>
     * @param claz
     * @param cache
     * @return
     */
    <T> boolean addSingleCache(Class<T> claz, InnerSingleCacheBean<T> cache);

    /**
     * 增加query的缓存
     * 
     * @param <T>
     * @param claz
     * @param cache
     * @return
     */
    <T> boolean addMoreCache(Class<T> claz, InnerMoreCacheBean<T> cache);

    /**
     * 根据ID获取缓存
     * 
     * @param <T>
     * @param claz
     * @param cache
     * @return
     */
    <T> T findSingleCache(Class<T> claz, String key);

    /**
     * 根据ID获取缓存
     * 
     * @param <T>
     * @param claz
     * @param cache
     * @return
     */
    <T> List<T> findMoreCaches(Class<T> claz, String key, String condtion, Object... args);

    /**
     * 删除单体缓存
     * 
     * @param <T>
     * @param claz
     * @param cache
     * @return
     */
    void removeSingleCache(Class claz, String key);

    /**
     * 删除整个class的缓存
     * 
     * @param <T>
     * @param claz
     */
    void removeCache(Class claz);

    /**
     * 删除single bean的缓存
     * 
     * @param <T>
     * @param claz
     */
    void removeSingelCache(Class claz);

    /**
     * 删除more bean的缓存
     * 
     * @param <T>
     * @param claz
     */
    void removeMoreCache(Class claz);

    /**
     * 删除查询缓存
     * 
     * @param <T>
     * @param claz
     * @param cache
     * @return
     */
    void removeMoreCache(Class claz, String key);

    /**
     * 更新通知
     * 
     * @param claz
     */
    void noteUpdate(Class claz);

    /**
     * 插入通知
     * 
     * @param claz
     */
    void noteInsert(Class claz);

    /**
     * 删除通知
     * 
     * @param claz
     */
    void noteDelete(Class claz);

    /**
     * 设置boot
     * 
     * @param manager
     */
    void setBootstrap(CacheBootstrap manager);

    /**
     * 是否集群
     * 
     * @param cluster
     */
    void setCluster(boolean cluster);

    /**
     * 接受通知
     * 
     * @param notify
     */
    void noyify(String notify);

    /**
     * 初始化
     */
    void init();
}
