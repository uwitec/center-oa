/**
 * File Name: FilterListenerService.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-3<br>
 * Grant: open source to everybody
 */
package com.china.center.webportal.filter;


import java.util.HashMap;
import java.util.Map;


/**
 * FilterListenerService
 * 
 * @author ZHUZHU
 * @version 2010-7-3
 * @see FilterListenerService
 * @since 1.0
 */
public class FilterListenerService
{
    private static Map<String, FilterListener> listenerMap = new HashMap();

    public static void putFilterListener(FilterListener listener)
    {
        listenerMap.put(listener.getListenerType(), listener);
    }

    public static void removeFilterListener(String key)
    {
        listenerMap.remove(key);
    }

    /**
     * @return the listenerMap
     */
    public static Map<String, FilterListener> getListenerMap()
    {
        return listenerMap;
    }
}
