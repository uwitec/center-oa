/**
 * File Name: BuffaloOSGiConfigurerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-3<br>
 * Grant: open source to everybody
 */
package com.china.center.webplugin.inter.impl;


import java.util.HashMap;
import java.util.Map;

import com.china.center.webplugin.inter.BuffaloOSGiConfigurer;


/**
 * BuffaloOSGiConfigurerImpl
 * 
 * @author ZHUZHU
 * @version 2010-7-3
 * @see BuffaloOSGiConfigurerImpl
 * @since 1.0
 */
public class BuffaloOSGiConfigurerImpl implements BuffaloOSGiConfigurer
{
    private static Map buffaloOSGiConfigurerContext = new HashMap();

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.webplugin.inter.BuffaloOSGiConfigurer#putBuffaloService(java.lang.String, java.lang.Object)
     */
    public void putBuffaloService(String key, Object service)
    {
        buffaloOSGiConfigurerContext.put(key, service);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.webplugin.inter.BuffaloOSGiConfigurer#removeBuffaloService(java.lang.String)
     */
    public void removeBuffaloService(String key)
    {
        buffaloOSGiConfigurerContext.remove(key);
    }

    /**
     * @return the buffaloOSGiConfigurerContext
     */
    public static Map getBuffaloOSGiConfigurerContext()
    {
        return buffaloOSGiConfigurerContext;
    }

    public static Object getService(String key)
    {
        return buffaloOSGiConfigurerContext.get(key);
    }

}
