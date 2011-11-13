/**
 * File Name: PaymentListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener;


import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.PaymentApplyBean;


/**
 * PaymentListener
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see PaymentApplyListener
 * @since 3.0
 */
public interface PaymentApplyListener extends ParentListener
{
    /**
     * 回款转预收/销售单绑定(预收转应收)/坏账/预收转费用 通过(退领不再这个监听)
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    void onPassBean(User user, PaymentApplyBean bean)
        throws MYException;
}
