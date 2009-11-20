/**
 * File Name: VisitWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-7-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.wrap;


import com.china.center.oa.worklog.bean.VisitBean;


/**
 * VisitWrap
 * 
 * @author zhuzhu
 * @version 2009-7-16
 * @see VisitWrap
 * @since 1.0
 */
public class VisitWrap extends VisitBean
{
    private String logTime = "";

    private String stafferName = "";

    /**
     * default constructor
     */
    public VisitWrap()
    {}

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime
     *            the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

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
