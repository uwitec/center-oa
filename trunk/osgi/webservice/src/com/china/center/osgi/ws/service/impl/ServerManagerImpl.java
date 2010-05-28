package com.china.center.osgi.ws.service.impl;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.service.ServiceFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.china.center.osgi.ws.service.SOAPAbstractHandler;
import com.china.center.osgi.ws.service.ServerManager;
import com.china.center.osgi.ws.service.WSModal;
import com.china.center.osgi.ws.service.bean.WSServiceBean;


public class ServerManagerImpl implements ServerManager, ApplicationContextAware
{
    private final Log _logger = LogFactory.getLog(getClass());

    /**
     * bundle context
     */
    private BundleContext bundleContext = null;

    private ApplicationContext applicationContext = null;

    public static Map<String, WSServiceBean> wsMap = new ConcurrentHashMap();

    /** 
     * 
     */
    public ServerManagerImpl()
    {}

    /**
     * @param serviceName
     * @return
     */
    public synchronized boolean logoutWSService(String serviceName)
    {
        Object oo = wsMap.remove(serviceName);

        _logger.info("logoutWSService:" + serviceName);

        return oo != null;
    }

    /**
     * @param serviceName
     * @param serviceInterface
     * @param modal
     * @return
     */
    public synchronized boolean registerHessianWSService(String serviceName,
                                                         String serviceInterface)
    {
        if (wsMap.containsKey(serviceName))
        {
            logoutWSService(serviceName);
        }

        WSServiceBean wsBean = new WSServiceBean();

        wsBean.setModal(WSModal.HESSIAN);

        wsBean.setServiceName(serviceName);

        wsBean.setServiceInterface(serviceInterface);

        wsBean.setInit(false);

        wsMap.put(serviceName, wsBean);

        // initWSService(serviceName);

        return true;
    }

    public synchronized boolean registerSOAPWSService(String serviceName, String serviceInterface,
                                                      SOAPAbstractHandler handler)
    {
        if (wsMap.containsKey(serviceName))
        {
            logoutWSService(serviceName);
        }

        WSServiceBean wsBean = new WSServiceBean();

        wsBean.setModal(WSModal.SOAP);

        wsBean.setServiceName(serviceName);

        wsBean.setServiceInterface(serviceInterface);

        wsBean.setSoapHandler(handler);

        wsBean.setInit(false);

        wsMap.put(serviceName, wsBean);

        // initWSService(serviceName);

        return true;
    }

    public void init()
    {
        new InnerThread().start();
    }

    /**
     * InnerThread
     */
    class InnerThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e)
                {
                    _logger.error(e, e);
                }

                synchronized (this)
                {
                    for (Map.Entry<String, WSServiceBean> entry : wsMap.entrySet())
                    {
                        if ( !entry.getValue().isInit())
                        {
                            initWSService(entry.getKey());
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化webservice服务
     * 
     * @param serviceName
     * @return
     */
    public synchronized boolean initWSService(String serviceName)
    {
        WSServiceBean wsBean = wsMap.get(serviceName);

        if (wsBean == null)
        {
            return false;
        }

        if (wsBean.isInit())
        {
            return true;
        }

        _logger.info("init initWSService:" + wsBean);

        ServiceReference reference = bundleContext.getServiceReference(wsBean.getServiceInterface());

        if (reference == null)
        {
            return false;
        }

        Object server = bundleContext.getService(reference);

        if (server != null)
        {
            wsBean.setBundleId(reference.getBundle().getBundleId());

            wsBean.setServer(server);

            wsBean.setClassLoader(server.getClass().getClassLoader());

            Class[] interfaces = server.getClass().getInterfaces();

            for (Class eachClass : interfaces)
            {
                if (eachClass.getName().equals(wsBean.getServiceInterface()))
                {
                    wsBean.setServiceClass(eachClass);
                }
            }

            if (wsBean.getModal() == WSModal.HESSIAN)
            {
                wsBean.setHttpRequestHandler(ServerTools.createHessianServiceExporter(wsBean));
            }
            else
            {
                ServiceFactory serviceFactory = (ServiceFactory)applicationContext.getBean("xfire.serviceFactory");

                XFire xfire = (XFire)applicationContext.getBean("xfire");

                wsBean.setHttpRequestHandler(ServerTools.createXFireExporter(wsBean,
                    serviceFactory, xfire, applicationContext));
            }

            wsBean.setInit(true);

            return true;
        }

        return false;
    }

    /**
     * @return 返回 bundleContext
     */
    public BundleContext getBundleContext()
    {
        return bundleContext;
    }

    /**
     * @param 对bundleContext进行赋值
     */
    public void setBundleContext(BundleContext bundleContext)
    {
        this.bundleContext = bundleContext;
    }

    public void setApplicationContext(ApplicationContext context)
        throws BeansException
    {
        this.applicationContext = context;
    }

    public synchronized void logoutBundleWSService(long bundleId)
    {
        Set<String> keySet = wsMap.keySet();

        for (String key : keySet)
        {
            WSServiceBean serviceBean = wsMap.get(key);

            if (serviceBean != null)
            {
                if (serviceBean.getBundleId() == bundleId)
                {
                    logoutWSService(key);
                }
            }
        }
    }
}
