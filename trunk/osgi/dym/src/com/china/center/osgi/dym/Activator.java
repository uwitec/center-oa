package com.china.center.osgi.dym;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.center.china.osgi.publics.dym.DynamicApplicationContextTools;


/**
 * Activator
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see Activator
 * @since 1.0
 */
public class Activator implements BundleActivator
{
    private OSGIApplicationContextImpl impl = new OSGIApplicationContextImpl();

    public void start(BundleContext context)
        throws Exception
    {
        DynamicBundleTools.setBundleContext(context);

        this.impl.setBundleContext(context);

        DynamicApplicationContextTools.setOsgiApplicationContext(this.impl);
    }

    public void stop(BundleContext context)
        throws Exception
    {
        DynamicBundleTools.removeBundleContext(context);

        this.impl.removeBundleContext(context);

        DynamicApplicationContextTools.setOsgiApplicationContext(null);
    }

}
