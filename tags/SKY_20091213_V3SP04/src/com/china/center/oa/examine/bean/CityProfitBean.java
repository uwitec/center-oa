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
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.oa.publics.bean.CityBean;


/**
 * ÇøÓòÃ«ÀûÂÊ
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see CityProfitBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_CITYPROFIT")
public class CityProfitBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private double profit = 0.0d;

    @FK
    @Join(tagClass = CityBean.class)
    private String cityId = "";
    
    private int month = 0;

    public CityProfitBean()
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
     * @return the profit
     */
    public double getProfit()
    {
        return profit;
    }

    /**
     * @param profit the profit to set
     */
    public void setProfit(double profit)
    {
        this.profit = profit;
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
     * @return the month
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month)
    {
        this.month = month;
    }
}
