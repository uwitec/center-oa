/**
 * File Name: PaymentManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.PaymentApplyBean;


/**
 * PaymentManager
 * 
 * @author ZHUZHU
 * @version 2010-12-22
 * @see PaymentApplyManager
 * @since 3.0
 */
public interface PaymentApplyManager
{
    boolean addPaymentApply(User user, PaymentApplyBean bean)
        throws MYException;

    boolean updatePaymentApply(User user, PaymentApplyBean bean)
        throws MYException;

    boolean deletePaymentApply(User user, String id)
        throws MYException;

    /**
     * 通过(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean passPaymentApply(User user, String id)
        throws MYException;

    /**
     * 驳回(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean rejectPaymentApply(User user, String id, String reason)
        throws MYException;
}
