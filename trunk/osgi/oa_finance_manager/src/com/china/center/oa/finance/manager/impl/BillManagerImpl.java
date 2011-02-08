/**
 * File Name: BillManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.StatBankManager;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.stock.manager.StockManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * BillManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see BillManagerImpl
 * @since 3.0
 */
@Exceptional
public class BillManagerImpl implements BillManager
{
    private InBillDAO inBillDAO = null;

    private OutBillDAO outBillDAO = null;

    private OutDAO outDAO = null;

    private CommonDAO commonDAO = null;

    private PaymentDAO paymentDAO = null;

    private StockManager stockManager = null;

    private StatBankManager statBankManager = null;

    private static Object LOCK = new Object();

    /**
     * default constructor
     */
    public BillManagerImpl()
    {
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addInBillBean(User user, InBillBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        return addInBillBeanWithoutTransaction(user, bean);
    }

    public boolean addInBillBeanWithoutTransaction(User user, InBillBean bean)
        throws MYException
    {
        synchronized (LOCK)
        {
            bean.setId(commonDAO.getSquenceString20());

            bean.setLogTime(TimeTools.now());

            // 验证销售单绑定策略
            if ( !StringTools.isNullOrNone(bean.getOutId()))
            {
                OutBean out = outDAO.find(bean.getOutId());

                // 已经支付的
                double hasPay = inBillDAO.sumByOutId(bean.getOutId());

                // 发现支付的金额过多
                if (hasPay + bean.getMoneys() > out.getTotal())
                {
                    throw new MYException("销售单[%s]的总金额[%.2f],当前已付金额[%.2f],本次申请付款[%.2f],付款金额超出销售金额", bean.getOutId(),
                        out.getTotal(), hasPay, bean.getMoneys());
                }

                // 更新已经支付的金额
                outDAO.updateHadPay(bean.getOutId(), hasPay + bean.getMoneys());
            }

            return inBillDAO.saveEntityBean(bean);
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteInBillBean(User user, String id)
        throws MYException
    {
        // 如果被OUTID绑定不能删除
        JudgeTools.judgeParameterIsNull(user, id);

        InBillBean bill = inBillDAO.find(id);

        if (bill == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !StringTools.isNullOrNone(bill.getOutId()))
        {
            throw new MYException("单据已经被销售单[%s]绑定,请确认操作", bill.getOutId());
        }

        if (bill.getLock() == FinanceConstant.BILL_LOCK_YES)
        {
            throw new MYException("单据已经被统计固化,请确认操作");
        }

        inBillDAO.deleteEntityBean(id);

        // 更新回款单的状态
        if ( !StringTools.isNullOrNone(bill.getPaymentId()))
        {
            PaymentBean payment = paymentDAO.find(bill.getPaymentId());

            double hasUsed = inBillDAO.sumByPaymentId(bill.getPaymentId());

            payment.setUseMoney(hasUsed);

            if (hasUsed >= payment.getMoney())
            {
                payment.setUseall(FinanceConstant.PAYMENT_USEALL_END);
            }
            else
            {
                payment.setUseall(FinanceConstant.PAYMENT_USEALL_INIT);
            }

            paymentDAO.updateEntityBean(payment);
        }

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateInBillBean(User user, InBillBean bean)
        throws MYException
    {
        return inBillDAO.updateEntityBean(bean);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addOutBillBean(User user, OutBillBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        double total = statBankManager.findTotalByBankId(bean.getBankId());

        if (total - bean.getMoneys() < 0)
        {
            throw new MYException("帐户剩余[%.2f],当前付款金额[%.2f],金额不足", total, bean.getMoneys());
        }

        bean.setId(commonDAO.getSquenceString20());

        bean.setLogTime(TimeTools.now());

        if ( !StringTools.isNullOrNone(bean.getStockItemId()))
        {
            // 关联采购项付款状态
            stockManager.payStockItemWithoutTransaction(user, bean.getStockItemId());
        }

        return outBillDAO.saveEntityBean(bean);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteOutBillBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBillBean bill = outBillDAO.find(id);

        if (bill == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bill.getLock() == FinanceConstant.BILL_LOCK_YES)
        {
            throw new MYException("单据已经被统计固化,请确认操作");
        }

        outBillDAO.deleteEntityBean(id);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean splitInBillBean(User user, String id, double newMoney)
        throws MYException
    {
        return splitInBillBeanWithoutTransactional(user, id, newMoney);
    }

    public boolean splitInBillBeanWithoutTransactional(User user, String id, double newMoney)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InBillBean bill = inBillDAO.find(id);

        if (bill == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (newMoney <= 0.0)
        {
            throw new MYException("分拆金额不能小于0,请确认操作");
        }

        if (bill.getMoneys() < newMoney)
        {
            throw new MYException("金额不足,无法分拆,请确认操作");
        }

        if ( !user.getStafferId().equals(bill.getOwnerId()))
        {
            throw new MYException("不是自己的收款单,请确认操作");
        }

        if (bill.getStatus() != FinanceConstant.INBILL_STATUS_NOREF)
        {
            throw new MYException("只能分拆预收的收款单,请确认操作");
        }

        bill.setMoneys(bill.getMoneys() - newMoney);

        inBillDAO.updateEntityBean(bill);

        // 分拆后时间不能变(锁定状态不能变)
        bill.setId(commonDAO.getSquenceString20());

        bill.setMoneys(newMoney);

        bill.setDescription("分拆" + id + "后自动生成新的收款单");

        inBillDAO.saveEntityBean(bill);

        return true;
    }

    /**
     * @return the inBillDAO
     */
    public InBillDAO getInBillDAO()
    {
        return inBillDAO;
    }

    /**
     * @param inBillDAO
     *            the inBillDAO to set
     */
    public void setInBillDAO(InBillDAO inBillDAO)
    {
        this.inBillDAO = inBillDAO;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    /**
     * @return the paymentDAO
     */
    public PaymentDAO getPaymentDAO()
    {
        return paymentDAO;
    }

    /**
     * @param paymentDAO
     *            the paymentDAO to set
     */
    public void setPaymentDAO(PaymentDAO paymentDAO)
    {
        this.paymentDAO = paymentDAO;
    }

    /**
     * @return the outBillDAO
     */
    public OutBillDAO getOutBillDAO()
    {
        return outBillDAO;
    }

    /**
     * @param outBillDAO
     *            the outBillDAO to set
     */
    public void setOutBillDAO(OutBillDAO outBillDAO)
    {
        this.outBillDAO = outBillDAO;
    }

    /**
     * @return the statBankManager
     */
    public StatBankManager getStatBankManager()
    {
        return statBankManager;
    }

    /**
     * @param statBankManager
     *            the statBankManager to set
     */
    public void setStatBankManager(StatBankManager statBankManager)
    {
        this.statBankManager = statBankManager;
    }

    /**
     * @return the stockManager
     */
    public StockManager getStockManager()
    {
        return stockManager;
    }

    /**
     * @param stockManager
     *            the stockManager to set
     */
    public void setStockManager(StockManager stockManager)
    {
        this.stockManager = stockManager;
    }
}
