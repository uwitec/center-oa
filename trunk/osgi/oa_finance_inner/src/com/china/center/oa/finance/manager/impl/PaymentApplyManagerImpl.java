/**
 * File Name: PaymentApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.BackPayApplyConstant;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.BackPayApplyDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.PaymentApplyDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.dao.PaymentVSOutDAO;
import com.china.center.oa.finance.helper.BillHelper;
import com.china.center.oa.finance.listener.PaymentApplyListener;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.PaymentApplyManager;
import com.china.center.oa.finance.vo.InBillVO;
import com.china.center.oa.finance.vo.PaymentVO;
import com.china.center.oa.finance.vs.PaymentVSOutBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.IDPrefixConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.helper.LockHelper;
import com.china.center.oa.publics.helper.OATools;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
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
public class PaymentApplyManagerImpl extends AbstractListenerManager<PaymentApplyListener> implements PaymentApplyManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private PaymentApplyDAO paymentApplyDAO = null;

    private PaymentVSOutDAO paymentVSOutDAO = null;

    private CommonDAO commonDAO = null;

    private PaymentDAO paymentDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private InBillDAO inBillDAO = null;

    private BillManager billManager = null;

    private ParameterDAO parameterDAO = null;

    private BackPayApplyDAO backPayApplyDAO = null;

    private OutDAO outDAO = null;

    private OutManager outManager = null;

    private OutBalanceDAO outBalanceDAO = null;

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

        bean.setId(commonDAO.getSquenceString20(IDPrefixConstant.ID_PAYMENTAPPLY_PREFIX));

        bean.setLogTime(TimeTools.now());

        bean.setStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        List<PaymentVSOutBean> vsList = bean.getVsList();

        // 校验是否是特殊单据
        for (PaymentVSOutBean vsItem : vsList)
        {
            vsItem.setId(commonDAO.getSquenceString20());

            vsItem.setParentId(bean.getId());

            vsItem.setLogTime(bean.getLogTime());

            // 处理每个节点(如果就是待稽核)
            fillEachItem(bean, vsItem);
        }

        // 原来的type
        int oldType = bean.getType();

        if (bean.getType() == FinanceConstant.PAYAPPLY_TYPE_TEMP)
        {
            bean.setType(FinanceConstant.PAYAPPLY_TYPE_BING);
        }

        // 销售单绑定(销售与收款单直接关联)
        if (oldType == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            for (PaymentVSOutBean vsItem : vsList)
            {
                // 校验是否一个销售单被多次绑定
                int count = paymentApplyDAO.countApplyByOutId(vsItem.getOutId());

                if (count > 0)
                {
                    throw new MYException("单据[%s]已经申请付款,请审批付款后再提交新的申请", vsItem.getOutId());
                }

                OutBean out = outDAO.find(vsItem.getOutId());

                // 更新预付金额
                InBillVO bill = inBillDAO.findVO(vsItem.getBillId());

                if (bill == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                // 2012后
                if (OATools.getManagerFlag() && out.getOutTime().compareTo("2012-01-01") >= 0)
                {
                    if ( !out.getDutyId().equals(bill.getDutyId()))
                    {
                        throw new MYException("勾款认领时不可跨纳税实体扣款,请确认操作");
                    }
                }

                if (bill.getStatus() != FinanceConstant.INBILL_STATUS_NOREF)
                {
                    throw new MYException("关联的收款单必须是预收,请确认操作");
                }

                bill.setStatus(FinanceConstant.INBILL_STATUS_PREPAYMENTS);

                inBillDAO.updateEntityBean(bill);
            }
        }

        // 界面上直接回款绑定销售和预收
        if (oldType == FinanceConstant.PAYAPPLY_TYPE_PAYMENT)
        {
            PaymentVO payment = paymentDAO.findVO(bean.getPaymentId());

            if (payment == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 指定认领的操作检查
            if ( !"0".equals(payment.getDestStafferId())
                && !StringTools.isNullOrNone(payment.getDestStafferId()))
            {
                if ( !user.getStafferId().equals(payment.getDestStafferId()))
                {
                    throw new MYException("此回款只能[%s]认领,请确认操作", user.getStafferName());
                }
            }

            for (PaymentVSOutBean vsItem : vsList)
            {
                if ( !StringTools.isNullOrNone(vsItem.getOutId()))
                {
                    // 校验是否一个销售单被多次绑定(因为委托单里面也是关联销售单的)
                    int count = paymentApplyDAO.countApplyByOutId(vsItem.getOutId());

                    if (count > 0)
                    {
                        throw new MYException("单据[%s]已经申请付款,请审批付款后再提交新的申请", vsItem.getOutId());
                    }

                    OutBean out = outDAO.find(vsItem.getOutId());

                    // 2012后(废除)
                    if (OATools.getManagerFlag() && out.getOutTime().compareTo("2012-01-01") >= 0
                        && false)
                    {
                        if ( !out.getDutyId().equals(payment.getDutyId()))
                        {
                            throw new MYException("勾款认领时不可跨纳税实体扣款,请确认操作");
                        }
                    }
                }
            }
        }

        double tt = bean.getMoneys();

        // 业务员勾款(销售单界面勾款)
        if (oldType == FinanceConstant.PAYAPPLY_TYPE_TEMP)
        {
            // 只有一个销售单
            String outId = vsList.get(0).getOutId();

            // 校验是否一个销售单被多次申请付款
            int count = paymentApplyDAO.countApplyByOutId(outId);

            if (count > 0)
            {
                throw new MYException("单据[%s]已经申请付款,请审批付款后再提交新的申请", outId);
            }

            OutBean out = outDAO.find(outId);

            if (out == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            String outBalanceId = vsList.get(0).getOutBalanceId();

            tt = 0.0d;

            double lastMoney = 0.0d;

            // 600
            if (StringTools.isNullOrNone(outBalanceId))
            {
                lastMoney = outManager.outNeedPayMoney(user, outId);
            }
            else
            {
                // 看结算单的钱
                OutBalanceBean outBal = outBalanceDAO.find(outBalanceId);

                if (outBal == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                lastMoney = outBal.getTotal() - outBal.getPayMoney();
            }

            double total = 0.0d;

            boolean remove = false;

            for (Iterator iterator = vsList.iterator(); iterator.hasNext();)
            {
                PaymentVSOutBean vsItem = (PaymentVSOutBean)iterator.next();

                if (remove)
                {
                    iterator.remove();
                }

                // 1800
                total += vsItem.getMoneys();

                // 保证金额不超出
                if (MathTools.compare(total, lastMoney) > 0)
                {
                    // 拆分此单
                    billManager.splitInBillBeanWithoutTransactional(user, vsItem.getBillId(),
                        (total - lastMoney));

                    remove = true;
                }

                // 更新预付金额
                InBillVO bill = inBillDAO.findVO(vsItem.getBillId());

                if (bill.getStatus() != FinanceConstant.INBILL_STATUS_NOREF)
                {
                    throw new MYException("关联的收款单必须是预收,请确认操作");
                }

                // 2012后
                if (OATools.getManagerFlag() && out.getOutTime().compareTo("2012-01-01") >= 0
                    && false)
                {
                    if ( !out.getDutyId().equals(bill.getDutyId()))
                    {
                        throw new MYException("勾款认领时不可跨纳税实体扣款,请确认操作");
                    }
                }

                vsItem.setMoneys(bill.getMoneys());

                bill.setStatus(FinanceConstant.INBILL_STATUS_PREPAYMENTS);

                inBillDAO.updateEntityBean(bill);

                tt += bill.getMoneys();
            }
        }

        // 预收转费用
        if (oldType == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
        {
            String billId = bean.getVsList().get(0).getBillId();

            checkHaveBackPay(billId);

            // 更新预付金额
            InBillBean bill = inBillDAO.find(billId);

            if (bill.getStatus() != FinanceConstant.INBILL_STATUS_NOREF)
            {
                throw new MYException("预收转费用必须是预收,请确认操作");
            }

            if ( !bill.getOwnerId().equals(user.getStafferId()))
            {
                throw new MYException("只能操作自己的单据,请确认操作");
            }

            bill.setStatus(FinanceConstant.INBILL_STATUS_PREPAYMENTS);

            inBillDAO.updateEntityBean(bill);
        }

        bean.setMoneys(tt);

        paymentApplyDAO.saveEntityBean(bean);

        paymentVSOutDAO.saveAllEntityBeans(vsList);

        saveFlowlog(user, bean);

        return true;
    }

    /**
     * 处理每个节点
     * 
     * @param bean
     * @param vsItem
     */
    private void fillEachItem(PaymentApplyBean bean, PaymentVSOutBean vsItem)
    {
        if (bean.getType() == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
        {
            return;
        }

        String dutyId = "";

        String bdutyId = "";

        if ( !StringTools.isNullOrNone(vsItem.getOutId()))
        {
            OutBean out = outDAO.find(vsItem.getOutId());

            if (out != null)
            {
                dutyId = out.getDutyId();
            }
        }

        if ( !StringTools.isNullOrNone(vsItem.getOutBalanceId()))
        {
            OutBalanceBean outBalanceBean = outBalanceDAO.find(vsItem.getOutBalanceId());

            if (outBalanceBean != null)
            {
                OutBean out = outDAO.find(outBalanceBean.getOutId());

                if (out != null)
                {
                    dutyId = out.getDutyId();
                }
            }
        }

        // 客户预收直接返回(没有任何关联)
        if (StringTools.isNullOrNone(dutyId))
        {
            return;
        }

        if ( !StringTools.isNullOrNone(vsItem.getBillId()))
        {
            InBillVO inbill = inBillDAO.findVO(vsItem.getBillId());

            if (inbill != null)
            {
                bdutyId = inbill.getDutyId();
            }
        }

        if ( !StringTools.isNullOrNone(vsItem.getPaymentId()))
        {
            PaymentVO paymentBean = paymentDAO.findVO(vsItem.getPaymentId());

            if (paymentBean != null)
            {
                bdutyId = paymentBean.getDutyId();
            }
        }

        // 关注单据
        if ( !dutyId.equals(bdutyId))
        {
            bean.setVtype(PublicConstant.VTYPE_SPECIAL);

            bean.setStatus(FinanceConstant.PAYAPPLY_STATUS_CHECK);
        }
    }

    /**
     * 是否存在退款申请
     * 
     * @param billId
     * @throws MYException
     */
    private void checkHaveBackPay(String billId)
        throws MYException
    {
        // 一个单子只能存在一个申请
        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addCondition("BackPayApplyBean.billId", "=", billId);

        condition.addIntCondition("BackPayApplyBean.status", "<",
            BackPayApplyConstant.STATUS_REJECT);

        int countByCondition = backPayApplyDAO.countByCondition(condition.toString());

        if (countByCondition > 0)
        {
            throw new MYException("此预收存在未结束的退款申请,请确认操作");
        }
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

        log.setPreStatus(bean.getStatus());

        flowLogDAO.saveEntityBean(log);
    }

    private void checkAdd(User user, PaymentApplyBean bean)
        throws MYException
    {
        if (bean.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            return;
        }

        if (bean.getType() == FinanceConstant.PAYAPPLY_TYPE_TEMP)
        {
            return;
        }

        if (bean.getType() == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
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
            throw new MYException("回款单已经全部被使用,请确认操作");
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

        if (MathTools.compare(hasUsed + bean.getMoneys(), payment.getMoney()) > 0)
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

    /**
     * CORE 回款认领等的审核通过(回款转预收/销售单绑定(预收转应收)/预收转费用)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean passPaymentApply(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        PaymentApplyBean apply = checkPass(id);

        apply.setStatus(FinanceConstant.PAYAPPLY_STATUS_PASS);

        paymentApplyDAO.updateEntityBean(apply);

        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        // CORE 生成收款单,更新销售单和委托清单付款状态/或者转成费用
        createInbill(user, apply, payment, reason);

        // 更新回款单的状态和使用金额
        updatePayment(apply);

        // TAX_ADD 回款转预收/销售单绑定(预收转应收)/预收转费用 通过
        Collection<PaymentApplyListener> listenerMapValues = this.listenerMapValues();

        for (PaymentApplyListener listener : listenerMapValues)
        {
            listener.onPassBean(user, apply);
        }

        savePassLog(user, FinanceConstant.PAYAPPLY_STATUS_INIT, apply, reason);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean passCheck(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        PaymentApplyBean apply = paymentApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        apply.setStatus(FinanceConstant.PAYAPPLY_STATUS_INIT);

        paymentApplyDAO.updateEntityBean(apply);

        savePassLog(user, FinanceConstant.PAYAPPLY_STATUS_CHECK, apply, reason);

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
    private void createInbill(User user, PaymentApplyBean apply, PaymentBean payment, String reason)
        throws MYException
    {
        List<PaymentVSOutBean> vsList = apply.getVsList();

        for (PaymentVSOutBean item : vsList)
        {
            if (item.getMoneys() == 0.0d)
            {
                continue;
            }

            // 生成收款单(回款转预收)
            if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_PAYMENT)
            {
                // 回款转收款通过,增加收款单
                saveBillInner(user, apply, payment, item, reason);
            }
            // 预收转费用
            else if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
            {
                // 把预收转成费用,且新生成的需要核对
                String billId = item.getBillId();

                // 更新预付金额
                InBillBean bill = inBillDAO.find(billId);

                if (bill.getStatus() != FinanceConstant.INBILL_STATUS_PREPAYMENTS)
                {
                    throw new MYException("预收转费用必须是关联申请态,请确认操作");
                }

                bill.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

                bill.setOutId("");

                bill.setOutBalanceId("");

                // 变成未核对的状态
                bill.setCheckStatus(PublicConstant.CHECK_STATUS_INIT);

                // 转成费用的收款单
                bill.setType(FinanceConstant.INBILL_TYPE_FEE);

                bill.setDescription(bill.getDescription() + " " + apply.getDescription());

                inBillDAO.updateEntityBean(bill);
            }
            else
            {
                // 绑定销售单(回款转预收&&预收转应收)
                InBillBean bill = inBillDAO.find(item.getBillId());

                if (bill == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                if (bill.getStatus() == FinanceConstant.INBILL_STATUS_PAYMENTS)
                {
                    throw new MYException("收款单状态错误,请确认操作");
                }

                // 这里防止并行对销售单操作
                synchronized (LockHelper.getLock(item.getOutId()))
                {
                    OutBean outBean = outDAO.find(item.getOutId());

                    if (outBean == null)
                    {
                        throw new MYException("数据错误,请确认操作");
                    }

                    if ( !OutHelper.canFeeOpration(outBean))
                    {
                        throw new MYException("销售单状态错误,请确认操作");
                    }

                    if ( !StringTools.isNullOrNone(outBean.getChecks()))
                    {
                        bill.setDescription(bill.getDescription() + "<br>销售单核对信息:"
                                            + outBean.getChecks() + "<br>审批意见(" + item.getOutId()
                                            + "):" + reason);
                    }

                    if (bill.getCheckStatus() == PublicConstant.CHECK_STATUS_END)
                    {
                        bill.setDescription(bill.getDescription() + "<br>与销售单关联付款所以重置核对状态,原核对信息:"
                                            + bill.getChecks() + "<br>审批意见(" + item.getOutId()
                                            + "):" + reason);
                    }
                    else
                    {
                        bill.setDescription(bill.getDescription() + "<br>审批意见(" + item.getOutId()
                                            + "):" + reason);
                    }

                    if (BillHelper.isPreInBill(bill))
                    {
                        // 这里需要把收款单的状态变成未核对
                        BillHelper.initInBillCheckStatus(bill);
                    }

                    bill.setOutId(item.getOutId());

                    bill.setOutBalanceId(item.getOutBalanceId());

                    bill.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

                    // 谁审批的就是谁的单子
                    bill.setStafferId(user.getStafferId());

                    inBillDAO.updateEntityBean(bill);
                }
            }
        }

        // 更新收款单ID到申请里面
        paymentVSOutDAO.updateAllEntityBeans(vsList);

        // 销售单绑定
        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            String outId = vsList.get(0).getOutId();

            String outBalanceId = vsList.get(0).getOutBalanceId();

            // 可能存在坏账处理
            processOut(user, apply, outId, outBalanceId);
        }

        // 里面存在多个销售单或者委托清单(回款转收款 )/这里没有坏账
        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_PAYMENT)
        {
            // 指定认领的操作检查
            if ( !"0".equals(payment.getDestStafferId())
                && !StringTools.isNullOrNone(payment.getDestStafferId()))
            {
                if ( !apply.getStafferId().equals(payment.getDestStafferId()))
                {
                    throw new MYException("此回款只能[%s]认领,请确认操作", user.getStafferName());
                }
            }

            // 手续费
            if (payment.getHandling() > 0)
            {
                int maxFee = parameterDAO.getInt(SysConfigConstant.MAX_RECVIVE_FEE);

                OutBillBean out = new OutBillBean();

                out.setBankId(payment.getBankId());
                out.setDescription("回款转收款自动生成手续费:" + payment.getId() + ".回款金额:"
                                   + MathTools.formatNum(payment.getMoney()));
                out.setLocationId(user.getLocationId());
                out.setMoneys(payment.getHandling());

                if (payment.getMoney() < maxFee)
                {
                    // 个人承担这个费用
                    out.setOwnerId(apply.getStafferId());
                }

                out.setType(FinanceConstant.OUTBILL_TYPE_HANDLING);

                out.setProvideId(payment.getCustomerId());

                out.setStafferId(user.getStafferId());

                // REF
                out.setStockId(payment.getId());

                billManager.addOutBillBeanWithoutTransaction(user, out);
            }

            // 处理销售的回款和付款绑定的核心
            for (PaymentVSOutBean item : vsList)
            {
                if (StringTools.isNullOrNone(item.getOutId()))
                {
                    continue;
                }

                String outId = item.getOutId();

                String outBalanceId = item.getOutBalanceId();

                // 这里肯定是没有坏账的(从设计上就保证)
                apply.setBadMoney(0);
                processOut(user, apply, outId, outBalanceId);
            }
        }
    }

    private void saveBillInner(User user, PaymentApplyBean apply, PaymentBean payment,
                               PaymentVSOutBean item, String reason)
        throws MYException
    {
        InBillBean inBean = new InBillBean();

        inBean.setBankId(payment.getBankId());

        inBean.setCustomerId(apply.getCustomerId());

        inBean.setLocationId(user.getLocationId());

        inBean.setLogTime(TimeTools.now());

        inBean.setMoneys(item.getMoneys());

        inBean.setSrcMoneys(item.getMoneys());

        if (StringTools.isNullOrNone(item.getOutId()))
        {
            inBean.setStatus(FinanceConstant.INBILL_STATUS_NOREF);

            inBean.setDescription("自动生成预收收款单,从回款单:" + payment.getId() + ",未关联销售单.审批意见:" + reason);
        }
        else
        {
            inBean.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

            inBean.setDescription("自动生成收款单,从回款单:" + payment.getId() + ",关联的销售单:" + item.getOutId()
                                  + ".审批意见:" + reason);
        }

        inBean.setOutId(item.getOutId());

        inBean.setOutBalanceId(item.getOutBalanceId());

        inBean.setOwnerId(apply.getStafferId());

        inBean.setPaymentId(payment.getId());

        inBean.setStafferId(user.getStafferId());

        inBean.setType(FinanceConstant.INBILL_TYPE_SAILOUT);

        billManager.addInBillBeanWithoutTransaction(user, inBean);

        item.setBillId(inBean.getId());
    }

    /**
     * CORE 处理销售的回款和付款绑定的核心(关联多个就是多次)
     * 
     * @param user
     * @param apply
     * @param outId
     * @param outBalanceId
     * @throws MYException
     */
    private void processOut(User user, PaymentApplyBean apply, String outId, String outBalanceId)
        throws MYException
    {
        synchronized (LockHelper.getLock(outId))
        {
            OutBean out = outDAO.find(outId);

            // 看看销售单是否溢出
            double hasPay = inBillDAO.sumByOutId(outId);

            // 有坏账的存在
            if (apply.getBadMoney() != 0)
            {
                out.setBadDebts(apply.getBadMoney());

                outDAO.modifyBadDebts(outId, apply.getBadMoney());
            }

            out.setHadPay(hasPay);

            // 更新已经支付的金额
            outDAO.updateHadPay(outId, hasPay);

            // 先把委托代销的全部搞定
            if ( !StringTools.isNullOrNone(outBalanceId))
            {
                // 更新委托清单
                OutBalanceBean outBal = outBalanceDAO.find(outBalanceId);

                // 看看委托代销是否溢出
                double hasOutBalancePay = inBillDAO.sumByOutBalanceId(outBal.getId());

                // 发现支付的金额过多
                if (MathTools.compare(hasOutBalancePay, outBal.getTotal()) > 0)
                {
                    throw new MYException("委托清单[%s]的总金额[%.2f],当前已付金额[%.2f]付款金额超出销售金额", outBal
                        .getId(), outBal.getTotal(), hasOutBalancePay);
                }

                outBalanceDAO.updateHadPay(outBal.getId(), hasOutBalancePay);

                // 如果全部支付就自动表示收款
                if (MathTools.equal2(outBal.getTotal(), hasOutBalancePay))
                {
                    outBalanceDAO.updateHadPay(outId, OutConstant.PAY_YES);
                }
            }

            tryUpdateOutPayStatus(user, out);
        }
    }

    /**
     * 尝试更新销售单的付款状态 *
     * 
     * @param user
     * @param out
     * @throws MYException
     */
    private void tryUpdateOutPayStatus(User user, OutBean out)
        throws MYException
    {
        // 看看销售单是否可以结帐
        ResultBean result = outManager.checkOutPayStatus(user, out);

        // 如果全部支付就自动表示收款
        if (result.getResult() == 0)
        {
            try
            {
                // 尝试全部付款
                outManager.payOutWithoutTransactional(user, out.getFullId(), "付款申请通过");
            }
            catch (MYException e)
            {
                _logger.warn(e, e);
            }
        }

        // 回款超出了限制(非法)
        if (result.getResult() == -1)
        {
            throw new MYException(result.getMessage());
        }

        // 付款未完全,逻辑是正常的
        if (result.getResult() == 1)
        {
            _logger.info(result.getMessage());
        }
    }

    private void updatePayment(PaymentApplyBean apply)
    {
        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_BING)
        {
            return;
        }

        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
        {
            return;
        }

        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        double hasUsed = inBillDAO.sumByPaymentId(apply.getPaymentId());

        payment.setUseMoney(hasUsed);

        if (MathTools.compare(hasUsed, payment.getMoney()) >= 0)
        {
            payment.setUseall(FinanceConstant.PAYMENT_USEALL_END);
            payment.setUpdateTime(TimeTools.now());
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

        if (apply.getStatus() != FinanceConstant.PAYAPPLY_STATUS_INIT)
        {
            throw new MYException("状态不正确,请确认操作");
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

        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
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

        if (MathTools.compare(hasUsed + apply.getMoneys(), payment.getMoney()) > 0)
        {
            throw new MYException("回款使用金额溢出,总金额[%.2f],已使用金额[%.2f],本次申请金额[%.2f],请确认操作", payment
                .getMoney(), hasUsed, apply.getMoneys());
        }

        return apply;
    }

    private void savePassLog(User user, int oldStatus, PaymentApplyBean apply, String reason)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_PASS);

        log.setDescription(reason);

        log.setLogTime(TimeTools.now());

        log.setPreStatus(oldStatus);

        log.setAfterStatus(apply.getStatus());

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

        if (apply.getStatus() == FinanceConstant.PAYAPPLY_STATUS_PASS)
        {
            throw new MYException("状态错误,请确认操作");
        }

        apply.setStatus(FinanceConstant.PAYAPPLY_STATUS_REJECT);

        paymentApplyDAO.updateEntityBean(apply);

        List<PaymentVSOutBean> vsList = paymentVSOutDAO.queryEntityBeansByFK(id);

        for (PaymentVSOutBean item : vsList)
        {
            // 如果是关联收款单则取消/预收转费用
            if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_BING
                || apply.getType() == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
            {
                InBillBean bill = inBillDAO.find(item.getBillId());

                if (bill != null)
                {
                    bill.setOutId("");

                    bill.setStatus(FinanceConstant.INBILL_STATUS_NOREF);

                    inBillDAO.updateEntityBean(bill);
                }
            }
        }

        // 这里驳回需要单据复原
        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_PAYMENT)
        {
            PaymentBean pay = paymentDAO.find(apply.getPaymentId());

            if (pay != null)
            {
                pay.setStafferId("");

                pay.setCustomerId("");

                pay.setStatus(FinanceConstant.PAYMENT_STATUS_INIT);

                pay.setUpdateTime("");

                paymentDAO.updateEntityBean(pay);
            }
        }

        saveRejectLog(user, apply, reason);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checkPaymentApply(User user, String id, String checks, String refId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        PaymentApplyBean apply = paymentApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        apply.setCheckStatus(PublicConstant.CHECK_STATUS_END);
        apply.setChecks(checks + " [" + TimeTools.now() + ']');
        apply.setCheckrefId(refId);

        paymentApplyDAO.updateEntityBean(apply);

        return true;
    }

    private void saveRejectLog(User user, PaymentApplyBean apply, String reason)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_REJECT);

        log.setDescription(reason);

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

            fillEachItem(bean, vsItem);
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

    /**
     * @return the parameterDAO
     */
    public ParameterDAO getParameterDAO()
    {
        return parameterDAO;
    }

    /**
     * @param parameterDAO
     *            the parameterDAO to set
     */
    public void setParameterDAO(ParameterDAO parameterDAO)
    {
        this.parameterDAO = parameterDAO;
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

}
