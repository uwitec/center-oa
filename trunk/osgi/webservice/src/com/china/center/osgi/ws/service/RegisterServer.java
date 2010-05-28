package com.china.center.osgi.ws.service;


import java.util.HashMap;
import java.util.Map;


/**
 * RegisterServer
 * 
 * @author ZHUZHU
 * @version 2010-5-24
 * @see RegisterServer
 * @since 1.0
 */
public class RegisterServer
{
    private ServerManager serverManager = null;

    private Map<String, String> hessianMap = new HashMap();

    private Map<String, String> soapMap = new HashMap();

    private Map<String, SOAPAbstractHandler> soapHandler = new HashMap();

    /**
     * default constructor
     */
    public RegisterServer()
    {}

    public void init()
    {
        for (Map.Entry<String, String> entry : this.hessianMap.entrySet())
        {
            serverManager.registerHessianWSService(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : this.soapMap.entrySet())
        {
            serverManager.registerSOAPWSService(entry.getKey(), entry.getValue(),
                soapHandler.get(entry.getKey()));
        }
    }

    public void destroy()
    {
        for (Map.Entry<String, String> entry : this.hessianMap.entrySet())
        {
            serverManager.logoutWSService(entry.getKey());
        }

        for (Map.Entry<String, String> entry : this.soapMap.entrySet())
        {
            serverManager.logoutWSService(entry.getKey());
        }
    }

    /**
     * @return 返回 serverManager
     */
    public ServerManager getServerManager()
    {
        return serverManager;
    }

    /**
     * @param 对serverManager进行赋值
     */
    public void setServerManager(ServerManager serverManager)
    {
        this.serverManager = serverManager;
    }

    /**
     * @return 返回 hessianMap
     */
    public Map<String, String> getHessianMap()
    {
        return hessianMap;
    }

    /**
     * @param 对hessianMap进行赋值
     */
    public void setHessianMap(Map<String, String> hessianMap)
    {
        this.hessianMap = hessianMap;
    }

    /**
     * @return 返回 soapMap
     */
    public Map<String, String> getSoapMap()
    {
        return soapMap;
    }

    /**
     * @param 对soapMap进行赋值
     */
    public void setSoapMap(Map<String, String> soapMap)
    {
        this.soapMap = soapMap;
    }

    /**
     * get soapHandler
     * 
     * @return soapHandler
     */
    public Map<String, SOAPAbstractHandler> getSoapHandler()
    {
        return soapHandler;
    }

    /**
     * set soapHandler
     * 
     * @param soapHandler
     *            the value of soapHandler
     */
    public void setSoapHandler(Map<String, SOAPAbstractHandler> soapHandler)
    {
        this.soapHandler = soapHandler;
    }
}
