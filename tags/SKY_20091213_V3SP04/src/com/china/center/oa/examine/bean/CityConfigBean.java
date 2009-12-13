/**
 * File Name: CityConfigBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.oa.publics.bean.CityBean;


/**
 * 地市配置
 * 
 * @author zhuzhu
 * @version 2009-1-3
 * @see CityConfigBean
 * @since 1.0
 */
@Entity
@Table(name = "t_center_cityconfig")
public class CityConfigBean implements Serializable
{
    @Id
    @Join(tagClass = CityBean.class)
    private String cityId = "";

    /**
     * 成交量(年度)
     */
    private int business = 0;

    /**
     * 铺样率指标(指标，非年度)
     */
    private int bespread = 0;

    /**
     * 保留字段
     */
    private int resolve0 = 0;

    private int resolve1 = 0;

    private int resolve2 = 0;

    private int resolve3 = 0;

    private int resolve4 = 0;

    /**
     * @return the cityId
     */
    public String getCityId()
    {
        return cityId;
    }

    /**
     * @param cityId
     *            the cityId to set
     */
    public void setCityId(String cityId)
    {
        this.cityId = cityId;
    }

    /**
     * @return the business
     */
    public int getBusiness()
    {
        return business;
    }

    /**
     * @param business
     *            the business to set
     */
    public void setBusiness(int business)
    {
        this.business = business;
    }

    /**
     * @return the bespread
     */
    public int getBespread()
    {
        return bespread;
    }

    /**
     * @param bespread
     *            the bespread to set
     */
    public void setBespread(int bespread)
    {
        this.bespread = bespread;
    }

    /**
     * @return the resolve0
     */
    public int getResolve0()
    {
        return resolve0;
    }

    /**
     * @param resolve0
     *            the resolve0 to set
     */
    public void setResolve0(int resolve0)
    {
        this.resolve0 = resolve0;
    }

    /**
     * @return the resolve1
     */
    public int getResolve1()
    {
        return resolve1;
    }

    /**
     * @param resolve1
     *            the resolve1 to set
     */
    public void setResolve1(int resolve1)
    {
        this.resolve1 = resolve1;
    }

    /**
     * @return the resolve2
     */
    public int getResolve2()
    {
        return resolve2;
    }

    /**
     * @param resolve2
     *            the resolve2 to set
     */
    public void setResolve2(int resolve2)
    {
        this.resolve2 = resolve2;
    }

    /**
     * @return the resolve3
     */
    public int getResolve3()
    {
        return resolve3;
    }

    /**
     * @param resolve3
     *            the resolve3 to set
     */
    public void setResolve3(int resolve3)
    {
        this.resolve3 = resolve3;
    }

    /**
     * @return the resolve4
     */
    public int getResolve4()
    {
        return resolve4;
    }

    /**
     * @param resolve4
     *            the resolve4 to set
     */
    public void setResolve4(int resolve4)
    {
        this.resolve4 = resolve4;
    }

    /**
     * @return the resolve5
     */
    public int getResolve5()
    {
        return resolve5;
    }

    /**
     * @param resolve5
     *            the resolve5 to set
     */
    public void setResolve5(int resolve5)
    {
        this.resolve5 = resolve5;
    }

    public CityConfigBean()
    {}

    private int resolve5 = 0;
}
