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
import com.china.center.oa.publics.bean.RoleBean;
import com.china.center.oa.publics.vo.RoleVO;


/**
 * DepartmentDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see RoleDAO
 * @since 1.0
 */
@Bean(name = "roleDAO")
public class RoleDAO extends BaseDAO2<RoleBean, RoleVO>
{
    /**
     * 查询区域下的角色
     * 
     * @param locationId
     * @return
     */
    public List<RoleBean> queryRoleByLocationId(String locationId)
    {
        return this.jdbcOperation.queryForList("where locationId = ?", claz, locationId);
    }
}
