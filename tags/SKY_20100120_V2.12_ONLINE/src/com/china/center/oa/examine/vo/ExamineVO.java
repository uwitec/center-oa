/**
 * File Name: ExamineVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.examine.bean.ExamineBean;


/**
 * ExamineVO
 * 
 * @author zhuzhu
 * @version 2009-1-7
 * @see ExamineVO
 * @since 1.0
 */
@Entity(inherit = true)
public class ExamineVO extends ExamineBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "createrId")
    private String createrName = "";
    
    @Relationship(relationField = "parentId")
    private String parentName = "";
    
    @Relationship(relationField = "locationId")
    private String locationName = "";

    public ExamineVO()
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
     * @return the parentName
     */
    public String getParentName()
    {
        return parentName;
    }

    /**
     * @param parentName the parentName to set
     */
    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    /**
     * @return the locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }
}
