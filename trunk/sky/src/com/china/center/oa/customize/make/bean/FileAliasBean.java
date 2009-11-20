/**
 * File Name: FileAliasBean.java<br>
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
import com.china.center.annotation.Unique;


/**
 * FileAliasBean
 * 
 * @author ZHUZHU
 * @version 2009-10-18
 * @see FileAliasBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_MAKE_TOKEN_ALIAS")
public class FileAliasBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Unique
    private int tokenItemId = 0;

    private int aliasTokenId = 0;

    private int aliasId = 0;

    private int type = 0;

    /**
     * default constructor
     */
    public FileAliasBean()
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
     * @return the aliasTokenId
     */
    public int getAliasTokenId()
    {
        return aliasTokenId;
    }

    /**
     * @param aliasTokenId
     *            the aliasTokenId to set
     */
    public void setAliasTokenId(int aliasTokenId)
    {
        this.aliasTokenId = aliasTokenId;
    }

    /**
     * @return the aliasId
     */
    public int getAliasId()
    {
        return aliasId;
    }

    /**
     * @param aliasId
     *            the aliasId to set
     */
    public void setAliasId(int aliasId)
    {
        this.aliasId = aliasId;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String tab = ",";

        StringBuilder retValue = new StringBuilder();

        retValue.append("FileAliasBean ( ").append(super.toString()).append(tab).append("id = ").append(
            this.id).append(tab).append("tokenItemId = ").append(this.tokenItemId).append(tab).append(
            "aliasTokenId = ").append(this.aliasTokenId).append(tab).append("aliasId = ").append(
            this.aliasId).append(tab).append("type = ").append(this.type).append(tab).append(" )");

        return retValue.toString();
    }

}
