/**
 * File Name: CitySailBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-2-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * CitySailBean
 * 
 * @author ZHUZHU
 * @version 2010-2-25
 * @see CitySailBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_CITYSAIL")
public class CitySailBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private String name = "";

    private int amount = 0;

    private int month = 1;

    /**
     * default constructor
     */
    public CitySailBean()
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
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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
     * @return the month
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * @param month
     *            the month to set
     */
    public void setMonth(int month)
    {
        this.month = month;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String tab = ",";

        StringBuilder retValue = new StringBuilder();

        retValue.append("CitySailBean ( ").append(super.toString()).append(tab).append("id = ").append(
            this.id).append(tab).append("name = ").append(this.name).append(tab).append(
            "amount = ").append(this.amount).append(tab).append("month = ").append(this.month).append(
            tab).append(" )");

        return retValue.toString();
    }

}
