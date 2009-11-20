/*
 * File Name: Bill.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-4-11
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;
import java.util.Date;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Table;


/**
 * 收付款
 * 
 * @author zhuzhu
 * @version 2007-4-11
 * @see
 * @since
 */
@Entity(name = "收付款")
@Table(name = "T_CENTER_BILL")
public class Bill implements Serializable
{
    @Id
    private String id = "";

    private Date dates = new Date();

    /**
     * 0:收款 1：付款
     */
    private int type = 0;

    /**
     * 0:无 1：客户 2：供应商
     */
    private int mtype = 0;

    private int inway = 0;

    private String bank = "";

    private String billType = "";

    /**
     * 收据(已经不使用)
     */
    @Ignore
    private String receipt = "";

    private String outId = "";

    private double moneys = 0.0d;

    private String customerName = "";

    private String customerId = "";

    private String stafferName = "";

    private String description = "";

    @Ignore
    private String temp = "";

    private String mark = "";

    private String locationId = "";

    private String destLocationId = "";

    private String destBank = "";

    private String refBillId = "";

    /**
     * default constructor
     */
    public Bill()
    {}

    /**
     * @return the bank
     */
    public String getBank()
    {
        return bank;
    }

    /**
     * @param bank
     *            the bank to set
     */
    public void setBank(String bank)
    {
        this.bank = bank;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * @param customerName
     *            the customerName to set
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
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
     * @return the money
     */
    public double getMoney()
    {
        return moneys;
    }

    /**
     * @param money
     *            the money to set
     */
    public void setMoney(double money)
    {
        this.moneys = money;
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
     * @return the receipt
     */
    public String getReceipt()
    {
        return receipt;
    }

    /**
     * @param receipt
     *            the receipt to set
     */
    public void setReceipt(String receipt)
    {
        this.receipt = receipt;
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
     * @return the billType
     */
    public String getBillType()
    {
        return billType;
    }

    /**
     * @param billType
     *            the billType to set
     */
    public void setBillType(String billType)
    {
        this.billType = billType;
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
     * @return the dates
     */
    public Date getDates()
    {
        return dates;
    }

    /**
     * @param dates
     *            the dates to set
     */
    public void setDates(Date dates)
    {
        this.dates = dates;
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
     * @return the temp
     */
    public String getTemp()
    {
        return temp;
    }

    /**
     * @param temp
     *            the temp to set
     */
    public void setTemp(String temp)
    {
        this.temp = temp;
    }

    /**
     * @return the mark
     */
    public String getMark()
    {
        return mark;
    }

    /**
     * @param mark
     *            the mark to set
     */
    public void setMark(String mark)
    {
        if (mark != null)
        {
            this.mark = mark;
        }
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
     * @return the inway
     */
    public int getInway()
    {
        return inway;
    }

    /**
     * @return the destLocationId
     */
    public String getDestLocationId()
    {
        return destLocationId;
    }

    /**
     * @return the destBank
     */
    public String getDestBank()
    {
        return destBank;
    }

    /**
     * @param inway
     *            the inway to set
     */
    public void setInway(int inway)
    {
        this.inway = inway;
    }

    /**
     * @param destLocationId
     *            the destLocationId to set
     */
    public void setDestLocationId(String destLocationId)
    {
        this.destLocationId = destLocationId;
    }

    /**
     * @param destBank
     *            the destBank to set
     */
    public void setDestBank(String destBank)
    {
        this.destBank = destBank;
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
     * @return the mtype
     */
    public int getMtype()
    {
        return mtype;
    }

    /**
     * @param mtype
     *            the mtype to set
     */
    public void setMtype(int mtype)
    {
        this.mtype = mtype;
    }

}
