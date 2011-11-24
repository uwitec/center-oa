/**
 * File Name: BudgetItemVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.budget.bean.BudgetItemBean;


/**
 * BudgetItemVO
 * 
 * @author ZHUZHU
 * @version 2008-12-7
 * @see BudgetItemVO
 * @since 1.0
 */
@Entity(inherit = true)
public class BudgetItemVO extends BudgetItemBean
{
    @Relationship(relationField = "feeItemId")
    private String feeItemName = "";

    @Relationship(relationField = "budgetId")
    private String budgetName = "";

    @Ignore
    private String budgetStr = "";

    /**
     * 未分配
     */
    @Ignore
    private String sbudget = "";

    @Ignore
    private String srealMonery = "";

    @Ignore
    private String suseMonery = "";

    /**
     * 剩余预算
     */
    @Ignore
    private String sremainMonery = "";

    @Ignore
    private String schangeMonery = "";

    public BudgetItemVO()
    {
    }

    /**
     * @return the feeItemName
     */
    public String getFeeItemName()
    {
        return feeItemName;
    }

    /**
     * @param feeItemName
     *            the feeItemName to set
     */
    public void setFeeItemName(String feeItemName)
    {
        this.feeItemName = feeItemName;
    }

    /**
     * @return the sbudget
     */
    public String getSbudget()
    {
        return sbudget;
    }

    /**
     * @param sbudget
     *            the sbudget to set
     */
    public void setSbudget(String sbudget)
    {
        this.sbudget = sbudget;
    }

    /**
     * @return the srealMonery
     */
    public String getSrealMonery()
    {
        return srealMonery;
    }

    /**
     * @param srealMonery
     *            the srealMonery to set
     */
    public void setSrealMonery(String srealMonery)
    {
        this.srealMonery = srealMonery;
    }

    public String getSuseMonery()
    {
        return suseMonery;
    }

    public void setSuseMonery(String suseMonery)
    {
        this.suseMonery = suseMonery;
    }

    /**
     * @return the sremainMonery
     */
    public String getSremainMonery()
    {
        return sremainMonery;
    }

    /**
     * @param sremainMonery
     *            the sremainMonery to set
     */
    public void setSremainMonery(String sremainMonery)
    {
        this.sremainMonery = sremainMonery;
    }

    /**
     * @return the schangeMonery
     */
    public String getSchangeMonery()
    {
        return schangeMonery;
    }

    /**
     * @param schangeMonery
     *            the schangeMonery to set
     */
    public void setSchangeMonery(String schangeMonery)
    {
        this.schangeMonery = schangeMonery;
    }

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
     * @return the budgetStr
     */
    public String getBudgetStr()
    {
        return budgetStr;
    }

    /**
     * @param budgetStr
     *            the budgetStr to set
     */
    public void setBudgetStr(String budgetStr)
    {
        this.budgetStr = budgetStr;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("BudgetItemVO ( ")
            .append(super.toString())
            .append(TAB)
            .append("feeItemName = ")
            .append(this.feeItemName)
            .append(TAB)
            .append("budgetName = ")
            .append(this.budgetName)
            .append(TAB)
            .append("budgetStr = ")
            .append(this.budgetStr)
            .append(TAB)
            .append("sbudget = ")
            .append(this.sbudget)
            .append(TAB)
            .append("srealMonery = ")
            .append(this.srealMonery)
            .append(TAB)
            .append("suseMonery = ")
            .append(this.suseMonery)
            .append(TAB)
            .append("sremainMonery = ")
            .append(this.sremainMonery)
            .append(TAB)
            .append("schangeMonery = ")
            .append(this.schangeMonery)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
