/**
 * File Name: BillBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * BillBean
 * 
 * @author ZHUZHU
 * @version 2010-12-25
 * @see InBillBean
 * @since 3.0
 */
@Entity(name = "收款单")
@Table(name = "T_CENTER_INBILL")
public class InBillBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "类型", type = Element.SELECT)
    private int type = FinanceConstant.INBILL_TYPE_SAILOUT;

    /**
     * 已经收取 预收(关联的销售单还没有正式生效) 未关联(还没有和销售单关联)(这个只有在销售收入下才有意义哦)
     */
    @Html(title = "状态", type = Element.SELECT)
    private int status = FinanceConstant.INBILL_STATUS_PAYMENTS;

    @Join(tagClass = BankBean.class)
    private String bankId = "";

    @FK
    private String outId = "";

    private double moneys = 0.0d;

    @Join(tagClass = CustomerBean.class)
    private String customerId = "";

    /**
     * 单据生成人
     */
    @Join(tagClass = StafferBean.class, type = JoinType.LEFT, alias = "SB1")
    private String stafferId = "";

    /**
     * 属于哪个职员的
     */
    @Join(tagClass = StafferBean.class, type = JoinType.LEFT, alias = "SB2")
    private String ownerId = "";

    private String locationId = "";

    private String destBankId = "";

    private String refBillId = "";

    private String paymentId = "";

    private String logTime = "";

    @Html(title = "备注", maxLength = 200, type = Element.TEXTAREA)
    private String description = "";

    /**
     * default constructor
     */
    public InBillBean()
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
     * @return the bankId
     */
    public String getBankId()
    {
        return bankId;
    }

    /**
     * @param bankId
     *            the bankId to set
     */
    public void setBankId(String bankId)
    {
        this.bankId = bankId;
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
     * @return the destBankId
     */
    public String getDestBankId()
    {
        return destBankId;
    }

    /**
     * @param destBankId
     *            the destBankId to set
     */
    public void setDestBankId(String destBankId)
    {
        this.destBankId = destBankId;
    }

    /**
     * @return the refBillId
     */
    public String getRefBillId()
    {
        return refBillId;
    }

    /**
     * @param refBillId
     *            the refBillId to set
     */
    public void setRefBillId(String refBillId)
    {
        this.refBillId = refBillId;
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
     * @return the ownerId
     */
    public String getOwnerId()
    {
        return ownerId;
    }

    /**
     * @param ownerId
     *            the ownerId to set
     */
    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("InBillBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("bankId = ")
            .append(this.bankId)
            .append(TAB)
            .append("outId = ")
            .append(this.outId)
            .append(TAB)
            .append("moneys = ")
            .append(this.moneys)
            .append(TAB)
            .append("customerId = ")
            .append(this.customerId)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("ownerId = ")
            .append(this.ownerId)
            .append(TAB)
            .append("locationId = ")
            .append(this.locationId)
            .append(TAB)
            .append("destBankId = ")
            .append(this.destBankId)
            .append(TAB)
            .append("refBillId = ")
            .append(this.refBillId)
            .append(TAB)
            .append("paymentId = ")
            .append(this.paymentId)
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
