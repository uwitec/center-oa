/**
 * File Name: ProviderUserDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.ProviderUserBean;


/**
 * ProviderUserDAO
 * 
 * @author ZHUZHU
 * @version 2009-12-27
 * @see ProviderUserDAO
 * @since 1.0
 */
@Bean(name = "providerUserDAO")
public class ProviderUserDAO extends BaseDAO2<ProviderUserBean, ProviderUserBean>
{
    /**
     * updatePassword
     * 
     * @param id
     * @param newPassword
     * @return
     */
    public boolean updatePassword(String id, String newPassword)
    {
        this.jdbcOperation.updateField("password", newPassword, id, claz);

        return true;
    }

    /**
     * updatePwkey
     * 
     * @param id
     * @param newPwkey
     * @return
     */
    public boolean updatePwkey(String id, String newPwkey)
    {
        this.jdbcOperation.updateField("pwkey", newPwkey, id, claz);

        return true;
    }
}
