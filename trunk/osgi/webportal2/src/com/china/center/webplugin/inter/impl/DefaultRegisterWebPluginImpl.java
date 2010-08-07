package com.china.center.webplugin.inter.impl;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.webplugin.inter.RegisterWebPlugin;


/**
 * DefaultRegisterWebPluginImpl
 * 
 * @author ZHUZHU
 * @version 2009-8-11
 * @see DefaultRegisterWebPluginImpl
 * @since 1.0
 */
public class DefaultRegisterWebPluginImpl implements RegisterWebPlugin
{
    private final Log _logger = LogFactory.getLog(getClass());

    /**
     * registerWebResource
     * 
     * @param webPluginName
     * @param filePath
     * @param webResource(in
     *            this function,application do not close InputStream)
     * @return
     */
    public boolean registerWebResource(String webPluginName, String filePath,
                                       InputStream webResource)
    {
        String webRootPath = getCommonPath(WebPluginServletContextListener.getWebRootPath());

        String pluginRoot = "";

        if (webPluginName == null || "".equals(webPluginName.trim()))
        {
            pluginRoot = webRootPath;
        }
        else
        {
            pluginRoot = webRootPath + webPluginName + "/";
        }

        mkdir(pluginRoot);

        String cfilePath = getCommonPath(filePath);

        String dirPath = pluginRoot + cfilePath;

        _logger.info("add resource in web portal:" + dirPath);

        mkdir2(pluginRoot, cfilePath);

        writeFile(webResource, dirPath);

        return true;
    }

    private String getCommonPath(String path)
    {
        return path.replaceAll("\\\\", "/");
    }

    private void mkdir(String pluginRoot)
    {
        try
        {
            File dir = new File(pluginRoot);

            dir.mkdir();
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
    }

    private void mkdir2(String pluginRoot, String cfilePath)
    {
        String[] split = cfilePath.split("/");

        String path = pluginRoot;

        for (int i = 0; i < split.length - 1; i++ )
        {
            path = path + '/' + split[i];

            try
            {
                File dir = new File(path);

                dir.mkdir();
            }
            catch (Exception e)
            {
                _logger.error(e, e);
            }
        }

    }

    private void writeFile(InputStream in, final String toPath)
    {
        OutputStream out = null;
        try
        {
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
                out.close();
            }
            catch (Exception e)
            {}
        }
    }

    /**
     * removeWebResource
     * 
     * @param webPluginName
     * @return
     */
    public boolean removeWebResource(String webPluginName)
    {
        // if empty return
        if (webPluginName == null || "".equals(webPluginName.trim()))
        {
            return true;
        }

        String webRootPath = getCommonPath(WebPluginServletContextListener.getWebRootPath());

        String pluginRoot = webRootPath + webPluginName + "/";

        try
        {
            delete(pluginRoot);

            _logger.info("delete path:" + pluginRoot + " success");
        }
        catch (IOException e)
        {
            _logger.error(e, e);
        }

        return true;
    }

    private void delete(String path)
        throws IOException
    {
        deleteIn(path);
    }

    private void deleteIn(String path)
        throws IOException
    {
        File file = new File(path);

        File[] f = file.listFiles();

        if (f != null)
        {
            for (int i = 0; i < f.length; i++ )
            {
                if (f[i].isFile())
                {
                    f[i].delete();
                }
                else
                {
                    deleteIn(f[i].getPath());
                    f[i].delete();
                }
            }
        }
    }
}
