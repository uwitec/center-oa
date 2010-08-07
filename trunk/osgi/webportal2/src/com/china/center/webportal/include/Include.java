package com.china.center.webportal.include;


import org.springframework.osgi.web.context.support.OsgiBundleXmlWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.china.center.struts.wrap.WrapActionServlet;
import com.china.center.webplugin.inter.impl.WebPluginServletContextListener;


/**
 * 为了能显示的import这些类
 * 
 * @author ZHUZHU
 * @version 2009-10-13
 * @see Include
 * @since 1.0
 */
public class Include
{
    private OsgiBundleXmlWebApplicationContext context = null;

    private WebPluginServletContextListener listener = null;

    private ContextLoaderListener listener1 = null;

    private WrapActionServlet wrapActionServlet = null;

    /**
     * default constructor
     */
    public Include()
    {}

    /**
     * get context
     * 
     * @return context
     */
    public OsgiBundleXmlWebApplicationContext getContext()
    {
        return context;
    }

    /**
     * set context
     * 
     * @param context
     *            the value of context
     */
    public void setContext(OsgiBundleXmlWebApplicationContext context)
    {
        this.context = context;
    }

    /**
     * get listener
     * 
     * @return listener
     */
    public WebPluginServletContextListener getListener()
    {
        return listener;
    }

    /**
     * set listener
     * 
     * @param listener
     *            the value of listener
     */
    public void setListener(WebPluginServletContextListener listener)
    {
        this.listener = listener;
    }

    /**
     * get listener1
     * 
     * @return listener1
     */
    public ContextLoaderListener getListener1()
    {
        return listener1;
    }

    /**
     * set listener1
     * 
     * @param listener1
     *            the value of listener1
     */
    public void setListener1(ContextLoaderListener listener1)
    {
        this.listener1 = listener1;
    }

    /**
     * get wrapActionServlet
     * 
     * @return wrapActionServlet
     */
    public WrapActionServlet getWrapActionServlet()
    {
        return wrapActionServlet;
    }

    /**
     * set wrapActionServlet
     * 
     * @param wrapActionServlet
     *            the value of wrapActionServlet
     */
    public void setWrapActionServlet(WrapActionServlet wrapActionServlet)
    {
        this.wrapActionServlet = wrapActionServlet;
    }
}
