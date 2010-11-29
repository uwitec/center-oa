/*
 * File Name: TransportBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-14
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.JCheck;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.Element;


/**
 * 运输方式
 * 
 * @author ZHUZHU
 * @version 2008-1-14
 * @see
 * @since
 */
@Entity(name = "运输方式")
@Table(name = "T_CENTER_TRANSPORT")
public class TransportBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "名称", must = true, maxLength = 10, oncheck = JCheck.NOT_NONE)
    private String name = "";

    private int type = 0;

    @Html(title = "所属分类", type = Element.SELECT, must = true, oncheck = JCheck.NOT_NONE)
    private String parent = "0";

    /**
     * default constructor
     */
    public TransportBean()
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
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @return the parent
     */
    public String getParent()
    {
        return parent;
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
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(String parent)
    {
        if (parent != null)
        {
            this.parent = parent;
        }
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
            .append("TransportBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("name = ")
            .append(this.name)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("parent = ")
            .append(this.parent)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
