/**
 * File Name: CityConfigVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.examine.bean.CityConfigBean;


/**
 * CityConfigVO
 * 
 * @author zhuzhu
 * @version 2009-1-3
 * @see CityConfigVO
 * @since 1.0
 */
@Entity(inherit = true)
public class CityConfigVO extends CityConfigBean
{
    @Relationship(relationField = "cityId")
    private String cityName = "";
    
    @Relationship(relationField = "bespread")
    private String bespreadName = "";

    public CityConfigVO()
    {}

    /**
     * @return the cityName
     */
    public String getCityName()
    {
        return cityName;
    }

    /**
     * @param cityName
     *            the cityName to set
     */
    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getBespreadName()
    {
        return bespreadName;
    }

    public void setBespreadName(String bespreadName)
    {
        this.bespreadName = bespreadName;
    }
}
