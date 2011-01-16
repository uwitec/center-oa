/**
 * File Name: PaymentApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.PaymentApplyDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.dao.PaymentVSOutDAO;
import com.china.center.oa.finance.manager.PaymentApplyManager;
import com.china.center.oa.finance.vs.PaymentVSOutBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * PaymentApplyManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-1-8
 * @see PaymentApplyManagerImpl
 * @since 3.0
 */
@Exceptional
public class PaymentApplyManagerImpl implements PaymentApplyManager
{
    private PaymentApplyDAO paymentApplyDAO = null;

    private PaymentVSOutDAO paymentVSOutDAO = null;

    private CommonDAO commonDAO = null;

    private PaymentDAO paymentDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private InBillDAO inBillDAO = null;

    private OutDAO outDAO = null;

    /**
     * default constructor
     */
    public PaymentApplyManagerImpl()
    {
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addPaymentApply(User user, PaymentApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getVsList());

        checkAdd(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        bean.setLogTime(TimeTools.now());

        bean.setStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        List<PaymentVSOutBean> vsList = bean.getVsList();

        for (PaymentVSOutBean vsItem : vsList)
        {
            vsItem.setId(commonDAO.getSquenceString20());

            vsItem.setParentId(bean.getId());

            vsItem.setLogTime(bean.getLogTime());
        }

        paymentApplyDAO.saveEntityBean(bean);

        paymentVSOutDAO.saveAllEntityBeans(vsList);

        if (bean.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            for (PaymentVSOutBean vsItem : vsList)
            {
                // 更新预付金额
                InBillBean bill = inBillDAO.find(vsItem.getBillId());

                if (bill.getStatus() != FinanceConstant.INBILL_STATUS_NOREF)
                {
                    throw new MYException("关联的收款单必须是预收,请确认操作");
                }

                bill.setStatus(FinanceConstant.INBILL_STATUS_PREPAYMENTS);

                inBillDAO.updateEntityBean(bill);
            }
        }

        saveFlowlog(user, bean);

        return true;
    }

    private void saveFlowlog(User user, PaymentApplyBean bean)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(bean.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_SUBMIT);

        log.setDescription(user.getStafferName() + "提交付款申请");

        log.setLogTime(TimeTools.now());

        log.setAfterStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        log.setPreStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        flowLogDAO.saveEntityBean(log);
    }

    private void checkAdd(User user, PaymentApplyBean bean)
        throws MYException
    {
        if (bean.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            return;
        }

        PaymentBean payment = paymentDAO.find(bean.getPaymentId());

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !payment.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能操作自己的回款单,请确认操作");
        }

        if (payment.getUseall() == FinanceConstant.PAYMENT_USEALL_END)
        {
            throw new MYException("回款单已经使用结束,请确认操作");
        }

        List<PaymentVSOutBean> vsList = bean.getVsList();

        double total = 0.0d;

        for (PaymentVSOutBean vsItem : vsList)
        {
            total += vsItem.getMoneys();
        }

        bean.setMoneys(total);

        double hasUsed = inBillDAO.sumByPaymentId(bean.getPaymentId());

        if (hasUsed + bean.getMoneys() > payment.getMoney())
        {
            throw new MYException("回款使用金额溢出,总金额[%.2f],已使用金额[%.2f],本次申请金额[%.2f],请确认操作", payment
                .getMoney(), hasUsed, bean.getMoneys());
        }
    }

    private void checkUpdate(PaymentApplyBean bean)
        throws MYException
    {
        PaymentBean payment = paymentDAO.find(bean.getPaymentId());

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !payment.getStafferId().equals(bean.getStafferId()))
        {
            throw new MYException("只能操作自己的回款单,请确认操作");
        }

        if (payment.getUseall() == FinanceConstant.PAYMENT_USEALL_END)
        {
            throw new MYException("回款单已经使用结束,请确认操作");
        }

        if (bean.getStatus() != FinanceConstant.PAYAPPLY_STATUS_REJECT)
        {
            throw new MYException("只有驳回才可以被修改,请确认操作");
        }

        double hasUsed = inBillDAO.sumByPaymentId(bean.getPaymentId());

        if (hasUsed + bean.getMoneys() > payment.getMoney())
        {
            throw new MYException("回款使用金额溢出,总金额[%.2f],已使用金额[%.2f],本次申请金额[%.2f],请确认操作", payment
                .getMoney(), hasUsed, bean.getMoneys());
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deletePaymentApply(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        PaymentApplyBean payment = paymentApplyDAO.find(id);

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (payment.getStatus() != FinanceConstant.PAYAPPLY_STATUS_REJECT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        paymentApplyDAO.deleteEntityBean(id);

        paymentVSOutDAO.deleteEntityBeansByFK(id);

        flowLogDAO.deleteEntityBeansByFK(id);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean passPaymentApply(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        PaymentApplyBean apply = checkPass(id);

        apply.setStatus(FinanceConstant.PAYAPPLY_STATUS_PASS);

        paymentApplyDAO.updateEntityBean(apply);

        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        // 生成收款单
        createInbill(user, apply, payment);

        // 更新回款单的状态和使用金额
        updatePayment(apply);

        savePassLog(user, apply, reason);

        return true;
    }

    /**
     * createInbill
     * 
     * @param user
     * @param apply
     * @param payment
     * @throws MYException
     */
    private void createInbill(User user, PaymentApplyBean apply, PaymentBean payment)
        throws MYException
    {
        List<PaymentVSOutBean> vsList = apply.getVsList();

        for (PaymentVSOutBean item : vsList)
        {
            if (item.getMoneys() == 0.0d)
            {
                continue;
            }

            // 生成收款单
            if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_PAYMENT)
            {
                InBillBean inBean = new InBillBean();

                inBean.setId(commonDAO.getSquenceString20());

                inBean.setBankId(payment.getBankId());

                inBean.setCustomerId(apply.getCustomerId());

                inBean.setLocationId(user.getLocationId());

                inBean.setLogTime(TimeTools.now());

                inBean.setMoneys(item.getMoneys());

                if (StringTools.isNullOrNone(item.getOutId()))
                {
                    inBean.setStatus(FinanceConstant.INBILL_STATUS_NOREF);

                    inBean.setDescription("自动生成预收收款单,从回款单:" + payment.getId() + ",未关联销售单:");
                }
                else
                {
                    inBean.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

                    inBean.setDescription("自动生成收款单,从回款单:" + payment.getId() + ",关联的销售单:"
                                          + item.getOutId());
                }

                inBean.setOutId(item.getOutId());

                inBean.setOwnerId(apply.getStafferId());

                inBean.setPaymentId(payment.getId());

                inBean.setStafferId(user.getStafferId());

                inBean.setType(FinanceConstant.INBILL_TYPE_SAILOUT);

                inBillDAO.saveEntityBean(inBean);

                item.setBillId(inBean.getId());
            }
            else
            {
                // 绑定销售单
                InBillBean bill = inBillDAO.find(item.getBillId());

                if (bill == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                if (bill.getStatus() == FinanceConstant.INBILL_STATUS_PAYMENTS)
                {
                    throw new MYException("收款单状态错误,请确认操作");
                }

                bill.setOutId(item.getOutId());

                bill.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

                inBillDAO.updateEntityBean(bill);
            }
        }

        // 更新收款单ID到申请里面
        paymentVSOutDAO.updateAllEntityBeans(vsList);

        // outDAO
        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            String outId = vsList.get(0).getOutId();

            OutBean out = outDAO.find(outId);

            // 看看销售单是否溢出
            double hasPay = inBillDAO.sumByOutId(outId);

            // 发现支付的金额过多
            if (hasPay + out.getBadDebts() > out.getTotal())
            {
                throw new MYException("销售单[%s]的总金额[%.2f],当前已付金额[%.2f],坏账金额[%.2f],付款金额超出销售金额",
                    outId, out.getTotal(), hasPay, out.getBadDebts());
            }

            // 更新已经支付的金额
            outDAO.updateHadPay(outId, hasPay);
        }

        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_PAYMENT)
        {
            for (PaymentVSOutBean item : vsList)
            {
                if (StringTools.isNullOrNone(item.getOutId()))
                {
                    continue;
                }

                String outId = item.getOutId();

                OutBean out = outDAO.find(outId);

                // 看看销售单是否溢出
                double hasPay = inBillDAO.sumByOutId(outId);

                // 发现支付的金额过多
                if (hasPay + out.getBadDebts() > out.getTotal())
                {
                    throw new MYException("销售单[%s]的总金额[%.2f],当前已付金额[%.2f],坏账金额[%.2f],付款金额超出销售金额",
                        outId, out.getTotal(), hasPay, out.getBadDebts());
                }

                // 更新已经支付的金额
                outDAO.updateHadPay(outId, hasPay);
            }
        }
    }

    private void updatePayment(PaymentApplyBean apply)
    {
        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            return;
        }

        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        double hasUsed = inBillDAO.sumByPaymentId(apply.getPaymentId());

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

    private PaymentApplyBean checkPass(String id)
        throws MYException
    {
        PaymentApplyBean apply = paymentApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        List<PaymentVSOutBean> vsList = paymentVSOutDAO.queryEntityBeansByFK(id);

        double total = 0.0d;

        for (PaymentVSOutBean vsItem : vsList)
        {
            total += vsItem.getMoneys();
        }

        apply.setVsList(vsList);

        apply.setMoneys(total);

        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            return apply;
        }

        // 检查是否溢出
        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        double hasUsed = inBillDAO.sumByPaymentId(apply.getPaymentId());

        if (hasUsed + apply.getMoneys() > payment.getMoney())
        {
            throw new MYException("回款使用金额溢出,总金额[%.2f],已使用金额[%.2f],本次申请金额[%.2f],请确认操作", payment
                .getMoney(), hasUsed, apply.getMoneys());
        }

        return apply;
    }

    private void savePassLog(User user, PaymentApplyBean apply, String reason)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_PASS);

        log.setDescription(reason);

        log.setLogTime(TimeTools.now());

        log.setPreStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        log.setAfterStatus(FinanceConstant.PAYAPPLY_STATUS_PASS);

        flowLogDAO.saveEntityBean(log);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectPaymentApply(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        PaymentApplyBean apply = paymentApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (apply.getStatus() != FinanceConstant.PAYAPPLY_STATUS_INIT)
        {
            throw new MYException("只有提交才可以被驳回,请确认操作");
        }

        apply.setStatus(FinanceConstant.PAYAPPLY_STATUS_REJECT);

        paymentApplyDAO.updateEntityBean(apply);

        List<PaymentVSOutBean> vsList = paymentVSOutDAO.queryEntityBeansByFK(id);

        for (PaymentVSOutBean item : vsList)
        {
            // 如果是关联收款单则取消
            if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
            {
                InBillBean bill = inBillDAO.find(item.getBillId());

                bill.setOutId("");

                bill.setStatus(FinanceConstant.INBILL_STATUS_NOREF);

                inBillDAO.updateEntityBean(bill);
            }
        }

        saveRejectLog(user, apply);

        return true;
    }

    private void saveRejectLog(User user, PaymentApplyBean apply)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_REJECT);

        log.setDescription(user.getStafferName() + "驳回付款申请");

        log.setLogTime(TimeTools.now());

        log.setPreStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        log.setAfterStatus(FinanceConstant.PAYAPPLY_STATUS_REJECT);

        flowLogDAO.saveEntityBean(log);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updatePaymentApply(User user, PaymentApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getVsList());

        checkUpdate(bean);

        bean.setLogTime(TimeTools.now());

        bean.setStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        List<PaymentVSOutBean> vsList = bean.getVsList();

        for (PaymentVSOutBean vsItem : vsList)
        {
            vsItem.setId(commonDAO.getSquenceString20());

            vsItem.setParentId(bean.getId());

            vsItem.setLogTime(bean.getLogTime());
        }

        paymentApplyDAO.updateEntityBean(bean);

        paymentVSOutDAO.deleteEntityBeansByFK(bean.getId());

        paymentVSOutDAO.saveAllEntityBeans(vsList);

        saveUpdateFlowlog(user, bean);

        return true;
    }

    /**
     * saveUpdateFlowlog
     * 
     * @param user
     * @param bean
     */
    private void saveUpdateFlowlog(User user, PaymentApplyBean bean)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(bean.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_SUBMIT);

        log.setDescription(user.getStafferName() + "修改付款申请");

        log.setLogTime(TimeTools.now());

        log.setAfterStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        log.setPreStatus(FinanceConstant.PAYAPPLY_STATUS_REJECT);

        flowLogDAO.saveEntityBean(log);
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

}
