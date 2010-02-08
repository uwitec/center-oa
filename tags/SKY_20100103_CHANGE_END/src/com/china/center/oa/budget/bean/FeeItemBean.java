/**
 * File Name: FeeItemBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;


/**
 * FeeItemBean
 * 
 * @author zhuzhu
 * @version 2008-12-7
 * @see FeeItemBean
 * @since 1.0
 */
@Entity(cache = true)
@Table(name = "T_CENTER_FEEITEM")
public class FeeItemBean implements Serializable
{
    @Id
    private String id = "";

    @Unique
    @Html(title = "Ԥ����", must = true, maxLength = 100)
    private String name = "";

    public FeeItemBean()
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
}