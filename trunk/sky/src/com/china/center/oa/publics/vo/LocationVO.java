/**
 * File Name: LocationVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.vo;

import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.oa.publics.bean.LocationBean;

/**
 * LocationVO
 * 
 * @author zhuzhu
 * @version 2008-11-8
 * @see LocationVO
 * @since 1.0
 */
@Entity(inherit = true, cache = true)
public class LocationVO extends LocationBean
{
    @Ignore
    private List<LocationVSCityVO> cityVOs = null;

    /**
     * default constructor
     */
    public LocationVO()
    {}

    /**
     * @return the cityVOs
     */
    public List<LocationVSCityVO> getCityVOs()
    {
        return cityVOs;
    }

    /**
     * @param cityVOs the cityVOs to set
     */
    public void setCityVOs(List<LocationVSCityVO> cityVOs)
    {
        this.cityVOs = cityVOs;
    }
}
