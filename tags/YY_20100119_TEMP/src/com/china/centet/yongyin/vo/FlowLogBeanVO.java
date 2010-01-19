/*
 * File Name: OutLogVO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-13
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.centet.yongyin.bean.FlowLogBean;


/**
 * »’÷æVO
 * 
 * @author zhuzhu
 * @version 2008-1-13
 * @see
 * @since
 */
@Entity(inherit = true)
public class FlowLogBeanVO extends FlowLogBean
{
    private String oprModeName = "";

    private String preStatusName = "";

    private String afterStatusName = "";

    /**
     * default constructor
     */
    public FlowLogBeanVO()
    {}

    /**
     * @return the oprModeName
     */
    public String getOprModeName()
    {
        return oprModeName;
    }

    /**
     * @param oprModeName
     *            the oprModeName to set
     */
    public void setOprModeName(String oprModeName)
    {
        this.oprModeName = oprModeName;
    }

    /**
     * @return the preStatusName
     */
    public String getPreStatusName()
    {
        return preStatusName;
    }

    /**
     * @return the afterStatusName
     */
    public String getAfterStatusName()
    {
        return afterStatusName;
    }

    /**
     * @param preStatusName
     *            the preStatusName to set
     */
    public void setPreStatusName(String preStatusName)
    {
        this.preStatusName = preStatusName;
    }

    /**
     * @param afterStatusName
     *            the afterStatusName to set
     */
    public void setAfterStatusName(String afterStatusName)
    {
        this.afterStatusName = afterStatusName;
    }

}
