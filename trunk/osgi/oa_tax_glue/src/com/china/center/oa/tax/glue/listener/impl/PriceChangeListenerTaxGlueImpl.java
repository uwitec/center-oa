/**
 * File Name: PriceChangeListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.bean.PriceChangeBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.listener.PriceChangeListener;
import com.china.center.oa.product.vs.StorageRelationBean;


/**
 * TODO_OSGI 产品调价
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see PriceChangeListenerTaxGlueImpl
 * @since 3.0
 */
public class PriceChangeListenerTaxGlueImpl implements PriceChangeListener
{

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.listener.PriceChangeListener#onConfirmPriceChange(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.PriceChangeBean)
     */
    public void onConfirmPriceChange(User user, PriceChangeBean bean)
        throws MYException
    {
        // 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.listener.PriceChangeListener#onPriceChange(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.ProductBean)
     */
    public void onPriceChange(User user, ProductBean bean)
        throws MYException
    {
        // 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.listener.PriceChangeListener#onPriceChange2(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.vs.StorageRelationBean)
     */
    public int onPriceChange2(User user, StorageRelationBean relation)
    {
        // 
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.listener.PriceChangeListener#onPriceChange3(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.vs.StorageRelationBean)
     */
    public int onPriceChange3(User user, StorageRelationBean relation)
    {
        // 
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "PriceChangeListener.TaxGlueImpl";
    }

    public void onRollbackPriceChange(User user, PriceChangeBean bean)
        throws MYException
    {
        // 

    }

}
