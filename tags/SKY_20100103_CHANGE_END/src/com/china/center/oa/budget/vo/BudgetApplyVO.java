/**
 * File Name: BudgetApplyVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.vo;


import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.center.oa.budget.bean.BudgetApplyBean;


/**
 * BudgetApplyVO
 * 
 * @author zhuzhu
 * @version 2009-6-14
 * @see BudgetApplyVO
 * @since 1.0
 */
@Entity(inherit = true)
public class BudgetApplyVO extends BudgetApplyBean
{
    @Relationship(relationField = "budgetId")
    private String budgetName = "";

    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Ignore
    private List<BudgetItemVO> itemVOs = null;

    /**
     * default constructor
     */
    public BudgetApplyVO()
    {}

    /**
     * @return the budgetName
     */
    public String getBudgetName()
    {
        return budgetName;
    }

    /**
     * @param budgetName
     *            the budgetName to set
     */
    public void setBudgetName(String budgetName)
    {
        this.budgetName = budgetName;
    }

    /**
     * @return the itemVOs
     */
    public List<BudgetItemVO> getItemVOs()
    {
        return itemVOs;
    }

    /**
     * @param itemVOs
     *            the itemVOs to set
     */
    public void setItemVOs(List<BudgetItemVO> itemVOs)
    {
        this.itemVOs = itemVOs;
    }

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }
}
