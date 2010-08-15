/**
 * File Name: DefaultLoadListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-15<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics;


import java.util.List;


/**
 * DefaultLoadListener
 * 
 * @author ZHUZHU
 * @version 2010-8-15
 * @see DefaultLoadListener
 * @since 1.0
 */
public class DefaultLoadListener
{
    private ListenerManager listenerManager = null;

    private List<ParentListener> listenerList = null;

    /**
     * default constructor
     */
    public DefaultLoadListener()
    {
    }

    public void init()
    {
        for (ParentListener each : listenerList)
        {
            listenerManager.putListener(each);
        }
    }

    public void destroy()
    {
        for (ParentListener each : listenerList)
        {
            listenerManager.removeListener(each.getListenerType());
        }
    }

    /**
     * @return the listenerManager
     */
    public ListenerManager getListenerManager()
    {
        return listenerManager;
    }

    /**
     * @param listenerManager
     *            the listenerManager to set
     */
    public void setListenerManager(ListenerManager listenerManager)
    {
        this.listenerManager = listenerManager;
    }

    /**
     * @return the listenerList
     */
    public List<ParentListener> getListenerList()
    {
        return listenerList;
    }

    /**
     * @param listenerList
     *            the listenerList to set
     */
    public void setListenerList(List<ParentListener> listenerList)
    {
        this.listenerList = listenerList;
    }

}
