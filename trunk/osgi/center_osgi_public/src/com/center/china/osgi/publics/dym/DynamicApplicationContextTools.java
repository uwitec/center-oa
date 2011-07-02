/**
 * File Name: DynamicApplicationContextTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.dym;

/**
 * DynamicApplicationContextTools
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see DynamicApplicationContextTools
 * @since 1.0
 */
public class DynamicApplicationContextTools
{
    private static OSGIApplicationContext osgiApplicationContext = null;

    public static Object getServiceFromApplicationContext(String idName)
    {
        if (osgiApplicationContext == null)
        {
            return null;
        }

        return getOsgiApplicationContext().getServiceFromApplicationContext(idName);
    }

    public static Object getServiceFromApplicationContext(String idName, String bundleName)
    {
        if (osgiApplicationContext == null)
        {
            return null;
        }

        return getOsgiApplicationContext().getServiceFromApplicationContext(idName, bundleName);
    }

    public static OSGIApplicationContext getOsgiApplicationContext()
    {
        return osgiApplicationContext;
    }

    public static void setOsgiApplicationContext(OSGIApplicationContext osgiApplicationContext)
    {
        DynamicApplicationContextTools.osgiApplicationContext = osgiApplicationContext;
    }
}
