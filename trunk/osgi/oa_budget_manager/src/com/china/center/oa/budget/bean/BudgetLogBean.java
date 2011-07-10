/**
 * File Name: BudgetLogBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * BudgetLogBean
 * 
 * @author ZHUZHU
 * @version 2009-6-26
 * @see BudgetLogBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_BUDGETLOG")
public class BudgetLogBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    /**
     * 使用人
     */
    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    /**
     * 预算
     */
    @Join(tagClass = BudgetBean.class)
    private String budgetId = "";

    /**
     * 关联bill
     */
    private String billId = "";

    /**
     * 关联ID
     */
    private String refId = "";

    private double beforemonery = 0.0d;

    private double aftermonery = 0.0d;

    private double monery = 0.0d;

    @Join(tagClass = LocationBean.class)
    private String locationId = "";

    private String logTime = "";

    @FK
    private String budgetItemId = "";

    @Join(tagClass = FeeItemBean.class)
    private String feeItemId = "";

    private String log = "";

    /**
     * default constructor
     */
    public BudgetLogBean()
    {
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the stafferId
     */
    public String getStafferId()
    {
        return stafferId;
    }

    /**
     * @param stafferId
     *            the stafferId to set
     */
    public void setStafferId(String stafferId)
    {
        this.stafferId = stafferId;
    }

    /**
     * @return the budgetId
     */
    public String getBudgetId()
    {
        return budgetId;
    }

    /**
     * @param budgetId
     *            the budgetId to set
     */
    public void setBudgetId(String budgetId)
    {
        this.budgetId = budgetId;
    }

    /**
     * @return the billId
     */
    public String getBillId()
    {
        return billId;
    }

    /**
     * @param billId
     *            the billId to set
     */
    public void setBillId(String billId)
    {
        this.billId = billId;
    }

    /**
     * @return the beforemonery
     */
    public double getBeforemonery()
    {
        return beforemonery;
    }

    /**
     * @param beforemonery
     *            the beforemonery to set
     */
    public void setBeforemonery(double beforemonery)
    {
        this.beforemonery = beforemonery;
    }

    /**
     * @return the aftermonery
     */
    public double getAftermonery()
    {
        return aftermonery;
    }

    /**
     * @param aftermonery
     *            the aftermonery to set
     */
    public void setAftermonery(double aftermonery)
    {
        this.aftermonery = aftermonery;
    }

    /**
     * @return the monery
     */
    public double getMonery()
    {
        return monery;
    }

    /**
     * @param monery
     *            the monery to set
     */
    public void setMonery(double monery)
    {
        this.monery = monery;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime
     *            the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    /**
     * @return the log
     */
    public String getLog()
    {
        return log;
    }

    /**
     * @param log
     *            the log to set
     */
    public void setLog(String log)
    {
        this.log = log;
    }

    /**
     * @return the budgetItemId
     */
    public String getBudgetItemId()
    {
        return budgetItemId;
    }

    /**
     * @param budgetItemId
     *            the budgetItemId to set
     */
    public void setBudgetItemId(String budgetItemId)
    {
        this.budgetItemId = budgetItemId;
    }

    /**
     * @return the feeItemId
     */
    public String getFeeItemId()
    {
        return feeItemId;
    }

    /**
     * @param feeItemId
     *            the feeItemId to set
     */
    public void setFeeItemId(String feeItemId)
    {
        this.feeItemId = feeItemId;
    }

    /**
     * @return the refId
     */
    public String getRefId()
    {
        return refId;
    }

    /**
     * @param refId
     *            the refId to set
     */
    public void setRefId(String refId)
    {
        this.refId = refId;
    }
}
