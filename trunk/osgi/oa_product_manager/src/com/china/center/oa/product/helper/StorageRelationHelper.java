/**
 * File Name: StorageRelationHelper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-31<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.helper;

/**
 * StorageRelationHelper
 * 
 * @author ZHUZHU
 * @version 2010-8-31
 * @see StorageRelationHelper
 * @since 1.0
 */
public abstract class StorageRelationHelper
{
    /**
     * getPriceKey
     * 
     * @param value
     * @return
     */
    public static String getPriceKey(double value)
    {
        double last = value * 100;

        return String.valueOf(Math.round(last));
    }
}
