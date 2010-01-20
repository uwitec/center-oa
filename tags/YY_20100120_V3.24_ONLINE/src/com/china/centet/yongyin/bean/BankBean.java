/*
 * File Name: BankBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;


/**
 * 银行
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
@Entity(cache = true)
@Table(name = "T_CENTER_BANK")
public class BankBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Html(title = "银行名称", must = true, maxLength = 40)
    private String name = "";

    private String userId = "";

    @Html(title = "所属区域", type = Element.SELECT)
    private String locationId = "";

    @Ignore
    private String locationName = "";

    /**
     * 上月余额
     */
    @Ignore
    private double lmoney = 0.0d;

    /**
     * default constructor
     */
    public BankBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
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
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
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
     * @return the lmoney
     */
    public double getLmoney()
    {
        return lmoney;
    }

    /**
     * @param lmoney
     *            the lmoney to set
     */
    public void setLmoney(double lmoney)
    {
        this.lmoney = lmoney;
    }
}
