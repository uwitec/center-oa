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
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.StatBankManager;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stock.dao.StockItemDAO;
import com.china.center.oa.stock.manager.StockManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
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

    private StockItemDAO stockItemDAO = null;

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

            if (StringTools.isNullOrNone(bean.getLogTime()))
            {
                bean.setLogTime(TimeTools.now());
            }

            // 验证销售单绑定策略
            if ( !StringTools.isNullOrNone(bean.getOutId())
                && bean.getType() != FinanceConstant.INBILL_TYPE_BADOUT)
            {
                OutBean out = outDAO.find(bean.getOutId());

                if (out == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                // 已经支付的
                double hasPay = inBillDAO.sumByOutId(bean.getOutId());

                // 发现支付的金额过多
                if (hasPay + bean.getMoneys() > out.getTotal())
                {
                    throw new MYException("销售单[%s]的总金额[%.2f],当前已付金额[%.2f],本次申请付款[%.2f],付款金额超出销售金额",
                        bean.getOutId(), out.getTotal(), hasPay, bean.getMoneys());
                }

                // 更新已经支付的金额
                outDAO.updateHadPay(bean.getOutId(), hasPay + bean.getMoneys());
            }

            // 更新坏账状态
            if ( !StringTools.isNullOrNone(bean.getOutId())
                && bean.getType() == FinanceConstant.INBILL_TYPE_BADOUT)
            {
                OutBean out = outDAO.find(bean.getOutId());

                if (out == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                bean.setCustomerId(out.getCustomerId());

                bean.setOwnerId(out.getStafferId());

                outDAO.updataBadDebtsCheckStatus(bean.getOutId(),
                    OutConstant.BADDEBTSCHECKSTATUS_YES);
            }

            return saveInBillInner(bean);
        }
    }

    private boolean saveInBillInner(InBillBean bean)
    {
        if (bean.getSrcMoneys() == 0.0)
        {
            bean.setSrcMoneys(bean.getMoneys());
        }

        return inBillDAO.saveEntityBean(bean);
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

        if (bill.getLock() == FinanceConstant.BILL_LOCK_YES)
        {
            throw new MYException("单据已经被统计固化,请确认操作");
        }

        if (bill.getCheckStatus() == PublicConstant.CHECK_STATUS_END)
        {
            throw new MYException("单据已经被核对,请确认操作");
        }

        if ( !StringTools.isNullOrNone(bill.getOutId())
            || !StringTools.isNullOrNone(bill.getOutBalanceId()))
        {
            throw new MYException("单据已经被销售单[%s]绑定,请确认操作", bill.getOutId());
        }

        if ( !StringTools.isNullOrNone(bill.getPaymentId()))
        {
            throw new MYException("单据已经和回款绑定,只能通过退领删除收款,请确认操作", bill.getOutId());
        }

        // 帐户余额
        double total = statBankManager.findTotalByBankId(bill.getBankId());

        if (total - bill.getMoneys() < 0)
        {
            throw new MYException("帐户剩余[%.2f],当前删除总金额[%.2f],帐户金额不足", total, bill.getMoneys());
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
        return addOutBillBeanWithoutTransaction(user, bean);
    }

    public boolean addOutBillBeanWithoutTransaction(User user, OutBillBean bean)
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

        if (bean.getType() == FinanceConstant.OUTBILL_TYPE_TRANSFER)
        {
            bean.setStatus(FinanceConstant.OUTBILL_STATUS_SUBMIT);
        }

        // 处理采购付款
        handleStockItem(user, bean);

        return saveOutBillInner(bean);
    }

    private boolean saveOutBillInner(OutBillBean bean)
    {
        if (bean.getSrcMoneys() == 0.0)
        {
            bean.setSrcMoneys(bean.getMoneys());
        }

        return outBillDAO.saveEntityBean(bean);
    }

    /**
     * handleStockItem
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void handleStockItem(User user, OutBillBean bean)
        throws MYException
    {
        if ( !StringTools.isNullOrNone(bean.getStockItemId()))
        {
            // 检查是否item全部付款
            StockItemBean item = stockItemDAO.find(bean.getStockItemId());

            if (item == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            ConditionParse con = new ConditionParse();

            con.addWhereStr();

            con.addCondition("OutBillBean.stockItemId", "=", bean.getStockItemId());

            double sum = outBillDAO.sumByCondition(con);

            // 全部付款
            if ( (sum + bean.getMoneys()) == item.getTotal())
            {
                // 关联采购项付款状态
                stockManager.payStockItemWithoutTransaction(user, bean.getStockItemId());
            }
        }
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

        if (bill.getCheckStatus() == PublicConstant.CHECK_STATUS_END)
        {
            throw new MYException("单据已经被核对,请确认操作");
        }

        if ( !StringTools.isNullOrNone(bill.getStockId())
            || !StringTools.isNullOrNone(bill.getStockItemId()))
        {
            throw new MYException("单据已经被采购单[%s]关联,请确认操作", bill.getStockId());
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
        splitInBillBeanWithoutTransactional(user, id, newMoney);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean passTransferOutBillBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBillBean bean = outBillDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getType() != FinanceConstant.OUTBILL_TYPE_TRANSFER)
        {
            throw new MYException("不是转账付款,请确认操作");
        }

        if (bean.getStatus() != FinanceConstant.OUTBILL_STATUS_SUBMIT)
        {
            throw new MYException("转账付款已经成功,请确认操作");
        }

        InBillBean inbill = saveInBill(user, id, bean);

        bean.setStatus(FinanceConstant.OUTBILL_STATUS_END);

        bean.setLock(FinanceConstant.BILL_LOCK_YES);

        bean.setRefBillId(inbill.getId());

        outBillDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * 生成收款单(同时锁定)
     * 
     * @param user
     * @param id
     * @param bean
     * @return
     */
    private InBillBean saveInBill(User user, String id, OutBillBean bean)
    {
        // 生成收款单
        InBillBean inbill = new InBillBean();

        inbill.setId(commonDAO.getSquenceString20());

        inbill.setBankId(bean.getDestBankId());

        inbill.setDescription("自动接收转账:" + id);

        inbill.setDestBankId(bean.getBankId());

        inbill.setLocationId(user.getLocationId());

        inbill.setLock(FinanceConstant.BILL_LOCK_YES);

        inbill.setLogTime(TimeTools.now());

        inbill.setMoneys(bean.getMoneys());

        inbill.setRefBillId(id);

        inbill.setStafferId(user.getStafferId());

        inbill.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

        inbill.setType(FinanceConstant.INBILL_TYPE_TRANSFER);

        saveInBillInner(inbill);

        return inbill;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectTransferOutBillBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBillBean bean = outBillDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getType() != FinanceConstant.OUTBILL_TYPE_TRANSFER)
        {
            throw new MYException("不是转账付款,请确认操作");
        }

        if (bean.getStatus() != FinanceConstant.OUTBILL_STATUS_SUBMIT)
        {
            throw new MYException("转账付款已经成功,请确认操作");
        }

        // 驳回有两种,如果没有锁定直接删除(这里要要是删除付款对于帐户来说是增加钱)
        if (bean.getLock() == FinanceConstant.BILL_LOCK_NO)
        {
            outBillDAO.deleteEntityBean(id);
        }
        else
        {
            // 锁定了生成一个自己的收款单
            InBillBean inbill = new InBillBean();

            inbill.setId(commonDAO.getSquenceString20());

            inbill.setBankId(bean.getBankId());

            inbill.setDescription("驳回自动生成收款单(转账):" + id);

            inbill.setDestBankId(bean.getBankId());

            inbill.setLocationId(user.getLocationId());

            inbill.setLock(FinanceConstant.BILL_LOCK_YES);

            inbill.setLogTime(TimeTools.now());

            inbill.setMoneys(bean.getMoneys());

            inbill.setRefBillId(id);

            inbill.setStafferId(bean.getStafferId());

            inbill.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

            inbill.setType(FinanceConstant.INBILL_TYPE_TRANSFER);

            saveInBillInner(inbill);

            // 更新源单状态
            bean.setStatus(FinanceConstant.OUTBILL_STATUS_END);

            bean.setLock(FinanceConstant.BILL_LOCK_YES);

            bean.setRefBillId(inbill.getId());

            bean.setDescription(bean.getDescription() + "" + "此单被驳回,由于锁定自动生成了同样金额的收款单:"
                                + inbill.getId());

            outBillDAO.updateEntityBean(bean);
        }

        return true;
    }

    public String splitInBillBeanWithoutTransactional(User user, String id, double newMoney)
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

        bill.setMoneys(bill.getMoneys() - newMoney);

        bill.setDescription(bill.getDescription() + "<br>" + "销售关联分拆了:"
                            + MathTools.formatNum(newMoney) + "出去");

        inBillDAO.updateEntityBean(bill);

        // 分拆后时间不能变(锁定状态不能变)
        bill.setId(commonDAO.getSquenceString20());

        bill.setMoneys(newMoney);

        bill.setSrcMoneys(newMoney);

        bill.setUpdateId(0);

        bill.setDescription("分拆" + id + "后自动生成新的收款单");

        saveInBillInner(bill);

        return bill.getId();
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateInBillBeanChecks(User user, String id, String checks)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InBillBean inBill = inBillDAO.find(id);

        if (inBill == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        inBill.setChecks("[" + user.getStafferName() + "]" + checks);

        inBill.setCheckStatus(PublicConstant.CHECK_STATUS_END);

        inBillDAO.updateEntityBean(inBill);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateOutBillBeanChecks(User user, String id, String checks)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBillBean bill = outBillDAO.find(id);

        if (bill == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bill.setChecks("[" + user.getStafferName() + "]" + checks);

        bill.setCheckStatus(PublicConstant.CHECK_STATUS_END);

        outBillDAO.updateEntityBean(bill);

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

    /**
     * @return the stockItemDAO
     */
    public StockItemDAO getStockItemDAO()
    {
        return stockItemDAO;
    }

    /**
     * @param stockItemDAO
     *            the stockItemDAO to set
     */
    public void setStockItemDAO(StockItemDAO stockItemDAO)
    {
        this.stockItemDAO = stockItemDAO;
    }
}
