/**
 * File Name: DepartmentDAO.java<br>
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
import com.china.center.oa.publics.bean.PrincipalshipBean;


/**
 * DepartmentDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see PrincipalshipDAO
 * @since 1.0
 */
@Bean(name = "principalshipDAO")
public class PrincipalshipDAO extends BaseDAO2<PrincipalshipBean, PrincipalshipBean>
{
    public List<PrincipalshipBean> querySubPrincipalship(String id)
    {
        String sql = "select t1.* from T_CENTER_PRINCIPALSHIP t1, t_center_org t2 where t1.id = t2.subid and t2.PARENTID = ?";

        return jdbcOperation.queryForListBySql(sql, claz, id);
    }
}
