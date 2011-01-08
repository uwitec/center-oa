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
import com.china.center.tools.JudgeTools;
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

        checkAdd(bean);

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

    private void checkAdd(PaymentApplyBean bean)
        throws MYException
    {
        PaymentBean payment = paymentDAO.find(bean.getPaymentId());

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
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
            throw new MYException("回款使用金额溢出,总金额[%f],已使用金额[%f],本次申请金额[%f],请确认操作",
                payment.getMoney(), hasUsed, bean.getMoneys());
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

        if (bean.getStatus() != FinanceConstant.PAYAPPLY_STATUS_REJECT)
        {
            throw new MYException("只有驳回才可以被修改,请确认操作");
        }

        double hasUsed = inBillDAO.sumByPaymentId(bean.getPaymentId());

        if (hasUsed + bean.getMoneys() > payment.getMoney())
        {
            throw new MYException("回款使用金额溢出,总金额[%f],已使用金额[%f],本次申请金额[%f],请确认操作",
                payment.getMoney(), hasUsed, bean.getMoneys());
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
    public boolean passPaymentApply(User user, String id)
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

        savePassLog(user, apply);

        return true;
    }

    /**
     * createInbill
     * 
     * @param user
     * @param apply
     * @param payment
     */
    private void createInbill(User user, PaymentApplyBean apply, PaymentBean payment)
    {
        List<PaymentVSOutBean> vsList = apply.getVsList();

        for (PaymentVSOutBean item : vsList)
        {
            InBillBean inBean = new InBillBean();

            inBean.setId(commonDAO.getSquenceString20());

            inBean.setBankId(payment.getBankId());

            inBean.setCustomerId(apply.getCustomerId());

            inBean.setDescription("自动生成收款单,从回款单:" + payment.getId() + ",关联的销售单:" + item.getOutId());

            inBean.setLocationId(user.getLocationId());

            inBean.setLogTime(TimeTools.now());

            inBean.setMoneys(item.getMoneys());

            inBean.setOutId(item.getOutId());

            inBean.setOwnerId(apply.getStafferId());

            inBean.setPaymentId(payment.getId());

            inBean.setStafferId(user.getStafferId());

            inBean.setType(FinanceConstant.INBILL_TYPE_SAILOUT);

            inBillDAO.saveEntityBean(inBean);
        }
    }

    private void updatePayment(PaymentApplyBean apply)
    {
        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        double hasUsed = inBillDAO.sumByPaymentId(apply.getPaymentId());

        payment.setUseMoney(hasUsed);

        if (hasUsed >= payment.getMoney())
        {
            payment.setUseall(FinanceConstant.PAYMENT_USEALL_END);
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

        // 检查是否溢出
        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        if (payment == null)
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

        double hasUsed = inBillDAO.sumByPaymentId(apply.getPaymentId());

        if (hasUsed + apply.getMoneys() > payment.getMoney())
        {
            throw new MYException("回款使用金额溢出,总金额[%f],已使用金额[%f],本次申请金额[%f],请确认操作",
                payment.getMoney(), hasUsed, apply.getMoneys());
        }

        return apply;
    }

    private void savePassLog(User user, PaymentApplyBean apply)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_PASS);

        log.setDescription(user.getStafferName() + "通过付款申请");

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

}
