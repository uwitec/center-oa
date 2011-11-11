/**
 * File Name: StockPayApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.bean.StockPayApplyBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.constant.StockPayApplyConstant;
import com.china.center.oa.finance.dao.StockPayApplyDAO;
import com.china.center.oa.finance.listener.StockPayApplyListener;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.finance.manager.StockPayApplyManager;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * StockPayApplyManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-2-18
 * @see StockPayApplyManagerImpl
 * @since 3.0
 */
@Exceptional
public class StockPayApplyManagerImpl extends AbstractListenerManager<StockPayApplyListener> implements StockPayApplyManager
{
    private StockPayApplyDAO stockPayApplyDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private BillManager billManager = null;

    private CommonDAO commonDAO = null;

    private ParameterDAO parameterDAO = null;

    private UserManager userManager = null;

    /**
     * default constructor
     */
    public StockPayApplyManagerImpl()
    {
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean submitStockPayApply(User user, String id, double payMoney, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockPayApplyBean apply = checkSubmit(id, payMoney);

        int preStatus = apply.getStatus();

        // 是否未完全付款
        if (payMoney != apply.getMoneys())
        {
            double last = apply.getMoneys() - payMoney;

            StockPayApplyBean lastApply = new StockPayApplyBean();

            BeanUtil.copyProperties(lastApply, apply);

            lastApply.setMoneys(last);

            lastApply.setStatus(StockPayApplyConstant.APPLY_STATUS_INIT);

            lastApply.setDescription(apply.getDescription() + ".分拆");

            lastApply.setLogTime(TimeTools.now());

            lastApply.setId(commonDAO.getSquenceString20());

            stockPayApplyDAO.saveEntityBean(lastApply);
        }

        apply.setStatus(getNextStatus(user, apply));

        apply.setMoneys(payMoney);

        stockPayApplyDAO.updateEntityBean(apply);

        saveFlowLog(user, preStatus, apply, reason, PublicConstant.OPRMODE_PASS);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean composeStockPayApply(User user, List<String> idList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, idList);

        if (idList.size() <= 1)
        {
            throw new MYException("超过两个申请才可以合并,请确认操作");
        }

        List<StockPayApplyBean> beanList = new ArrayList<StockPayApplyBean>();

        double total = 0.0d;

        String pid = "";

        StringBuffer stockBuffer = new StringBuffer();

        StringBuffer stockItemBuffer = new StringBuffer();

        StringBuffer idBuffer = new StringBuffer();

        for (String id : idList)
        {
            StockPayApplyBean bean = stockPayApplyDAO.find(id);

            if (bean == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            if (bean.getStatus() != StockPayApplyConstant.APPLY_STATUS_INIT)
            {
                throw new MYException("付款申请不再初始态,不能合并,请确认操作");
            }

            if (StringTools.isNullOrNone(pid))
            {
                pid = bean.getProvideId();
            }
            else
            {
                if ( !pid.equals(bean.getProvideId()))
                {
                    throw new MYException("供应商一致才能合并,请确认操作");
                }
            }

            idBuffer
                .append(id)
                .append("/金额:")
                .append(MathTools.formatNum(bean.getMoneys()))
                .append(';');

            stockBuffer.append(bean.getStockId()).append(';');
            stockItemBuffer.append(bean.getStockItemId()).append(';');

            total += bean.getMoneys();

            beanList.add(bean);
        }

        StockPayApplyBean apply = new StockPayApplyBean();

        apply.setId(commonDAO.getSquenceString20());

        apply.setDescription("合并后系统生成付款申请:" + idBuffer.toString() + ".采购单:"
                             + stockBuffer.toString() + ".采购项:" + stockItemBuffer.toString());

        apply.setDutyId(beanList.get(0).getDutyId());

        apply.setInvoiceId(beanList.get(0).getInvoiceId());

        apply.setLocationId(beanList.get(0).getLocationId());

        apply.setLogTime(TimeTools.now());

        apply.setMoneys(total);

        // 今天
        apply.setPayDate(TimeTools.now_short());

        apply.setProvideId(beanList.get(0).getProvideId());

        apply.setStatus(StockPayApplyConstant.APPLY_STATUS_INIT);

        apply.setStockId("");

        apply.setStockItemId("");

        apply.setStafferId(user.getStafferId());

        stockPayApplyDAO.saveEntityBean(apply);

        // 自动结束掉
        for (StockPayApplyBean each : beanList)
        {
            each.setStatus(StockPayApplyConstant.APPLY_STATUS_END);

            stockPayApplyDAO.updateEntityBean(each);

            saveFlowLog(user, StockPayApplyConstant.APPLY_STATUS_INIT, each, "自动合并付款单",
                PublicConstant.OPRMODE_EXCEPTION);
        }

        return true;
    }

    private void saveFlowLog(User user, int preStatus, StockPayApplyBean apply, String reason,
                             int oprMode)
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
     * checkSubmit
     * 
     * @param id
     * @param payMoney
     * @return
     * @throws MYException
     */
    private StockPayApplyBean checkSubmit(String id, double payMoney)
        throws MYException
    {
        StockPayApplyBean apply = stockPayApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( ! (apply.getStatus() == StockPayApplyConstant.APPLY_STATUS_INIT || apply.getStatus() == StockPayApplyConstant.APPLY_STATUS_REJECT))
        {
            throw new MYException("状态不能提交,请确认操作");
        }

        if (TimeTools.now_short().compareTo(apply.getPayDate()) < 0)
        {
            throw new MYException("付款的最早时间还没有到,请确认操作");
        }

        if (MathTools.compare(apply.getMoneys(), payMoney) < 0)
        {
            throw new MYException("申请付款金额溢出,最大付款金额[%.2f],请确认操作", apply.getMoneys());
        }

        return apply;
    }

    /**
     * checkPassByCEO
     * 
     * @param id
     * @param payMoney
     * @return
     * @throws MYException
     */
    private StockPayApplyBean checkPassByCEO(String id)
        throws MYException
    {
        StockPayApplyBean apply = stockPayApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (TimeTools.now_short().compareTo(apply.getPayDate()) < 0)
        {
            throw new MYException("付款的最早时间还没有到,请确认操作");
        }

        return apply;
    }

    private StockPayApplyBean checkEndPass(String id)
        throws MYException
    {
        StockPayApplyBean apply = stockPayApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (apply.getStatus() != StockPayApplyConstant.APPLY_STATUS_SEC)
        {
            throw new MYException("状态不能通过,请确认操作");
        }

        if (TimeTools.now_short().compareTo(apply.getPayDate()) < 0)
        {
            throw new MYException("付款的最早时间还没有到,请确认操作");
        }

        return apply;
    }

    private StockPayApplyBean checkReject(String id)
        throws MYException
    {
        StockPayApplyBean apply = stockPayApplyDAO.find(id);

        if (apply == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (TimeTools.now_short().compareTo(apply.getPayDate()) < 0)
        {
            throw new MYException("付款的最早时间还没有到,请确认操作");
        }

        return apply;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean passStockPayByCEO(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockPayApplyBean apply = checkPassByCEO(id);

        int oldStatus = apply.getStatus();

        apply.setStatus(getNextStatus(user, apply));

        stockPayApplyDAO.updateEntityBean(apply);

        saveFlowLog(user, oldStatus, apply, reason, PublicConstant.OPRMODE_PASS);

        return true;
    }

    /**
     * getNextStatus
     * 
     * @param apply
     * @return
     * @throws MYException
     */
    private int getNextStatus(User user, StockPayApplyBean apply)
        throws MYException
    {
        int next = StockPayApplyConstant.APPLY_STATUS_SAIL;

        if (apply.getStatus() == StockPayApplyConstant.APPLY_STATUS_INIT)
        {
            if ( !userManager.containAuth(user.getId(), AuthConstant.STOCK_PAY_APPLY))
            {
                throw new MYException("没有操作权限");
            }

            next = StockPayApplyConstant.APPLY_STATUS_SAIL;
        }

        if (apply.getStatus() == StockPayApplyConstant.APPLY_STATUS_SAIL)
        {
            if ( !userManager.containAuth(user.getId(), AuthConstant.STOCK_PAY_SAIL))
            {
                throw new MYException("没有操作权限");
            }

            next = StockPayApplyConstant.APPLY_STATUS_CHECK;
        }

        if (apply.getStatus() == StockPayApplyConstant.APPLY_STATUS_CHECK)
        {
            if ( !userManager.containAuth(user.getId(), AuthConstant.STOCK_PAY_CHECK))
            {
                throw new MYException("没有操作权限");
            }

            next = StockPayApplyConstant.APPLY_STATUS_CFO;
        }

        if (apply.getStatus() == StockPayApplyConstant.APPLY_STATUS_CFO)
        {
            if ( !userManager.containAuth(user.getId(), AuthConstant.STOCK_PAY_CFO))
            {
                throw new MYException("没有操作权限");
            }

            int max = parameterDAO.getInt(SysConfigConstant.STOCK_MAX_SINGLE_MONEY);

            if (apply.getMoneys() >= max)
            {
                next = StockPayApplyConstant.APPLY_STATUS_CEO;
            }
            else
            {
                next = StockPayApplyConstant.APPLY_STATUS_SEC;
            }
        }

        if (apply.getStatus() == StockPayApplyConstant.APPLY_STATUS_CEO)
        {
            if ( !userManager.containAuth(user.getId(), AuthConstant.STOCK_PAY_CEO))
            {
                throw new MYException("没有操作权限");
            }

            next = StockPayApplyConstant.APPLY_STATUS_SEC;
        }

        return next;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectStockPayApply(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockPayApplyBean apply = checkReject(id);

        int preStatus = apply.getStatus();

        apply.setStatus(StockPayApplyConstant.APPLY_STATUS_REJECT);

        stockPayApplyDAO.updateEntityBean(apply);

        saveFlowLog(user, preStatus, apply, reason, PublicConstant.OPRMODE_REJECT);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean closeStockPayApply(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockPayApplyBean apply = checkReject(id);

        int preStatus = apply.getStatus();

        apply.setStatus(StockPayApplyConstant.APPLY_STATUS_END);

        stockPayApplyDAO.updateEntityBean(apply);

        saveFlowLog(user, preStatus, apply, reason, PublicConstant.OPRMODE_EXCEPTION);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean endStockPayBySEC(User user, String id, String reason,
                                    List<OutBillBean> outBillList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id, outBillList);

        StockPayApplyBean apply = checkEndPass(id);

        StringBuffer sb = new StringBuffer();

        double totla = 0.0d;
        // 可以生成多个
        for (OutBillBean outBill : outBillList)
        {
            // 生成付款
            createOutBill(user, outBill, apply);

            sb.append(outBill.getId()).append(';');

            totla += outBill.getMoneys();
        }

        if ( !MathTools.equal(apply.getMoneys(), totla))
        {
            throw new MYException("付款金额不正确,应该付款[%.2f],会计填写金额[[%.2f]]", apply.getMoneys(), totla);
        }

        apply.setStatus(StockPayApplyConstant.APPLY_STATUS_END);

        apply.setInBillId(sb.toString());

        // 结束申请流程
        stockPayApplyDAO.updateEntityBean(apply);

        // TAX_ADD 采购付款--会计付款
        Collection<StockPayApplyListener> listenerMapValues = this.listenerMapValues();

        for (StockPayApplyListener listener : listenerMapValues)
        {
            listener.onEndStockPayBySEC(user, apply, outBillList);
        }

        saveFlowLog(user, StockPayApplyConstant.APPLY_STATUS_SEC, apply, reason,
            PublicConstant.OPRMODE_PASS);

        return true;
    }

    /**
     * createOutBill
     * 
     * @param user
     * @param outBill
     * @param apply
     * @throws MYException
     */
    private void createOutBill(User user, OutBillBean outBill, StockPayApplyBean apply)
        throws MYException
    {
        // 自动生成付款单
        outBill.setDescription("采购付款申请:" + apply.getId());

        outBill.setInvoiceId(apply.getInvoiceId());

        outBill.setLocationId(apply.getLocationId());

        outBill.setLogTime(TimeTools.now());

        outBill.setType(FinanceConstant.OUTBILL_TYPE_STOCK);

        outBill.setOwnerId(apply.getStafferId());

        outBill.setStafferId(user.getStafferId());

        outBill.setProvideId(apply.getProvideId());

        outBill.setStockId(apply.getStockId());

        outBill.setStockItemId(apply.getStockItemId());

        billManager.addOutBillBeanWithoutTransaction(user, outBill);
    }

    /**
     * @return the stockPayApplyDAO
     */
    public StockPayApplyDAO getStockPayApplyDAO()
    {
        return stockPayApplyDAO;
    }

    /**
     * @param stockPayApplyDAO
     *            the stockPayApplyDAO to set
     */
    public void setStockPayApplyDAO(StockPayApplyDAO stockPayApplyDAO)
    {
        this.stockPayApplyDAO = stockPayApplyDAO;
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
     * @return the userManager
     */
    public UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * @param userManager
     *            the userManager to set
     */
    public void setUserManager(UserManager userManager)
    {
        this.userManager = userManager;
    }
}
