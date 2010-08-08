/**
 * File Name: DepartmentBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.Unique;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.oa.publics.vs.OrgBean;


/**
 * 组织结构
 * 
 * @author ZHUZHU
 * @version 2008-11-2
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_PRINCIPALSHIP")
public class PrincipalshipBean implements Serializable
{
    @Id
    private String id = "";

    @Unique
    @Html(title = "组织名称", must = true, maxLength = 20)
    private String name = "";

    private String parentId = "";

    /**
     * 职务的级别
     */
    @Html(title = "组织级别", must = true, type = Element.SELECT)
    private int level = 0;

    /**
     * 上级职务的列表
     */
    @Ignore
    private List<OrgBean> parentOrgList = null;

    /**
     * default constructor
     */
    public PrincipalshipBean()
    {
    }

    /**
     * @return the level
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level)
    {
        this.level = level;
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
     * @return the parentOrgList
     */
    public List<OrgBean> getParentOrgList()
    {
        return parentOrgList;
    }

    /**
     * @param parentOrgList
     *            the parentOrgList to set
     */
    public void setParentOrgList(List<OrgBean> parentOrgList)
    {
        this.parentOrgList = parentOrgList;
    }

    /**
     * @return the parentId
     */
    public String getParentId()
    {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
}
