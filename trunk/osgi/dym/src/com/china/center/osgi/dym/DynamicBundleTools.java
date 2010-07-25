/**
 * File Name: DynamicBundleTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-25<br>
 * Grant: open source to everybody
 */
package com.china.center.osgi.dym;


import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


/**
 * DynamicBundleTools
 * 
 * @author ZHUZHU
 * @version 2010-7-25
 * @see DynamicBundleTools
 * @since 1.0
 */
public class DynamicBundleTools
{
    private static BundleContext bundleContext = null;

    private static boolean ready = false;

    /**
     * isBundleExist
     * 
     * @param bundleName
     * @return
     */
    public static boolean isBundleExist(String bundleName)
    {
        if ( !ready)
        {
            return false;
        }

        Bundle[] bundles = bundleContext.getBundles();

        for (Bundle bundle : bundles)
        {
            if (bundle.getSymbolicName().equalsIgnoreCase(bundleName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * isServiceExist
     * 
     * @param serviceClassName
     * @return
     */
    public static boolean isServiceExist(String serviceClassName)
    {
        if ( !ready)
        {
            return false;
        }

        ServiceReference serviceReference = bundleContext.getServiceReference(serviceClassName);

        if (serviceReference == null)
        {
            return false;
        }

        Object service = bundleContext.getService(serviceReference);

        if (service == null)
        {
            return false;
        }

        return true;
    }

    /**
     * getService
     * 
     * @param <T>
     * @param claz
     * @return
     */
    public static <T> T getService(Class<T> claz)
    {
        if ( !ready)
        {
            return null;
        }

        ServiceReference serviceReference = bundleContext.getServiceReference(claz.getName());

        if (serviceReference == null)
        {
            return null;
        }

        Object service = bundleContext.getService(serviceReference);

        if (service == null)
        {
            return null;
        }

        return (T)service;
    }

    /**
     * isServiceExist(线程睡眠等待)
     * 
     * @param serviceClassName
     * @param waitTime
     * @return
     */
    public static boolean isServiceExist(String serviceClassName, long waitTime)
    {
        if ( !ready)
        {
            return false;
        }

        ServiceReference serviceReference = bundleContext.getServiceReference(serviceClassName);

        if (serviceReference == null)
        {
            try
            {
                Thread.sleep(waitTime);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        serviceReference = bundleContext.getServiceReference(serviceClassName);

        if (serviceReference == null)
        {
            return false;
        }

        Object service = bundleContext.getService(serviceReference);

        if (service == null)
        {
            return false;
        }

        return true;
    }

    /**
     * @return the bundleContext
     */
    public static BundleContext getBundleContext()
    {
        return bundleContext;
    }

    /**
     * @param bundleContext
     *            the bundleContext to set
     */
    protected static void setBundleContext(BundleContext bundleContext)
    {
        DynamicBundleTools.ready = true;

        DynamicBundleTools.bundleContext = bundleContext;
    }

    protected static void removeBundleContext(BundleContext bundleContext)
    {
        DynamicBundleTools.ready = false;

        DynamicBundleTools.bundleContext = null;
    }

    /**
     * @return the ready
     */
    public static boolean isReady()
    {
        return ready;
    }
}
