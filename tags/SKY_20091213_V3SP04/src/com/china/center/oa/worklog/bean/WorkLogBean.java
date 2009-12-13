/**
 * File Name: WorkLogBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.oa.constant.WorkLogConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * WorkLogBean
 * 
 * @author zhuzhu
 * @version 2009-2-15
 * @see WorkLogBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_WORKLOG")
public class WorkLogBean implements Serializable
{
    @Id
    private String id = "";

    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    private String workDate = "";

    private String logTime = "";

    private int status = WorkLogConstant.WORKLOG_STATUS_INIT;

    private int type = 0;

    private int result = WorkLogConstant.WORKLOG_RESULT_COMMON;

    @Ignore
    private List<VisitBean> visits = null;

    public WorkLogBean()
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
     * @return the stafferId
     */
    public String getStafferId()
    {
        return stafferId;
    }

    /**
     * @param stafferId
     *            the stafferId to set
     */
    public void setStafferId(String stafferId)
    {
        this.stafferId = stafferId;
    }

    /**
     * @return the workDate
     */
    public String getWorkDate()
    {
        return workDate;
    }

    /**
     * @param workDate
     *            the workDate to set
     */
    public void setWorkDate(String workDate)
    {
        this.workDate = workDate;
    }

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
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
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
     * @return the result
     */
    public int getResult()
    {
        return result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(int result)
    {
        this.result = result;
    }

    /**
     * @return the visits
     */
    public List<VisitBean> getVisits()
    {
        return visits;
    }

    /**
     * @param visits
     *            the visits to set
     */
    public void setVisits(List<VisitBean> visits)
    {
        this.visits = visits;
    }
}
