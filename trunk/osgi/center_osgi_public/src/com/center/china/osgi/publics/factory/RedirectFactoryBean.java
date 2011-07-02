/**
 * File Name: ImplFactoryBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.factory;


import org.springframework.beans.factory.FactoryBean;


/**
 * ImplFactoryBean
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see RedirectFactoryBean
 * @since 1.0
 */
public class RedirectFactoryBean implements FactoryBean
{
    private String interfaceName = "";

    private String serverName = "";

    private RedirectProxy impl = null;

    public void init()
    {
        impl = new RedirectProxy();

        impl.setInterfaceName(interfaceName);

        impl.setServerName(serverName);

        impl.init();
    }

    public Object getObject()
        throws Exception
    {
        return impl.getProxy();
    }

    public Class getObjectType()
    {
        return impl.getLoadClass();
    }

    public boolean isSingleton()
    {
        return true;
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
}
