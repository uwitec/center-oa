/*
 * File Name: Helper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.center.oa.helper;


import javax.servlet.http.HttpServletRequest;

import com.china.center.oa.constant.OAConstant;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.constant.StafferConstant;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.vo.UserVO;


/**
 * Helper
 * 
 * @author ZHUZHU
 * @version 2007-12-15
 * @see
 * @since
 */
public abstract class Helper
{
    public static String getCurrentLocationId(HttpServletRequest request)
    {
        return request.getSession().getAttribute(OAConstant.CURRENTLOCATIONID).toString();
    }

    public static User getUser(HttpServletRequest request)
    {
        return (User)request.getSession().getAttribute("user");
    }

    public static User getSystemUser()
    {
        UserVO system = new UserVO();

        system.setId("0");

        system.setName("system");

        system.setStafferName("system");

        system.setRoleName("system");

        system.setLocationName("system");

        system.setLocationId(PublicConstant.VIRTUAL_LOCATION);

        system.setStafferId(StafferConstant.SUPER_STAFFER);

        return system;
    }
}
