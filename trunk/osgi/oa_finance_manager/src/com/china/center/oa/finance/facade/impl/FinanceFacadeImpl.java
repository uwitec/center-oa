/**
 * File Name: FinanceFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.facade.impl;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BackPayApplyBean;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.manager.BackPayApplyManager;
import com.china.center.oa.finance.manager.BankManager;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.InvoiceinsManager;
import com.china.center.oa.finance.manager.PaymentApplyManager;
import com.china.center.oa.finance.manager.PaymentManager;
import com.china.center.oa.finance.manager.StockPayApplyManager;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.facade.AbstarctFacade;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * FinanceFacadeImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-12
 * @see FinanceFacadeImpl
 * @since 3.0
 */
public class FinanceFacadeImpl extends AbstarctFacade implements FinanceFacade
{
    private BankManager bankManager = null;

    private PaymentManager paymentManager = null;

    private UserManager userManager = null;

    private BillManager billManager = null;

    private InvoiceinsManager invoiceinsManager = null;

    private PaymentApplyManager paymentApplyManager = null;

    private StockPayApplyManager stockPayApplyManager = null;

    private BackPayApplyManager backPayApplyManager = null;

    /**
     * 回款操作锁
     */
    private static Object PAYMENT_LOCK = new Object();

    private static Object PAYMENT_APPLY_LOCK = new Object();

    private static Object INBILL_LOCK = new Object();

    private static Object OUTBILL_LOCK = new Object();

    private static Object INVOICEINS_LOCK = new Object();

    private static Object STOCKPAYAPPLY_LOCK = new Object();

    private static Object BACKPAYAPPLY_LOCK = new Object();

    private static Object BILLAPPLY_LOCK = new Object();

    /**
     * default constructor
     */
    public FinanceFacadeImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.facade.FinanceFacade#addBankBean(java.lang.String,
     *      com.china.center.oa.finance.bean.BankBean)
     */
    public boolean addBankBean(String userId, BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BANK_OPR))
        {
            return bankManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.facade.FinanceFacade#deleteBankBean(java.lang.String, java.lang.String)
     */
    public boolean deleteBankBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BANK_OPR))
        {
            return bankManager.deleteBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.facade.FinanceFacade#updateBankBean(java.lang.String,
     *      com.china.center.oa.finance.bean.BankBean)
     */
    public boolean updateBankBean(String userId, BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BANK_OPR))
        {
            return bankManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean addPaymentBean(String userId, PaymentBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PAYMENT_OPR))
        {
            return paymentManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean addPaymentBeanList(String userId, List<PaymentBean> beanList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, beanList);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PAYMENT_OPR))
        {
            return paymentManager.addBeanList(user, beanList);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean deletePaymentBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PAYMENT_OPR))
        {
            return paymentManager.deleteBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean batchDeletePaymentBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PAYMENT_OPR))
        {
            return paymentManager.batchDeleteBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean drawPaymentBean(String userId, String id, String customerId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (PAYMENT_LOCK)
        {
            paymentManager.drawBean(user, id, customerId);
        }

        return true;
    }

    public boolean dropPaymentBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (PAYMENT_LOCK)
        {
            paymentManager.dropBean(user, id);
        }

        return true;
    }

    public boolean updatePaymentBean(String userId, PaymentBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PAYMENT_OPR))
        {
            return paymentManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean addInvoiceinsBean(String userId, InvoiceinsBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (INVOICEINS_LOCK)
        {
            if (containAuth(user, AuthConstant.SAIL_SUBMIT))
            {
                return invoiceinsManager.addInvoiceinsBean(user, bean);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean deleteInvoiceinsBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (INVOICEINS_LOCK)
        {
            if (containAuth(user, AuthConstant.INVOICEINS_DEL, AuthConstant.SAIL_SUBMIT))
            {
                return invoiceinsManager.deleteInvoiceinsBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean passInvoiceinsBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (INVOICEINS_LOCK)
        {
            if (containAuth(user, AuthConstant.INVOICEINS_OPR))
            {
                return invoiceinsManager.passInvoiceinsBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean rejectInvoiceinsBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (INVOICEINS_LOCK)
        {
            if (containAuth(user, AuthConstant.INVOICEINS_OPR))
            {
                return invoiceinsManager.rejectInvoiceinsBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean addPaymentApply(String userId, PaymentApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (PAYMENT_APPLY_LOCK)
        {
            return paymentApplyManager.addPaymentApply(user, bean);
        }
    }

    public boolean deletePaymentApply(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (PAYMENT_APPLY_LOCK)
        {
            return paymentApplyManager.deletePaymentApply(user, id);
        }
    }

    public boolean passPaymentApply(String userId, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (PAYMENT_APPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.INBILL_APPROVE))
            {
                return paymentApplyManager.passPaymentApply(user, id, reason);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean rejectPaymentApply(String userId, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (PAYMENT_APPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.INBILL_APPROVE))
            {
                return paymentApplyManager.rejectPaymentApply(user, id, reason);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean updatePaymentApply(String userId, PaymentApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (PAYMENT_APPLY_LOCK)
        {
            return paymentApplyManager.updatePaymentApply(user, bean);
        }
    }

    public boolean splitInBillBean(String userId, String id, double newMoney)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        return billManager.splitInBillBean(user, id, newMoney);
    }

    public boolean addInBillBean(String userId, InBillBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (INBILL_LOCK)
        {
            if (containAuth(user, AuthConstant.INBILL_OPR))
            {
                return billManager.addInBillBean(user, bean);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean deleteInBillBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (INBILL_LOCK)
        {
            if (containAuth(user, AuthConstant.INBILL_OPR))
            {
                return billManager.deleteInBillBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean addOutBillBean(String userId, OutBillBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (OUTBILL_LOCK)
        {
            if (containAuth(user, AuthConstant.OUTBILL_OPR))
            {
                return billManager.addOutBillBean(user, bean);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean deleteOutBillBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (OUTBILL_LOCK)
        {
            if (containAuth(user, AuthConstant.OUTBILL_OPR))
            {
                return billManager.deleteOutBillBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean rejectStockPayApply(String userId, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (STOCKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.STOCK_PAY_CEO, AuthConstant.STOCK_PAY_SEC))
            {
                return stockPayApplyManager.rejectStockPayApply(user, id, reason);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean submitStockPayApply(String userId, String id, double payMoney, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (STOCKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.STOCK_PAY_APPLY))
            {
                return stockPayApplyManager.submitStockPayApply(user, id, payMoney, reason);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean passStockPayByCEO(String userId, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (STOCKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.STOCK_PAY_CEO))
            {
                return stockPayApplyManager.passStockPayByCEO(user, id, reason);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean endStockPayBySEC(String userId, String id, String reason, List<OutBillBean> outBillList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (STOCKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.STOCK_PAY_SEC))
            {
                return stockPayApplyManager.endStockPayBySEC(user, id, reason, outBillList);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean addBackPayApplyBean(String userId, BackPayApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (BACKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.SAIL_SUBMIT))
            {
                return backPayApplyManager.addBackPayApplyBean(user, bean);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean deleteBackPayApplyBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (BACKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.SAIL_SUBMIT))
            {
                return backPayApplyManager.deleteBackPayApplyBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean passBackPayApplyBean(String userId, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (BACKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.SAIL_BACKPAY_CENTER))
            {
                return backPayApplyManager.passBackPayApplyBean(user, id, reason);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean rejectBackPayApplyBean(String userId, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (BACKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.SAIL_BACKPAY_CENTER, AuthConstant.SAIL_BACKPAY_SEC))
            {
                return backPayApplyManager.rejectBackPayApplyBean(user, id, reason);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean endBackPayApplyBean(String userId, String id, String reason, OutBillBean outBill)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (BACKPAYAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.SAIL_BACKPAY_SEC))
            {
                return backPayApplyManager.endBackPayApplyBean(user, id, reason, outBill);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean passTransferOutBillBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (BILLAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.OUTBILL_OPR))
            {
                return billManager.passTransferOutBillBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean rejectTransferOutBillBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        synchronized (BILLAPPLY_LOCK)
        {
            if (containAuth(user, AuthConstant.OUTBILL_OPR))
            {
                return billManager.rejectTransferOutBillBean(user, id);
            }
            else
            {
                throw noAuth();
            }
        }
    }

    public boolean updateInBillBeanChecks(String userId, String id, String checks)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.INBILL_OPR))
        {
            return billManager.updateInBillBeanChecks(user, id, checks);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean updateOutBillBeanChecks(String userId, String id, String checks)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.OUTBILL_OPR))
        {
            return billManager.updateOutBillBeanChecks(user, id, checks);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * @return the bankManager
     */
    public BankManager getBankManager()
    {
        return bankManager;
    }

    /**
     * @param bankManager
     *            the bankManager to set
     */
    public void setBankManager(BankManager bankManager)
    {
        this.bankManager = bankManager;
    }

    /**
     * @return the userManager
     */
    public UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * @param userManager
     *            the userManager to set
     */
    public void setUserManager(UserManager userManager)
    {
        this.userManager = userManager;
    }

    /**
     * @return the paymentManager
     */
    public PaymentManager getPaymentManager()
    {
        return paymentManager;
    }

    /**
     * @param paymentManager
     *            the paymentManager to set
     */
    public void setPaymentManager(PaymentManager paymentManager)
    {
        this.paymentManager = paymentManager;
    }

    /**
     * @return the invoiceinsManager
     */
    public InvoiceinsManager getInvoiceinsManager()
    {
        return invoiceinsManager;
    }

    /**
     * @param invoiceinsManager
     *            the invoiceinsManager to set
     */
    public void setInvoiceinsManager(InvoiceinsManager invoiceinsManager)
    {
        this.invoiceinsManager = invoiceinsManager;
    }

    /**
     * @return the paymentApplyManager
     */
    public PaymentApplyManager getPaymentApplyManager()
    {
        return paymentApplyManager;
    }

    /**
     * @param paymentApplyManager
     *            the paymentApplyManager to set
     */
    public void setPaymentApplyManager(PaymentApplyManager paymentApplyManager)
    {
        this.paymentApplyManager = paymentApplyManager;
    }

    /**
     * @return the billManager
     */
    public BillManager getBillManager()
    {
        return billManager;
    }

    /**
     * @param billManager
     *            the billManager to set
     */
    public void setBillManager(BillManager billManager)
    {
        this.billManager = billManager;
    }

    /**
     * @return the stockPayApplyManager
     */
    public StockPayApplyManager getStockPayApplyManager()
    {
        return stockPayApplyManager;
    }

    /**
     * @param stockPayApplyManager
     *            the stockPayApplyManager to set
     */
    public void setStockPayApplyManager(StockPayApplyManager stockPayApplyManager)
    {
        this.stockPayApplyManager = stockPayApplyManager;
    }

    /**
     * @return the backPayApplyManager
     */
    public BackPayApplyManager getBackPayApplyManager()
    {
        return backPayApplyManager;
    }

    /**
     * @param backPayApplyManager
     *            the backPayApplyManager to set
     */
    public void setBackPayApplyManager(BackPayApplyManager backPayApplyManager)
    {
        this.backPayApplyManager = backPayApplyManager;
    }
}
