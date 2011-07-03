/**
 * File Name: BaseBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-26
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;


/**
 * 库存的子项
 * 
 * @author ZHUZHU
 * @version 2007-3-26
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_BASE")
public class BaseBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    private String outId = "";

    private String productId = "";

    private String productName = "";

    private String showName = "";

    private String showId = "";

    private String locationId = "";

    private String unit = "";

    private int amount = 0;

    private int inway = 0;

    /**
     * 储位(仓库下面是通过 仓区+产品+价格+产品所有者获取具体的信息的,所以storageId不使用了)
     */
    private String storageId = "";

    /**
     * 产品所在的仓区
     */
    private String depotpartId = "";

    private String depotpartName = "";

    /**
     * 产品的所有者
     */
    private String owner = "0";

    /**
     * 产品的所有者名称
     */
    private String ownerName = "公共";

    /**
     * 销售价格(含税价)
     */
    private double price = 0.0d;

    /**
     * 输入价格
     */
    private double inputPrice = 0.0d;

    /**
     * 成本
     */
    private double costPrice = 0.0d;

    /**
     * 成本的string值
     */
    private String costPriceKey = "";

    /**
     * 总销售价
     */
    private double value = 0.0d;

    /**
     * 成本
     */
    private String description = "";

    /**
     * default constructor
     */
    public BaseBean()
    {
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

    /**
     * @return the showName
     */
    public String getShowName()
    {
        return showName;
    }

    /**
     * @param showName
     *            the showName to set
     */
    public void setShowName(String showName)
    {
        this.showName = showName;
    }

    /**
     * @return the showId
     */
    public String getShowId()
    {
        return showId;
    }

    /**
     * @param showId
     *            the showId to set
     */
    public void setShowId(String showId)
    {
        this.showId = showId;
    }

    /**
     * @return the costPrice
     */
    public double getCostPrice()
    {
        return costPrice;
    }

    /**
     * @param costPrice
     *            the costPrice to set
     */
    public void setCostPrice(double costPrice)
    {
        this.costPrice = costPrice;
    }

    /**
     * @return the owner
     */
    public String getOwner()
    {
        return owner;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    /**
     * @return the ownerName
     */
    public String getOwnerName()
    {
        return ownerName;
    }

    /**
     * @param ownerName
     *            the ownerName to set
     */
    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    /**
     * @return the costPriceKey
     */
    public String getCostPriceKey()
    {
        return costPriceKey;
    }

    /**
     * @param costPriceKey
     *            the costPriceKey to set
     */
    public void setCostPriceKey(String costPriceKey)
    {
        this.costPriceKey = costPriceKey;
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
     * @return the depotpartName
     */
    public String getDepotpartName()
    {
        return depotpartName;
    }

    /**
     * @param depotpartName
     *            the depotpartName to set
     */
    public void setDepotpartName(String depotpartName)
    {
        this.depotpartName = depotpartName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (costPriceKey == null) ? 0 : costPriceKey.hashCode());
        result = prime * result + ( (depotpartId == null) ? 0 : depotpartId.hashCode());
        result = prime * result + ( (owner == null) ? 0 : owner.hashCode());
        result = prime * result + ( (productId == null) ? 0 : productId.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if ( ! (obj instanceof BaseBean)) return false;
        final BaseBean other = (BaseBean)obj;
        if (costPriceKey == null)
        {
            if (other.costPriceKey != null) return false;
        }
        else if ( !costPriceKey.equals(other.costPriceKey)) return false;
        if (depotpartId == null)
        {
            if (other.depotpartId != null) return false;
        }
        else if ( !depotpartId.equals(other.depotpartId)) return false;
        if (owner == null)
        {
            if (other.owner != null) return false;
        }
        else if ( !owner.equals(other.owner)) return false;
        if (productId == null)
        {
            if (other.productId != null) return false;
        }
        else if ( !productId.equals(other.productId)) return false;
        return true;
    }

    /**
     * 这里没有仓区
     * 
     * @param obj
     * @return
     */
    public boolean equals2(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if ( ! (obj instanceof BaseBean)) return false;
        final BaseBean other = (BaseBean)obj;
        if (costPriceKey == null)
        {
            if (other.costPriceKey != null) return false;
        }
        else if ( !costPriceKey.equals(other.costPriceKey)) return false;

        if (owner == null)
        {
            if (other.owner != null) return false;
        }
        else if ( !owner.equals(other.owner)) return false;

        if (productId == null)
        {
            if (other.productId != null) return false;
        }
        else if ( !productId.equals(other.productId)) return false;
        return true;
    }

    /**
     * @return the inputPrice
     */
    public double getInputPrice()
    {
        return inputPrice;
    }

    /**
     * @param inputPrice
     *            the inputPrice to set
     */
    public void setInputPrice(double inputPrice)
    {
        this.inputPrice = inputPrice;
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
            .append("BaseBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("outId = ")
            .append(this.outId)
            .append(TAB)
            .append("productId = ")
            .append(this.productId)
            .append(TAB)
            .append("productName = ")
            .append(this.productName)
            .append(TAB)
            .append("showName = ")
            .append(this.showName)
            .append(TAB)
            .append("showId = ")
            .append(this.showId)
            .append(TAB)
            .append("locationId = ")
            .append(this.locationId)
            .append(TAB)
            .append("unit = ")
            .append(this.unit)
            .append(TAB)
            .append("amount = ")
            .append(this.amount)
            .append(TAB)
            .append("inway = ")
            .append(this.inway)
            .append(TAB)
            .append("storageId = ")
            .append(this.storageId)
            .append(TAB)
            .append("depotpartId = ")
            .append(this.depotpartId)
            .append(TAB)
            .append("depotpartName = ")
            .append(this.depotpartName)
            .append(TAB)
            .append("owner = ")
            .append(this.owner)
            .append(TAB)
            .append("ownerName = ")
            .append(this.ownerName)
            .append(TAB)
            .append("price = ")
            .append(this.price)
            .append(TAB)
            .append("inputPrice = ")
            .append(this.inputPrice)
            .append(TAB)
            .append("costPrice = ")
            .append(this.costPrice)
            .append(TAB)
            .append("costPriceKey = ")
            .append(this.costPriceKey)
            .append(TAB)
            .append("value = ")
            .append(this.value)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
