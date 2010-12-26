/**
 * File Name: PaymentManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.manager.PaymentManager;
import com.china.center.oa.publics.dao.CommonDAO;
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

    private CommonDAO commonDAO = null;

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
    public boolean drawBean(String stafferId, String id)
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

        pay.setStafferId(stafferId);

        pay.setStatus(FinanceConstant.PAYMENT_STATUS_END);

        return paymentDAO.updateEntityBean(pay);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean dropBean(String stafferId, String id)
        throws MYException
    {
        PaymentBean pay = paymentDAO.find(id);

        if (pay == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !stafferId.equals(pay.getStafferId()))
        {
            throw new MYException("只能释放自己的回款单,请确认操作");
        }

        pay.setStafferId("");

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
}
