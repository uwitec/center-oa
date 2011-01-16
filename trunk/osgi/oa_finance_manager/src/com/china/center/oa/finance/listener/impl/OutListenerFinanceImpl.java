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
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutBalanceDAO;
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

    private OutBalanceDAO outBalanceDAO = null;

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
        // 如果是库管通过，而且是先款后货的话,直接是付款结束
        if (bean.getType() != OutConstant.OUT_TYPE_OUTBILL)
        {
            return;
        }

        // 已经支付的
        double hasPay = inBillDAO.sumByOutId(bean.getFullId());

        double balancePay = 0.0d;
        if (bean.getOutType() == OutConstant.OUTTYPE_OUT_CONSIGN)
        {
            ConditionParse condition = new ConditionParse();

            condition.addWhereStr();

            condition.addCondition("OutBalanceBean.outId", "=", bean.getFullId());

            condition.addIntCondition("OutBalanceBean.type", "=", OutConstant.OUTBALANCE_TYPE_BACK);
            condition.addIntCondition("OutBalanceBean.status", "=",
                OutConstant.OUTBALANCE_STATUS_PASS);

            List<OutBalanceBean> balanceList = outBalanceDAO.queryEntityBeansByCondition(condition);

            for (OutBalanceBean outBalanceBean : balanceList)
            {
                balancePay += outBalanceBean.getTotal();
            }
        }

        if (bean.getTotal() != (hasPay + bean.getBadDebts() + balancePay))
        {
            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_CONSIGN)
            {

                throw new MYException("销售单总金额[%.2f],当前已经付款金额[%.2f],委托退货金额[%.2f],坏账金额[%.2f],没有完全付款",
                    bean.getTotal(), hasPay, balancePay, bean.getBadDebts());
            }
            else
            {
                throw new MYException("销售单总金额[%.2f],当前已经付款金额[%.2f],坏账金额[%.2f],没有完全付款", bean
                    .getTotal(), hasPay, bean.getBadDebts());
            }
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

    /**
     * @return the outBalanceDAO
     */
    public OutBalanceDAO getOutBalanceDAO()
    {
        return outBalanceDAO;
    }

    /**
     * @param outBalanceDAO
     *            the outBalanceDAO to set
     */
    public void setOutBalanceDAO(OutBalanceDAO outBalanceDAO)
    {
        this.outBalanceDAO = outBalanceDAO;
    }
}
