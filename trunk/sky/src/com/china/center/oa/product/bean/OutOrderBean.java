/**
 * 
 */
package com.china.center.oa.product.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.oa.constant.ProductConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * ProductStatBean
 * 
 * @author ZHUZHU
 */
@Entity
@Table(name = "T_CENTER_OUTORDER")
public class OutOrderBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "产品", type = Element.INPUT, readonly = true, name = "productName", must = true)
    @Join(tagClass = ProductBean.class)
    private String productId = "";

    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    @Html(title = "订货数量", type = Element.INPUT, oncheck = JCheck.ONLY_NUMBER, must = true)
    private int orderAmount = 0;

    private int status = ProductConstant.ORDER_STATUS_COMMON;

    private String logTime = "";

    @Html(title = "备注", type = Element.TEXTAREA, maxLength = 200)
    private String description = "";

    /**
     * default constructor
     */
    public OutOrderBean()
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
     * @return the orderAmount
     */
    public int getOrderAmount()
    {
        return orderAmount;
    }

    /**
     * @param orderAmount
     *            the orderAmount to set
     */
    public void setOrderAmount(int orderAmount)
    {
        this.orderAmount = orderAmount;
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
}
