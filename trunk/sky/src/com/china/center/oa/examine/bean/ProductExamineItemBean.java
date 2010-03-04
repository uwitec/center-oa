/**
 * File Name: ProductExamineItemBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;

import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.publics.bean.StafferBean;

/**
 * ProductExamineItemBean(已经废弃)
 * 
 * @author ZHUZHU
 * @version 2009-2-14
 * @see ProductExamineItemBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_PROSTAFFER")
public class ProductExamineItemBean implements Serializable
{
    @Id
    private String id = "";
    
    @FK
    private String pid = "";
    
    @Join(tagClass = StafferBean.class)
    private String stafferId = "";
    
    private int planValue = 0;
    
    private int realValue = 0;
    
    /**
     * 0:init 1：刚好 2：超过预期 3：没有达到
     */
    private int result = ExamineConstant.EXAMINE_RESULT_INIT;
    
    /**
     * 考评状态
     */
    private int status = ExamineConstant.EXAMINE_ITEM_STATUS_INIT;

    public ProductExamineItemBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the pid
     */
    public String getPid()
    {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid)
    {
        this.pid = pid;
    }

    /**
     * @return the stafferId
     */
    public String getStafferId()
    {
        return stafferId;
    }

    /**
     * @param stafferId the stafferId to set
     */
    public void setStafferId(String stafferId)
    {
        this.stafferId = stafferId;
    }

    /**
     * @return the planValue
     */
    public int getPlanValue()
    {
        return planValue;
    }

    /**
     * @param planValue the planValue to set
     */
    public void setPlanValue(int planValue)
    {
        this.planValue = planValue;
    }

    /**
     * @return the realValue
     */
    public int getRealValue()
    {
        return realValue;
    }

    /**
     * @param realValue the realValue to set
     */
    public void setRealValue(int realValue)
    {
        this.realValue = realValue;
    }

    /**
     * @return the result
     */
    public int getResult()
    {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(int result)
    {
        this.result = result;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }
}
