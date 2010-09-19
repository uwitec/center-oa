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

    private String depotpartId = "";

    private String productId = "";

    private double price = 0.0d;

    private String serializeId = "";

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
     * @return the serializeId
     */
    public String getSerializeId()
    {
        return serializeId;
    }

    /**
     * @param serializeId
     *            the serializeId to set
     */
    public void setSerializeId(String serializeId)
    {
        this.serializeId = serializeId;
    }

    /**
     * @return the depotpartId
     */
    public String getDepotpartId()
    {
        return depotpartId;
    }

    /**
     * @param depotpartId
     *            the depotpartId to set
     */
    public void setDepotpartId(String depotpartId)
    {
        this.depotpartId = depotpartId;
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
            .append("ProductChangeWrap ( ")
            .append(super.toString())
            .append(TAB)
            .append("storageId = ")
            .append(this.storageId)
            .append(TAB)
            .append("depotpartId = ")
            .append(this.depotpartId)
            .append(TAB)
            .append("productId = ")
            .append(this.productId)
            .append(TAB)
            .append("price = ")
            .append(this.price)
            .append(TAB)
            .append("serializeId = ")
            .append(this.serializeId)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append("change = ")
            .append(this.change)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}