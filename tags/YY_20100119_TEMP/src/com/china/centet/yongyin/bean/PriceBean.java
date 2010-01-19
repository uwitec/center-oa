/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
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
@Table(name = "T_CENTER_PRICE")
public class PriceBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    /**
     * 产品的ID
     */
    @Html(title = "产品名称", name = "productName", must = true, maxLength = 40, readonly = true)
    @Join(tagClass = Product.class, type = JoinType.EQUAL)
    private String productId = "";

    @Html(title = "录入时间", must = true, maxLength = 40, readonly = true)
    private String logTime = "";

    @Html(title = "价格", tip = "只能填写数字", must = true, maxLength = 40, oncheck = JCheck.ONLY_FLOAT)
    private double price = 0.0d;

    @Join(tagClass = User.class, type = JoinType.LEFT)
    private String userId = "";

    @Html(title = "来源网站", type = Element.SELECT, must = true, maxLength = 255)
    @Join(tagClass = PriceWebBean.class, type = JoinType.EQUAL)
    private String priceWebId = "";

    private int status = PriceConstant.PRICE_COMMON;

    @Html(title = "备注", type = Element.TEXTAREA, maxLength = 255)
    private String description = "";

    /**
     *
     */
    public PriceBean()
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
     * @return the priceWebId
     */
    public String getPriceWebId()
    {
        return priceWebId;
    }

    /**
     * @param priceWebId
     *            the priceWebId to set
     */
    public void setPriceWebId(String priceWebId)
    {
        this.priceWebId = priceWebId;
    }
}
