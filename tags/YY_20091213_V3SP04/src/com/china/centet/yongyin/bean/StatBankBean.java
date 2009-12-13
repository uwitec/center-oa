/*
 * File Name: StstBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-2
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * 资金每日统计的bean
 * 
 * @author zhuzhu
 * @version 2007-9-2
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_STATBANK")
public class StatBankBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private String bank = "";

    private String statId = "";

    private String logDate = "";

    private String locationId = "0";

    private double outMoney = 0.0d;

    private double inMoney = 0.0d;

    private double tatolMoney = 0d;

    /**
     * default constructor
     */
    public StatBankBean()
    {}

    public String toString()
    {
        return id + bank + statId;
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the bank
     */
    public String getBank()
    {
        return bank;
    }

    /**
     * @return the statId
     */
    public String getStatId()
    {
        return statId;
    }

    /**
     * @return the logDate
     */
    public String getLogDate()
    {
        return logDate;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @return the outMoney
     */
    public double getOutMoney()
    {
        return outMoney;
    }

    /**
     * @return the inMoney
     */
    public double getInMoney()
    {
        return inMoney;
    }

    /**
     * @return the tatolMoney
     */
    public double getTatolMoney()
    {
        return tatolMoney;
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
     * @param bank
     *            the bank to set
     */
    public void setBank(String bank)
    {
        this.bank = bank;
    }

    /**
     * @param statId
     *            the statId to set
     */
    public void setStatId(String statId)
    {
        this.statId = statId;
    }

    /**
     * @param logDate
     *            the logDate to set
     */
    public void setLogDate(String logDate)
    {
        this.logDate = logDate;
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
     * @param outMoney
     *            the outMoney to set
     */
    public void setOutMoney(double outMoney)
    {
        this.outMoney = outMoney;
    }

    /**
     * @param inMoney
     *            the inMoney to set
     */
    public void setInMoney(double inMoney)
    {
        this.inMoney = inMoney;
    }

    /**
     * @param tatolMoney
     *            the tatolMoney to set
     */
    public void setTatolMoney(double tatolMoney)
    {
        this.tatolMoney = tatolMoney;
    }
}
