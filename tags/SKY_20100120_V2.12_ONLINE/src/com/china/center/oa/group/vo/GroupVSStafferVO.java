/**
 * File Name: GroupVSStafferVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.group.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.group.vs.GroupVSStafferBean;


/**
 * GroupVSStafferVO
 * 
 * @author zhuzhu
 * @version 2009-4-7
 * @see GroupVSStafferVO
 * @since 1.0
 */
@Entity(inherit = true)
public class GroupVSStafferVO extends GroupVSStafferBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    /**
     * default constructor
     */
    public GroupVSStafferVO()
    {}

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }
}
