/**
 * File Name: RedrictProxy.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.factory;


import java.lang.reflect.Proxy;


/**
 * RedrictProxy
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see RedirectProxy
 * @since 1.0
 */
public class RedirectProxy
{
    private String interfaceName = "";

    private String serverName = "";

    private Class<?> loadClass = null;

    private Object proxy = null;

    /**
     * default constructor
     */
    public RedirectProxy()
    {
    }

    public void init()
    {
        this.proxy = getServerProxy();
    }

    public Object getServerProxy()
    {
        RedirectInvocationHandler handler = new RedirectInvocationHandler();

        handler.setServerName(this.serverName);

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        Class<?> loadClass = null;

        try
        {
            loadClass = contextClassLoader.loadClass(interfaceName);
        }
        catch (ClassNotFoundException e)
        {

        }

        Object proxy = Proxy.newProxyInstance(contextClassLoader, new Class[] {loadClass}, handler);

        return proxy;
    }

    /**
     * @return the interfaceName
     */
    public String getInterfaceName()
    {
        return interfaceName;
    }

    /**
     * @param interfaceName
     *            the interfaceName to set
     */
    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
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

    /**
     * @return the loadClass
     */
    public Class<?> getLoadClass()
    {
        return loadClass;
    }

    /**
     * @return the proxy
     */
    public Object getProxy()
    {
        return proxy;
    }
}
