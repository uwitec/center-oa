/**
 * File Name: StorageRelationDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao;


import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.product.vo.StorageRelationVO;
import com.china.center.oa.product.vs.StorageRelationBean;


/**
 * StorageRelationDAO
 * 
 * @author ZHUZHU
 * @version 2010-8-22
 * @see StorageRelationDAO
 * @since 1.0
 */
public interface StorageRelationDAO extends DAO<StorageRelationBean, StorageRelationVO>
{
    int sumProductInStorage(String storageId);

    int sumProductByProductId(String productId);
}
