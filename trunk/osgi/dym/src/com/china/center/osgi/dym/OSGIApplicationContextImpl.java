/**
 * File Name: OSGIApplicationContextImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.china.center.osgi.dym;


import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.BeanFactory;

import com.center.china.osgi.publics.dym.OSGIApplicationContext;


/**
 * OSGIApplicationContextImpl
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see OSGIApplicationContextImpl
 * @since 1.0
 */
public class OSGIApplicationContextImpl implements OSGIApplicationContext
{
    private BundleContext bundleContext = null;

    private boolean ready = false;

    public Object getServiceFromApplicationContext(String idName)
    {
        if ( !this.ready)
        {
            return null;
        }

        ServiceReference[] serviceReferences = null;
        try
        {
            serviceReferences = this.bundleContext.getServiceReferences(BeanFactory.class.getName(), null);
        }
        catch (InvalidSyntaxException e1)
        {
            return null;
        }

        for (ServiceReference serviceReference : serviceReferences)
        {
            BeanFactory applicationContext = (BeanFactory)this.bundleContext.getService(serviceReference);
            try
            {
                Object bean = applicationContext.getBean(idName);

                if (bean != null)
                {
                    return bean;
                }
            }
            catch (Exception e)
            {
            }

        }

        return null;
    }

    public Object getServiceFromApplicationContext(String idName, String bundleName)
    {
        if ( !this.ready)
        {
            return null;
        }

        ServiceReference[] serviceReferences = null;
        try
        {
            serviceReferences = this.bundleContext.getServiceReferences(BeanFactory.class.getName(), null);
        }
        catch (InvalidSyntaxException e1)
        {
            return null;
        }

        for (ServiceReference serviceReference : serviceReferences)
        {
            if ( !serviceReference.getBundle().getSymbolicName().equals(bundleName))
            {
                continue;
            }
            BeanFactory applicationContext = (BeanFactory)this.bundleContext.getService(serviceReference);
            try
            {
                Object bean = applicationContext.getBean(idName);

                if (bean != null)
                {
                    return bean;
                }

            }
            catch (Exception e)
            {
            }

        }

        return null;
    }

    public BundleContext getBundleContext()
    {
        return this.bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext)
    {
        this.ready = true;

        this.bundleContext = bundleContext;
    }

    public void removeBundleContext(BundleContext bundleContext)
    {
        this.ready = false;

        this.bundleContext = null;
    }

    public boolean isReady()
    {
        return this.ready;
    }
}
