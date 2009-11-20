/**
 * File Name: MakeTokenBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.center.oa.constant.MakeConstant;


/**
 * MakeTokenBean
 * 
 * @author ZHUZHU
 * @version 2009-10-11
 * @see MakeTokenBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_MAKE_TOKEN")
public class MakeTokenBean implements Serializable
{
    @Id
    private String id = "";

    private String name = "";

    /**
     * 0:NO 1:YES
     */
    private int ends = MakeConstant.END_TOKEN_NO;

    /**
     * default constructor
     */
    public MakeTokenBean()
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
     * @return the ends
     */
    public int getEnds()
    {
        return ends;
    }

    /**
     * @param ends
     *            the ends to set
     */
    public void setEnds(int ends)
    {
        this.ends = ends;
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

        retValue.append("MakeTokenBean ( ").append(super.toString()).append(tab).append("id = ").append(
            this.id).append(tab).append("name = ").append(this.name).append(tab).append("ends = ").append(
            this.ends).append(tab).append(" )");

        return retValue.toString();
    }

}
