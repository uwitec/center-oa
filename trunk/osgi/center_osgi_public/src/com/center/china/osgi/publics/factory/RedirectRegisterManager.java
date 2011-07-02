/**
 * File Name: RedirectRegisterManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.factory;


import java.util.HashMap;
import java.util.Map;


/**
 * RedirectRegisterManager
 * 
 * @author ZHUZHU
 * @version 2011-7-2
 * @see RedirectRegisterManager
 * @since 3.0
 */
public class RedirectRegisterManager
{
    protected static Map<String, Object> g_registerMap = new HashMap<String, Object>();

    private Map<String, Object> registerMap = new HashMap<String, Object>();

    /**
     * default constructor
     */
    public RedirectRegisterManager()
    {
    }

    public void init()
    {
        synchronized (RedirectRegisterManager.g_registerMap)
        {
            for (Map.Entry<String, Object> each : registerMap.entrySet())
            {
                RedirectRegisterManager.g_registerMap.put(each.getKey(), each.getValue());
            }
        }
    }

    public void destroy()
    {
        synchronized (RedirectRegisterManager.g_registerMap)
        {
            for (Map.Entry<String, Object> each : registerMap.entrySet())
            {
                RedirectRegisterManager.g_registerMap.remove(each.getKey());
            }
        }
    }

    public static Object findService(String key)
    {
        synchronized (RedirectRegisterManager.g_registerMap)
        {
            return RedirectRegisterManager.g_registerMap.get(key);
        }
    }

    /**
     * @return the registerMap
     */
    public Map<String, Object> getRegisterMap()
    {
        return registerMap;
    }

    /**
     * @param registerMap
     *            the registerMap to set
     */
    public void setRegisterMap(Map<String, Object> registerMap)
    {
        this.registerMap = registerMap;
    }
}
