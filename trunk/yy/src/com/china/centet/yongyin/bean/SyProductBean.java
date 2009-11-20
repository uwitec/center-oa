/*
 * File Name: SyProductBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-9
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;


/**
 * 产品同步bean
 * 
 * @author zhuzhu
 * @version 2007-9-9
 * @see
 * @since
 */
public class SyProductBean implements Serializable
{
    private String cityFlag = "";

    private String oldId = "";

    private String newId = "";

    private String name = "";

    private String dateTimes = "";

    /**
     * default constructor
     */
    public SyProductBean()
    {}

    /**
     * @return the cityFlag
     */
    public String getCityFlag()
    {
        return cityFlag;
    }

    /**
     * @return the oldId
     */
    public String getOldId()
    {
        return oldId;
    }

    /**
     * @return the newId
     */
    public String getNewId()
    {
        return newId;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the dateTimes
     */
    public String getDateTimes()
    {
        return dateTimes;
    }

    /**
     * @param cityFlag
     *            the cityFlag to set
     */
    public void setCityFlag(String cityFlag)
    {
        this.cityFlag = cityFlag;
    }

    /**
     * @param oldId
     *            the oldId to set
     */
    public void setOldId(String oldId)
    {
        this.oldId = oldId;
    }

    /**
     * @param newId
     *            the newId to set
     */
    public void setNewId(String newId)
    {
        this.newId = newId;
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
     * @param dateTimes
     *            the dateTimes to set
     */
    public void setDateTimes(String dateTimes)
    {
        this.dateTimes = dateTimes;
    }
}
