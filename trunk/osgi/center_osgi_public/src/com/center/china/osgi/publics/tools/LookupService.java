/**
 * File Name: LookService.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.tools;


import com.center.china.osgi.publics.dym.DynamicApplicationContextTools;


/**
 * LookService
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see LookupService
 * @since 1.0
 */
public abstract class LookupService
{
    /**
     * DYM get server by serverName
     * 
     * @param name
     * @return
     */
    public static Object findService(String name)
    {
        int lastIndexOf = name.lastIndexOf(".");

        String idName = name.substring(lastIndexOf + 1);

        String bundleName = name.substring(0, lastIndexOf);

        return DynamicApplicationContextTools.getServiceFromApplicationContext(idName, bundleName);
    }
}
