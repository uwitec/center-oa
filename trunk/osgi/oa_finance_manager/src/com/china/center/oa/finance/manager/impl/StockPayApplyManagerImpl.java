/**
 * File Name: StockPayApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.StockPayApplyBean;
import com.china.center.oa.finance.constant.StockPayApplyConstant;
import com.china.center.oa.finance.dao.StockPayApplyDAO;
import com.china.center.oa.finance.manager.StockPayApplyManager;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
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
public class StockPayApplyManagerImpl implements StockPayApplyManager
{
    private StockPayApplyDAO stockPayApplyDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private CommonDAO commonDAO = null;

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

        apply.setStatus(StockPayApplyConstant.APPLY_STATUS_CEO);

        apply.setMoneys(payMoney);

        stockPayApplyDAO.updateEntityBean(apply);

        savePassLog(user, preStatus, apply, reason);

        return true;
    }

    private void savePassLog(User user, int preStatus, StockPayApplyBean apply, String reason)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setOprMode(PublicConstant.OPRMODE_PASS);

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

        if (apply.getMoneys() < payMoney)
        {
            throw new MYException("申请付款金额溢出,最大付款金额[%.2f],请确认操作", apply.getMoneys());
        }

        return apply;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectStockPayApply(User user, String id, String reason)
        throws MYException
    {
        return false;
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

}
