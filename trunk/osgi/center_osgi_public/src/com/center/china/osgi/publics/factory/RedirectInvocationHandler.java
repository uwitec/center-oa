/**
 * File Name: RedrictInvocationHandler.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.factory;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.center.china.osgi.publics.NoServiceFoundException;


/**
 * RedrictInvocationHandler
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see RedirectInvocationHandler
 * @since 1.0
 */
public class RedirectInvocationHandler implements InvocationHandler
{
    private String serverName = "";

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        return method.invoke(getTarget(), args);
    }

    /**
     * @return the target
     */
    public Object getTarget()
    {
        Object findService = RedirectRegisterManager.findService(this.serverName);

        if (findService == null)
        {
            throw new NoServiceFoundException(this.serverName + " not found!");
        }

        return findService;
    }

    /**
     * @return the serverName
     */
    public String getServerName()
    {
        return serverName;
    }

    /**
     * @param serverName
     *            the serverName to set
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }
}
