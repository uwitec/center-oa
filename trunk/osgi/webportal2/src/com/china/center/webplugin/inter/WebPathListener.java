package com.china.center.webplugin.inter;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * WebPluginServletContextListener
 * 
 * @author ZHUZHU
 * @version 2009-8-11
 * @see WebPathListener
 * @since 1.0
 */
public class WebPathListener implements ServletContextListener
{
    private static String webRootPath = "";

    private static Map gMap = new HashMap();

    public void contextDestroyed(ServletContextEvent arg0)
    {}

    public void contextInitialized(ServletContextEvent evt)
    {
        ServletContext servletContext = evt.getServletContext();

        webRootPath = servletContext.getRealPath("/");

        evt.getServletContext().setAttribute("ggMap", gMap);
    }

    /**
     * webRootPath
     * 
     * @return the webRootPath
     */
    public static String getWebRootPath()
    {
        return webRootPath;
    }

    /**
     * @return the gMap
     */
    public static Map getGMap()
    {
        return gMap;
    }

}
