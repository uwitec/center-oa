/**
 * File Name: BudgetLogVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.budget.bean.BudgetLogBean;


/**
 * BudgetLogVO
 * 
 * @author ZHUZHU
 * @version 2009-6-26
 * @see BudgetLogVO
 * @since 1.0
 */
@Entity(inherit = true)
public class BudgetLogVO extends BudgetLogBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "locationId", tagField = "name")
    private String locationName = "";

    @Relationship(relationField = "feeItemId")
    private String feeItemName = "";

    @Relationship(relationField = "budgetId")
    private String budgetName = "";

    @Ignore
    private String sbeforemonery = "";

    @Ignore
    private String saftermonery = "";

    @Ignore
    private String smonery = "";

    /**
     * default constructor
     */
    public BudgetLogVO()
    {
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

    /**
     * @return the locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param locationName
     *            the locationName to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
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
     * @return the sbeforemonery
     */
    public String getSbeforemonery()
    {
        return sbeforemonery;
    }

    /**
     * @param sbeforemonery
     *            the sbeforemonery to set
     */
    public void setSbeforemonery(String sbeforemonery)
    {
        this.sbeforemonery = sbeforemonery;
    }

    /**
     * @return the saftermonery
     */
    public String getSaftermonery()
    {
        return saftermonery;
    }

    /**
     * @param saftermonery
     *            the saftermonery to set
     */
    public void setSaftermonery(String saftermonery)
    {
        this.saftermonery = saftermonery;
    }

    /**
     * @return the smonery
     */
    public String getSmonery()
    {
        return smonery;
    }

    /**
     * @param smonery
     *            the smonery to set
     */
    public void setSmonery(String smonery)
    {
        this.smonery = smonery;
    }
}
