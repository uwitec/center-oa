package com.china.center.oa.publics.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.UserBean;
import com.china.center.oa.publics.vo.UserVO;
import com.china.center.tools.ListTools;


/**
 * @author zhu
 * @version 2006-7-16
 * @see UserDAO
 * @since
 */
@Bean(name = "userDAO")
public class UserDAO extends BaseDAO2<UserBean, UserVO>
{
    public UserVO findUserByName(String name)
    {
        return this.jdbcOperation.find(name, "name", this.clazVO);
    }

    public User findFirstUserByStafferId(String stafferId)
    {
        List<UserVO> list = this.queryEntityVOsByFK(stafferId);

        if (ListTools.isEmptyOrNull(list))
        {
            return null;
        }

        return list.get(0);
    }

    public boolean updatePassword(String id, String password)
    {
        int i = jdbcOperation.updateField("password", password, id, this.claz);

        return i != 0;
    }

    public boolean updateStatus(String id, int status)
    {
        int i = jdbcOperation.updateField("status", status, id, this.claz);

        return i != 0;
    }

    /**
     * updateLocation
     * 
     * @param id
     * @param locationId
     * @return
     */
    public boolean updateLocation(String id, String locationId)
    {
        int i = jdbcOperation.updateField("locationId", locationId, id, this.claz);

        return i != 0;
    }

    public boolean updateFail(String id, int fail)
    {
        int i = jdbcOperation.updateField("fail", fail, id, this.claz);

        return i != 0;
    }

    public boolean updateLogTime(String id, String logTime)
    {
        int i = jdbcOperation.updateField("loginTime", logTime, id, this.claz);

        return i != 0;
    }

    public int countByLocationId(String locationId)
    {
        return jdbcOperation.queryForInt("where locationId = ? ", this.claz, locationId);
    }

    public int countByRoleId(String roleId)
    {
        return jdbcOperation.queryForInt("where roleId = ? ", this.claz, roleId);
    }

    public int countByStafferId(String stafferId)
    {
        return jdbcOperation.queryForInt("where stafferId = ? ", this.claz, stafferId);
    }

}
