/**
 * File Name: OSGIApplicationContext.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.dym;

/**
 * OSGIApplicationContext
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see OSGIApplicationContext
 * @since 1.0
 */
public interface OSGIApplicationContext
{
    Object getServiceFromApplicationContext(String paramString);

    Object getServiceFromApplicationContext(String paramString1, String paramString2);
}
