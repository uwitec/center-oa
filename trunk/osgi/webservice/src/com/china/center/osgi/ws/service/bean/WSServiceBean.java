package com.china.center.osgi.ws.service.bean;


import com.china.center.osgi.ws.service.SOAPAbstractHandler;
import com.china.center.osgi.ws.service.WSModal;


/**
 * WSServiceBean
 * 
 * @author ZHUZHU
 * @version 2010-5-24
 * @see WSServiceBean
 * @since 1.0
 */
public class WSServiceBean
{
    private String serviceName = "";

    private String serviceInterface = "";

    private long bundleId = 0L;

    private Class serviceClass = null;

    private boolean init = false;

    private WSModal modal = WSModal.HESSIAN;

    private Object server = null;

    private ClassLoader classLoader = null;

    private Object httpRequestHandler = null;

    private SOAPAbstractHandler soapHandler = null;

    /** 
     * 
     */
    public WSServiceBean()
    {}

    /**
     * @return 返回 serviceName
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * @param 对serviceName进行赋值
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    /**
     * @return 返回 serviceInterface
     */
    public String getServiceInterface()
    {
        return serviceInterface;
    }

    /**
     * @param 对serviceInterface进行赋值
     */
    public void setServiceInterface(String serviceInterface)
    {
        this.serviceInterface = serviceInterface;
    }

    /**
     * @return 返回 init
     */
    public boolean isInit()
    {
        return init;
    }

    /**
     * @param 对init进行赋值
     */
    public void setInit(boolean init)
    {
        this.init = init;
    }

    /**
     * @return 返回 server
     */
    public Object getServer()
    {
        return server;
    }

    /**
     * @param 对server进行赋值
     */
    public void setServer(Object server)
    {
        this.server = server;
    }

    /**
     * @return 返回 modal
     */
    public WSModal getModal()
    {
        return modal;
    }

    /**
     * @param 对modal进行赋值
     */
    public void setModal(WSModal modal)
    {
        this.modal = modal;
    }

    /**
     * @return 返回 classLoader
     */
    public ClassLoader getClassLoader()
    {
        return classLoader;
    }

    /**
     * @param 对classLoader进行赋值
     */
    public void setClassLoader(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    /**
     * @return 返回 serviceClass
     */
    public Class getServiceClass()
    {
        return serviceClass;
    }

    /**
     * @param 对serviceClass进行赋值
     */
    public void setServiceClass(Class serviceClass)
    {
        this.serviceClass = serviceClass;
    }

    /**
     * @return 返回 httpRequestHandler
     */
    public Object getHttpRequestHandler()
    {
        return httpRequestHandler;
    }

    /**
     * @param 对httpRequestHandler进行赋值
     */
    public void setHttpRequestHandler(Object httpRequestHandler)
    {
        this.httpRequestHandler = httpRequestHandler;
    }

    /**
     * @return 返回 bundleId
     */
    public long getBundleId()
    {
        return bundleId;
    }

    /**
     * @param 对bundleId进行赋值
     */
    public void setBundleId(long bundleId)
    {
        this.bundleId = bundleId;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue.append(this.serviceInterface).append("(").append(this.modal).append(TAB).append(
            this.init).append(TAB).append(this.bundleId).append(")");

        return retValue.toString();
    }

    /**
     * get soapHandler
     * 
     * @return soapHandler
     */
    public SOAPAbstractHandler getSoapHandler()
    {
        return soapHandler;
    }

    /**
     * set soapHandler
     * 
     * @param soapHandler
     *            the value of soapHandler
     */
    public void setSoapHandler(SOAPAbstractHandler soapHandler)
    {
        this.soapHandler = soapHandler;
    }
}
