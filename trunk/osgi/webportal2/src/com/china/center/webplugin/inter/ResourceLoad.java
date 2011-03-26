package com.china.center.webplugin.inter;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.core.io.Resource;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;


/**
 * ResourceLoad
 * 
 * @author ZHUZHU
 * @version 2009-8-11
 * @see ResourceLoad
 * @since 1.0
 */
public class ResourceLoad
{
    private static final Log LOGGER = LogFactory.getLog(ResourceLoad.class);

    /**
     * USER_FLODER
     */
    private static final List<String> USER_FLODER = new ArrayList<String>()
    {
        {
            add("web-inf");
            add("js");
            add("css");
            add("images");
            add("public");
            add("common");
            add("meta-inf");
            add("application");
            add("com");
        }
    };

    public static void init(final BundleContext context, final String webPluginName)
        throws Exception
    {
        // here must start a new thread to load resource,or applcation may be wait in this function
        new Thread()
        {
            public void run()
            {
                RegisterWebPlugin server = null;

                ServiceReference reference = null;

                if (USER_FLODER.contains(webPluginName.toLowerCase()))
                {
                    LOGGER.warn("webPluginName[" + webPluginName + "] in " + USER_FLODER
                                + ".So stop init.");

                    return;
                }

                LOGGER.info("****************IN[" + webPluginName + "]*****************");

                while (server == null)
                {
                    reference = context.getServiceReference(RegisterWebPlugin.class.getName());

                    if (reference == null)
                    {
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (Exception e)
                        {
                            LOGGER.error(e, e);
                        }
                        continue;
                    }

                    server = (RegisterWebPlugin)context.getService(reference);

                    if (server == null)
                    {
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (Exception e)
                        {
                            LOGGER.error(e, e);
                        }
                    }
                }

                LOGGER.info("****************OUT[" + webPluginName + "]*****************");

                Bundle bundle = context.getBundle();

                try
                {
                    // first delete resource,then copy resource
                    server.removeWebResource(webPluginName);
                }
                catch (Exception e)
                {
                    LOGGER.error(e, e);
                }

                search(webPluginName, bundle, "osgibundle:webroot/**/*", server);
            }
        }.start();
    }

    private static String getPath(String path)
    {
        int index = path.indexOf("webroot/");

        if (index == -1)
        {
            return "";
        }

        return path.substring(index + "webroot/".length());
    }

    private static String getCommonPath(String path)
    {
        return path.replaceAll("\\\\", "/");
    }

    private static void search(String pnane, Bundle bundle, String pattern, RegisterWebPlugin server)
    {
        OsgiBundleResourcePatternResolver patternResolver = new OsgiBundleResourcePatternResolver(
            bundle);

        try
        {
            for (Resource resource : patternResolver.getResources(pattern))
            {
                InputStream in = null;

                try
                {
                    String path = getCommonPath(getPath(resource.getURI().toString()));

                    if (pnane == null || "".equals(pnane.trim()))
                    {
                        // 防止拷贝受保护的目录
                        boolean protect = false;

                        for (String itemPath : USER_FLODER)
                        {
                            if (path.toLowerCase().startsWith(itemPath + "/"))
                            {
                                protect = true;
                                break;
                            }
                        }

                        if (protect)
                        {
                            continue;
                        }
                    }

                    // 这里不拷贝.svn和CVS
                    if (path.indexOf(".svn") != -1 || path.indexOf("CVS") != -1)
                    {
                        continue;
                    }

                    if (path != null && resource.exists())
                    {
                        in = resource.getInputStream();

                        server
                            .registerWebResource(pnane, getPath(resource.getURI().toString()), in);
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error(e, e);
                }
                finally
                {
                    if (in != null)
                    {
                        try
                        {
                            in.close();
                        }
                        catch (IOException e)
                        {
                            LOGGER.error(e, e);
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            LOGGER.error(ex, ex);
            return;
        }
    }

    /**
     * detory
     * 
     * @param context
     * @param webPluginName
     * @throws Exception
     */
    public static void destory(final BundleContext context, final String webPluginName)
        throws Exception
    {
        if (USER_FLODER.contains(webPluginName.toLowerCase()))
        {
            LOGGER.warn("webPluginName[" + webPluginName + "] in " + USER_FLODER
                        + ".So stop destory.");

            return;
        }

        RegisterWebPlugin server = null;

        LOGGER.info("****************DESTORY-IN[" + webPluginName + "]*****************");

        ServiceReference reference = context.getServiceReference(RegisterWebPlugin.class.getName());

        if (reference == null)
        {
            LOGGER.info("****************DESTORY-OUT[" + webPluginName + "]*****************");
            return;
        }

        server = (RegisterWebPlugin)context.getService(reference);

        if (server == null)
        {
            LOGGER.info("****************DESTORY-OUT[" + webPluginName + "]*****************");
            return;
        }

        server.removeWebResource(webPluginName);

        LOGGER.info("****************DESTORY AND REMOVE-OUT[" + webPluginName
                    + "]*****************");
    }
}
