/**
 * File Name: FinanceFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.facade;


import java.util.List;

import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BackPayApplyBean;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.OutBillBean;
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

    boolean addPaymentBeanList(String userId, List<PaymentBean> beanList)
        throws MYException;

    boolean updatePaymentBean(String userId, PaymentBean bean)
        throws MYException;

    boolean deletePaymentBean(String userId, String id)
        throws MYException;

    /**
     * batchDeletePaymentBean
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    boolean batchDeletePaymentBean(String userId, String id)
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
    boolean passPaymentApply(String userId, String id, String reason)
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

    /**
     * 分拆
     * 
     * @param user
     * @param id
     * @param newMoney
     * @return
     * @throws MYException
     */
    boolean splitInBillBean(String userId, String id, double newMoney)
        throws MYException;

    boolean addInBillBean(String userId, InBillBean bean)
        throws MYException;

    boolean deleteInBillBean(String userId, String id)
        throws MYException;

    /**
     * addOutBillBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addOutBillBean(String userId, OutBillBean bean)
        throws MYException;

    /**
     * deleteOutBillBean
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean deleteOutBillBean(String userId, String id)
        throws MYException;

    /**
     * 通过(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean submitStockPayApply(String userId, String id, double payMoney, String reason)
        throws MYException;

    /**
     * 驳回(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean rejectStockPayApply(String userId, String id, String reason)
        throws MYException;

    /**
     * 强制关闭
     * 
     * @param userId
     * @param id
     * @param reason
     * @return
     * @throws MYException
     */
    boolean closeStockPayApply(String userId, String id, String reason)
        throws MYException;

    boolean passStockPayByCEO(String userId, String id, String reason)
        throws MYException;

    boolean endStockPayBySEC(String userId, String id, String reason, List<OutBillBean> outBillList)
        throws MYException;

    boolean addBackPayApplyBean(String userId, BackPayApplyBean bean)
        throws MYException;

    boolean passBackPayApplyBean(String userId, String id, String reason)
        throws MYException;

    boolean rejectBackPayApplyBean(String userId, String id, String reason)
        throws MYException;

    boolean deleteBackPayApplyBean(String userId, String id)
        throws MYException;

    boolean endBackPayApplyBean(String userId, String id, String reason, OutBillBean outBill)
        throws MYException;

    /**
     * 通过转账
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean passTransferOutBillBean(String userId, String id)
        throws MYException;

    /**
     * 驳回转账
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean rejectTransferOutBillBean(String userId, String id)
        throws MYException;

    boolean passInvoiceinsBean(String userId, String id)
        throws MYException;

    boolean rejectInvoiceinsBean(String userId, String id)
        throws MYException;

    boolean updateInBillBeanChecks(String userId, String id, String checks)
        throws MYException;

    boolean updateOutBillBeanChecks(String userId, String id, String checks)
        throws MYException;

    /**
     * 合并付款
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean composeStockPayApply(String userId, List<String> idList)
        throws MYException;
}
