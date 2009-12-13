/**
 * File Name: User.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-11-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics;


import java.util.List;

import com.china.center.oa.publics.vs.RoleAuthBean;


/**
 * User
 * 
 * @author ZHUZHU
 * @version 2009-11-14
 * @see User
 * @since 1.0
 */
public interface User
{
    /**
     * getId
     * 
     * @return
     */
    String getId();

    /**
     * getName
     * 
     * @return
     */
    String getName();

    /**
     * @return the password
     */
    String getPassword();

    /**
     * @return the stafferId
     */
    String getStafferId();

    /**
     * @return the locationId
     */
    String getLocationId();

    /**
     * @return the roleId
     */
    String getRoleId();

    /**
     * @return the status
     */
    int getStatus();

    /**
     * @return the fail
     */
    int getFail();

    /**
     * @return the loginTime
     */
    String getLoginTime();

    /**
     * @return the auth
     */
    List<RoleAuthBean> getAuth();

    /**
     * getStafferName
     * 
     * @return
     */
    String getStafferName();

    /**
     * getRoleName
     * 
     * @return
     */
    String getRoleName();

    /**
     * getLocationName
     * 
     * @return
     */
    String getLocationName();
}
