/**
 * File Name: ProductChangeWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.wrap;


import java.io.Serializable;

import com.china.center.oa.product.constant.StorageConstant;


/**
 * ProductChangeWrap
 * 
 * @author ZHUZHU
 * @version 2010-8-25
 * @see ProductChangeWrap
 * @since 1.0
 */
public class ProductChangeWrap implements Serializable
{
    private String storageId = "";

    private String productId = "";

    /**
     * 价格主键
     */
    private String priceKey = "";

    private double price = 0.0d;

    private String description = "";

    /**
     * 正数增加库存 负数减少库存
     */
    private int change = 0;

    private int type = StorageConstant.OPR_STORAGE_INIT;

    /**
     * default constructor
     */
    public ProductChangeWrap()
    {
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
     * @return the change
     */
    public int getChange()
    {
        return change;
    }

    /**
     * @param change
     *            the change to set
     */
    public void setChange(int change)
    {
        this.change = change;
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
     * @return the priceKey
     */
    public String getPriceKey()
    {
        return priceKey;
    }

    /**
     * @param priceKey
     *            the priceKey to set
     */
    public void setPriceKey(String priceKey)
    {
        this.priceKey = priceKey;
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
}
