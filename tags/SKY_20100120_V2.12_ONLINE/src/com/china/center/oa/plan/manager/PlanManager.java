/**
 * File Name: PlanManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.plan.manager;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.china.center.common.ConditionParse;
import com.china.center.oa.constant.PlanConstant;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.dao.PlanDAO;
import com.china.center.tools.TimeTools;


/**
 * PlanManager
 * 
 * @author zhuzhu
 * @version 2009-1-11
 * @see PlanManager
 * @since 1.0
 */
@Bean(name = "planManager")
public class PlanManager implements ApplicationContextAware
{
    private final Log _logger = LogFactory.getLog(getClass());

    /**
     * 触发器的日志
     */
    private final Log planLog = LogFactory.getLog("plan");

    private PlanDAO planDAO = null;

    private static ApplicationContext content = null;

    private DataSourceTransactionManager transactionManager = null;

    /**
     * 通过静态方法注入需要的plan
     */
    private static List<CarryPlan> carryPlanList = new ArrayList<CarryPlan>();

    private static List<String> carryBeanList = new ArrayList<String>();

    public PlanManager()
    {}

    /**
     * 计划的执行(每天03点30执行，一天执行一次)
     */
    public void carryPlan()
    {
        planLog.info("begin carryPlan...");

        ConditionParse condotion = new ConditionParse();

        setOnceCondition(condotion);

        List<PlanBean> plans = planDAO.queryEntityBeansByCondition(condotion);

        // 处理结束的计划
        processPlans(plans, true);

        everydayPlan(condotion);

        // 处理每天的计划
        plans = planDAO.queryEntityBeansByCondition(condotion);

        processPlans(plans, false);

        planLog.info("end carryPlan");
    }

    /**
     * @param condotion
     */
    private void setOnceCondition(ConditionParse condotion)
    {
        condotion.addIntCondition("status", "=", PlanConstant.PLAN_STATUS_INIT);

        // FIX bug 这里不能carryType=PLAN_CARRYTYPES_ONCE，因为是结束的，主要时间在carryTime之后就要结束
        // condotion.addIntCondition("carryType", "=", PlanConstant.PLAN_CARRYTYPES_ONCE);

        condotion.addCondition("carryTime", "<=", TimeTools.now());

        // 获得执行顺序
        condotion.addCondition("order by orderIndex");
    }

    /**
     * @param condotion
     */
    private void everydayPlan(ConditionParse condotion)
    {
        condotion.clear();

        condotion.addIntCondition("carryType", "=", PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        condotion.addIntCondition("status", "=", PlanConstant.PLAN_STATUS_INIT);

        condotion.addCondition("beginTime", "<=", TimeTools.now());

        condotion.addCondition("carryTime", ">", TimeTools.now());

        condotion.addCondition("order by orderIndex");
    }

    /**
     * @param plans
     *            计划list
     * @param end
     *            是否是最后一次执行
     */
    private void processPlans(List<PlanBean> plans, boolean end)
    {
        for (PlanBean planBean : plans)
        {
            for (CarryPlan item : carryPlanList)
            {
                if (planBean.getType() == item.getPlanType())
                {
                    // 全面保护
                    try
                    {
                        // 如果计划执行成功保存计划且是最后一次执行，更改计划状态
                        if (item.carryPlan(planBean, end) && end)
                        {
                            savePlan(planBean);
                        }

                        planLog.info("processPlan success:" + planBean.getFkId() + ";end is "
                                     + end + ";beginTime:" + planBean.getBeginTime()
                                     + ";carryTime:" + planBean.getCarryTime() + ";" + item);
                    }
                    catch (Throwable e)
                    {
                        planLog.error("processPlan error:" + planBean.getFkId() + ";end is " + end
                                      + ";beginTime:" + planBean.getBeginTime() + ";carryTime:"
                                      + planBean.getCarryTime() + ";" + item);

                        _logger.error(e, e);
                    }

                    break;
                }
            }
        }
    }

    /**
     * 事务内保存计划
     * 
     * @param planBean
     */
    private void savePlan(final PlanBean planBean)
    {
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        planBean.setStatus(PlanConstant.PLAN_STATUS_END);

        planBean.setRealCarryTime(TimeTools.now());

        tran.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus arg0)
            {
                planDAO.updateEntityBean(planBean);

                return Boolean.TRUE;
            }
        });
    }

    /**
     * @return the planDAO
     */
    public PlanDAO getPlanDAO()
    {
        return planDAO;
    }

    /**
     * @param planDAO
     *            the planDAO to set
     */
    public void setPlanDAO(PlanDAO planDAO)
    {
        this.planDAO = planDAO;
    }

    /**
     * 系统建立后演示触发
     */
    public void afterSystemSetup()
    {
        for (String item : carryBeanList)
        {
            addCarryPlanInner(item);
        }
    }

    /**
     * 增加执行策略
     * 
     * @param plan
     */
    public static void addCarryPlan(CarryPlan plan)
    {
        // 保护
        for (CarryPlan item : carryPlanList)
        {
            if (item.getPlanType() == plan.getPlanType())
            {
                return;
            }
        }

        carryPlanList.add(plan);
    }

    public static void addCarryPlan(String plan)
    {
        carryBeanList.add(plan);
    }

    private static void addCarryPlanInner(String plan)
    {
        Object obj = content.getBean(plan);

        if (obj == null)
        {
            return;
        }

        if ( ! (obj instanceof CarryPlan))
        {
            return;
        }

        CarryPlan carryPlan = (CarryPlan)obj;

        // 保护
        for (CarryPlan item : carryPlanList)
        {
            if (item.getPlanType() == carryPlan.getPlanType())
            {
                return;
            }
        }

        carryPlanList.add(carryPlan);
    }

    /**
     * @return the transactionManager
     */
    public DataSourceTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    public void setApplicationContext(ApplicationContext con)
        throws BeansException
    {
        content = con;
    }
}
