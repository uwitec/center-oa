/**
 * File Name: GroupListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.listener;


import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.vs.StorageRelationBean;


/**
 * PriceChangeListener
 * 
 * @author ZHUZHU
 * @version 2010-7-4
 * @see PriceChangeListener
 * @since 1.0
 */
public interface PriceChangeListener extends ParentListener
{
    /**
     * onPriceChange
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    void onPriceChange(User user, ProductBean bean)
        throws MYException;

    /**
     * onPriceChange
     * 
     * @param user
     * @param relation
     * @throws MYException
     */
    int onPriceChange2(User user, StorageRelationBean relation);
}
