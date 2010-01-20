/*
 * File Name: RoleBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.centet.yongyin.constant.Constant;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
@Entity
@Table(name = "t_center_role")
public class RoleBean implements Serializable
{
    @Id
    private String id = "";

    private String roleName = "";

    private int visible = Constant.ROLE_VISIBLE_YES;

    private String description = "";

    /**
     * default constructor
     */
    public RoleBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the roleName
     */
    public String getRoleName()
    {
        return roleName;
    }

    /**
     * @return the visible
     */
    public int getVisible()
    {
        return visible;
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
     * @param roleName
     *            the roleName to set
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    /**
     * @param visible
     *            the visible to set
     */
    public void setVisible(int visible)
    {
        this.visible = visible;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}
