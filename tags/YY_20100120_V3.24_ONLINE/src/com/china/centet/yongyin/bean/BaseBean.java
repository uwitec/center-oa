/*
 * File Name: BaseBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-26
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-3-26
 * @see
 * @since
 */
@Entity
@Table(name = "t_center_base")
public class BaseBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private String outId = "";

    private String productId = "";

    private String productName = "";

    private String locationId = "";

    private String unit = "";

    private int amount = 0;

    private int inway = 0;

    /**
     * 储位
     */
    private String storageId = "";

    private double price = 0.0d;

    private double value = 0.0d;

    private String description = "";

    /**
     * default constructor
     */
    public BaseBean()
    {}

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
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
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
     * @return the unit
     */
    public String getUnit()
    {
        return unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    /**
     * @return the value
     */
    public double getValue()
    {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(double value)
    {
        this.value = value;
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
     * @return 返回 locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param 对locationId进行赋值
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
     * @param inway
     *            the inway to set
     */
    public void setInway(int inway)
    {
        this.inway = inway;
    }

    /**
     * @return the storageId
     */
    public String getStorageId()
    {
        return storageId;
    }

    /**
     * @param storageId
     *            the storageId to set
     */
    public void setStorageId(String storageId)
    {
        this.storageId = storageId;
    }
}
