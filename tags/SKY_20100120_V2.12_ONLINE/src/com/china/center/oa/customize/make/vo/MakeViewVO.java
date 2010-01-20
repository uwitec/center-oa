/**
 * File Name: MakeViewVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-29<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.customize.make.bean.MakeViewBean;


/**
 * MakeViewVO
 * 
 * @author ZHUZHU
 * @version 2009-12-29
 * @see MakeViewVO
 * @since 1.0
 */
@Entity(inherit = true)
public class MakeViewVO extends MakeViewBean
{
    @Relationship(relationField = "makeId", tagField = "title")
    private String makeName = "";

    /**
     * default constructor
     */
    public MakeViewVO()
    {}

    /**
     * @return the makeName
     */
    public String getMakeName()
    {
        return makeName;
    }

    /**
     * @param makeName
     *            the makeName to set
     */
    public void setMakeName(String makeName)
    {
        this.makeName = makeName;
    }
}
