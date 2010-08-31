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

    /**
     * deleteStorageRelation(删除产品数量为0的数据)
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean deleteStorageRelation(User user, String id)
        throws MYException;

    /**
     * transferStorageRelation(储位产品转移)
     * 
     * @param user
     * @param sourceStorageId
     * @param dirStorageId
     * @param relations
     * @return
     * @throws MYException
     */
    boolean transferStorageRelation(User user, String sourceStorageId, String dirStorageId, String[] relations)
        throws MYException;
}
