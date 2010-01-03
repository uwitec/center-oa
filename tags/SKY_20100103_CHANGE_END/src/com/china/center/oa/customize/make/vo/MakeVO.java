/**
 * File Name: MakeVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.customize.make.bean.MakeBean;


/**
 * MakeVO
 * 
 * @author ZHUZHU
 * @version 2009-10-11
 * @see MakeVO
 * @since 1.0
 */
@Entity(inherit = true)
public class MakeVO extends MakeBean
{
    @Relationship(relationField = "status")
    private String statusName = "";

    @Relationship(relationField = "position")
    private String positionName = "";

    @Relationship(relationField = "createrId")
    private String createrName = "";

    @Relationship(relationField = "handerId")
    private String handerName = "";

    /**
     * default constructor
     */
    public MakeVO()
    {}

    /**
     * @return the statusName
     */
    public String getStatusName()
    {
        return statusName;
    }

    /**
     * @param statusName
     *            the statusName to set
     */
    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    /**
     * @return the positionName
     */
    public String getPositionName()
    {
        return positionName;
    }

    /**
     * @param positionName
     *            the positionName to set
     */
    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }

    /**
     * @return the createrName
     */
    public String getCreaterName()
    {
        return createrName;
    }

    /**
     * @param createrName
     *            the createrName to set
     */
    public void setCreaterName(String createrName)
    {
        this.createrName = createrName;
    }

    /**
     * @return the handerName
     */
    public String getHanderName()
    {
        return handerName;
    }

    /**
     * @param handerName
     *            the handerName to set
     */
    public void setHanderName(String handerName)
    {
        this.handerName = handerName;
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

        retValue.append("MakeVO ( ").append(super.toString()).append(tab).append("statusName = ").append(
            this.statusName).append(tab).append("positionName = ").append(this.positionName).append(
            tab).append("createrName = ").append(this.createrName).append(tab).append(
            "handerName = ").append(this.handerName).append(tab).append(" )");

        return retValue.toString();
    }

}
