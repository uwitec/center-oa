/**
 * File Name: StafferBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-24<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;


/**
 * StafferBean
 * 
 * @author zhuzhu
 * @version 2008-8-24
 * @see
 * @since
 */
@Entity(name = "ְԱ")
@Table(name = "T_CENTER_OASTAFFER")
public class StafferBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Unique
    private String name = "";

    private String locationId = "";

    private String pwkey = "";

    private int status = 0;

    /**
     * default constructor
     */
    public StafferBean()
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
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    /**
     * @return the pwkey
     */
    public String getPwkey()
    {
        return pwkey;
    }

    /**
     * @param pwkey
     *            the pwkey to set
     */
    public void setPwkey(String pwkey)
    {
        this.pwkey = pwkey;
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
}
