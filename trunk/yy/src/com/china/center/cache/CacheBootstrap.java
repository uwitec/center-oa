/**
 *
 */
package com.china.center.cache;


import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.china.center.cache.bean.StatEfficiency;


/**
 * cache root manager
 * 
 * @author Administrator
 */
public interface CacheBootstrap<T>
{
    /**
     * 引导函数
     * 
     * @param in
     */
    void bootstrap(InputStream in);

    /**
     * 注册缓存
     * 
     * @param claz
     */
    boolean registerCache(Class claz);

    /**
     * 注册缓存
     * 
     * @param claz
     */
    void logOutCahce(Class claz);

    /**
     * 获得相关的class
     * 
     * @param claz
     * @return
     */
    List<Class> refClass(Class claz);

    /**
     * 根据class查询单体缓存
     * 
     * @param claz
     * @return
     */
    T findSingleCache(Class claz);

    /**
     * 根据class查询query缓存
     * 
     * @param claz
     * @return
     */
    T findMoreCache(Class claz);

    /**
     * 缓存效率
     * 
     * @param claz
     * @return
     */
    StatEfficiency singleCacheEfficiency(Class claz);

    /**
     * 缓存效率
     * 
     * @param claz
     * @return
     */
    StatEfficiency moreCacheEfficiency(Class claz);

    /**
     * 设置过滤器
     * 
     * @param filtrate
     */
    void setFiltrate(String filtrate);

    /**
     * 是否可以使用
     * 
     * @return
     */
    boolean enable();

    /**
     * key set
     * 
     * @return
     */
    Set<Class> cacheKeySet();

    /**
     * 获得类的对应表名
     * 
     * @param claz
     * @return
     */
    Class getClassByTableName(String tableName);
}
