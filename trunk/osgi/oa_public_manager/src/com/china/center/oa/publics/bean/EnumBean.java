/**
 * File Name: EnumBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Column;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.clone.DataClone;


/**
 * EnumBean
 * 
 * @author ZHUZHU
 * @version 2008-11-9
 * @see EnumBean
 * @since 1.0
 */
@Entity(cache = true)
@Table(name = "T_CENTER_ENUM")
public class EnumBean implements DataClone<EnumBean>, Serializable
{
    @Id
    private String id = "";

    @FK
    private int type = 0;

    @Column(name = "keyss")
    private String key = "";

    @Column(name = "val")
    private String value = "";

    /**
     * Copy Constructor
     * 
     * @param enumBean
     *            a <code>EnumBean</code> object
     */
    public EnumBean(EnumBean enumBean)
    {
        this.id = enumBean.id;
        this.type = enumBean.type;
        this.key = enumBean.key;
        this.value = enumBean.value;
    }

    public EnumBean()
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
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    public EnumBean clones()
    {
        return new EnumBean(this);
    }
}
