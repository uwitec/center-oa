/**
 * File Name: BackPayApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-3-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.BackPayApplyBean;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.constant.BackPayApplyConstant;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.BackPayApplyDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.manager.BackPayApplyManager;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.TimeTools;


/**
 * BackPayApplyManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-3-3
 * @see BackPayApplyManagerImpl
 * @since 3.0
 */
@Exceptional
public class BackPayApplyManagerImpl implements BackPayApplyManager
{
    private BackPayApplyDAO backPayApplyDAO = null;

    private CommonDAO commonDAO = null;

    private OutDAO outDAO = null;

    private InBillDAO inBillDAO = null;

    private OutBillDAO outBillDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private BillManager billManager = null;

    private OutManager outManager = null;

    /**
     * default constructor
     */
    public BackPayApplyManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.BackPayApplyManager#addBackPayApplyBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.BackPayApplyBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addBackPayApplyBean(User user, BackPayApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        if (bean.getType() == BackPayApplyConstant.TYPE_OUT)
        {
            checkPay(user, bean);
        }
        else
        {
            checkBillPay(bean);
        }

        bean.setId(commonDAO.getSquenceString20());

        bean.setLogTime(TimeTools.now());

        bean.setStafferId(user.getStafferId());

        bean.setLocationId(user.getLocationId());

        if (bean.getType() == BackPayApplyConstant.TYPE_OUT)
        {
            bean.setStatus(BackPayApplyConstant.STATUS_SUBMIT);
        }
        else
        {
            bean.setStatus(BackPayApplyConstant.STATUS_SEC);
        }

        saveFlowLog(user, BackPayApplyConstant.STATUS_INIT, bean, "提交", PublicConstant.OPRMODE_PASS);

        return backPayApplyDAO.saveEntityBean(bean);
    }

    /**
     * checkBillPay
     * 
     * @param bean
     * @throws MYException
     */
    private void checkBillPay(BackPayApplyBean bean)
        throws MYException
    {
        InBillBean inBill = inBillDAO.find(bean.getBillId());

        if (inBill == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getBackPay() <= 0.0d)
        {
            throw new MYException("退款金额错误,请确认操作");
        }

        if (inBill.getMoneys() < bean.getBackPay())
        {
            throw new MYException("退款金额溢出,请确认操作");
        }

        // 一个单子只能存在一个申请
        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addCondition("BackPayApplyBean.billId", "=", bean.getBillId());

        condition.addIntCondition("BackPayApplyBean.status", "<", BackPayApplyConstant.STATUS_REJECT);

        int countByCondition = backPayApplyDAO.countByCondition(condition.toString());

        if (countByCondition > 0)
        {
            throw new MYException("此预收存在未结束的退款申请,请确认操作");
        }
    }

    /**
     * saveFlowLog
     * 
     * @param user
     * @param preStatus
     * @param apply
     * @param reason
     * @param oprMode
     */
    private void saveFlowLog(User user, int preStatus, BackPayApplyBean apply, String reason, int oprMode)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(oprMode);

        log.setDescription(reason);

        log.setLogTime(TimeTools.now());

        log.setPreStatus(preStatus);

        log.setAfterStatus(apply.getStatus());

        flowLogDAO.saveEntityBean(log);
    }

    /**
     * checkAdd
     * 
     * @param bean
     * @throws MYException
     */
    private void checkPay(User user, BackPayApplyBean bean)
        throws MYException
    {
        double backTotal = outDAO.sumOutBackValue(bean.getOutId());

        OutBean out = outDAO.find(bean.getOutId());

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 查询收款单
        double hasdIn = inBillDAO.sumByOutId(bean.getOutId());

        double hasdOut = outBillDAO.sumByRefId(bean.getOutId());

        double max = -outManager.outNeedPayMoney(user, out.getFullId());

        // 付款金额-退货金额-已经退款金额
        // double max = out.getTotal() - hasdIn + backTotal - hasdOut;

        if ( !MathTools.equal(bean.getBackPay() + bean.getChangePayment(), max))
        {
            throw new MYException(
                "销售单支付金额[%.2f],退货实物价值[%.2f],退货返还金额[%.2f],申请退货返还金额[%.2f],申请转预收金额[%.2f],申请金额必须等于[%.2f]", hasdIn,
                backTotal, hasdOut, bean.getBackPay(), bean.getChangePayment(), max);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.BackPayApplyManager#deleteBackPayApplyBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteBackPayApplyBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        BackPayApplyBean old = backPayApplyDAO.find(id);

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (old.getStatus() != BackPayApplyConstant.STATUS_REJECT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !user.getStafferId().equals(old.getStafferId()))
        {
            throw new MYException("数据错误,请确认操作");
        }

        backPayApplyDAO.deleteEntityBean(id);

        flowLogDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.BackPayApplyManager#passBackPayApplyBean(com.center.china.osgi.publics.User,
     *      java.lang.String, java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean rejectBackPayApplyBean(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        BackPayApplyBean bean = backPayApplyDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        int preStatus = bean.getStatus();

        if (bean.getStatus() != BackPayApplyConstant.STATUS_SUBMIT
            && bean.getStatus() != BackPayApplyConstant.STATUS_SEC)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setStatus(BackPayApplyConstant.STATUS_REJECT);

        backPayApplyDAO.updateEntityBean(bean);

        saveFlowLog(user, preStatus, bean, reason, PublicConstant.OPRMODE_REJECT);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.BackPayApplyManager#rejectBackPayApplyBean(com.center.china.osgi.publics.User,
     *      java.lang.String, java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean passBackPayApplyBean(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        BackPayApplyBean bean = backPayApplyDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getType() == BackPayApplyConstant.TYPE_OUT)
        {
            checkPay(user, bean);
        }

        int next = 0;

        int preStatus = bean.getStatus();

        if (bean.getStatus() == BackPayApplyConstant.STATUS_SUBMIT)
        {
            next = BackPayApplyConstant.STATUS_SEC;
        }
        else if (bean.getStatus() == BackPayApplyConstant.STATUS_SEC)
        {
            next = BackPayApplyConstant.STATUS_END;
        }
        else
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setStatus(next);

        backPayApplyDAO.updateEntityBean(bean);

        saveFlowLog(user, preStatus, bean, reason, PublicConstant.OPRMODE_PASS);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean endBackPayApplyBean(User user, String id, String reason, OutBillBean outBill)
        throws MYException
    {
        // 先通过
        passBackPayApplyBean(user, id, reason);

        BackPayApplyBean bean = backPayApplyDAO.find(id);

        String outBillId = "";

        // 付款
        if (outBill != null)
        {
            outBillId = createOutBill(user, outBill, bean);
        }

        if (bean.getType() == BackPayApplyConstant.TYPE_OUT)
        {
            // 处理销售退款
            handleOut(user, bean);
        }
        else
        {
            // 处理预收退款
            InBillBean inBill = inBillDAO.find(bean.getBillId());

            if (inBill == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            if (inBill.getStatus() != FinanceConstant.INBILL_STATUS_NOREF)
            {
                throw new MYException("收款单已经不在预收状态,请确认操作");
            }

            // 直接把预收拆分成两个单据
            String newId = billManager.splitInBillBeanWithoutTransactional(user, inBill.getId(), bean.getBackPay());

            // 预收
            InBillBean newInBill = inBillDAO.find(newId);

            if (newInBill == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 取消关联了
            newInBill.setOutId("");
            newInBill.setOutBalanceId("");
            newInBill.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);
            newInBill.setLock(FinanceConstant.BILL_LOCK_YES);
            newInBill.setRefBillId(outBillId);
            newInBill.setDescription(newInBill.getDescription() + ";自动关联退款的付款单:" + outBillId);

            inBillDAO.updateEntityBean(newInBill);
        }

        return true;
    }

    /**
     * 处理销售退款
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void handleOut(User user, BackPayApplyBean bean)
        throws MYException
    {
        if (bean.getChangePayment() > 0)
        {
            // 找出预收,然后自动拆分
            List<InBillBean> inList = inBillDAO.queryEntityBeansByFK(bean.getOutId());

            // 从大到小
            Collections.sort(inList, new Comparator<InBillBean>()
            {
                public int compare(InBillBean o1, InBillBean o2)
                {
                    return (int) (o1.getMoneys() - o2.getMoneys());
                }
            });

            double hasOut = bean.getChangePayment();

            for (int i = 0; i < inList.size(); i++ )
            {
                InBillBean each = inList.get(i);

                // 最后的处理
                if (each.getMoneys() > hasOut)
                {
                    String newId = billManager.splitInBillBeanWithoutTransactional(user, each.getId(), hasOut);

                    // 预收
                    InBillBean newInBill = inBillDAO.find(newId);

                    if (newInBill == null)
                    {
                        throw new MYException("数据错误,请确认操作");
                    }

                    // 取消关联了
                    newInBill.setOutId("");
                    newInBill.setOutBalanceId("");
                    newInBill.setStatus(FinanceConstant.INBILL_STATUS_NOREF);

                    inBillDAO.updateEntityBean(newInBill);

                    break;
                }
                else
                {
                    // 逐步的转预收
                    hasOut = hasOut - each.getMoneys();
                    each.setOutId("");
                    each.setOutBalanceId("");
                    each.setStatus(FinanceConstant.INBILL_STATUS_NOREF);
                    each.setDescription("销售退款转预收:" + bean.getOutId());

                    inBillDAO.updateEntityBean(each);

                    if (hasOut == 0.0d)
                    {
                        break;
                    }
                }
            }

            double newPay = inBillDAO.sumByOutId(bean.getOutId());

            // 这里不更新单子的状态,只更新付款金额
            outDAO.updateHadPay(bean.getOutId(), newPay);
        }
    }

    private String createOutBill(User user, OutBillBean outBill, BackPayApplyBean apply)
        throws MYException
    {
        if (apply.getType() == BackPayApplyConstant.TYPE_OUT)
        {
            // 自动生成付款单
            outBill.setDescription("销售退货付款:" + apply.getOutId());

            outBill.setStockId(apply.getOutId());
        }
        else
        {
            InBillBean inBill = inBillDAO.find(apply.getBillId());

            if (inBill == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            outBill.setDescription("预收退款:" + apply.getBillId());

            outBill.setStockId(inBill.getPaymentId());

            outBill.setRefBillId(apply.getBillId());
        }

        outBill.setLocationId(user.getLocationId());

        outBill.setLogTime(TimeTools.now());

        outBill.setType(FinanceConstant.OUTBILL_TYPE_OUTBACK);

        outBill.setMoneys(apply.getBackPay());

        outBill.setOwnerId(apply.getStafferId());

        outBill.setStafferId(user.getStafferId());

        outBill.setProvideId(apply.getCustomerId());

        outBill.setLock(FinanceConstant.BILL_LOCK_YES);

        billManager.addOutBillBeanWithoutTransaction(user, outBill);

        return outBill.getId();
    }

    /**
     * @return the backPayApplyDAO
     */
    public BackPayApplyDAO getBackPayApplyDAO()
    {
        return backPayApplyDAO;
    }

    /**
     * @param backPayApplyDAO
     *            the backPayApplyDAO to set
     */
    public void setBackPayApplyDAO(BackPayApplyDAO backPayApplyDAO)
    {
        this.backPayApplyDAO = backPayApplyDAO;
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

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }
}
