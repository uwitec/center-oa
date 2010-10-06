/**
 * File Name: ComposeFeeBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.publics.bean.EnumBean;


/**
 * ComposeFeeBean
 * 
 * @author ZHUZHU
 * @version 2010-10-2
 * @see ComposeFeeBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_COMPOSE_FEE")
public class ComposeFeeBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    private String parentId = "";

    @Join(tagClass = EnumBean.class, tagField = "key")
    private String feeItemId = "";

    private double price = 0.0d;

    private String logTime = "";

    /**
     * default constructor
     */
    public ComposeFeeBean()
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
     * @return the feeItemId
     */
    public String getFeeItemId()
    {
        return feeItemId;
    }

    /**
     * @param feeItemId
     *            the feeItemId to set
     */
    public void setFeeItemId(String feeItemId)
    {
        this.feeItemId = feeItemId;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuffer retValue = new StringBuffer();

        retValue.append("ComposeFeeBean ( ").append(super.toString()).append(TAB).append("id = ").append(this.id).append(
            TAB).append("parentId = ").append(this.parentId).append(TAB).append("feeItemId = ").append(this.feeItemId).append(
            TAB).append("price = ").append(this.price).append(TAB).append("logTime = ").append(this.logTime).append(TAB).append(
            " )");

        return retValue.toString();
    }

}
