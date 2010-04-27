/*
 * File Name: ProductAmount.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Table;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
@Entity
@Table(name = "t_center_productnumber")
public class ProductAmount implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "0";

    private String productId = "";

    @Ignore
    private String productCode = "";

    private String productName = "";

    private int num = 0;

    @Ignore
    private int total = 0;

    private String locationId = "";

    @Ignore
    private String locationName = "";

    /**
     * default constructor
     */
    public ProductAmount()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the productId
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * @return the num
     */
    public int getNum()
    {
        return num;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
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
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * @param num
     *            the num to set
     */
    public void setNum(int num)
    {
        this.num = num;
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
     * @return 返回 productCode
     */
    public String getProductCode()
    {
        return productCode;
    }

    /**
     * @param 对productCode进行赋值
     */
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    /**
     * @return the locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param locationName
     *            the locationName to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * @return the total
     */
    public int getTotal()
    {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(int total)
    {
        this.total = total;
    }

}
