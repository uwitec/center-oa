/**
 * File Name: UserVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.vo;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.publics.bean.UserBean;


/**
 * UserVO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see UserVO
 * @since 1.0
 */
@Entity(inherit = true)
public class UserVO extends UserBean implements User
{
    @Relationship(relationField = "stafferId", tagField = "name")
    private String stafferName = "";

    @Relationship(relationField = "roleId")
    private String roleName = "";

    @Relationship(relationField = "locationId")
    private String locationName = "";

    public UserVO()
    {}

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

    /**
     * @return the roleName
     */
    public String getRoleName()
    {
        return roleName;
    }

    /**
     * @param roleName
     *            the roleName to set
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

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

    public List getAuth()
    {
        return super.getAuth();
    }
}
