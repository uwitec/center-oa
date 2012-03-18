/**
 * File Name: InvoiceinsItemBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;


/**
 * InvoiceinsItemBean
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see InvoiceinsItemBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_INVOICEINS_ITEM")
public class InvoiceinsItemBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    private String parentId = "";

    private String showId = "";

    private String showName = "";

    /**
     * 规格
     */
    private String special = "";

    /**
     * 单位
     */
    private String unit = "";

    /**
     * 产品数量
     */
    private int amount = 0;

    /**
     * 单价
     */
    private double price = 0.0d;

    /**
     * 总价
     */
    private double moneys = 0.0d;

    /**
     * default constructor
     */
    public InvoiceinsItemBean()
    {
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
     * @return the parentId
     */
    public String getParentId()
    {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
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
     * @return the moneys
     */
    public double getMoneys()
    {
        return moneys;
    }

    /**
     * @param moneys
     *            the moneys to set
     */
    public void setMoneys(double moneys)
    {
        this.moneys = moneys;
    }

    /**
     * @return the special
     */
    public String getSpecial()
    {
        return special;
    }

    /**
     * @param special
     *            the special to set
     */
    public void setSpecial(String special)
    {
        this.special = special;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("InvoiceinsItemBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("parentId = ")
            .append(this.parentId)
            .append(TAB)
            .append("showId = ")
            .append(this.showId)
            .append(TAB)
            .append("showName = ")
            .append(this.showName)
            .append(TAB)
            .append("special = ")
            .append(this.special)
            .append(TAB)
            .append("unit = ")
            .append(this.unit)
            .append(TAB)
            .append("amount = ")
            .append(this.amount)
            .append(TAB)
            .append("price = ")
            .append(this.price)
            .append(TAB)
            .append("moneys = ")
            .append(this.moneys)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
