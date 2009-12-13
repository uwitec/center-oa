/**
 * File Name: BillFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-24<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.interfaces.impl;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.MYException;
import com.china.center.oa.budget.manager.BudgetManager;
import com.china.center.oa.interfaces.BillFacade;
import com.china.centet.yongyin.bean.Bill;


/**
 * BillFacadeImpl
 * 
 * @author zhuzhu
 * @version 2009-6-24
 * @see BillFacadeImpl
 * @since 1.0
 */
@Bean(name = "billFacade")
public class BillFacadeImpl implements BillFacade
{
    private BudgetManager budgetManager = null;

    /**
     * default constructor
     */
    public BillFacadeImpl()
    {}

    /**
     * addBill
     */
    public boolean addBill(String stafferId, Bill bill, String budgetItemId, String budgetId)
        throws MYException
    {
        return budgetManager.addBill(stafferId, bill, budgetItemId, budgetId);
    }

    /**
     * @return the budgetManager
     */
    public BudgetManager getBudgetManager()
    {
        return budgetManager;
    }

    /**
     * @param budgetManager
     *            the budgetManager to set
     */
    public void setBudgetManager(BudgetManager budgetManager)
    {
        this.budgetManager = budgetManager;
    }

}
