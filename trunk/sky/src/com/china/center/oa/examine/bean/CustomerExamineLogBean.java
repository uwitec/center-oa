/**
 * File Name: NewCustomerExamineHisBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * NewCustomerExamineHisBean
 * 
 * @author zhuzhu
 * @version 2009-1-14
 * @see CustomerExamineLogBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_LOG_EXNEW")
public class CustomerExamineLogBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    private String itemId = "";
    
    private String planId = "";

    private String outId = "";

    private String customerId = "";

    private String customerName = "";
    
    private String logTime = "";
    
    private int amount = 0;

    public CustomerExamineLogBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the planId
     */
    public String getPlanId()
    {
        return planId;
    }

    /**
     * @param planId the planId to set
     */
    public void setPlanId(String planId)
    {
        this.planId = planId;
    }

    /**
     * @return the outId
     */
    public String getOutId()
    {
        return outId;
    }

    /**
     * @param outId the outId to set
     */
    public void setOutId(String outId)
    {
        this.outId = outId;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    /**
     * @return the itemId
     */
    public String getItemId()
    {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }
}
