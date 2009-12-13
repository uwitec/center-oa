/**
 * File Name: RoleAuthBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.vs;


import java.io.Serializable;

import com.china.center.annosql.constant.AnoConstant;
import com.china.center.annotation.CacheRelation;
import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;
import com.china.center.oa.publics.bean.CityBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.ProvinceBean;


/**
 * RoleAuthBean
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see
 * @since
 */
@Entity(cache = true)
@Table(name = "T_CENTER_VS_LOCATIONCITY")
@CacheRelation(relation = CityBean.class)
public class LocationVSCityBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    @Join(tagClass = LocationBean.class)
    private String locationId = "";

    @Join(tagClass = ProvinceBean.class)
    private String provinceId = "";

    @Unique
    @FK(index = AnoConstant.FK_FIRST)
    @Join(tagClass = CityBean.class)
    private String cityId = "";

    /**
     * default constructor
     */
    public LocationVSCityBean()
    {}

    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if ( ! (obj instanceof LocationVSCityBean))
        {
            return false;
        }

        LocationVSCityBean cobj = (LocationVSCityBean)obj;

        return cobj.getCityId().equals(cityId);
    }

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
     * @return the provinceId
     */
    public String getProvinceId()
    {
        return provinceId;
    }

    /**
     * @param provinceId
     *            the provinceId to set
     */
    public void setProvinceId(String provinceId)
    {
        this.provinceId = provinceId;
    }

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
}
