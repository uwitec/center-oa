/**
 * File Name: PrincipalshipDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao.impl;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.constant.OrgConstant;
import com.china.center.oa.publics.dao.PrincipalshipDAO;


/**
 * PrincipalshipDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-6-21
 * @see PrincipalshipDAOImpl
 * @since 1.0
 */
public class PrincipalshipDAOImpl extends BaseDAO<PrincipalshipBean, PrincipalshipBean> implements PrincipalshipDAO
{
    public List<PrincipalshipBean> querySubPrincipalship(String id)
    {
        String sql = "select t1.* from T_CENTER_PRINCIPALSHIP t1, t_center_org t2 where t1.id = t2.subid and t2.PARENTID = ?";

        return jdbcOperation.queryForListBySql(sql, claz, id);
    }

    public List<PrincipalshipBean> listSYBSubPrincipalship()
    {
        return queryEntityBeansByFK(OrgConstant.ORG_BIG_DEPARTMENT);
    }
}
