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

import com.china.center.jdbc.annotation.Column;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * OutBillBean
 * 
 * @author ZHUZHU
 * @version 2010-12-25
 * @see OutBillBean
 * @since 3.0
 */
@Entity(name = "付款单")
@Table(name = "T_CENTER_OUTBILL")
public class OutBillBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "类型", type = Element.SELECT)
    private int type = FinanceConstant.OUTBILL_TYPE_STOCK;

    @Html(title = "付款方式", type = Element.SELECT)
    private int payType = FinanceConstant.OUTBILL_PAYTYPE_MONEY;

    /**
     * 付款单没有太多意义
     */
    @Html(title = "状态", type = Element.SELECT)
    private int status = FinanceConstant.INBILL_STATUS_PAYMENTS;

    /**
     * 是否被统计锁定
     */
    @Column(name = "ulock")
    @Html(title = "锁定", type = Element.SELECT)
    private int lock = FinanceConstant.BILL_LOCK_NO;

    @Html(title = "帐户", type = Element.SELECT, must = true)
    @Join(tagClass = BankBean.class)
    private String bankId = "";

    @FK
    private String stockId = "";

    @Html(title = "金额", type = Element.DOUBLE, must = true)
    private double moneys = 0.0d;

    @Html(title = "供应商", name = "provideName", readonly = true, must = true)
    @Join(tagClass = ProviderBean.class)
    private String provideId = "";

    /**
     * 单据生成人
     */
    @Join(tagClass = StafferBean.class, type = JoinType.LEFT, alias = "SB1")
    private String stafferId = "";

    @Html(title = "发票类型", type = Element.SELECT)
    @Join(tagClass = InvoiceBean.class, type = JoinType.LEFT)
    private String invoiceId = "";

    /**
     * 属于哪个职员的
     */
    @Html(title = "所属职员", name = "ownerName", readonly = true)
    @Join(tagClass = StafferBean.class, type = JoinType.LEFT, alias = "SB2")
    private String ownerId = "";

    private String locationId = "";

    private String destBankId = "";

    private String refBillId = "";

    private String logTime = "";

    @Html(title = "备注", maxLength = 200, type = Element.TEXTAREA)
    private String description = "";

    /**
     * default constructor
     */
    public OutBillBean()
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
     * @return the lock
     */
    public int getLock()
    {
        return lock;
    }

    /**
     * @param lock
     *            the lock to set
     */
    public void setLock(int lock)
    {
        this.lock = lock;
    }

    /**
     * @return the provideId
     */
    public String getProvideId()
    {
        return provideId;
    }

    /**
     * @param provideId
     *            the provideId to set
     */
    public void setProvideId(String provideId)
    {
        this.provideId = provideId;
    }

    /**
     * @return the payType
     */
    public int getPayType()
    {
        return payType;
    }

    /**
     * @param payType
     *            the payType to set
     */
    public void setPayType(int payType)
    {
        this.payType = payType;
    }

    /**
     * @return the stockId
     */
    public String getStockId()
    {
        return stockId;
    }

    /**
     * @param stockId
     *            the stockId to set
     */
    public void setStockId(String stockId)
    {
        this.stockId = stockId;
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
            .append("OutBillBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("payType = ")
            .append(this.payType)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("lock = ")
            .append(this.lock)
            .append(TAB)
            .append("bankId = ")
            .append(this.bankId)
            .append(TAB)
            .append("stockId = ")
            .append(this.stockId)
            .append(TAB)
            .append("moneys = ")
            .append(this.moneys)
            .append(TAB)
            .append("provideId = ")
            .append(this.provideId)
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
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

    /**
     * @return the invoiceId
     */
    public String getInvoiceId()
    {
        return invoiceId;
    }

    /**
     * @param invoiceId
     *            the invoiceId to set
     */
    public void setInvoiceId(String invoiceId)
    {
        this.invoiceId = invoiceId;
    }

}
