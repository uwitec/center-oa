package com.china.center.osgi.ws.service;

/**
 * ServerManager
 * 
 * @author ZHUZHU
 * @version 2010-5-24
 * @see ServerManager
 * @since 1.0
 */
public interface ServerManager
{
    boolean registerHessianWSService(String serviceName, String serviceInterface);

    boolean registerSOAPWSService(String serviceName, String serviceInterface,
                                  SOAPAbstractHandler handler);

    boolean logoutWSService(String serviceName);

    boolean initWSService(String serviceName);

    void logoutBundleWSService(long bundleId);
}
