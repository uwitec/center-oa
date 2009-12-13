/**
 * File Name: PluginManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.manager;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import net.sourceforge.sannotations.annotation.Bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.china.center.oa.flow.plugin.HandlerPlugin;
import com.china.center.tools.ResourceLocator;


/**
 * PluginManager
 * 
 * @author zhuzhu
 * @version 2009-5-3
 * @see PluginManager
 * @since 1.0
 */
@Bean(name = "pluginManager")
public class PluginManager implements ApplicationContextAware
{
    private final Log _logger = LogFactory.getLog(getClass());

    private static ApplicationContext content = null;

    private String proPath = "classpath:config/flowPlugin.properties";

    private Properties properties = new Properties();

    private List<HandlerPlugin> handlers = new ArrayList<HandlerPlugin>();

    /**
     * default constructor
     */
    public PluginManager()
    {}

    public boolean hasAuth(String instanceId, int pluginType, List<String> processers)
    {
        HandlerPlugin handler = getHandlerPlugin(pluginType);

        if (handler == null)
        {
            return false;
        }

        return handler.hasAuth(instanceId, processers);
    }

    /**
     * getHandlerPlugin
     * 
     * @param type
     * @return
     */
    public HandlerPlugin getHandlerPlugin(int type)
    {
        for (HandlerPlugin handlerPlugin : handlers)
        {
            if (handlerPlugin.getType() == type)
            {
                return handlerPlugin;
            }
        }

        return null;
    }

    public void setApplicationContext(ApplicationContext arg0)
        throws BeansException
    {
        InputStream in = null;

        try
        {
            in = ResourceLocator.getResource(proPath);

            properties.load(in);
        }
        catch (FileNotFoundException e)
        {
            _logger.error(e, e);
        }
        catch (IOException e)
        {
            _logger.error(e, e);
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
                    _logger.error(e, e);
                }
            }
        }

        content = arg0;

        Collection coll = properties.values();

        for (Object object : coll)
        {
            String value = object.toString();

            HandlerPlugin item = (HandlerPlugin)content.getBean(value);

            handlers.add(item);
        }
    }
}
