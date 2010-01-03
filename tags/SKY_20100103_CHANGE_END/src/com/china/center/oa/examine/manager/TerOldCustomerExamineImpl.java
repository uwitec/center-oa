/**
 * File Name: NewCustomerExamineImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.examine.bean.CustomerExamineLogBean;
import com.china.center.oa.examine.bean.ExamineBean;
import com.china.center.oa.examine.bean.OldCustomerExamineBean;
import com.china.center.oa.examine.dao.CustomerExamineLogDAO;
import com.china.center.oa.examine.dao.ExamineDAO;
import com.china.center.oa.examine.dao.OldCustomerExamineDAO;
import com.china.center.oa.examine.wrap.CustomerWrap;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.manager.CarryPlan;
import com.china.center.oa.plan.manager.PlanManager;
import com.china.center.oa.worklog.dao.VisitDAO;
import com.china.center.tools.StringTools;


/**
 * 新客户的考核实现
 * 
 * @author zhuzhu
 * @version 2009-1-11
 * @see TerOldCustomerExamineImpl
 * @since 1.0
 */
@Exceptional
@Bean(name = "terOldCustomerExamineImpl", initMethod = "init")
public class TerOldCustomerExamineImpl implements CarryPlan
{
    private OldCustomerExamineDAO oldCustomerExamineDAO = null;

    private ExamineDAO examineDAO = null;

    private CustomerDAO customerDAO = null;

    private VisitDAO visitDAO = null;

    private CustomerExamineLogDAO customerExamineLogDAO = null;

    /**
     * default constructor
     */
    public TerOldCustomerExamineImpl()
    {}

    /**
     * 初始化注入
     */
    public void init()
    {
        PlanManager.addCarryPlan("terOldCustomerExamineImpl");
    }

    /**
     * 实施计划
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean carryPlan(PlanBean plan, boolean end)
    {
        String itemId = plan.getFkId();

        if (StringTools.isNullOrNone(itemId))
        {
            return true;
        }

        OldCustomerExamineBean item = oldCustomerExamineDAO.find(itemId);

        if (item == null)
        {
            return true;
        }

        // 状态如果已经结束,则不执行
        if (item.getStatus() == ExamineConstant.EXAMINE_ITEM_STATUS_END)
        {
            return true;
        }

        ExamineBean examine = examineDAO.find(item.getParentId());

        if (examine == null)
        {
            return true;
        }

        List<CustomerWrap> clist = visitDAO.queryCustomerInTer(CustomerConstant.NEWTYPE_OLD,
            examine.getStafferId(), item.getBeginTime(), item.getEndTime());

        // 删除临时log
        customerExamineLogDAO.deleteEntityBeansByFK(itemId);

        for (CustomerWrap customerBean : clist)
        {
            saveLog(plan, item, customerBean);
        }

        item.setRealValue(clist.size());

        if (end)
        {
            processResult(item);
        }
        else
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_INIT);
        }

        // 修改
        oldCustomerExamineDAO.updateEntityBean(item);

        return true;
    }

    /**
     * @param item
     */
    private void processResult(OldCustomerExamineBean item)
    {
        // 超过预期
        if (item.getRealValue() > item.getPlanValue())
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_GOOD);
        }

        if (item.getRealValue() == item.getPlanValue())
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_OK);
        }

        if (item.getRealValue() < item.getPlanValue())
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_LESS);
        }

        item.setStatus(ExamineConstant.EXAMINE_ITEM_STATUS_END);
    }

    /**
     * 记入日志，方便考核查询
     * 
     * @param plan
     * @param item
     * @param customerBean
     */
    private void saveLog(PlanBean plan, OldCustomerExamineBean item, CustomerWrap customerBean)
    {
        CustomerExamineLogBean log = new CustomerExamineLogBean();

        log.setPlanId(plan.getId());

        log.setItemId(item.getId());

        log.setCustomerId(customerBean.getId());

        log.setCustomerName(customerBean.getName());

        log.setOutId(customerBean.getOutId());

        customerExamineLogDAO.saveEntityBean(log);
    }

    /**
     * 老客户考核的标志
     */
    public int getPlanType()
    {
        return ExamineConstant.EXAMINE_ITEM_TYPE_TER_OLDCUSTOMER;
    }

    /**
     * @return the oldCustomerExamineDAO
     */
    public OldCustomerExamineDAO getOldCustomerExamineDAO()
    {
        return oldCustomerExamineDAO;
    }

    /**
     * @param oldCustomerExamineDAO
     *            the oldCustomerExamineDAO to set
     */
    public void setOldCustomerExamineDAO(OldCustomerExamineDAO oldCustomerExamineDAO)
    {
        this.oldCustomerExamineDAO = oldCustomerExamineDAO;
    }

    /**
     * @return the examineDAO
     */
    public ExamineDAO getExamineDAO()
    {
        return examineDAO;
    }

    /**
     * @param examineDAO
     *            the examineDAO to set
     */
    public void setExamineDAO(ExamineDAO examineDAO)
    {
        this.examineDAO = examineDAO;
    }

    /**
     * @return the customerExamineLogDAO
     */
    public CustomerExamineLogDAO getCustomerExamineLogDAO()
    {
        return customerExamineLogDAO;
    }

    /**
     * @param customerExamineLogDAO
     *            the customerExamineLogDAO to set
     */
    public void setCustomerExamineLogDAO(CustomerExamineLogDAO customerExamineLogDAO)
    {
        this.customerExamineLogDAO = customerExamineLogDAO;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
    }

    /**
     * @return the visitDAO
     */
    public VisitDAO getVisitDAO()
    {
        return visitDAO;
    }

    /**
     * @param visitDAO
     *            the visitDAO to set
     */
    public void setVisitDAO(VisitDAO visitDAO)
    {
        this.visitDAO = visitDAO;
    }
}
