/**
 * File Name: ProfitExamineBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.oa.publics.bean.CityBean;


/**
 * øº∆¿--«¯”Ú¿˚»Û±Ì
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see CityProfitExamineBean
 * @since 1.0
 */
@Entity(inherit = true)
@Table(name = "T_CENTER_EXAMINE_CPROFIT")
public class CityProfitExamineBean extends AbstractExamineItem
{
    @Id
    private String id = "";

    private double realValue = 0.0d;

    private double planValue = 0.0d;
    
    @Join(tagClass = CityBean.class)
    private String cityId = "";
    
    /**
     * default constructor
     */
    public CityProfitExamineBean()
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
     * @return the realValue
     */
    public double getRealValue()
    {
        return realValue;
    }

    /**
     * @param realValue
     *            the realValue to set
     */
    public void setRealValue(double realValue)
    {
        this.realValue = realValue;
    }

    /**
     * @return the planValue
     */
    public double getPlanValue()
    {
        return planValue;
    }

    /**
     * @param planValue
     *            the planValue to set
     */
    public void setPlanValue(double planValue)
    {
        this.planValue = planValue;
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
}
