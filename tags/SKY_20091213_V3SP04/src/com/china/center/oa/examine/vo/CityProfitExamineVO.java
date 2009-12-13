/**
 * File Name: CityProfitExamineVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-20<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.center.oa.examine.bean.CityProfitExamineBean;


/**
 * CityProfitExamineVO
 * 
 * @author zhuzhu
 * @version 2009-2-20
 * @see CityProfitExamineVO
 * @since 1.0
 */
@Entity(inherit = true)
public class CityProfitExamineVO extends CityProfitExamineBean
{
    @Relationship(relationField = "cityId")
    private String cityName = "";
    
    @Ignore
    private String stafferName = "";

    /**
     * default constructor
     */
    public CityProfitExamineVO()
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

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }
}
