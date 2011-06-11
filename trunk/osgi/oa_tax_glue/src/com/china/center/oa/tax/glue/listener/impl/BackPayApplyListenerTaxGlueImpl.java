/**
 * File Name: BackPayApplyListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BackPayApplyBean;
import com.china.center.oa.finance.listener.BackPayApplyListener;


/**
 * TODO_OSGI 销售退款/预收退款 通过
 * 
 * @author ZHUZHU
 * @version 2011-6-11
 * @see BackPayApplyListenerTaxGlueImpl
 * @since 3.0
 */
public class BackPayApplyListenerTaxGlueImpl implements BackPayApplyListener
{

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.listener.BackPayApplyListener#onEndBackPayApplyBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.BackPayApplyBean)
     */
    public void onEndBackPayApplyBean(User user, BackPayApplyBean bean)
        throws MYException
    {
        // 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "BackPayApplyListener.TaxGlueImpl";
    }

}
