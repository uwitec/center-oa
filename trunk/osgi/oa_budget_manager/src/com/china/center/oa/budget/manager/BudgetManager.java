/**
 * File Name: BudgetManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-5-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.vo.BudgetVO;
import com.china.center.oa.finance.bean.OutBillBean;


/**
 * BudgetManager
 * 
 * @author ZHUZHU
 * @version 2011-5-14
 * @see BudgetManager
 * @since 3.0
 */
public interface BudgetManager
{
    boolean addBean(User user, BudgetBean bean)
        throws MYException;

    boolean addBill(String stafferId, OutBillBean bill, String budgetItemId, String budgetId)
        throws MYException;

    boolean updateBean(User user, BudgetBean bean)
        throws MYException;

    boolean updateItemBean(User user, BudgetItemBean bean, String reason)
        throws MYException;

    boolean delItemBean(User user, String id)
        throws MYException;

    boolean delBean(User user, String id)
        throws MYException;

    boolean passBean(User user, String id)
        throws MYException;

    boolean rejectBean(User user, String id, String reson)
        throws MYException;

    BudgetVO findBudgetVO(String id)
        throws MYException;

    BudgetBean findBudget(String id)
        throws MYException;
}
