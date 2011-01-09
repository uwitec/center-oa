/**
 * File Name: OutListenerFinanceImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener.impl;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.listener.OutListener;


/**
 * OutListenerFinanceImpl
 * 
 * @author ZHUZHU
 * @version 2011-1-9
 * @see OutListenerFinanceImpl
 * @since 3.0
 */
public class OutListenerFinanceImpl implements OutListener
{
    private InBillDAO inBillDAO = null;

    private OutDAO outDAO = null;

    /**
     * default constructor
     */
    public OutListenerFinanceImpl()
    {
    }

    public void onPass(User user, OutBean bean)
        throws MYException
    {
        // 如果是库管通过，而且是先款后货的话,直接是付款结束
        if (bean.getType() != OutConstant.OUT_TYPE_OUTBILL)
        {
            return;
        }

        // 待回款状态 且是款到发货
        if (bean.getStatus() == OutConstant.STATUS_PASS
            && bean.getReserve3() == OutConstant.OUT_SAIL_TYPE_MONEY)
        {
            List<InBillBean> billList = inBillDAO.queryEntityBeansByFK(bean.getFullId());

            if (billList.size() > 0)
            {
                for (InBillBean inBillBean : billList)
                {
                    // 固化收款单和销售单的关联关系
                    inBillBean.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);
                }

                inBillDAO.updateAllEntityBeans(billList);
            }

            // 已经支付的
            double hasPay = inBillDAO.sumByOutId(bean.getFullId());

            // 再次更新已经支付的金额
            outDAO.updateHadPay(bean.getFullId(), hasPay);
        }
    }

    public void onHadPay(User user, OutBean bean)
        throws MYException
    {
        // 已经支付的
        double hasPay = inBillDAO.sumByOutId(bean.getFullId());

        if (bean.getTotal() != (hasPay + bean.getBadDebts()))
        {
            throw new MYException("销售单总金额[%f],当前已经付款金额[%f],坏账金额[%f],没有完全付款");
        }
    }

    public void onReject(User user, OutBean bean)
        throws MYException
    {
        List<InBillBean> list = inBillDAO.queryEntityBeansByFK(bean.getFullId());

        if (list.size() > 0)
        {
            for (InBillBean inBillBean : list)
            {
                inBillBean.setOutId("");

                inBillBean.setStatus(FinanceConstant.INBILL_STATUS_NOREF);
            }

            inBillDAO.updateAllEntityBeans(list);
        }

        // 清空预收
        outDAO.updateHadPay(bean.getFullId(), 0.0d);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "OutListener.FinanceImpl";
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
