/**
 * File Name: BillListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.listener.BillListener;
import com.china.center.oa.sail.bean.OutBean;


/**
 * TODO_OSGI 销售单驳回后,应收转预收
 * 
 * @author ZHUZHU
 * @version 2011-6-11
 * @see BillListenerTaxGlueImpl
 * @since 3.0
 */
public class BillListenerTaxGlueImpl implements BillListener
{

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.listener.BillListener#onRejectOut(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean, java.util.List)
     */
    public void onRejectOut(User user, OutBean bean, List<InBillBean> list)
        throws MYException
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "BillListener.TaxGlueImpl";
    }

}
