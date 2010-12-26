/**
 * File Name: InsVSOutBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.vs;


import java.io.Serializable;

import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;


/**
 * InsVSOutBean
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see InsVSOutBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_VS_INSOUT")
public class InsVSOutBean implements Serializable
{
    @Id
    private String id = "";

    @FK(index = AnoConstant.FK_FIRST)
    private String insId = "";

    @FK(index = AnoConstant.FK_DEFAULT)
    private String outId = "";

    /**
     * default constructor
     */
    public InsVSOutBean()
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
     * @return the insId
     */
    public String getInsId()
    {
        return insId;
    }

    /**
     * @param insId
     *            the insId to set
     */
    public void setInsId(String insId)
    {
        this.insId = insId;
    }

    /**
     * @return the outId
     */
    public String getOutId()
    {
        return outId;
    }

    /**
     * @param outId
     *            the outId to set
     */
    public void setOutId(String outId)
    {
        this.outId = outId;
    }

}
