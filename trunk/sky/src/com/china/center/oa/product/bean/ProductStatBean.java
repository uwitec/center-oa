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
    
    private int sailAvg = 0;
    
    /**
     * È±¶î
     */
    private int subtractAmount = 0;
    
    /**
     * ¿â´æ
     */
    private int inventoryAmount = 0;
    
    private int status = ProductConstant.STAT_STATUS_COMMON;
    
    private String description = "";

    /**
     * ProductStatBean
     */
    public ProductStatBean()
    {
    }

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

    public int getSailAvg()
    {
        return sailAvg;
    }

    public void setSailAvg(int sailAvg)
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
}
