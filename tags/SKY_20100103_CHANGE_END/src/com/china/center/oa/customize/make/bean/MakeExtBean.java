/**
 * File Name: MakeExtBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * MakeExtBean
 * 
 * @author ZHUZHU
 * @version 2009-10-18
 * @see MakeExtBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_MAKE_EXT")
public class MakeExtBean implements Serializable
{
    @Id
    private String id = "";

    /**
     * default constructor
     */
    public MakeExtBean()
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
}
