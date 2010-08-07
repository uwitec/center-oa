/**
 * File Name: BuffaloOSGiConfigurerFactory.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-3<br>
 * Grant: open source to everybody
 */
package com.china.center.webplugin.inter;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * BuffaloOSGiConfigurerFactory
 * 
 * @author ZHUZHU
 * @version 2010-7-3
 * @see BuffaloOSGiConfigurerFactory
 * @since 1.0
 */
public class BuffaloOSGiConfigurerFactory
{
    private BuffaloOSGiConfigurer buffaloOSGiConfigurer = null;

    private Map<String, Object> services = new HashMap();

    /**
     * default constructor
     */
    public BuffaloOSGiConfigurerFactory()
    {}

    public void init()
    {
        Set<String> keySet = services.keySet();

        for (String string : keySet)
        {
            buffaloOSGiConfigurer.putBuffaloService(string, services.get(string));
        }
    }

    public void destory()
    {
        Set<String> keySet = services.keySet();

        for (String string : keySet)
        {
            buffaloOSGiConfigurer.removeBuffaloService(string);
        }
    }

    /**
     * @return the buffaloOSGiConfigurer
     */
    public BuffaloOSGiConfigurer getBuffaloOSGiConfigurer()
    {
        return buffaloOSGiConfigurer;
    }

    /**
     * @param buffaloOSGiConfigurer
     *            the buffaloOSGiConfigurer to set
     */
    public void setBuffaloOSGiConfigurer(BuffaloOSGiConfigurer buffaloOSGiConfigurer)
    {
        this.buffaloOSGiConfigurer = buffaloOSGiConfigurer;
    }

    /**
     * @return the services
     */
    public Map getServices()
    {
        return services;
    }

    /**
     * @param services
     *            the services to set
     */
    public void setServices(Map services)
    {
        this.services = services;
    }
}
