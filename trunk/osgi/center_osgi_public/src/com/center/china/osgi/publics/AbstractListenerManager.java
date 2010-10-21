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
    protected static Map listenerMap = new HashMap();

    public void putListener(Listener listener)
    {
        listenerMap.put(listener.getListenerType(), listener);
    }

    public void removeListener(String listener)
    {
        listenerMap.remove(listener);
    }

    public Collection<Listener> listenerMapValues()
    {
        return listenerMap.values();
    }
}
