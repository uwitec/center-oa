/**
 * File Name: ExamineBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;
import com.china.center.annotation.enums.Element;
import com.china.center.annotation.enums.JoinType;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * ExamineBean
 * 
 * @author zhuzhu
 * @version 2009-1-7
 * @see ExamineBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_EXAMINE")
public class ExamineBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    @Html(title = "父考核", name = "parentName")
    @Join(tagClass = ExamineBean.class, alias = "pExamineBean", type = JoinType.LEFT)
    private String parentId = "0";

    @Html(title = "考核名称", must = true, maxLength = 40)
    private String name = "";

    @Unique(dependFields = {"year", "attType"})
    @Join(tagClass = StafferBean.class, alias = "StafferBean1", type = JoinType.LEFT)
    @Html(title = "考核人", must = true, name = "stafferName")
    private String stafferId = "";

    @Join(tagClass = StafferBean.class, alias = "StafferBean2")
    private String createrId = "";

    /**
     * 0:分公司经理考核 1:部门考核 2:个人考核
     */
    @Html(title = "考核类型", must = true, type = Element.SELECT)
    private int attType = ExamineConstant.EXAMINE_ATTTYPE_PERSONAL;

    /**
     * 总的利润
     */
    private double totalProfit = 0.0d;

    /**
     * 考核所在的区域
     */
    @Html(title = "分公司", must = true, type = Element.SELECT)
    @Join(tagClass = LocationBean.class)
    private String locationId = "";

    @Html(title = "考核分类", must = true, type = Element.SELECT)
    private int type = ExamineConstant.EXAMINE_TYPE_TER;

    private int status = ExamineConstant.EXAMINE_STATUS_INIT;

    private int result = -1;
    
    /**
     * 0:非抽象 1:抽象(仅仅针对终端考评有意义)
     */
    private int abs = ExamineConstant.EXAMINE_ABS_FALSE;

    @Html(title = "考核年度", must = true, type = Element.SELECT)
    private int year = 0;

    private String logTime = "";

    private String beginDate = "";

    private String endDate = "";

    /**
     * 评语
     */
    private String remark = "";

    @Html(title = "其他", type = Element.TEXTAREA, maxLength = 200)
    private String description = "";

    public ExamineBean()
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
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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
     * @return the createrId
     */
    public String getCreaterId()
    {
        return createrId;
    }

    /**
     * @param createrId
     *            the createrId to set
     */
    public void setCreaterId(String createrId)
    {
        this.createrId = createrId;
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
     * @return the beginDate
     */
    public String getBeginDate()
    {
        return beginDate;
    }

    /**
     * @param beginDate
     *            the beginDate to set
     */
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate()
    {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
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
     * @return the year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(int year)
    {
        this.year = year;
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
     * @return the remark
     */
    public String getRemark()
    {
        return remark;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    /**
     * @return the attType
     */
    public int getAttType()
    {
        return attType;
    }

    /**
     * @param attType
     *            the attType to set
     */
    public void setAttType(int attType)
    {
        this.attType = attType;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
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
     * @return the totalProfit
     */
    public double getTotalProfit()
    {
        return totalProfit;
    }

    /**
     * @param totalProfit
     *            the totalProfit to set
     */
    public void setTotalProfit(double totalProfit)
    {
        this.totalProfit = totalProfit;
    }

    /**
     * @return the abs
     */
    public int getAbs()
    {
        return abs;
    }

    /**
     * @param abs the abs to set
     */
    public void setAbs(int abs)
    {
        this.abs = abs;
    }
}
