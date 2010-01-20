/**
 * File Name: AuthDAo.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.publics.bean.AuthBean;


/**
 * AuthDAo
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see AuthDAO
 * @since 1.0
 */
@Bean(name = "authDAO")
public class AuthDAO extends BaseDAO2<AuthBean, AuthBean>
{
    /**
     * 查询区域权限
     * 
     * @return
     */
    public List<AuthBean> listLocationAuth()
    {
        return this.jdbcOperation.queryForList("where type = ? order by LEVEL, id", claz,
            PublicConstant.AUTH_TYPE_LOCATION);
    }
}
