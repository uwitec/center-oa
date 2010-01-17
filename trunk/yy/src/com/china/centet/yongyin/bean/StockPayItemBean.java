/**
 * File Name: StockPayItemBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-1-17<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.JoinType;
import com.china.centet.yongyin.constant.StockConstant;


/**
 * StockPayItemBean
 * 
 * @author ZHUZHU
 * @version 2010-1-17
 * @see StockPayItemBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_STOCKPAYITEM")
public class StockPayItemBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    private String payId = "";

    private String stockItemId = "";

    private String stockId = "";

    private String stafferId = "";

    @Join(tagClass = ProviderBean.class, type = JoinType.LEFT)
    private String providerId = "";

    @Join(tagClass = Product.class)
    private String productId = "";

    private double price = 0.0d;

    private int amount = 0;

    private double total = 0.0d;

    private int status = StockConstant.STOCK_ITEM_PAY_STATUS_INIT;

    private String logTime = "";

    /**
     * default constructor
     */
    public StockPayItemBean()
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
     * @return the stockItemId
     */
    public String getStockItemId()
    {
        return stockItemId;
    }

    /**
     * @param stockItemId
     *            the stockItemId to set
     */
    public void setStockItemId(String stockItemId)
    {
        this.stockItemId = stockItemId;
    }

    /**
     * @return the stockId
     */
    public String getStockId()
    {
        return stockId;
    }

    /**
     * @param stockId
     *            the stockId to set
     */
    public void setStockId(String stockId)
    {
        this.stockId = stockId;
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

    /**
     * @return the providerId
     */
    public String getProviderId()
    {
        return providerId;
    }

    /**
     * @param providerId
     *            the providerId to set
     */
    public void setProviderId(String providerId)
    {
        this.providerId = providerId;
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
     * @return the total
     */
    public double getTotal()
    {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(double total)
    {
        this.total = total;
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
     * @return the payId
     */
    public String getPayId()
    {
        return payId;
    }

    /**
     * @param payId
     *            the payId to set
     */
    public void setPayId(String payId)
    {
        this.payId = payId;
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
}
