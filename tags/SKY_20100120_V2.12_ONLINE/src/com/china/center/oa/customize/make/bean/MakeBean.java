/**
 * File Name: MakeBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.oa.constant.MakeConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * MakeBean
 * 
 * @author ZHUZHU
 * @version 2009-10-8
 * @see MakeBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_MAKE")
public class MakeBean implements Serializable
{
    @Id
    private String id = "";

    private String title = "";

    private String logTime = "";

    @Join(tagClass = StafferBean.class)
    private String createrId = "";

    @Join(tagClass = StafferBean.class, alias = "StafferBean2")
    private String handerId = "";

    private int type = MakeConstant.MAKE_TYPE_CUSTOMIZE;

    @Join(tagClass = MakeTokenBean.class)
    private int status = MakeConstant.MAKE_TOKEN_01;

    @Join(tagClass = MakeTokenItemBean.class)
    private int position = 11;

    private String description = "";

    /**
     * default constructor
     */
    public MakeBean()
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
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
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

    /**
     * @return the position
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
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
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if ( ! (obj instanceof MakeBean)) return false;
        final MakeBean other = (MakeBean)obj;
        if (id == null)
        {
            if (other.id != null) return false;
        }
        else if ( !id.equals(other.id)) return false;
        return true;
    }

    /**
     * @return the handerId
     */
    public String getHanderId()
    {
        return handerId;
    }

    /**
     * @param handerId
     *            the handerId to set
     */
    public void setHanderId(String handerId)
    {
        this.handerId = handerId;
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

        retValue.append("MakeBean ( ").append(super.toString()).append(tab).append("id = ").append(
            this.id).append(tab).append("title = ").append(this.title).append(tab).append(
            "logTime = ").append(this.logTime).append(tab).append("createrId = ").append(
            this.createrId).append(tab).append("handerId = ").append(this.handerId).append(tab).append(
            "type = ").append(this.type).append(tab).append("status = ").append(this.status).append(
            tab).append("position = ").append(this.position).append(tab).append("description = ").append(
            this.description).append(tab).append(" )");

        return retValue.toString();
    }

}
