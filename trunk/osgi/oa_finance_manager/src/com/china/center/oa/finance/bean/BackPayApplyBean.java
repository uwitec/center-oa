/**
 * File Name: BackPayApplyBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-3-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.finance.constant.BackPayApplyConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * BackPayApplyBean
 * 
 * @author ZHUZHU
 * @version 2011-3-3
 * @see BackPayApplyBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_BACKPAYAPPLY")
public class BackPayApplyBean implements Serializable
{
    @Id
    private String id = "";

    private String outId = "";

    private int status = BackPayApplyConstant.STATUS_INIT;

    private double backPay = 0.0d;

    private double changePayment = 0.0d;

    @Join(tagClass = StafferBean.class, type = JoinType.LEFT)
    private String stafferId = "";

    @Join(tagClass = CustomerBean.class, type = JoinType.LEFT)
    private String customerId = "";

    private String logTime = "";

    private String description = "";

    /**
     * default constructor
     */
    public BackPayApplyBean()
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
     * @return the outId
     */
    public String getOutId()
    {
        return outId;
    }

    /**
     * @param outId
     *            the outId to set
     */
    public void setOutId(String outId)
    {
        this.outId = outId;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /**
     * @return the backPay
     */
    public double getBackPay()
    {
        return backPay;
    }

    /**
     * @param backPay
     *            the backPay to set
     */
    public void setBackPay(double backPay)
    {
        this.backPay = backPay;
    }

    /**
     * @return the changePayment
     */
    public double getChangePayment()
    {
        return changePayment;
    }

    /**
     * @param changePayment
     *            the changePayment to set
     */
    public void setChangePayment(double changePayment)
    {
        this.changePayment = changePayment;
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
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
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
            .append("BackPayApplyBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("outId = ")
            .append(this.outId)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("backPay = ")
            .append(this.backPay)
            .append(TAB)
            .append("changePayment = ")
            .append(this.changePayment)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("customerId = ")
            .append(this.customerId)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
