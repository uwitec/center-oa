/*
 * File Name: Helper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin;


import javax.servlet.http.HttpServletRequest;

import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.constant.Constant;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public abstract class Helper
{
    public static String getCurrentLocationId(HttpServletRequest request)
    {
        return request.getSession().getAttribute(Constant.CURRENTLOCATIONID).toString();
    }

    public static User getUser(HttpServletRequest request)
    {
        return (User)request.getSession().getAttribute("user");
    }
}
