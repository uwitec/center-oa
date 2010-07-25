/**
 * File Name: ConfigLoader.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-4<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.config;


import java.util.Properties;

import com.china.center.tools.RegularExpress;
import com.china.center.tools.StringTools;


/**
 * ConfigLoader
 * 
 * @author ZHUZHU
 * @version 2010-7-4
 * @see ConfigLoader
 * @since 1.0
 */
public abstract class ConfigLoader
{
    private static Properties properties = new Properties();

    public static String getProperty(String key)
    {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue)
    {
        return properties.getProperty(key, defaultValue);
    }

    public static long getLongProperty(String key)
    {
        String tem = properties.getProperty(key);

        if (StringTools.isNullOrNone(tem))
        {
            return 0;
        }

        if ( !RegularExpress.isNumber(tem))
        {
            return 0;
        }

        return Long.parseLong(tem);
    }

    public static long getLongProperty(String key, long defaultValue)
    {
        String tem = properties.getProperty(key, String.valueOf(defaultValue));

        if (StringTools.isNullOrNone(tem))
        {
            return 0;
        }

        if ( !RegularExpress.isNumber(tem))
        {
            return 0;
        }

        return Long.parseLong(tem);
    }

    public static int getIntProperty(String key)
    {
        String tem = properties.getProperty(key);

        if (StringTools.isNullOrNone(tem))
        {
            return 0;
        }

        if ( !RegularExpress.isNumber(tem))
        {
            return 0;
        }

        return Integer.parseInt(tem);
    }

    public static int getIntProperty(String key, int defaultValue)
    {
        String tem = properties.getProperty(key, String.valueOf(defaultValue));

        if (StringTools.isNullOrNone(tem))
        {
            return 0;
        }

        if ( !RegularExpress.isNumber(tem))
        {
            return 0;
        }

        return Integer.parseInt(tem);
    }

    public static boolean getBooleanProperty(String key)
    {
        String tem = properties.getProperty(key);

        if ("true".equalsIgnoreCase(tem))
        {
            return true;
        }

        return false;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue)
    {
        String tem = properties.getProperty(key, String.valueOf(defaultValue));

        if ("true".equalsIgnoreCase(tem))
        {
            return true;
        }

        return false;
    }

    /**
     * @return the properties
     */
    public static Properties getProperties()
    {
        return properties;
    }
}
