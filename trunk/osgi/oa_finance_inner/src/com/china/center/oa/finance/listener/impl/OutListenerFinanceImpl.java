/**
 * File Name: OutListenerFinanceImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener.impl;


import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.helper.BillHelper;
import com.china.center.oa.finance.listener.BillListener;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.BillOutManager;
import com.china.center.oa.publics.constant.PluginNameConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
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
import com.china.center.tools.TimeTools;


/**
 * OutListenerFinanceImpl
 * 
 * @author ZHUZHU
 * @version 2011-1-9
 * @see OutListenerFinanceImpl
 * @since 3.0
 */
public class OutListenerFinanceImpl extends AbstractListenerManager<BillListener> implements OutListener, BillOutManager
{
    private InBillDAO inBillDAO = null;

    private OutBillDAO outBillDAO = null;

    private BillManager billManager = null;

    private CommonDAO commonDAO = null;

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
        // 只监听销售行为哦
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
                OutConstant.OUTBALANCE_STATUS_END);

            List<OutBalanceBean> balanceList = outBalanceDAO.queryEntityBeansByCondition(condition);

            for (OutBalanceBean outBalanceBean : balanceList)
            {
                balancePay += outBalanceBean.getTotal();
            }
        }
        else
        {
            // 查询销售退货的价值
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
                        "销售单总金额[%.2f],当前已经收到的付款金额[%.2f],委托退货金额[%.2f],坏账金额[%.2f],退货金额[%.2f],没有完全结算",
                        bean.getTotal(), hasPay, balancePay, bean.getBadDebts(), refInOutTotal)
                    .toString();
            }
            else
            {
                message = formatter.format(
                    "销售单总金额[%.2f],当前已经收到的付款金额[%.2f],坏账金额[%.2f],退货实物价值[%.2f],退货返还金额[%.2f],没有完全结算",
                    bean.getTotal(), hasPay, bean.getBadDebts(), refInOutTotal, hasdOut).toString();
            }
        }
        else
        {
            message = formatter
                .format(
                    "【销售单总金额[%.2f],退货返还金额[%.2f]】,当前已经收到的付款金额[%.2f],坏账金额[%.2f],退货实物价值[%.2f],委托退货金额[%.2f],全部结算",
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
        receiveToPre(user, bean);

        // 清空已经付款
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

        result.setMessage("领样全部退库/转销售");

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

        con.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_IN_OTHER);

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

    public void onDelete(User user, OutBean bean)
        throws MYException
    {
        receiveToPre(user, bean);
    }

    /**
     * 已收转预收
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void receiveToPre(User user, OutBean bean)
        throws MYException
    {
        List<InBillBean> list = inBillDAO.queryEntityBeansByFK(bean.getFullId());

        if (list.size() > 0)
        {
            // 已收转预算
            for (InBillBean inBillBean : list)
            {
                inBillBean.setOutId("");

                inBillBean.setStatus(FinanceConstant.INBILL_STATUS_NOREF);

                if (inBillBean.getCheckStatus() == PublicConstant.CHECK_STATUS_END)
                {
                    inBillBean.setDescription(inBillBean.getDescription()
                                              + "<br>销售单被驳回/删除,重置收款单的核对状态.");

                    BillHelper.initInBillCheckStatus(inBillBean);
                }
            }

            inBillDAO.updateAllEntityBeans(list);

            // TAX_ADD 销售单驳回/删除后,应收转预收
            Collection<BillListener> listenerMapValues = this.listenerMapValues();

            for (BillListener listener : listenerMapValues)
            {
                listener.onFeeInReceiveToPre(user, bean, list);
            }
        }
    }

    public void onCancleBadDebts(User user, OutBean bean)
        throws MYException
    {
        // 取消坏账是这样的查询坏账的冲单,然后生成对冲的单据
        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addCondition("InBillBean.outId", "=", bean.getFullId());

        condition.addIntCondition("InBillBean.type", "=", FinanceConstant.INBILL_TYPE_BADOUT);

        List<InBillBean> inList = inBillDAO.queryEntityBeansByCondition(condition);

        for (InBillBean inBillBean : inList)
        {
            if (inBillBean.getCheckStatus() == PublicConstant.CHECK_STATUS_INIT
                && inBillBean.getLock() == FinanceConstant.BILL_LOCK_NO)
            {
                inBillDAO.deleteEntityBean(inBillBean.getId());
            }
            else
            {
                String id = inBillBean.getId();

                // 后生成对冲的单据(因为被锁定了)(且必须是)
                inBillBean.setId(commonDAO.getSquenceString20());
                inBillBean.setMoneys( -inBillBean.getMoneys());
                inBillBean.setSrcMoneys( -inBillBean.getMoneys());
                inBillBean.setCheckStatus(PublicConstant.CHECK_STATUS_INIT);
                inBillBean.setChecks("");
                inBillBean.setDescription("取消坏账生成的对冲单据");
                inBillBean.setRefBillId(id);
                inBillBean.setLock(FinanceConstant.BILL_LOCK_NO);
                inBillBean.setLogTime(TimeTools.now());

                billManager.saveInBillInner(inBillBean);
            }
        }
    }

    public void onConfirmBadDebts(User user, OutBean bean)
        throws MYException
    {
        // 在收款单的时候坏账是人工生成的,所以无实现
    }

    public void onCheck(User user, OutBean bean)
        throws MYException
    {
        // 更新未核对的收款单UPDATEID
        List<InBillBean> billList = inBillDAO.queryEntityBeansByFK(bean.getFullId());

        for (InBillBean inBillBean : billList)
        {
            if (inBillBean.getCheckStatus() == PublicConstant.CHECK_STATUS_INIT)
            {
                inBillDAO.updateUpdateId(inBillBean.getId(), commonDAO.getSquence());
            }
        }

    }

    public void onConfirmOutOrBuy(User user, OutBean bean)
        throws MYException
    {
        // do noting
    }

    public void onOutBalancePass(User user, OutBalanceBean bean)
        throws MYException
    {
        // do noting
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
