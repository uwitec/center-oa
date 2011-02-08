/**
 * File Name: PaymentVSOutBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.vs.PaymentVSOutBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * 销售单回款申请
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see PaymentApplyBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_PAYAPPLY")
public class PaymentApplyBean implements Serializable
{
    @Id
    private String id = "";

    /**
     * 不是必填哦
     */
    @FK
    private String paymentId = "";

    @Join(tagClass = CustomerBean.class)
    private String customerId = "";

    /**
     * 总金额
     */
    private double moneys = 0.0d;

    /**
     * 申请类型
     */
    private int type = FinanceConstant.PAYAPPLY_TYPE_PAYMENT;

    private int status = FinanceConstant.PAYAPPLY_STATUS_INIT;

    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    /**
     * 申请是分公司内处理的哦
     */
    private String locationId = "";

    private String logTime = "";

    private String approve = "";

    private String description = "";

    @Ignore
    private List<PaymentVSOutBean> vsList = null;

    /**
     * default constructor
     */
    public PaymentApplyBean()
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
     * @return the paymentId
     */
    public String getPaymentId()
    {
        return paymentId;
    }

    /**
     * @param paymentId
     *            the paymentId to set
     */
    public void setPaymentId(String paymentId)
    {
        this.paymentId = paymentId;
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
     * @return the moneys
     */
    public double getMoneys()
    {
        return moneys;
    }

    /**
     * @param moneys
     *            the moneys to set
     */
    public void setMoneys(double moneys)
    {
        this.moneys = moneys;
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
     * @return the approve
     */
    public String getApprove()
    {
        return approve;
    }

    /**
     * @param approve
     *            the approve to set
     */
    public void setApprove(String approve)
    {
        this.approve = approve;
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
     * @return the vsList
     */
    public List<PaymentVSOutBean> getVsList()
    {
        return vsList;
    }

    /**
     * @param vsList
     *            the vsList to set
     */
    public void setVsList(List<PaymentVSOutBean> vsList)
    {
        this.vsList = vsList;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
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
            .append("PaymentApplyBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("paymentId = ")
            .append(this.paymentId)
            .append(TAB)
            .append("customerId = ")
            .append(this.customerId)
            .append(TAB)
            .append("moneys = ")
            .append(this.moneys)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("locationId = ")
            .append(this.locationId)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("approve = ")
            .append(this.approve)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append("vsList = ")
            .append(this.vsList)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}