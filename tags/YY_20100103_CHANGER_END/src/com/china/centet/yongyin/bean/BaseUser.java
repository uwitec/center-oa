/*
 * File Name: User.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-25
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.JoinType;


/**
 * 人员类
 * 
 * @author zhuzhu
 * @version 2007-3-25
 * @see
 * @since
 */
@Entity(cache = true)
@Table(name = "t_center_user")
public class BaseUser implements Serializable
{
    @Id
    private String id = "0";

    private String name = "";

    private String password = "";

    @FK
    private String stafferId = "";

    private String stafferName = "";

    @Join(tagClass = LocationBean.class, type = JoinType.LEFT)
    private String locationID = "";

    private int role = 0;

    /**
     * 0:正常 1:锁定
     */
    private int status = 0;

    @Ignore
    private Role baseRole = Role.COMMON;

    /**
     * 登录失败次数
     */
    private int fail = 0;

    private String loginTime = "";

    /**
     * default constructor
     */
    public BaseUser()
    {}

    /**
     * @return the loginTime
     */
    public String getLoginTime()
    {
        return loginTime;
    }

    /**
     * @param loginTime
     *            the loginTime to set
     */
    public void setLoginTime(String loginTime)
    {
        this.loginTime = loginTime;
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
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

    /**
     * @return the locationID
     */
    public String getLocationID()
    {
        return locationID;
    }

    /**
     * @param locationID
     *            the locationID to set
     */
    public void setLocationID(String locationID)
    {
        this.locationID = locationID;
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
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @return the fail
     */
    public int getFail()
    {
        return fail;
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
     * @param fail
     *            the fail to set
     */
    public void setFail(int fail)
    {
        this.fail = fail;
    }

    /**
     * @return the role
     */
    public int getRole()
    {
        return role;
    }

    /**
     * @param role
     *            the role to set
     */
    public void setRole(int role)
    {
        this.role = role;
    }

    /**
     * @return the baseRole
     */
    public Role getBaseRole()
    {
        return baseRole;
    }

    /**
     * @param baseRole
     *            the baseRole to set
     */
    public void setBaseRole(Role baseRole)
    {
        this.baseRole = baseRole;
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
        result = prime * result + ( (stafferId == null) ? 0 : stafferId.hashCode());
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
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if ( ! (obj instanceof BaseUser))
        {
            return false;
        }

        final BaseUser other = (BaseUser)obj;

        if (stafferId == null)
        {
            if (other.stafferId != null)
            {
                return false;
            }
        }
        else if ( !stafferId.equals(other.stafferId))
        {
            return false;
        }

        return true;
    }
}
