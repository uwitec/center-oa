/**
 * File Name: CarryPlan.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.plan.manager;


import com.china.center.oa.plan.bean.PlanBean;


/**
 * 执行计划
 * 
 * @author zhuzhu
 * @version 2009-1-11
 * @see CarryPlan
 * @since 1.0
 */
public interface CarryPlan
{
    /**
     * 执行计划
     * 
     * @param plan
     *            计划
     * @param end
     *            是否是最后一次执行
     * @return
     */
    boolean carryPlan(PlanBean plan, boolean end);

    /**
     * 获得计划类型
     * 
     * @return
     */
    int getPlanType();
}
