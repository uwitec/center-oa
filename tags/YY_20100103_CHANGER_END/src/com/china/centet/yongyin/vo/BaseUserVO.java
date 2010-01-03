/**
 *
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.BaseUser;


/**
 * @author Administrator
 */
@Entity(inherit = true, cache = true)
public class BaseUserVO extends BaseUser
{
    @Relationship(relationField = "locationID", tagField = "locationName")
    private String locationName = "";

    /**
     *
     */
    public BaseUserVO()
    {}

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
}
