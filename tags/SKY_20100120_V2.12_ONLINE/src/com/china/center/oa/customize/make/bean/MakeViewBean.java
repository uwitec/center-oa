/**
 * File Name: MakeViewBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-29<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;


/**
 * MakeViewBean
 * 
 * @author ZHUZHU
 * @version 2009-12-29
 * @see MakeViewBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_MAKE_VIEW")
public class MakeViewBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Unique(dependFields = "makeId")
    private String stafferId = "";

    @FK
    @Join(tagClass = MakeBean.class)
    private String makeId = "";

    private String createrId = "";

    private String logTime = "";

    /**
     * default constructor
     */
    public MakeViewBean()
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
     * @return the makeId
     */
    public String getMakeId()
    {
        return makeId;
    }

    /**
     * @param makeId
     *            the makeId to set
     */
    public void setMakeId(String makeId)
    {
        this.makeId = makeId;
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
     * @return the createrId
     */
    public String getCreaterId()
    {
        return createrId;
    }

    /**
     * @param createrId
     *            the createrId to set
     */
    public void setCreaterId(String createrId)
    {
        this.createrId = createrId;
    }
}
