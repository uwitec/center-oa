/**
 * File Name: PaymentManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.dao.PaymentApplyDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.dao.PaymentVSOutDAO;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.PaymentManager;
import com.china.center.oa.finance.manager.StatBankManager;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * PaymentManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-22
 * @see PaymentManagerImpl
 * @since 3.0
 */
@Exceptional
public class PaymentManagerImpl implements PaymentManager
{
    private PaymentDAO paymentDAO = null;

    private InBillDAO inBillDAO = null;

    private OutBillDAO outBillDAO = null;

    private PaymentApplyDAO paymentApplyDAO = null;

    private CommonDAO commonDAO = null;

    private BillManager billManager = null;

    private PaymentVSOutDAO paymentVSOutDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private StatBankManager statBankManager = null;

    /**
     * default constructor
     */
    public PaymentManagerImpl()
    {
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addBean(User user, PaymentBean bean)
        throws MYException
    {
        bean.setId(commonDAO.getSquenceString20());

        bean.setLogTime(TimeTools.now());

        return paymentDAO.saveEntityBean(bean);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addBeanList(User user, List<PaymentBean> beanList)
        throws MYException
    {
        for (PaymentBean paymentBean : beanList)
        {
            paymentBean.setId(commonDAO.getSquenceString20());

            paymentBean.setLogTime(TimeTools.now());
        }

        return paymentDAO.saveAllEntityBeans(beanList);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteBean(User user, String id)
        throws MYException
    {
        PaymentBean pay = paymentDAO.find(id);

        if (pay == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (pay.getStatus() != FinanceConstant.PAYMENT_STATUS_INIT)
        {
            throw new MYException("回款已经被人认领,不能删除");
        }

        double total = statBankManager.findTotalByBankId(pay.getBankId());

        if (total - pay.getMoney() < 0)
        {
            throw new MYException("帐户剩余[%.2f],当前删除回款总金额[%.2f],帐户金额不足", total, pay.getMoney());
        }

        return paymentDAO.deleteEntityBean(id);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean batchDeleteBean(User user, String id)
        throws MYException
    {
        PaymentBean pay = paymentDAO.find(id);

        if (pay == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        JudgeTools.judgeParameterIsNull(pay.getBatchId());

        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("batchId", "=", pay.getBatchId());

        List<PaymentBean> payList = paymentDAO.queryEntityBeansByCondition(con);

        double total = statBankManager.findTotalByBankId(pay.getBankId());

        double deletTotal = 0.0d;

        for (PaymentBean paymentBean : payList)
        {
            if (paymentBean.getStatus() != FinanceConstant.PAYMENT_STATUS_INIT)
            {
                throw new MYException("回款已经被人认领,不能删除");
            }

            deletTotal += paymentBean.getMoney();
        }

        if (total - deletTotal < 0)
        {
            throw new MYException("帐户剩余[%.2f],当前删除回款总金额[%.2f],帐户金额不足", total, deletTotal);
        }

        paymentDAO.deleteEntityBeansByCondition(con);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateBean(User user, PaymentBean bean)
        throws MYException
    {
        return paymentDAO.updateEntityBean(bean);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean drawBean(User user, String id, String customerId)
        throws MYException
    {
        PaymentBean pay = paymentDAO.find(id);

        if (pay == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (pay.getStatus() != FinanceConstant.PAYMENT_STATUS_INIT)
        {
            throw new MYException("回款已经被人认领,请确认操作");
        }

        pay.setStafferId(user.getStafferId());

        pay.setCustomerId(customerId);

        pay.setStatus(FinanceConstant.PAYMENT_STATUS_END);

        return paymentDAO.updateEntityBean(pay);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean dropBean(User user, String id)
        throws MYException
    {
        PaymentBean pay = paymentDAO.find(id);

        if (pay == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !user.getStafferId().equals(pay.getStafferId()))
        {
            throw new MYException("只能释放自己的回款单,请确认操作");
        }

        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addCondition("paymentId", "=", id);

        List<InBillBean> billList = inBillDAO.queryEntityBeansByCondition(condition);

        for (InBillBean inBillBean : billList)
        {
            if (inBillBean.getLock() == FinanceConstant.BILL_LOCK_YES)
            {
                throw new MYException("回款已经生成收款单且收款单已经被月结锁定,不能退领");
            }

            // 如果销售单已经全部付款,那么需要倒回,同时修改付款状态
            // 如果销售单已经结束,那么销售单状态需要回到待回款的状态,同时增加流程日志
            // 如果是委托代销的关联，委托代销也需要回到待回款状态
            if ( !StringTools.isNullOrNone(inBillBean.getOutId()))
            {
                throw new MYException("回款已经生成收款单且和销售绑定[%s],不能退领", inBillBean.getOutId());
            }
        }

        List<OutBillBean> outList = outBillDAO.queryEntityBeansByFK(id);

        for (OutBillBean outBillBean : outList)
        {
            if (outBillBean.getType() != FinanceConstant.OUTBILL_TYPE_HANDLING)
            {
                throw new MYException("回款单存在付款单据,无法退领,请确认操作");
            }
        }

        List<PaymentApplyBean> applyList = paymentApplyDAO.queryEntityBeansByFK(id);

        // 清除驳回的申请
        for (PaymentApplyBean paymentApplyBean : applyList)
        {
            paymentApplyDAO.deleteEntityBean(paymentApplyBean.getId());

            paymentVSOutDAO.deleteEntityBeansByFK(paymentApplyBean.getId());

            flowLogDAO.deleteEntityBeansByFK(paymentApplyBean.getId());

            List<InBillBean> inBillList = inBillDAO.queryEntityBeansByCondition(condition);

            for (InBillBean innerEach : inBillList)
            {
                if (innerEach.getCheckStatus() == PublicConstant.CHECK_STATUS_INIT)
                {
                    inBillDAO.deleteEntityBean(innerEach.getId());
                }
                else
                {
                    innerEach.setPaymentId("");
                    innerEach.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);
                    innerEach.setOutId("");
                    innerEach.setOutBalanceId("");

                    // 重置为普通的单据
                    inBillDAO.updateEntityBean(innerEach);

                    // 生成一个对冲的单据
                    innerEach.setMoneys( -innerEach.getMoneys());
                    innerEach.setDescription("退领[" + pay.getRefId() + "]生成对冲的单据:"
                                             + innerEach.getId());
                    innerEach.setRefBillId(innerEach.getId());
                    innerEach.setCheckStatus(PublicConstant.CHECK_STATUS_INIT);
                    innerEach.setChecks("");
                    billManager.addInBillBeanWithoutTransaction(user, innerEach);
                }
            }

            List<OutBillBean> outBillList = outBillDAO.queryEntityBeansByFK(id);

            for (OutBillBean outEach : outBillList)
            {
                if (outEach.getCheckStatus() == PublicConstant.CHECK_STATUS_INIT)
                {
                    outBillDAO.deleteEntityBean(outEach.getId());
                }
                else
                {
                    outEach.setStockId("");
                    outEach.setStockItemId("");
                    outEach.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

                    // 重置为普通的单据
                    outBillDAO.updateEntityBean(outEach);

                    // 生成一个对冲的单据
                    outEach.setMoneys( -outEach.getMoneys());
                    outEach.setDescription("退领[" + pay.getRefId() + "]生成对冲的单据:" + outEach.getId());
                    outEach.setRefBillId(outEach.getId());
                    outEach.setCheckStatus(PublicConstant.CHECK_STATUS_INIT);
                    outEach.setChecks("");
                    billManager.addOutBillBeanWithoutTransaction(user, outEach);
                }
            }
        }

        pay.setStafferId("");

        pay.setCustomerId("");

        pay.setStatus(FinanceConstant.PAYMENT_STATUS_INIT);

        pay.setUseall(FinanceConstant.PAYMENT_USEALL_INIT);

        return paymentDAO.updateEntityBean(pay);
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
     * @return the paymentApplyDAO
     */
    public PaymentApplyDAO getPaymentApplyDAO()
    {
        return paymentApplyDAO;
    }

    /**
     * @param paymentApplyDAO
     *            the paymentApplyDAO to set
     */
    public void setPaymentApplyDAO(PaymentApplyDAO paymentApplyDAO)
    {
        this.paymentApplyDAO = paymentApplyDAO;
    }

    /**
     * @return the paymentVSOutDAO
     */
    public PaymentVSOutDAO getPaymentVSOutDAO()
    {
        return paymentVSOutDAO;
    }

    /**
     * @param paymentVSOutDAO
     *            the paymentVSOutDAO to set
     */
    public void setPaymentVSOutDAO(PaymentVSOutDAO paymentVSOutDAO)
    {
        this.paymentVSOutDAO = paymentVSOutDAO;
    }

    /**
     * @return the flowLogDAO
     */
    public FlowLogDAO getFlowLogDAO()
    {
        return flowLogDAO;
    }

    /**
     * @param flowLogDAO
     *            the flowLogDAO to set
     */
    public void setFlowLogDAO(FlowLogDAO flowLogDAO)
    {
        this.flowLogDAO = flowLogDAO;
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
