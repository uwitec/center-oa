/**
 * File Name: AbstractListenerManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-15<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics;

/**
 * AbstractListenerManager
 * 
 * @author ZHUZHU
 * @version 2010-8-15
 * @see ListenerManager
 * @since 1.0
 */
public interface ListenerManager<Listener extends ParentListener>
{
    /**
     * putListener
     * 
     * @param listener
     */
    void putListener(Listener listener);

    /**
     * removeListener
     * 
     * @param listener
     */
    void removeListener(String listener);

    /**
     * getImplClassName
     * 
     * @return
     */
    String getImplClassName();
}
