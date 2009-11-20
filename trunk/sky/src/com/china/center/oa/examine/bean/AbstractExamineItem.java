/**
 * File Name: AbstractCustomerRxamine.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import java.io.Serializable;

import com.china.center.annotation.FK;
import com.china.center.oa.constant.ExamineConstant;


/**
 * AbstractCustomerRxamine
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see AbstractExamineItem
 * @since 1.0
 */
public abstract class AbstractExamineItem implements Serializable
{
    /**
     * 0:init 1：刚好 2：超过预期 3：没有达到
     */
    private int result = ExamineConstant.EXAMINE_RESULT_INIT;

    /**
     * 考评状态
     */
    private int status = ExamineConstant.EXAMINE_ITEM_STATUS_INIT;
    
    /**
     * 步长的批次
     */
    private int step = 0;


    @FK
    private String parentId = "";

    private String beginTime = "";

    private String endTime = "";

    private String description = "";

    public AbstractExamineItem()
    {}

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
     * @return the step
     */
    public int getStep()
    {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(int step)
    {
        this.step = step;
    }
}
