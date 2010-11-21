/**
 * File Name: StorageRelationListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.listener;


import com.center.china.osgi.publics.ParentListener;
import com.china.center.oa.product.vs.StorageRelationBean;


/**
 * StorageRelationListener
 * 
 * @author ZHUZHU
 * @version 2010-11-21
 * @see StorageRelationListener
 * @since 3.0
 */
public interface StorageRelationListener extends ParentListener
{
    /**
     * 查询预占的库存
     * 
     * @param bean
     *            当前实际的库存
     * @return
     */
    int onFindPreassignByStorageRelation(StorageRelationBean bean);
}
