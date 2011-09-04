/**
 * File Name: BudgetApplyManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-5-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.manager;


import com.center.china.osgi.publics.ListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.budget.bean.BudgetApplyBean;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.listener.BudgetListener;


/**
 * BudgetApplyManager
 * 
 * @author ZHUZHU
 * @version 2011-5-14
 * @see BudgetApplyManager
 * @since 3.0
 */
public interface BudgetApplyManager extends ListenerManager<BudgetListener>
{
    boolean addBean(User user, BudgetApplyBean bean)
        throws MYException;

    boolean passBean(User user, String id)
        throws MYException;

    boolean rejectBean(User user, String id, String reson)
        throws MYException;

    boolean whetherApply(String budgetId)
        throws MYException;

    /**
     * 合计所有的递归使用的子项
     * 
     * @param budgetItemBean
     * @return
     */
    double sumPreAndUseInEachBudgetItem(BudgetItemBean budgetItemBean);

    /**
     * 统计预算已经使用金额
     * 
     * @param budget
     * @return
     */
    double sumPreAndUseInEachBudget(BudgetBean budget);
}
