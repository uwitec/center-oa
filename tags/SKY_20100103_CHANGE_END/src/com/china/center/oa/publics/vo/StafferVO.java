/**
 * File Name: StafferVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * StafferVO
 * 
 * @author ZHUZHU
 * @version 2008-11-4
 * @see StafferVO
 * @since 1.0
 */
@Entity(inherit = true, cache = true)
public class StafferVO extends StafferBean
{
    @Relationship(relationField = "locationId", tagField = "name")
    private String locationName = "";

    @Relationship(relationField = "departmentId", tagField = "name")
    private String departmentName = "";

    @Relationship(relationField = "postId", tagField = "name")
    private String postName = "";

    @Relationship(relationField = "principalshipId", tagField = "name")
    private String principalshipName = "";

    @Ignore
    private String enc = "";

    /**
     * default constructor
     */
    public StafferVO()
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

    /**
     * @return the departmentName
     */
    public String getDepartmentName()
    {
        return departmentName;
    }

    /**
     * @param departmentName
     *            the departmentName to set
     */
    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    /**
     * @return the postName
     */
    public String getPostName()
    {
        return postName;
    }

    /**
     * @param postName
     *            the postName to set
     */
    public void setPostName(String postName)
    {
        this.postName = postName;
    }

    /**
     * @return the principalshipName
     */
    public String getPrincipalshipName()
    {
        return principalshipName;
    }

    /**
     * @param principalshipName
     *            the principalshipName to set
     */
    public void setPrincipalshipName(String principalshipName)
    {
        this.principalshipName = principalshipName;
    }

    /**
     * @return the enc
     */
    public String getEnc()
    {
        return enc;
    }

    /**
     * @param enc
     *            the enc to set
     */
    public void setEnc(String enc)
    {
        this.enc = enc;
    }
}
