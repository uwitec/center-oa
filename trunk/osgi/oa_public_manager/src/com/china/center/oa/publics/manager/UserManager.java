/**
 * File Name: UserManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import java.util.Map;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.bean.UserBean;


/**
 * UserManager
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see UserManager
 * @since 1.0
 */
public interface UserManager
{
    boolean addBean(User user, UserBean bean)
        throws MYException;

    boolean updateBean(User user, UserBean bean)
        throws MYException;

    boolean delBean(User user, String id)
        throws MYException;

    User findUser(String id)
        throws MYException;

    boolean containAuth(User user, String... authId);

    boolean containAuth(String id, String authId)
        throws MYException;

    boolean updatePassword(String id, String password);

    boolean updateStatus(String id, int status);

    boolean updateLocation(String id, String locationId);

    boolean updateFail(String id, int fail);

    boolean updateLogTime(String id, String logTime);

    /**
     * 过滤不正常的职员
     * 
     * @param locationId
     * @return
     */
    Map queryStafferAndRoleByLocationId(String locationId);
}