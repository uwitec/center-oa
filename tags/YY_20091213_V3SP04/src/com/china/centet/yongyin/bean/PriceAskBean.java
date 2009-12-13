/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.annotation.enums.JoinType;
import com.china.centet.yongyin.constant.PriceConstant;


/**
 * 询价的bean
 * 
 * @author Administrator
 */
@Entity(name = "询价")
@Table(name = "T_CENTER_PRICEASK")
public class PriceAskBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "产品名称", name = "productName", must = true, maxLength = 40, readonly = true)
    @Join(tagClass = Product.class, type = JoinType.EQUAL)
    private String productId = "";

    @Html(title = "数量", tip = "只能填写数字", must = true, maxLength = 40, oncheck = JCheck.ONLY_NUMBER)
    private int amount = 0;

    private double price = 0.0d;

    private String logTime = "";

    @Html(title = "处理时间", type = Element.DATETIME, must = true, maxLength = 40, readonly = true)
    private String processTime = "";

    private String endTime = "";

    @Join(tagClass = User.class, type = JoinType.LEFT)
    private String userId = "";

    @Join(tagClass = User.class, type = JoinType.LEFT, alias = "user1")
    private String puserId = "";

    @Html(title = "紧急程度", type = Element.SELECT, must = true, maxLength = 255)
    private int instancy = 0;

    private int status = PriceConstant.PRICE_ASK_STATUS_INIT;

    private int overTime = PriceConstant.OVERTIME_NO;

    @Join(tagClass = LocationBean.class)
    private String locationId = "";

    @Html(title = "备注", type = Element.TEXTAREA, maxLength = 255)
    private String description = "";

    private String reason = "";

    @Ignore
    private List<PriceAskProviderBean> item = null;

    /**
     *
     */
    public PriceAskBean()
    {}

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
     * @return the productId
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * @return the amount
     */
    public int getAmount()
    {
        return amount;
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
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(double price)
    {
        this.price = price;
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
     * @return the processTime
     */
    public String getProcessTime()
    {
        return processTime;
    }

    /**
     * @param processTime
     *            the processTime to set
     */
    public void setProcessTime(String processTime)
    {
        this.processTime = processTime;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @return the puserId
     */
    public String getPuserId()
    {
        return puserId;
    }

    /**
     * @param puserId
     *            the puserId to set
     */
    public void setPuserId(String puserId)
    {
        this.puserId = puserId;
    }

    /**
     * @return the instancy
     */
    public int getInstancy()
    {
        return instancy;
    }

    /**
     * @param instancy
     *            the instancy to set
     */
    public void setInstancy(int instancy)
    {
        this.instancy = instancy;
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
     * @return the item
     */
    public List<PriceAskProviderBean> getItem()
    {
        return item;
    }

    /**
     * @param item
     *            the item to set
     */
    public void setItem(List<PriceAskProviderBean> item)
    {
        this.item = item;
    }

    /**
     * @return the overTime
     */
    public int getOverTime()
    {
        return overTime;
    }

    /**
     * @param overTime
     *            the overTime to set
     */
    public void setOverTime(int overTime)
    {
        this.overTime = overTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return the reason
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason)
    {
        this.reason = reason;
    }
}
