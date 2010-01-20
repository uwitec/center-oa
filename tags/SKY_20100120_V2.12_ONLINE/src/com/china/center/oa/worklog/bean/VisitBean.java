/**
 * File Name: VisitBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.constant.WorkLogConstant;


/**
 * VisitBean
 * 
 * @author zhuzhu
 * @version 2009-2-15
 * @see VisitBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_WORKLOG_VISIT")
public class VisitBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    private String parentId = "";

    /**
     * $110
     */
    private int workType = WorkLogConstant.WORKTYPE_VISIT;

    private String workTypeName = "";

    private String targerId = "";

    private String targerName = "";

    /**
     * 新老属性(0新客户、1老客户) $c_newType
     */
    private int targerAtt = CustomerConstant.NEWTYPE_NEW;

    private String handPhone = "";

    private String tel = "";

    private String beginTime = "";

    private String endTime = "";

    /**
     * $111
     */
    private int result = 0;

    private String nextWork = "";

    private String nextDate = "";

    private String description = "";

    /**
     * default constructor
     */
    public VisitBean()
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
     * @return the parentId
     */
    public String getParentId()
    {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    /**
     * @return the workType
     */
    public int getWorkType()
    {
        return workType;
    }

    /**
     * @param workType
     *            the workType to set
     */
    public void setWorkType(int workType)
    {
        this.workType = workType;
    }

    /**
     * @return the targerId
     */
    public String getTargerId()
    {
        return targerId;
    }

    /**
     * @param targerId
     *            the targerId to set
     */
    public void setTargerId(String targerId)
    {
        this.targerId = targerId;
    }

    /**
     * @return the handPhone
     */
    public String getHandPhone()
    {
        return handPhone;
    }

    /**
     * @param handPhone
     *            the handPhone to set
     */
    public void setHandPhone(String handPhone)
    {
        this.handPhone = handPhone;
    }

    /**
     * @return the tel
     */
    public String getTel()
    {
        return tel;
    }

    /**
     * @param tel
     *            the tel to set
     */
    public void setTel(String tel)
    {
        this.tel = tel;
    }

    /**
     * @return the beginTime
     */
    public String getBeginTime()
    {
        return beginTime;
    }

    /**
     * @param beginTime
     *            the beginTime to set
     */
    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
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
     * @return the nextWork
     */
    public String getNextWork()
    {
        return nextWork;
    }

    /**
     * @param nextWork
     *            the nextWork to set
     */
    public void setNextWork(String nextWork)
    {
        this.nextWork = nextWork;
    }

    /**
     * @return the nextDate
     */
    public String getNextDate()
    {
        return nextDate;
    }

    /**
     * @param nextDate
     *            the nextDate to set
     */
    public void setNextDate(String nextDate)
    {
        this.nextDate = nextDate;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the workTypeName
     */
    public String getWorkTypeName()
    {
        return workTypeName;
    }

    /**
     * @param workTypeName
     *            the workTypeName to set
     */
    public void setWorkTypeName(String workTypeName)
    {
        this.workTypeName = workTypeName;
    }

    /**
     * @return the targerName
     */
    public String getTargerName()
    {
        return targerName;
    }

    /**
     * @param targerName
     *            the targerName to set
     */
    public void setTargerName(String targerName)
    {
        this.targerName = targerName;
    }

    /**
     * @return the targerAtt
     */
    public int getTargerAtt()
    {
        return targerAtt;
    }

    /**
     * @param targerAtt the targerAtt to set
     */
    public void setTargerAtt(int targerAtt)
    {
        this.targerAtt = targerAtt;
    }
}
