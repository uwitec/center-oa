/**
 * File Name: MakeFileBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * MakeFileBean
 * 
 * @author ZHUZHU
 * @version 2009-10-18
 * @see MakeFileBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_MAKE_FILE")
public class MakeFileBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    /**
     * makeId
     */
    @FK
    private String pid = "";

    private String name = "";

    /**
     * ∏Ωº”¿‡–Õ
     */
    private int type = 0;

    private int tokenId = 1;

    private int tokenItemId = 11;

    private String path = "";

    /**
     * default constructor
     */
    public MakeFileBean()
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
     * @return the pid
     */
    public String getPid()
    {
        return pid;
    }

    /**
     * @param pid
     *            the pid to set
     */
    public void setPid(String pid)
    {
        this.pid = pid;
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
     * @return the tokenId
     */
    public int getTokenId()
    {
        return tokenId;
    }

    /**
     * @param tokenId
     *            the tokenId to set
     */
    public void setTokenId(int tokenId)
    {
        this.tokenId = tokenId;
    }

    /**
     * @return the tokenItemId
     */
    public int getTokenItemId()
    {
        return tokenItemId;
    }

    /**
     * @param tokenItemId
     *            the tokenItemId to set
     */
    public void setTokenItemId(int tokenItemId)
    {
        this.tokenItemId = tokenItemId;
    }

    /**
     * @return the path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path)
    {
        this.path = path;
    }
}
