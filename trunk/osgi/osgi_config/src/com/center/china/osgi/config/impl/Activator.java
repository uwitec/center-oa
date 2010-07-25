package com.center.china.osgi.config.impl;


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
    public void start(BundleContext context)
        throws Exception
    {
        ResouceLoaderImpl impl = new ResouceLoaderImpl();

        impl.loadProperties();
    }

    public void stop(BundleContext context)
        throws Exception
    {
    }
}
