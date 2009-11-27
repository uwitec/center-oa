/**
 * File Name: CurOutBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-11-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * CurOutBean
 * 
 * @author ZHUZHU
 * @version 2009-11-27
 * @see CurOutBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_LOG_CUROUT")
public class CurOutBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private String cid = "";

    private String outId = "";

    private String logTime = "";

    private int delay = 0;

    private double val = 0.0d;

    /**
     * default constructor
     */
    public CurOutBean()
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
     * @return the cid
     */
    public String getCid()
    {
        return cid;
    }

    /**
     * @param cid
     *            the cid to set
     */
    public void setCid(String cid)
    {
        this.cid = cid;
    }

    /**
     * @return the outId
     */
    public String getOutId()
    {
        return outId;
    }

    /**
     * @param outId
     *            the outId to set
     */
    public void setOutId(String outId)
    {
        this.outId = outId;
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
     * @return the delay
     */
    public int getDelay()
    {
        return delay;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    /**
     * @return the val
     */
    public double getVal()
    {
        return val;
    }

    /**
     * @param val
     *            the val to set
     */
    public void setVal(double val)
    {
        this.val = val;
    }
}
