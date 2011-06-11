/**
 * File Name: PaymentApplyListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.listener.PaymentApplyListener;


/**
 * TODO_OSGI 回款转预收/销售单绑定(预收转应收)/预收转费用
 * 
 * @author ZHUZHU
 * @version 2011-6-11
 * @see PaymentApplyListenerTaxGlueImpl
 * @since 3.0
 */
public class PaymentApplyListenerTaxGlueImpl implements PaymentApplyListener
{

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.listener.PaymentApplyListener#onPassBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.PaymentApplyBean)
     */
    public void onPassBean(User user, PaymentApplyBean bean)
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
        return "PaymentApplyListener.TaxGlueImpl";
    }

}
