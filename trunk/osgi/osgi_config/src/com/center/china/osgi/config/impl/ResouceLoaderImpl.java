/**
 * File Name: ResouceLoaderImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-4<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.config.impl;


import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.center.china.osgi.config.ConfigLoader;


/**
 * ResouceLoaderImpl
 * 
 * @author ZHUZHU
 * @version 2010-7-4
 * @see ResouceLoaderImpl
 * @since 1.0
 */
public class ResouceLoaderImpl
{
    private final Log _logger = LogFactory.getLog(getClass());

    private String configPath = "";

    /**
     * default constructor
     */
    public ResouceLoaderImpl()
    {
    }

    public void loadProperties()
    {
        try
        {
            ConfigLoader.getProperties().loadFromXML(ResourceLocator.getResource("classpath:config/config.xml"));
        }
        catch (FileNotFoundException e)
        {
            _logger.error(e, e);
        }
        catch (IOException e)
        {
            _logger.error(e, e);
        }
    }

    /**
     * @return the configPath
     */
    public String getConfigPath()
    {
        return configPath;
    }

    /**
     * @param configPath
     *            the configPath to set
     */
    public void setConfigPath(String configPath)
    {
        this.configPath = configPath;
    }

}
