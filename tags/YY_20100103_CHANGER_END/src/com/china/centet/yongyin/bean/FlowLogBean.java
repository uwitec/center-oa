/*
 * File Name: OutLogBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-13
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * 销售单的历史审批记录
 * 
 * @author zhuzhu
 * @version 2008-1-13
 * @see
 * @since
 */
@Entity(name = "审批记录")
@Table(name = "T_CENTER_OUTLOG")
public class FlowLogBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    private String fullId = "";

    private String actor = "";

    private int oprMode = 0;

    private int oprAmount = 0;

    private int preStatus = 0;

    private int afterStatus = 0;

    private String logTime = "";

    private String description = "";

    /**
     * default constructor
     */
    public FlowLogBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the fullId
     */
    public String getFullId()
    {
        return fullId;
    }

    /**
     * @return the actor
     */
    public String getActor()
    {
        return actor;
    }

    /**
     * @return the oprMode
     */
    public int getOprMode()
    {
        return oprMode;
    }

    /**
     * @return the oprAmount
     */
    public int getOprAmount()
    {
        return oprAmount;
    }

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
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
     * @param fullId
     *            the fullId to set
     */
    public void setFullId(String fullId)
    {
        this.fullId = fullId;
    }

    /**
     * @param actor
     *            the actor to set
     */
    public void setActor(String actor)
    {
        this.actor = actor;
    }

    /**
     * @param oprMode
     *            the oprMode to set
     */
    public void setOprMode(int oprMode)
    {
        this.oprMode = oprMode;
    }

    /**
     * @param oprAmount
     *            the oprAmount to set
     */
    public void setOprAmount(int oprAmount)
    {
        this.oprAmount = oprAmount;
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
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the preStatus
     */
    public int getPreStatus()
    {
        return preStatus;
    }

    /**
     * @return the afterStatus
     */
    public int getAfterStatus()
    {
        return afterStatus;
    }

    /**
     * @param preStatus
     *            the preStatus to set
     */
    public void setPreStatus(int preStatus)
    {
        this.preStatus = preStatus;
    }

    /**
     * @param afterStatus
     *            the afterStatus to set
     */
    public void setAfterStatus(int afterStatus)
    {
        this.afterStatus = afterStatus;
    }

}
