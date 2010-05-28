package com.china.center.webplugin.inter.impl;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * WebPluginServletContextListener
 * 
 * @author ZHUZHU
 * @version 2009-8-11
 * @see WebPluginServletContextListener
 * @since 1.0
 */
public class WebPluginServletContextListener implements ServletContextListener
{
    private final Log _logger = LogFactory.getLog(getClass());

    public static final String WEBPLUGIN = "webplugin/";

    private static String webRootPath = "";

    public void contextDestroyed(ServletContextEvent arg0)
    {

    }

    public void contextInitialized(ServletContextEvent evt)
    {
        ServletContext servletContext = evt.getServletContext();

        webRootPath = servletContext.getRealPath("/");

        _logger.info("web root path is:" + webRootPath);

        // 建立资源目录
        createDitchnetDir(servletContext);
    }

    private void createDitchnetDir(final ServletContext servletContext)
    {
        String[] floders = new String[] {"/" + WEBPLUGIN};

        for (String floder : floders)
        {
            floder = servletContext.getRealPath(floder);

            File dir = null;
            try
            {
                dir = new File(floder);
                dir.mkdir();
            }
            catch (Exception e)
            {
                _logger.error(e, e);
            }
        }

    }

    public static void mkdirs(String toPath)
    {
        File file = new File(toPath);
        List<File> files = new ArrayList<File>();

        boolean bb = true;
        while (bb)
        {
            file = file.getParentFile();

            files.add(file);

            if (file.exists())
            {
                bb = false;
            }
        }

        for (File element : files)
        {
            element.mkdirs();
        }
    }

    public void writeFile(final URL fromURL, final String toPath)
    {
        mkdirs(toPath);
        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = new BufferedInputStream(fromURL.openStream());
            out = new BufferedOutputStream(new FileOutputStream(toPath));

            int len;
            byte[] buffer = new byte[4096];

            while ( (len = in.read(buffer, 0, buffer.length)) != -1)
            {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
        catch (Exception e)
        {
            // log.error("Error writing file dude: " + e.getMessage());
        }
        finally
        {
            try
            {
                in.close();
                out.close();
            }
            catch (Exception e)
            {}
        }
    }

    /**
     * get webRootPath
     * 
     * @return webRootPath
     */
    public static String getWebRootPath()
    {
        while ("".equals(webRootPath))
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {}
        }

        return webRootPath;
    }

}
