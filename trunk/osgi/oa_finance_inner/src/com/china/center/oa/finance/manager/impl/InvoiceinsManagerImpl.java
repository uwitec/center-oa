/**
 * File Name: InvoiceinsManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-1<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.InvoiceinsItemBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InsVSOutDAO;
import com.china.center.oa.finance.dao.InvoiceinsDAO;
import com.china.center.oa.finance.dao.InvoiceinsItemDAO;
import com.china.center.oa.finance.manager.InvoiceinsManager;
import com.china.center.oa.finance.vo.InvoiceinsVO;
import com.china.center.oa.finance.vs.InsVSOutBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.sail.bean.BaseBalanceBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.BaseBalanceDAO;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * InvoiceinsManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-1-1
 * @see InvoiceinsManagerImpl
 * @since 3.0
 */
@Exceptional
public class InvoiceinsManagerImpl implements InvoiceinsManager
{
    private final Log operationLog = LogFactory.getLog("opr");

    private CommonDAO commonDAO = null;

    private InvoiceinsDAO invoiceinsDAO = null;

    private InvoiceinsItemDAO invoiceinsItemDAO = null;

    private OutBalanceDAO outBalanceDAO = null;

    private InsVSOutDAO insVSOutDAO = null;

    private OutDAO outDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private BaseDAO baseDAO = null;

    private DutyDAO dutyDAO = null;

    private BaseBalanceDAO baseBalanceDAO = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.InvoiceinsManager#addInvoiceinsBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.InvoiceinsBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addInvoiceinsBean(User user, InvoiceinsBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        bean.setLogTime(TimeTools.now());

        DutyBean duty = dutyDAO.find(bean.getDutyId());

        if (duty == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 对分公司的直接OK
        if (bean.getType() == FinanceConstant.INVOICEINS_TYPE_DUTY)
        {
            bean.setStatus(FinanceConstant.INVOICEINS_STATUS_END);

            if ( !bean.getInvoiceId().equals(InvoiceConstant.INVOICE_INSTACE_DK_17))
            {
                throw new MYException("发票只能是:增值专用发票(一般纳税人)[可抵扣](17.00%)");
            }
        }
        else
        {
            // 设置状态
            fillStatus(bean);
        }

        bean.setMtype(duty.getMtype());

        invoiceinsDAO.saveEntityBean(bean);

        List<InvoiceinsItemBean> itemList = bean.getItemList();

        for (InvoiceinsItemBean invoiceinsItemBean : itemList)
        {
            invoiceinsItemBean.setId(commonDAO.getSquenceString20());

            invoiceinsItemBean.setParentId(bean.getId());
        }

        invoiceinsItemDAO.saveAllEntityBeans(itemList);

        List<InsVSOutBean> vsList = bean.getVsList();

        if ( !ListTools.isEmptyOrNull(vsList))
        {
            for (InsVSOutBean insVSOutBean : vsList)
            {
                insVSOutBean.setId(commonDAO.getSquenceString20());

                insVSOutBean.setInsId(bean.getId());
            }

            insVSOutDAO.saveAllEntityBeans(vsList);
        }

        // 这里仅仅是提交,审核通过后才能修改单据的状态
        return true;
    }

    /**
     * fillStatus
     * 
     * @param bean
     */
    private void fillStatus(InvoiceinsBean bean)
    {
        bean.setStatus(FinanceConstant.INVOICEINS_STATUS_SUBMIT);

        List<InsVSOutBean> vsList = bean.getVsList();

        if (ListTools.isEmptyOrNull(vsList))
        {
            return;
        }

        // 是否相同纳税属性
        boolean isEqualsMtype = true;

        // 是否都是A1
        boolean isAllCommon = (bean.getMtype() == PublicConstant.MANAGER_TYPE_COMMON);

        // 这里需要判断是否是关注单据
        for (InsVSOutBean insVSOutBean : vsList)
        {
            if (insVSOutBean.getType() == FinanceConstant.INSVSOUT_TYPE_OUT)
            {
                OutBean out = outDAO.find(insVSOutBean.getOutId());

                if ( !out.getDutyId().equals(bean.getDutyId()))
                {
                    bean.setVtype(PublicConstant.VTYPE_SPECIAL);

                    bean.setStatus(FinanceConstant.INVOICEINS_STATUS_CHECK);

                    break;
                }
            }

            if (insVSOutBean.getType() == FinanceConstant.INSVSOUT_TYPE_BALANCE)
            {
                OutBalanceBean ob = outBalanceDAO.find(insVSOutBean.getOutId());

                OutBean out = outDAO.find(ob.getOutId());

                if ( !out.getDutyId().equals(bean.getDutyId()))
                {
                    bean.setVtype(PublicConstant.VTYPE_SPECIAL);

                    bean.setStatus(FinanceConstant.INVOICEINS_STATUS_CHECK);

                    break;
                }
            }
        }

        Map<String, InvoiceinsItemBean> tmpMap = new HashMap();

        for (InsVSOutBean insVSOutBean : vsList)
        {
            OutBean out = null;

            if (insVSOutBean.getType() == FinanceConstant.INSVSOUT_TYPE_OUT)
            {
                out = outDAO.find(insVSOutBean.getOutId());

            }
            else
            {
                OutBalanceBean ob = outBalanceDAO.find(insVSOutBean.getOutId());

                out = outDAO.find(ob.getOutId());
            }

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(insVSOutBean.getOutId());

            for (BaseBean baseBean : baseList)
            {
                if ( !tmpMap.containsKey(baseBean.getShowId()))
                {
                    InvoiceinsItemBean item = new InvoiceinsItemBean();
                    item.setShowId(baseBean.getShowId());

                    tmpMap.put(baseBean.getShowId(), item);
                }

                InvoiceinsItemBean subItem = tmpMap.get(baseBean.getShowId());

                subItem.setAmount(subItem.getAmount() + baseBean.getAmount());
                subItem.setMoneys(subItem.getMoneys() + baseBean.getValue());
            }

            // 是否同一个纳税实体
            if (isEqualsMtype && out.getMtype() != bean.getMtype())
            {
                isEqualsMtype = false;
            }

            // 是否都是A1
            if (isAllCommon && (out.getMtype() != PublicConstant.MANAGER_TYPE_COMMON))
            {
                isAllCommon = false;
            }

            // 是否同一个纳税实体
            if (isEqualsMtype && out.getMtype() != bean.getMtype())
            {
                isEqualsMtype = false;
            }
        }

        // 设置特殊类型
        if (isAllCommon)
        {
            // 数量相同,但是价格不同
            List<InvoiceinsItemBean> itemList = bean.getItemList();

            for (InvoiceinsItemBean invoiceinsItemBean : itemList)
            {
                InvoiceinsItemBean compareItem = tmpMap.get(invoiceinsItemBean.getShowId());

                if (compareItem == null)
                {
                    bean.setStype(FinanceConstant.INVOICEINS_STYPE_A1A1_APD);

                    return;
                }

                if (compareItem.getAmount() != invoiceinsItemBean.getAmount()
                    && !MathTools.equal2(compareItem.getMoneys(), invoiceinsItemBean.getMoneys()))
                {
                    bean.setStype(FinanceConstant.INVOICEINS_STYPE_A1A1_APD);

                    return;
                }

                if (compareItem.getAmount() == invoiceinsItemBean.getAmount()
                    && !MathTools.equal2(compareItem.getMoneys(), invoiceinsItemBean.getMoneys()))
                {
                    bean.setStype(FinanceConstant.INVOICEINS_STYPE_A1A1_PD);

                    return;
                }
            }
        }
        else
        {
            if (bean.getMtype() != PublicConstant.MANAGER_TYPE_COMMON)
            {
                bean.setStype(FinanceConstant.INVOICEINS_STYPE_A1A2);
            }
            else
            {
                bean.setStype(FinanceConstant.INVOICEINS_STYPE_A2A1);
            }
        }
    }

    /**
     * 处理单据和发票实例
     * 
     * @param insVSOutBean
     * @throws MYException
     */
    private void handlerEachInAdd(InsVSOutBean insVSOutBean)
        throws MYException
    {
        if (insVSOutBean.getType() == FinanceConstant.INSVSOUT_TYPE_OUT)
        {
            // 销售单
            OutBean out = outDAO.find(insVSOutBean.getOutId());

            if (out == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            if (insVSOutBean.getMoneys() + out.getInvoiceMoney() > out.getTotal())
            {
                // TEMPLATE 数字格式化显示
                throw new MYException("单据[%s]开票溢出,开票金额[%.2f],销售金额[%.2f]", out.getFullId(),
                    (insVSOutBean.getMoneys() + out.getInvoiceMoney()), out.getTotal());
            }

            if (insVSOutBean.getMoneys() + out.getInvoiceMoney() == out.getTotal())
            {
                // 更新开票状态-结束
                outDAO.updateInvoiceStatus(out.getFullId(), out.getTotal(),
                    OutConstant.INVOICESTATUS_END);
            }

            if (insVSOutBean.getMoneys() + out.getInvoiceMoney() < out.getTotal())
            {
                // 更新开票状态-过程
                outDAO.updateInvoiceStatus(out.getFullId(), (insVSOutBean.getMoneys() + out
                    .getInvoiceMoney()), OutConstant.INVOICESTATUS_INIT);
            }
        }
        else
        {
            // 结算清单
            OutBalanceBean balance = outBalanceDAO.find(insVSOutBean.getOutId());

            if (balance == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            if (insVSOutBean.getMoneys() + balance.getInvoiceMoney() > balance.getTotal())
            {
                // TEMPLATE 数字格式化显示
                throw new MYException("委托结算单[%s]开票溢出,开票金额[%.2f],销售金额[%.2f]", balance.getId(),
                    (insVSOutBean.getMoneys() + balance.getInvoiceMoney()), balance.getTotal());
            }

            if (insVSOutBean.getMoneys() + balance.getInvoiceMoney() == balance.getTotal())
            {
                // 更新开票状态-结束
                outBalanceDAO.updateInvoiceStatus(balance.getId(), balance.getTotal(),
                    OutConstant.INVOICESTATUS_END);
            }

            if (insVSOutBean.getMoneys() + balance.getInvoiceMoney() < balance.getTotal())
            {
                // 更新开票状态-过程
                outBalanceDAO.updateInvoiceStatus(balance.getId(),
                    (insVSOutBean.getMoneys() + balance.getInvoiceMoney()),
                    OutConstant.INVOICESTATUS_INIT);
            }
        }
    }

    private void handlerEachInAdd2(InsVSOutBean insVSOutBean)
        throws MYException
    {
        if (insVSOutBean.getType() == FinanceConstant.INSVSOUT_TYPE_OUT)
        {
            // 销售单
            OutBean out = outDAO.find(insVSOutBean.getOutId());

            if (out == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            BaseBean base = baseDAO.find(insVSOutBean.getBaseId());

            // 溢出的
            if (MathTools.compare(insVSOutBean.getMoneys() + base.getInvoiceMoney(), base
                .getValue()) > 0)
            {
                throw new MYException("单据[%s]开票溢出,开票金额[%.2f],销售项金额[%.2f]", out.getFullId(),
                    (insVSOutBean.getMoneys() + base.getInvoiceMoney()), base.getValue());
            }

            if (MathTools.compare(insVSOutBean.getMoneys() + base.getInvoiceMoney(), base
                .getValue()) <= 0)
            {
                baseDAO.updateInvoice(base.getId(), (insVSOutBean.getMoneys() + base
                    .getInvoiceMoney()));
            }

            // 更新主单据
            updateOut(out);
        }
        else
        {
            // 结算清单
            OutBalanceBean balance = outBalanceDAO.find(insVSOutBean.getOutId());

            if (balance == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            BaseBalanceBean bbb = baseBalanceDAO.find(insVSOutBean.getBaseId());

            double baseTotal = bbb.getAmount() * bbb.getSailPrice();

            if (MathTools.compare(insVSOutBean.getMoneys() + bbb.getInvoiceMoney(), baseTotal) > 0)
            {
                throw new MYException("委托结算单项[%s]开票溢出,开票金额[%.2f],销售金额[%.2f]", balance.getId(),
                    (insVSOutBean.getMoneys() + bbb.getInvoiceMoney()), baseTotal);
            }

            if (MathTools.compare(insVSOutBean.getMoneys() + bbb.getInvoiceMoney(), baseTotal) <= 0)
            {
                baseBalanceDAO.updateInvoice(bbb.getId(), (insVSOutBean.getMoneys() + bbb
                    .getInvoiceMoney()));
            }

            updateOutBalance(balance);
        }
    }

    /**
     * 更新销售单的开票状态
     * 
     * @param out
     */
    private void updateOut(OutBean out)
    {
        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(out.getFullId());

        double total = 0.0d;

        for (BaseBean baseBean : baseList)
        {
            total += baseBean.getInvoiceMoney();
        }

        // 全部开票
        if (MathTools.compare(total, out.getTotal()) >= 0)
        {
            // 更新开票状态-结束
            outDAO.updateInvoiceStatus(out.getFullId(), out.getTotal(),
                OutConstant.INVOICESTATUS_END);
        }
        else
        {
            // 更新开票状态-过程
            outDAO.updateInvoiceStatus(out.getFullId(), total, OutConstant.INVOICESTATUS_INIT);
        }
    }

    /**
     * updateOutBalance
     * 
     * @param balance
     */
    private void updateOutBalance(OutBalanceBean balance)
    {
        List<OutBalanceBean> baseList = outBalanceDAO.queryEntityBeansByFK(balance.getId());

        double total = 0.0d;

        for (OutBalanceBean baseBean : baseList)
        {
            total += baseBean.getInvoiceMoney();
        }

        // 全部开票
        if (MathTools.compare(total, balance.getTotal()) >= 0)
        {
            // 更新开票状态-结束
            outBalanceDAO.updateInvoiceStatus(balance.getId(), balance.getTotal(),
                OutConstant.INVOICESTATUS_END);
        }
        else
        {
            // 更新开票状态-过程
            outBalanceDAO.updateInvoiceStatus(balance.getId(), total,
                OutConstant.INVOICESTATUS_INIT);
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public void clearRejectInvoiceinsBean()
        throws MYException
    {
        ConditionParse conditionParse = new ConditionParse();

        conditionParse.addWhereStr();

        // 驳回
        conditionParse.addIntCondition("status", "=", FinanceConstant.INVOICEINS_STATUS_REJECT);

        conditionParse.addCondition("logTime", "<=", TimeTools.now( -60));

        List<InvoiceinsBean> beanList = invoiceinsDAO.queryEntityBeansByCondition(conditionParse
            .toString());

        for (InvoiceinsBean invoiceinsBean : beanList)
        {
            realDelete(invoiceinsBean.getId());

            operationLog.info("clearRejectInvoiceinsBean:" + invoiceinsBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.InvoiceinsManager#deleteInvoiceinsBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteInvoiceinsBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InvoiceinsBean bean = invoiceinsDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !bean.getStafferId().equals(user.getStafferId())
            && !bean.getProcesser().equals(user.getStafferId()))
        {
            throw new MYException("只能删除自己的发票或者是自己审批的,请确认操作");
        }

        List<InsVSOutBean> vsList = insVSOutDAO.queryEntityBeansByFK(id, AnoConstant.FK_FIRST);

        realDelete(id);

        if (bean.getStatus() != FinanceConstant.INVOICEINS_STATUS_END)
        {
            return true;
        }

        if (ListTools.isEmptyOrNull(vsList))
        {
            return true;
        }

        // 倒回开票状态
        for (InsVSOutBean insVSOutBean : vsList)
        {
            if (insVSOutBean.getType() == FinanceConstant.INSVSOUT_TYPE_OUT)
            {
                // 销售单
                OutBean out = outDAO.find(insVSOutBean.getOutId());

                if (out == null)
                {
                    continue;
                }

                double im = Math.max(0.0, out.getInvoiceMoney() - insVSOutBean.getMoneys());

                // 更新单据的开票金额
                outDAO.updateInvoiceStatus(out.getFullId(), im, OutConstant.INVOICESTATUS_INIT);
            }
            else
            {
                // 结算清单
                OutBalanceBean balance = outBalanceDAO.find(insVSOutBean.getOutId());

                if (balance == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                double im = Math.max(0.0, balance.getInvoiceMoney() - insVSOutBean.getMoneys());

                // 更新开票状态-过程
                outBalanceDAO.updateInvoiceStatus(balance.getId(), im,
                    OutConstant.INVOICESTATUS_INIT);
            }
        }

        return true;
    }

    /**
     * 清除发票
     * 
     * @param id
     */
    private void realDelete(String id)
    {
        invoiceinsDAO.deleteEntityBean(id);

        invoiceinsItemDAO.deleteEntityBeansByFK(id);

        insVSOutDAO.deleteEntityBeansByFK(id, AnoConstant.FK_FIRST);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean passInvoiceinsBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InvoiceinsBean bean = invoiceinsDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != FinanceConstant.INVOICEINS_STATUS_SUBMIT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        int status = bean.getStatus();

        // 开票成功
        bean.setStatus(FinanceConstant.INVOICEINS_STATUS_END);

        invoiceinsDAO.updateEntityBean(bean);

        List<InsVSOutBean> vsList = insVSOutDAO.queryEntityBeansByFK(id, AnoConstant.FK_FIRST);

        // 单据的开票状态需要更新
        if ( !ListTools.isEmptyOrNull(vsList))
        {
            for (InsVSOutBean insVSOutBean : vsList)
            {
                if (StringTools.isNullOrNone(insVSOutBean.getBaseId()))
                {
                    handlerEachInAdd(insVSOutBean);
                }
                else
                {
                    // 新的开单规则
                    handlerEachInAdd2(insVSOutBean);
                }
            }
        }

        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());
        log.setActorId(user.getStafferId());
        log.setFullId(id);
        log.setDescription("OK");
        log.setLogTime(TimeTools.now());
        log.setPreStatus(status);
        log.setAfterStatus(FinanceConstant.INVOICEINS_STATUS_END);
        log.setOprMode(PublicConstant.OPRMODE_PASS);

        flowLogDAO.saveEntityBean(log);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checkInvoiceinsBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InvoiceinsBean bean = invoiceinsDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != FinanceConstant.INVOICEINS_STATUS_CHECK)
        {
            throw new MYException("数据错误,请确认操作");
        }

        int status = bean.getStatus();

        // 财务审核
        bean.setStatus(FinanceConstant.INVOICEINS_STATUS_SUBMIT);

        invoiceinsDAO.updateEntityBean(bean);

        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());
        log.setActorId(user.getStafferId());
        log.setFullId(id);
        log.setDescription("OK");
        log.setLogTime(TimeTools.now());
        log.setPreStatus(status);
        log.setAfterStatus(bean.getStatus());
        log.setOprMode(PublicConstant.OPRMODE_PASS);

        flowLogDAO.saveEntityBean(log);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectInvoiceinsBean(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InvoiceinsBean bean = invoiceinsDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() == FinanceConstant.INVOICEINS_STATUS_END)
        {
            throw new MYException("数据错误,请确认操作");
        }

        int status = bean.getStatus();

        // 驳回
        bean.setStatus(FinanceConstant.INVOICEINS_STATUS_REJECT);

        invoiceinsDAO.updateEntityBean(bean);

        // 删除对应关系
        insVSOutDAO.deleteEntityBeansByFK(id, AnoConstant.FK_FIRST);

        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());
        log.setActorId(user.getStafferId());
        log.setFullId(id);
        log.setDescription(reason);
        log.setLogTime(TimeTools.now());
        log.setPreStatus(status);
        log.setAfterStatus(FinanceConstant.INVOICEINS_STATUS_REJECT);
        log.setOprMode(PublicConstant.OPRMODE_REJECT);

        flowLogDAO.saveEntityBean(log);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checkInvoiceinsBean2(User user, String id, String checks, String refId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InvoiceinsBean bean = invoiceinsDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setCheckStatus(PublicConstant.CHECK_STATUS_END);
        bean.setChecks(checks + " [" + TimeTools.now() + ']');
        bean.setCheckrefId(refId);

        invoiceinsDAO.updateEntityBean(bean);

        return true;
    }

    public InvoiceinsVO findVO(String id)
    {
        InvoiceinsVO vo = invoiceinsDAO.findVO(id);

        if (vo == null)
        {
            return null;
        }

        vo.setItemList(invoiceinsItemDAO.queryEntityBeansByFK(id));

        vo.setVsList(insVSOutDAO.queryEntityBeansByFK(id, AnoConstant.FK_FIRST));

        return vo;
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
     * @return the invoiceinsDAO
     */
    public InvoiceinsDAO getInvoiceinsDAO()
    {
        return invoiceinsDAO;
    }

    /**
     * @param invoiceinsDAO
     *            the invoiceinsDAO to set
     */
    public void setInvoiceinsDAO(InvoiceinsDAO invoiceinsDAO)
    {
        this.invoiceinsDAO = invoiceinsDAO;
    }

    /**
     * @return the invoiceinsItemDAO
     */
    public InvoiceinsItemDAO getInvoiceinsItemDAO()
    {
        return invoiceinsItemDAO;
    }

    /**
     * @param invoiceinsItemDAO
     *            the invoiceinsItemDAO to set
     */
    public void setInvoiceinsItemDAO(InvoiceinsItemDAO invoiceinsItemDAO)
    {
        this.invoiceinsItemDAO = invoiceinsItemDAO;
    }

    /**
     * @return the insVSOutDAO
     */
    public InsVSOutDAO getInsVSOutDAO()
    {
        return insVSOutDAO;
    }

    /**
     * @param insVSOutDAO
     *            the insVSOutDAO to set
     */
    public void setInsVSOutDAO(InsVSOutDAO insVSOutDAO)
    {
        this.insVSOutDAO = insVSOutDAO;
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
     * @return the baseBalanceDAO
     */
    public BaseBalanceDAO getBaseBalanceDAO()
    {
        return baseBalanceDAO;
    }

    /**
     * @param baseBalanceDAO
     *            the baseBalanceDAO to set
     */
    public void setBaseBalanceDAO(BaseBalanceDAO baseBalanceDAO)
    {
        this.baseBalanceDAO = baseBalanceDAO;
    }

    /**
     * @return the dutyDAO
     */
    public DutyDAO getDutyDAO()
    {
        return dutyDAO;
    }

    /**
     * @param dutyDAO
     *            the dutyDAO to set
     */
    public void setDutyDAO(DutyDAO dutyDAO)
    {
        this.dutyDAO = dutyDAO;
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
