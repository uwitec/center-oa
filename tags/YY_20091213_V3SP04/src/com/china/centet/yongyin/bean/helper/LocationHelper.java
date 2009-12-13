/*
 * File Name: LocationHelper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-19
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean.helper;

/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2008-1-19
 * @see
 * @since
 */
public abstract class LocationHelper
{
    /**
     * 超级区域的ID
     */
    public static String SYSTEMLOCATION = "0";

    /**
     * 是否是超级区域
     * 
     * @param locationId
     * @return
     */
    public static boolean isSystemLocation(String locationId)
    {
        if (SYSTEMLOCATION.equals(locationId))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
