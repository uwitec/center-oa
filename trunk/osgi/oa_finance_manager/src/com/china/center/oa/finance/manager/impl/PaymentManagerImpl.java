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
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.PaymentApplyDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.dao.PaymentVSOutDAO;
import com.china.center.oa.finance.manager.PaymentManager;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
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

    private PaymentApplyDAO paymentApplyDAO = null;

    private CommonDAO commonDAO = null;

    private PaymentVSOutDAO paymentVSOutDAO = null;

    private FlowLogDAO flowLogDAO = null;

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

        return paymentDAO.deleteEntityBean(id);
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

        double hasUsed = inBillDAO.sumByPaymentId(id);

        if (hasUsed > 0.0)
        {
            throw new MYException("回款已经生成收款单,不能退领");
        }

        List<PaymentApplyBean> queryEntityBeansByFK = paymentApplyDAO.queryEntityBeansByFK(id);

        for (PaymentApplyBean paymentApplyBean : queryEntityBeansByFK)
        {
            if (paymentApplyBean.getStatus() == FinanceConstant.PAYAPPLY_STATUS_INIT)
            {
                throw new MYException("有付款申请没有结束,请重新操作");
            }

            if (paymentApplyBean.getStatus() == FinanceConstant.PAYAPPLY_STATUS_PASS)
            {
                throw new MYException("回款已经生成收款单,请重新操作");
            }
        }

        // 清除驳回的申请
        for (PaymentApplyBean paymentApplyBean : queryEntityBeansByFK)
        {
            paymentApplyDAO.deleteEntityBean(paymentApplyBean.getId());

            paymentVSOutDAO.deleteEntityBeansByFK(paymentApplyBean.getId());

            flowLogDAO.deleteEntityBeansByFK(paymentApplyBean.getId());
        }

        pay.setStafferId("");

        pay.setCustomerId("");

        pay.setStatus(FinanceConstant.PAYMENT_STATUS_INIT);

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
}
