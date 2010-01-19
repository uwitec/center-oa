/**
 * File Name: LoginHelper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-8-23<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean.helper;


import com.china.center.oa.customer.bean.ProviderUserBean;
import com.china.center.tools.CommonTools;
import com.china.center.tools.DecSecurity;
import com.china.center.tools.Security;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.enc.SoftKey;


/**
 * LoginHelper
 * 
 * @author ZHUZHU
 * @version 2009-8-23
 * @see LoginHelper
 * @since 1.0
 */
public abstract class LoginHelper
{
    /**
     * 加密锁的定制
     * 
     * @param pwkey
     * @param key
     * @param random
     * @return
     */
    public static String encRadomStr(String pwkey, String key, String random)
    {
        String md5key = Security.getSecurity(key);

        String deskeygen = md5key.substring(0, 4)
                           + md5key.substring(md5key.length() - 4, md5key.length());

        String realKey = DecSecurity.decrypt(pwkey, deskeygen);

        SoftKey mysoftkey = new SoftKey();

        return mysoftkey.StrEnc(random, realKey);
    }

    /**
     * getUser
     * 
     * @param puser
     * @return
     */
    public static User getUser(ProviderUserBean puser)
    {
        User user = new User();

        user.setName(puser.getName());

        user.setType(CommonTools.parseInt(puser.getRoleId()));

        user.setStafferId(puser.getId());

        // 供应商的ID
        user.setId(puser.getProvideId());

        user.setStafferName(puser.getName());

        RoleHelper.setRole(user, user.getType());

        return user;
    }
}
