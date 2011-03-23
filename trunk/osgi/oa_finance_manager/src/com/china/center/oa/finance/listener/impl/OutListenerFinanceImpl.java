/**
 * File Name: OutListenerFinanceImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener.impl;


import java.util.Formatter;
import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.publics.constant.PluginNameConstant;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.listener.OutListener;
import com.china.center.tools.MathTools;


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

    private OutBillDAO outBillDAO = null;

    private OutDAO outDAO = null;

    private BaseDAO baseDAO = null;

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

    /**
     * CORE 验证回款的核心逻辑
     */
    public ResultBean onHadPay(User user, OutBean bean)
    {
        ResultBean result = new ResultBean();

        result.setResult(0);

        // 非销售单没有回款
        if (bean.getType() != OutConstant.OUT_TYPE_OUTBILL)
        {
            return result;
        }

        // 赠送没有金额
        if (bean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
        {
            return result;
        }

        // 个人领样
        if (bean.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH)
        {
            return processSwithPay(bean.getFullId());
        }

        // 已经收到客户的金额
        double hasPay = inBillDAO.sumByOutId(bean.getFullId());

        // 已经退货付款的金额
        double hasdOut = outBillDAO.sumByRefId(bean.getFullId());

        // 委托的金额
        double balancePay = 0.0d;

        // 退货实物的价值
        double refInOutTotal = 0.0d;

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
        else
        {
            refInOutTotal = outDAO.sumOutBackValue(bean.getFullId());
        }

        double left = bean.getTotal() + hasdOut;

        double right = hasPay + bean.getBadDebts() + balancePay + refInOutTotal;

        // 金额不足
        if ( !MathTools.equal(left, right) && left > right)
        {
            result.setResult(1);
        }

        // 正好
        if (MathTools.equal(left, right))
        {
            result.setResult(0);
        }

        // 溢出
        if ( !MathTools.equal(left, right) && left < right)
        {
            result.setResult( -1);
        }

        Formatter formatter = new Formatter();

        String message = "";

        if ( !MathTools.equal(bean.getTotal(), (hasPay + bean.getBadDebts() + balancePay
                                                + refInOutTotal - hasdOut)))
        {
            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_CONSIGN)
            {
                message = formatter
                    .format(
                        "销售单总金额[%.2f],当前已经付款金额[%.2f],委托退货金额[%.2f],坏账金额[%.2f],退货金额[%.2f],没有完全付款",
                        bean.getTotal(), hasPay, balancePay, bean.getBadDebts(), refInOutTotal)
                    .toString();
            }
            else
            {
                message = formatter.format(
                    "销售单总金额[%.2f],当前已经付款金额[%.2f],坏账金额[%.2f],退货实物价值[%.2f],退货返还金额[%.2f],没有完全付款",
                    bean.getTotal(), hasPay, bean.getBadDebts(), refInOutTotal, hasdOut).toString();
            }
        }
        else
        {
            message = formatter
                .format(
                    "【销售单总金额[%.2f],退货返还金额[%.2f]】,当前已经付款金额[%.2f],坏账金额[%.2f],退货实物价值[%.2f],委托退货金额[%.2f],全部付款",
                    bean.getTotal(), hasdOut, hasPay, bean.getBadDebts(), refInOutTotal, balancePay)
                .toString();
        }

        // 差距值
        result.setValue(bean.getTotal()
                        - ( (hasPay + bean.getBadDebts() + balancePay + refInOutTotal - hasdOut)));

        result.setMessage(message);

        return result;
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

    public double outNeedPayMoney(User user, String fullId)
    {
        OutBean out = outDAO.find(fullId);

        double backTotal = outDAO.sumOutBackValue(fullId);

        double hadOut = outBillDAO.sumByRefId(fullId);

        double hadPay = inBillDAO.sumByOutId(fullId);

        return out.getTotal() + hadOut - hadPay - out.getBadDebts() - backTotal;
    }

    /**
     * 个人领样全部是自己验证回款
     * 
     * @param fullId
     * @throws MYException
     */
    private ResultBean processSwithPay(String fullId)
    {
        ResultBean result = new ResultBean();

        result.setMessage("领样全部退库或/转销售");

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

        // 退货
        List<OutBean> refBuyList = queryRefOut(fullId);

        // 销售
        List<OutBean> refList = querySailRef(fullId);

        // 计算出已经退货的数量
        for (BaseBean baseBean : baseList)
        {
            int hasBack = 0;

            for (OutBean ref : refBuyList)
            {
                List<BaseBean> refBaseList = ref.getBaseList();

                for (BaseBean refBase : refBaseList)
                {
                    if (refBase.equals(baseBean))
                    {
                        hasBack += refBase.getAmount();

                        break;
                    }
                }
            }

            for (OutBean ref : refList)
            {
                List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

                for (BaseBean refBase : refBaseList)
                {
                    if (refBase.equals(baseBean))
                    {
                        hasBack += refBase.getAmount();

                        break;
                    }
                }
            }

            baseBean.setInway(hasBack);
        }

        for (BaseBean baseBean : baseList)
        {
            if (baseBean.getInway() > baseBean.getAmount())
            {
                result.setResult( -1);
            }

            if (baseBean.getInway() < baseBean.getAmount())
            {
                result.setResult(1);
            }

            if (baseBean.getInway() != baseBean.getAmount())
            {
                result.setMessage(baseBean.getProductName() + "没有全部退库");

                break;
            }
        }

        return result;
    }

    /**
     * 返回个人领样退货的入库单
     * 
     * @param outId
     * @return
     */
    private List<OutBean> queryRefOut(String outId)
    {
        // 查询当前已经有多少个人领样
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition(" and OutBean.status in (3, 4)");

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        List<OutBean> refBuyList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : refBuyList)
        {
            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            outBean.setBaseList(list);
        }

        return refBuyList;
    }

    /**
     * 返回个人领样转销售的单据
     * 
     * @param fullId
     * @return
     */
    private List<OutBean> querySailRef(String fullId)
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", fullId);

        con.addCondition(" and OutBean.status in (3, 4)");

        con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        // 包括保存的,防止溢出
        List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

        return refList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return PluginNameConstant.OUTLISTENER_FINANCEIMPL;
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
     * @return the baseDAO
     */
    public BaseDAO getBaseDAO()
    {
        return baseDAO;
    }

    /**
     * @param baseDAO
     *            the baseDAO to set
     */
    public void setBaseDAO(BaseDAO baseDAO)
    {
        this.baseDAO = baseDAO;
    }
}
