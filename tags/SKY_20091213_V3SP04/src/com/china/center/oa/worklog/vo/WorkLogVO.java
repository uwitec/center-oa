/**
 * File Name: WorkLogVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.center.oa.worklog.bean.WorkLogBean;


/**
 * WorkLogVO
 * 
 * @author zhuzhu
 * @version 2009-2-16
 * @see WorkLogVO
 * @since 1.0
 */
@Entity(inherit = true)
public class WorkLogVO extends WorkLogBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";
    
    @Ignore
    private String week = "";

    /**
     * default constructor
     */
    public WorkLogVO()
    {
    }

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

    /**
     * @return the week
     */
    public String getWeek()
    {
        return week;
    }

    /**
     * @param week the week to set
     */
    public void setWeek(String week)
    {
        this.week = week;
    }
}
