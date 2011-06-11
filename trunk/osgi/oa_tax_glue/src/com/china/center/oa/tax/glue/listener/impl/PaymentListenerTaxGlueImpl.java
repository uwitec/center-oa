/**
 * File Name: PaymentListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.listener.PaymentListener;


/**
 * PaymentListenerTaxGlueImpl
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see PaymentListenerTaxGlueImpl
 * @since 3.0
 */
public class PaymentListenerTaxGlueImpl implements PaymentListener
{

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.listener.PaymentListener#onAddBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.PaymentBean)
     */
    public void onAddBean(User user, PaymentBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.listener.PaymentListener#onDeleteBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.PaymentBean)
     */
    public void onDeleteBean(User user, PaymentBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "PaymentListener.TaxGlueImpl";
    }

    public void onDropBean(User user, PaymentBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

}
