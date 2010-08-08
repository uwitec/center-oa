package com.china.center.common.taglib;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * 页面标签的加载类
 * 
 * @author admin
 * @version [版本号, 2007-10-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TagServletContextListener implements ServletContextListener, TagLibConstant
{
    /**
     * 加载的图片
     */
    private static final List<String> IMAGE_RESOURCE_NAMES = Arrays.asList(new String[] {
        "center_07.gif", "center_08.gif", "center_10.gif", "center_12.gif", "dot_a.gif",
        "dot_line.gif", "dot_r.gif", "calendar.gif"});

    /**
     * 加载的CSS样式
     */
    private List<String> CSS_RESOURCE_NAMES = Arrays.asList(new String[] {CSS_FILE_NAME});

    /**
     * 加载的时间JS目录
     */
    private List<String> JS_RESOURCE_NAMES = Arrays.asList(new String[] {"calendar.js",
        "calendar_debug.js", "lang/calendar-zh.js", "lang/cn_utf8.js", "skins/aqua/active-bg.gif",
        "skins/aqua/dark-bg.gif", "skins/aqua/hover-bg.gif", "skins/aqua/menuarrow.gif",
        "skins/aqua/normal-bg.gif", "skins/aqua/rowhover-bg.gif", "skins/aqua/status-bg.gif",
        "skins/aqua/theme.css", "skins/aqua/title-bg.gif", "skins/aqua/today-bg.gif"});

    /**
     * 加载的JS的资源文件
     */
    private List<String> JS_PUBLIC_LANG_NAMES = Arrays.asList(new String[] {
        "lang/zh/public_resources.js", "lang/eng/public_resources.js",
        "lang/utf8/public_resources.js"});

    public void contextDestroyed(ServletContextEvent arg0)
    {

    }

    public void contextInitialized(ServletContextEvent evt)
    {
        System.out.println("Loading Page Tag Resource...");

        ServletContext servletContext = evt.getServletContext();

        // 建立资源目录
        createDitchnetDir(servletContext);

        String sourcePath, destPath;

        URL sourceURL = null;

        String configLocation = evt.getServletContext().getInitParameter("CSS_FILE");

        if (configLocation != null && !"".equals(configLocation.trim()))
        {
            CSS_RESOURCE_NAMES = new ArrayList<String>();

            CSS_RESOURCE_NAMES.add(configLocation);
        }

        // 样式拷贝文件
        for (String fileName : CSS_RESOURCE_NAMES)
        {
            sourcePath = fileName;
            sourceURL = getClass().getResource(sourcePath);

            destPath = CSS_FOLDER_NAME + fileName;
            destPath = servletContext.getRealPath(destPath);

            writeFile(sourceURL, destPath);
        }

        // 拷贝资源文件
        for (String fileName : IMAGE_RESOURCE_NAMES)
        {
            sourcePath = fileName;
            sourceURL = getClass().getResource(sourcePath);

            destPath = DEST_FOLDER_NAME + fileName;
            destPath = servletContext.getRealPath(destPath);

            writeFile(sourceURL, destPath);
        }

        // 加载的JS的资源文件
        for (String fileName : JS_PUBLIC_LANG_NAMES)
        {
            sourcePath = fileName;

            sourceURL = getClass().getResource(sourcePath);

            destPath = JS_FOLDER_NAME + fileName;

            destPath = servletContext.getRealPath(destPath);

            writeFile(sourceURL, destPath);
        }

        // 拷贝日期资源目录
        for (String fileName : JS_RESOURCE_NAMES)
        {
            sourceURL = getClass().getResource(CAL_JS_FOLDER_NAME + fileName);

            destPath = JS_FOLDER_NAME + CAL_JS_FOLDER_NAME + fileName;

            destPath = servletContext.getRealPath(destPath);

            writeFile(sourceURL, destPath);
        }
    }

    private void createDitchnetDir(final ServletContext servletContext)
    {
        String[] floders = new String[] {DEST_FOLDER_NAME, CSS_FOLDER_NAME, JS_FOLDER_NAME};

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
                // log.error("Error creating Ditchnet dir");
            }
        }

    }

    private static void mkdirs(String toPath)
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

    private void writeFile(final URL fromURL, final String toPath)
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

}
