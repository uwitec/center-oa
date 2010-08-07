/**
 * File Name: AbstarctFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.facade;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.vs.RoleAuthBean;


/**
 * AbstarctFacade
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see AbstarctFacade
 * @since 1.0
 */
public abstract class AbstarctFacade
{
    public boolean containAuth(User user, String authId)
    {
        if (authId.equals(AuthConstant.PUNLIC_AUTH))
        {
            return true;
        }

        List<RoleAuthBean> authList = user.getAuth();

        for (RoleAuthBean roleAuthBean : authList)
        {
            if (roleAuthBean.getAuthId().equals(authId))
            {
                return true;
            }
        }

        return false;
    }

    public void checkUser(User user)
        throws MYException
    {
        if (user == null)
        {
            throw new MYException("用户不存在");
        }

        if (user.getStatus() == PublicConstant.LOGIN_STATUS_LOCK)
        {
            throw new MYException("用户被锁定,没有任何操作权限");
        }
    }

    protected MYException noAuth()
    {
        return new MYException("没有权限");
    }
}
