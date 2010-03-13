/**
 * 
 */
package com.china.center.oa.product.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.center.oa.constant.ProductConstant;


/**
 * ProductStatBean
 * 
 * @author ZHUZHU
 */
@Entity
@Table(name = "T_CENTER_PRODUCTSTAT")
public class ProductStatBean implements Serializable
{
    @Id
    private String id = "";

    private String productId = "";

    private String productName = "";

    private String productCode = "";

    @FK
    private String logTime = "";

    private int sailAmount = 0;

    private double sailAvg = 0.0d;

    /**
     * »±∂Ó
     */
    private int subtractAmount = 0;

    /**
     * ø‚¥Ê
     */
    private int inventoryAmount = 0;

    /**
     * ∂©ªı¡ø
     */
    private int orderAmount = 0;

    private int status = ProductConstant.STAT_STATUS_COMMON;

    private String description = "";

    /**
     * ProductStatBean
     */
    public ProductStatBean()
    {}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getLogTime()
    {
        return logTime;
    }

    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    public int getSailAmount()
    {
        return sailAmount;
    }

    public void setSailAmount(int sailAmount)
    {
        this.sailAmount = sailAmount;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the sailAvg
     */
    public double getSailAvg()
    {
        return sailAvg;
    }

    /**
     * @param sailAvg
     *            the sailAvg to set
     */
    public void setSailAvg(double sailAvg)
    {
        this.sailAvg = sailAvg;
    }

    public int getSubtractAmount()
    {
        return subtractAmount;
    }

    public void setSubtractAmount(int subtractAmount)
    {
        this.subtractAmount = subtractAmount;
    }

    public int getInventoryAmount()
    {
        return inventoryAmount;
    }

    public void setInventoryAmount(int inventoryAmount)
    {
        this.inventoryAmount = inventoryAmount;
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
        if ( ! (obj instanceof ProductStatBean)) return false;
        final ProductStatBean other = (ProductStatBean)obj;
        if (productId == null)
        {
            if (other.productId != null) return false;
        }
        else if ( !productId.equals(other.productId)) return false;
        return true;
    }
}
