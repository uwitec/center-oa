/**
 * File Name: CityProfitVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-30<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.examine.bean.CityProfitBean;


/**
 * CityProfitVO
 * 
 * @author zhuzhu
 * @version 2009-1-30
 * @see
 * @since
 */
@Entity(inherit = true)
public class CityProfitVO extends CityProfitBean
{
    @Relationship(relationField = "cityId")
    private String cityName = "";

    /**
     * default constructor
     */
    public CityProfitVO()
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
}
