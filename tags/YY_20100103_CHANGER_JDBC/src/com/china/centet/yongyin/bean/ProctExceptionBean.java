/*
 * File Name: BankBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;


/**
 * 银行
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
@Entity(name = "货物异常处理单")
@Table(name = "T_CENTER_PROCTEXCEPTION")
public class ProctExceptionBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "产品名称", must = true, maxLength = 40)
    private String productName = "";

    private String locationId = "";

    private String attachment = "";

    private String fileName = "";

    private String creater = "";

    @Html(title = "申请人", must = true, maxLength = 40)
    private String applyer = "";

    private String logDate = "";

    @Html(title = "原因", type = Element.TEXTAREA, maxLength = 100)
    private String description = "";

    private String apply = "";

    @Html(title = "产品数量", must = true, maxLength = 40, oncheck = JCheck.ONLY_NUMBER)
    private int amount = 0;

    private int status = 0;

    /**
     * default constructor
     */
    public ProctExceptionBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @return the attachment
     */
    public String getAttachment()
    {
        return attachment;
    }

    /**
     * @return the applyer
     */
    public String getApplyer()
    {
        return applyer;
    }

    /**
     * @return the logDate
     */
    public String getLogDate()
    {
        return logDate;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return the apply
     */
    public String getApply()
    {
        return apply;
    }

    /**
     * @return the amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
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
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
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
     * @param attachment
     *            the attachment to set
     */
    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }

    /**
     * @param applyer
     *            the applyer to set
     */
    public void setApplyer(String applyer)
    {
        this.applyer = applyer;
    }

    /**
     * @param logDate
     *            the logDate to set
     */
    public void setLogDate(String logDate)
    {
        this.logDate = logDate;
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
     * @param apply
     *            the apply to set
     */
    public void setApply(String apply)
    {
        this.apply = apply;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
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
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * @return the creater
     */
    public String getCreater()
    {
        return creater;
    }

    /**
     * @param creater
     *            the creater to set
     */
    public void setCreater(String creater)
    {
        this.creater = creater;
    }
}
