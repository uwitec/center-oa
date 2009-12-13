/**
 * File Name: GroupDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.group.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.GroupConstant;
import com.china.center.oa.group.bean.GroupBean;


/**
 * GroupDAO
 * 
 * @author zhuzhu
 * @version 2009-4-7
 * @see GroupDAO
 * @since 1.0
 */
@Bean(name = "groupDAO")
public class GroupDAO extends BaseDAO2<GroupBean, GroupBean>
{
    public int countByNameAndStafferId(String name, String stafferId)
    {
        return super.countByCondition("where name = ? and stafferId = ?", name, stafferId);
    }

    /**
     * listPublicGroup
     * @return
     */
    public List<GroupBean> listPublicGroup()
    {
        return this.jdbcOperation.queryForList("where type = ?", claz,
            GroupConstant.GROUP_TYPE_PUBLIC);
    }
}
