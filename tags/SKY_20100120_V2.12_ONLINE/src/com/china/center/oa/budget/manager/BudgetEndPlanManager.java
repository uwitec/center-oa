/**
 * File Name: BudgetEndPlanManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-31<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.dao.BudgetDAO;
import com.china.center.oa.budget.dao.BudgetItemDAO;
import com.china.center.oa.constant.BudgetConstant;
import com.china.center.oa.constant.ModuleConstant;
import com.china.center.oa.constant.OperationConstant;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.manager.CarryPlan;
import com.china.center.oa.plan.manager.PlanManager;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.tools.ListTools;
import com.china.center.tools.TimeTools;


/**
 * BudgetEndPlanManager
 * 
 * @author zhuzhu
 * @version 2009-5-31
 * @see BudgetEndPlanManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "budgetEndPlanManager", initMethod = "init")
public class BudgetEndPlanManager implements CarryPlan
{
    private final Log _logger = LogFactory.getLog(getClass());

    private BudgetItemDAO budgetItemDAO = null;

    private BudgetDAO budgetDAO = null;

    private LogDAO logDAO = null;

    /**
     * default constructor
     */
    public BudgetEndPlanManager()
    {}

    /**
     * 初始化注入
     */
    public void init()
    {
        PlanManager.addCarryPlan("budgetEndPlanManager");
    }

    /**
     * end 是否最后一次执行
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean carryPlan(PlanBean plan, boolean end)
    {
        String budgetId = plan.getFkId();

        return corePorcess(end, budgetId);
    }

    /**
     * 单独执行计划(有事务的)
     * 
     * @param budgetId
     * @return
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean carry(String budgetId)
    {
        return corePorcess(false, budgetId);
    }

    /**
     * @param end
     * @param budgetId
     * @return
     */
    private boolean corePorcess(boolean end, String budgetId)
    {
        BudgetBean bean = budgetDAO.find(budgetId);

        if (bean == null)
        {
            _logger.error("预算不存在:" + budgetId);
            return true;
        }

        List<BudgetBean> subItem = budgetDAO.queryEntityBeansByFK(bean.getId());

        // process subItem
        if ( !ListTools.isEmptyOrNull(subItem))
        {
            List<BudgetItemBean> items = budgetItemDAO.queryEntityBeansByFK(bean.getId());

            // process total in sub
            for (BudgetItemBean budgetItemBean : items)
            {
                double itemSubTotal = budgetItemDAO.sumRealTotalInSubBudget(bean.getId(),
                    budgetItemBean.getFeeItemId());

                budgetItemBean.setRealMonery(itemSubTotal);

                // update budgetItem
                budgetItemDAO.updateEntityBean(budgetItemBean);
            }
        }

        double total = budgetItemDAO.sumRealTotal(budgetId);

        // get current real monery
        bean.setRealMonery(total);

        if (end)
        {
            bean.setStatus(BudgetConstant.BUDGET_STATUS_END);

            // FIX BUG update UseMoney
            budgetItemDAO.updateUseMoneyEqualsRealMoney(budgetId);

            log(Helper.getSystemUser(), budgetId, OperationConstant.OPERATION_END,
                "到达预算截至时间,系统自动结束此预算");
        }

        // update BudgetBean
        budgetDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * log in db
     * 
     * @param user
     * @param id
     */
    private void log(User user, String id, String operation, String reson)
    {
        // 记录审批日志
        LogBean log = new LogBean();

        log.setFkId(id);

        log.setLocationId(user.getLocationId());
        log.setStafferId(user.getStafferId());
        log.setLogTime(TimeTools.now());
        log.setModule(ModuleConstant.MODULE_BUDGET);
        log.setOperation(operation);
        log.setLog(reson);

        logDAO.saveEntityBean(log);
    }

    public int getPlanType()
    {
        return BudgetConstant.BUGFET_PLAN_TYPE;
    }

    public BudgetItemDAO getBudgetItemDAO()
    {
        return budgetItemDAO;
    }

    public void setBudgetItemDAO(BudgetItemDAO budgetItemDAO)
    {
        this.budgetItemDAO = budgetItemDAO;
    }

    public BudgetDAO getBudgetDAO()
    {
        return budgetDAO;
    }

    public void setBudgetDAO(BudgetDAO budgetDAO)
    {
        this.budgetDAO = budgetDAO;
    }

    public LogDAO getLogDAO()
    {
        return logDAO;
    }

    public void setLogDAO(LogDAO logDAO)
    {
        this.logDAO = logDAO;
    }
}
