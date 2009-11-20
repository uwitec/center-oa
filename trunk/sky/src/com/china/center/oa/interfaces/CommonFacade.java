/**
 * File Name: CommonFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-23<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.interfaces;

/**
 * CommonFacade
 * 
 * @author zhuzhu
 * @version 2009-4-23
 * @see CommonFacade
 * @since 1.0
 */
public interface CommonFacade
{
    /**
     * auth
     * @param key
     * @param value
     * @return
     */
    boolean auth(String key, String value);
}
