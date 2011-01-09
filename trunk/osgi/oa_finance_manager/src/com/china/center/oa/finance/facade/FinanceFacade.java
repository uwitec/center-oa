/**
 * File Name: FinanceFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.facade;


import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;


/**
 * FinanceFacade
 * 
 * @author ZHUZHU
 * @version 2010-12-12
 * @see FinanceFacade
 * @since 3.0
 */
public interface FinanceFacade
{
    boolean addBankBean(String userId, BankBean bean)
        throws MYException;

    boolean updateBankBean(String userId, BankBean bean)
        throws MYException;

    boolean deleteBankBean(String userId, String id)
        throws MYException;

    boolean addPaymentBean(String userId, PaymentBean bean)
        throws MYException;

    boolean updatePaymentBean(String userId, PaymentBean bean)
        throws MYException;

    boolean deletePaymentBean(String userId, String id)
        throws MYException;

    /**
     * 领取回款(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean drawPaymentBean(String stafferId, String id, String customerId)
        throws MYException;

    /**
     * 退领(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean dropPaymentBean(String userId, String id)
        throws MYException;

    boolean addInvoiceinsBean(String userId, InvoiceinsBean bean)
        throws MYException;

    boolean deleteInvoiceinsBean(String userId, String id)
        throws MYException;

    boolean addPaymentApply(String userId, PaymentApplyBean bean)
        throws MYException;

    boolean updatePaymentApply(String userId, PaymentApplyBean bean)
        throws MYException;

    boolean deletePaymentApply(String userId, String id)
        throws MYException;

    /**
     * 通过(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean passPaymentApply(String userId, String id)
        throws MYException;

    /**
     * 驳回(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean rejectPaymentApply(String userId, String id, String reason)
        throws MYException;
}
