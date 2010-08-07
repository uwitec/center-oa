/**
 * File Name: CommonFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.facade;

/**
 * CommonFacade
 * 
 * @author ZHUZHU
 * @version 2010-7-18
 * @see CommonFacade
 * @since 1.0
 */
public interface CommonFacade
{
    /**
     * auth
     * 
     * @param key
     * @param value
     * @return
     */
    boolean auth(String key, String value);
}
