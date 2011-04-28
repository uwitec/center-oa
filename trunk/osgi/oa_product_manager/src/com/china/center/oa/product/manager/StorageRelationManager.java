/**
 * File Name: StorageRelationManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager;


import com.center.china.osgi.publics.ListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.listener.StorageRelationListener;
import com.china.center.oa.product.vs.StorageRelationBean;
import com.china.center.oa.product.wrap.ProductChangeWrap;


/**
 * StorageRelationManager
 * 
 * @author ZHUZHU
 * @version 2010-8-25
 * @see StorageRelationManager
 * @since 1.0
 */
public interface StorageRelationManager extends ListenerManager<StorageRelationListener>
{
    /**
     * changeStorageRelationWithOutTransaction(没有事务的,所有的库存逻辑都在此方法内实现,其他方法没有操作库存的)
     * 
     * @param user
     * @param bean
     * @param deleteZeroRelation
     *            是否删除为0的产品库存
     * @return
     */
    StorageRelationBean changeStorageRelationWithoutTransaction(User user, ProductChangeWrap bean,
                                                                boolean deleteZeroRelation)
        throws MYException;

    /**
     * changeStorageRelationWithTransaction(单独事务)
     * 
     * @param user
     * @param bean
     * @param deleteZeroRelation
     *            是否删除为0的产品库存
     * @return
     */
    StorageRelationBean changeStorageRelationWithTransaction(User user, ProductChangeWrap bean,
                                                             boolean deleteZeroRelation)
        throws MYException;

    /**
     * 检查库存是否满足要求(这里的relationId不启用,且会使用在途库存进行合计)
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    boolean checkStorageRelation(ProductChangeWrap bean, boolean includeSelf)
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
     * transferStorageRelation(储位产品转移,公私都可以转)
     * 
     * @param user
     * @param sourceStorageId
     * @param dirStorageId
     * @param relations
     * @return
     * @throws MYException
     */
    boolean transferStorageRelation(User user, String sourceStorageId, String dirStorageId,
                                    String[] relations)
        throws MYException;

    /**
     * transferStorageRelationInDepotpart(仓区产品转移)
     * 
     * @param user
     * @param sourceRelationId
     * @param dirDepotpartId
     * @param amount
     * @return
     * @throws MYException
     */
    String transferStorageRelationInDepotpart(User user, String sourceRelationId,
                                              String dirDepotpartId, int amount, String apply)
        throws MYException;

    void lockStorageRelation();

    void unlockStorageRelation();

    boolean isStorageRelationLock();

    /**
     * 合计已经在预占的库存
     * 
     * @param bean
     * @return
     */
    int sumPreassignByStorageRelation(StorageRelationBean bean);

    /**
     * 合计已经在预占的库存
     * 
     * @param bean
     * @return
     */
    int sumInwayByStorageRelation(StorageRelationBean bean);

    /**
     * initPriceKey
     * 
     * @return
     */
    int[] initPriceKey();
}
