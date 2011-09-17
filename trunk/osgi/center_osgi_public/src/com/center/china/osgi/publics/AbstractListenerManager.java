/**
 * File Name: AbstractListenerManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-15<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * AbstractListenerManager
 * 
 * @author ZHUZHU
 * @version 2010-8-15
 * @see AbstractListenerManager
 * @since 1.0
 */
public abstract class AbstractListenerManager<Listener extends ParentListener> implements ListenerManager<Listener>
{
    /**
     * 静态的在bundle重新加载的时候不会丢失数据
     */
    public static Map<String, Map> g_listenerMap = new HashMap<String, Map>();

    public void putListener(Listener listener)
    {
        synchronized (g_listenerMap)
        {
            getMap().put(listener.getListenerType(), listener);
        }
    }

    private Map<String, Listener> getMap()
    {
        Map<String, Listener> map = g_listenerMap.get(this.getClass().getName());

        if (map == null)
        {
            map = new HashMap<String, Listener>();

            g_listenerMap.put(this.getClass().getName(), map);
        }

        return map;
    }

    public void removeListener(String listener)
    {
        synchronized (g_listenerMap)
        {
            getMap().remove(listener);
        }
    }

    public Collection<Listener> listenerMapValues()
    {
        synchronized (g_listenerMap)
        {
            Collection<Listener> values = getMap().values();

            return values;
        }
    }

    public Listener findListener(String key)
    {
        synchronized (g_listenerMap)
        {
            Collection<Listener> values = getMap().values();

            for (Listener listener : values)
            {
                if (listener.getListenerType().equals(key))
                {
                    return listener;
                }
            }

            return null;
        }
    }
}
