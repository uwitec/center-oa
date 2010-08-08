package com.china.center.common.taglib.active;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * Activator
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see Activator
 * @since 1.0
 */
public class Activator implements BundleActivator
{
    private static BundleContext bundleContext = null;

    public void start(BundleContext context)
        throws Exception
    {
        bundleContext = context;
    }

    public void stop(BundleContext context)
        throws Exception
    {}

    /**
     * @return the bundleContext
     */
    public static BundleContext getBundleContext()
    {
        return bundleContext;
    }

}
