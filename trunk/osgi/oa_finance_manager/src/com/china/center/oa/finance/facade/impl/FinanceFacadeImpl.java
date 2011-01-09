/**
 * File Name: FinanceFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.facade.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.manager.BankManager;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.InvoiceinsManager;
import com.china.center.oa.finance.manager.PaymentApplyManager;
import com.china.center.oa.finance.manager.PaymentManager;
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

    /**
     * 回款操作锁
     */
    private static Object PAYMENT_LOCK = new Object();

    private static Object PAYMENT_APPLY_LOCK = new Object();

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

        if (containAuth(user, AuthConstant.INVOICEINS_OPR))
        {
            return invoiceinsManager.addInvoiceinsBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    public boolean deleteInvoiceinsBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.INVOICEINS_DEL))
        {
            return invoiceinsManager.deleteInvoiceinsBean(user, id);
        }
        else
        {
            throw noAuth();
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
}
