/**
 * File Name: CustomerBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.bean;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;


/**
 * CustomerBean
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see CustomerBean
 * @since 1.0
 */
@Entity(inherit = true)
@Table(name = "T_CENTER_CUSTOMER_NOW")
public class CustomerBean extends AbstractBean
{
    @Id
    private String id = "";

    @Unique
    @Html(title = "¿Í»§Ãû³Æ", must = true, maxLength = 80)
    private String name = "";

    /**
     * default constructor
     */
    public CustomerBean()
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
