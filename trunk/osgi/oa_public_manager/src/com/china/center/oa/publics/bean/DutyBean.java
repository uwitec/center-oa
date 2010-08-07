/**
 * File Name: DutyBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.Unique;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.clone.DataClone;


/**
 * 纳税实体
 * 
 * @author ZHUZHU
 * @version 2010-7-9
 * @see DutyBean
 * @since 1.0
 */
@Entity(cache = true)
@Table(name = "T_CENTER_DUTYENTITY")
public class DutyBean implements DataClone<DutyBean>, Serializable
{
    @Id
    private String id = "";

    @Unique
    @Html(title = "名称", must = true, maxLength = 100)
    private String name = "";

    @Html(title = "税务证号", must = true, maxLength = 100)
    private String icp = "";

    @Html(title = "其他", type = Element.TEXTAREA, maxLength = 200)
    private String description = "";

    /**
     * Copy Constructor
     * 
     * @param dutyBean
     *            a <code>DutyBean</code> object
     */
    public DutyBean(DutyBean dutyBean)
    {
        this.id = dutyBean.id;
        this.name = dutyBean.name;
        this.icp = dutyBean.icp;
        this.description = dutyBean.description;
    }

    /**
     * default constructor
     */
    public DutyBean()
    {
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
     * @return the icp
     */
    public String getIcp()
    {
        return icp;
    }

    /**
     * @param icp
     *            the icp to set
     */
    public void setIcp(String icp)
    {
        this.icp = icp;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("DutyBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("name = ")
            .append(this.name)
            .append(TAB)
            .append("icp = ")
            .append(this.icp)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

    public DutyBean clones()
    {
        return new DutyBean(this);
    }
}
