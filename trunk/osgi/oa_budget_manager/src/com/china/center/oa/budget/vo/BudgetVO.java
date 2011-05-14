/**
 * File Name: BudgetVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.vo;


import java.util.List;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.budget.bean.BudgetBean;


/**
 * BudgetVO
 * 
 * @author ZHUZHU
 * @version 2008-12-2
 * @see BudgetVO
 * @since 1.0
 */
@Entity(inherit = true)
public class BudgetVO extends BudgetBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "locationId")
    private String locationName = "";

    @Relationship(relationField = "parentId")
    private String parentName = "";

    @Ignore
    private String stotal = "";

    @Ignore
    private String srealMonery = "";

    @Ignore
    private List<BudgetItemVO> itemVOs = null;

    public BudgetVO()
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
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

    /**
     * @return the locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param locationName
     *            the locationName to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * @return the itemVOs
     */
    public List<BudgetItemVO> getItemVOs()
    {
        return itemVOs;
    }

    /**
     * @param itemVOs
     *            the itemVOs to set
     */
    public void setItemVOs(List<BudgetItemVO> itemVOs)
    {
        this.itemVOs = itemVOs;
    }

    /**
     * @return the parentName
     */
    public String getParentName()
    {
        return parentName;
    }

    /**
     * @param parentName
     *            the parentName to set
     */
    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    /**
     * @return the stotal
     */
    public String getStotal()
    {
        return stotal;
    }

    /**
     * @param stotal
     *            the stotal to set
     */
    public void setStotal(String stotal)
    {
        this.stotal = stotal;
    }

    /**
     * @return the srealMonery
     */
    public String getSrealMonery()
    {
        return srealMonery;
    }

    /**
     * @param srealMonery
     *            the srealMonery to set
     */
    public void setSrealMonery(String srealMonery)
    {
        this.srealMonery = srealMonery;
    }
}
