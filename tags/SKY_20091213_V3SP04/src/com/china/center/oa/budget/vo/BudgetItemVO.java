/**
 * File Name: BudgetItemVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.center.oa.budget.bean.BudgetItemBean;


/**
 * BudgetItemVO
 * 
 * @author zhuzhu
 * @version 2008-12-7
 * @see BudgetItemVO
 * @since 1.0
 */
@Entity(inherit = true)
public class BudgetItemVO extends BudgetItemBean
{
    @Relationship(relationField = "feeItemId")
    private String feeItemName = "";

    @Ignore
    private String sbudget = "";

    @Ignore
    private String srealMonery = "";

    @Ignore
    private String suseMonery = "";

    @Ignore
    private String sremainMonery = "";

    public BudgetItemVO()
    {}

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
}
