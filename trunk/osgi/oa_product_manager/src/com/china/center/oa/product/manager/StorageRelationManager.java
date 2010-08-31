/**
 * File Name: StorageRelationManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.wrap.ProductChangeWrap;


/**
 * StorageRelationManager
 * 
 * @author ZHUZHU
 * @version 2010-8-25
 * @see StorageRelationManager
 * @since 1.0
 */
public interface StorageRelationManager
{
    /**
     * changeStorageRelationWithOutTransaction(没有事务的)
     * 
     * @param user
     * @param bean
     * @return
     */
    boolean changeStorageRelationWithoutTransaction(User user, ProductChangeWrap bean)
        throws MYException;

    /**
     * changeStorageRelationWithTransaction(单独事务)
     * 
     * @param user
     * @param bean
     * @return
     */
    boolean changeStorageRelationWithTransaction(User user, ProductChangeWrap bean)
        throws MYException;
}
