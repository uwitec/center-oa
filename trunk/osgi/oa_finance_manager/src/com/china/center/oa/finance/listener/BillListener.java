/**
 * File Name: PaymentListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener;


import java.util.List;

import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.sail.bean.OutBean;


/**
 * PaymentListener
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see BillListener
 * @since 3.0
 */
public interface BillListener extends ParentListener
{
    /**
     * 销售单驳回后,应收转预收
     * 
     * @param user
     * @param bean
     * @param list
     * @throws MYException
     */
    void onRejectOut(User user, OutBean bean, List<InBillBean> list)
        throws MYException;
}