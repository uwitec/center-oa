/**
 * File Name: ProfitBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * 毛利率
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see ProfitBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_PROFIT")
public class ProfitBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private double profit = 0.0d;

    @Join(tagClass = CustomerBean.class)
    private String customerId = "";

    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    private String cityId = "";
    
    private int sellType = 0;

    private String provinceId = "";

    private String areaId = "";
    
    private String locationId = "";

    /**
     * 产生利润的日期
     */
    private String orgDate = "";

    /**
     * 进入数据库的时间
     */
    private String logTime = "";

    private String description = "";

    public ProfitBean()
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
     * @return the profit
     */
    public double getProfit()
    {
        return profit;
    }

    /**
     * @param profit
     *            the profit to set
     */
    public void setProfit(double profit)
    {
        this.profit = profit;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
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
     * @return the orgDate
     */
    public String getOrgDate()
    {
        return orgDate;
    }

    /**
     * @param orgDate
     *            the orgDate to set
     */
    public void setOrgDate(String orgDate)
    {
        this.orgDate = orgDate;
    }

    /**
     * @return the cityId
     */
    public String getCityId()
    {
        return cityId;
    }

    /**
     * @param cityId the cityId to set
     */
    public void setCityId(String cityId)
    {
        this.cityId = cityId;
    }

    /**
     * @return the provinceId
     */
    public String getProvinceId()
    {
        return provinceId;
    }

    /**
     * @param provinceId the provinceId to set
     */
    public void setProvinceId(String provinceId)
    {
        this.provinceId = provinceId;
    }

    /**
     * @return the areaId
     */
    public String getAreaId()
    {
        return areaId;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(String areaId)
    {
        this.areaId = areaId;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    /**
     * @return the sellType
     */
    public int getSellType()
    {
        return sellType;
    }

    /**
     * @param sellType the sellType to set
     */
    public void setSellType(int sellType)
    {
        this.sellType = sellType;
    }
}
