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

import com.china.center.annotation.Column;
import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Table;


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
public class User implements Serializable
{
    @Id
    private String id = "0";

    private String name = "";

    private String password = "";

    private String stafferId = "";

    private String stafferName = "";

    private String locationID = "";

    @Ignore
    private Role role = Role.COMMON;

    @Column(name = "role")
    private int type = 0;

    /**
     * 0:正常 1:锁定
     */
    private int status = 0;

    /**
     * 登录失败次数
     */
    private int fail = 0;

    private String loginTime = "";

    /**
     * default constructor
     */
    public User()
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
     * @return the role
     */
    public Role getRole()
    {
        return role;
    }

    /**
     * @param role
     *            the role to set
     */
    public void setRole(Role role)
    {
        this.role = role;
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

}
