/*
 * 文件名：ProductionDAOImpl.java
 * 版权：Copyright by www.centerchina.com
 * 描述：
 * 修改人：zhu
 * 修改时间：2006-7-16
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.china.centet.yongyin.dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.tools.ListTools;
import com.china.centet.yongyin.bean.BaseUser;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.RoleHelper;
import com.china.centet.yongyin.vo.BaseUserVO;


/**
 * @author zhu
 * @version 2006-7-16
 * @see UserDAO
 * @since
 */

public class UserDAO extends BaseDAO<BaseUser, BaseUserVO>
{
    private JdbcOperation jdbcOperation2 = null;

    /**
     * findFirstUserByStafferId
     * 
     * @param stafferId
     * @return
     */
    public User findFirstUserByStafferId(String stafferId)
    {
        List<BaseUser> list = this.queryEntityBeansByFK(stafferId);

        if (ListTools.isEmptyOrNull(list))
        {
            return null;
        }

        User user = new User();

        BaseUser baseUser = list.get(0);

        user.setId(baseUser.getId());
        user.setStafferId(baseUser.getStafferId());
        user.setStafferName(baseUser.getStafferName());
        user.setStatus(baseUser.getStatus());
        user.setType(baseUser.getRole());
        user.setLocationID(baseUser.getLocationID());

        RoleHelper.setRole(user, baseUser.getRole());

        return user;
    }

    public User findUserByLoginName(final String loginName)
    {
        final User user = new User();

        StringBuffer buffer = new StringBuffer();
        buffer.append("Select * From t_center_user Where name = ? ");

        jdbcOperation.query(buffer.toString(), new PreparedStatementSetter()
        {
            public void setValues(PreparedStatement ps)
                throws SQLException
            {
                // 设置参数
                ps.setString(1, loginName);
            }
        }, new RowCallbackHandler()
        {
            public void processRow(ResultSet rst)
                throws SQLException
            {
                getUser(user, rst);
            }
        });

        if ("".equals(user.getPassword()))
        {
            return null;
        }

        return user;
    }

    public User findUserById(final String id)
    {
        final User user = new User();

        StringBuffer buffer = new StringBuffer();
        buffer.append("Select * From t_center_user Where id = ? ");

        jdbcOperation.query(buffer.toString(), new PreparedStatementSetter()
        {
            public void setValues(PreparedStatement ps)
                throws SQLException
            {
                // 设置参数
                ps.setString(1, id);
            }
        }, new RowCallbackHandler()
        {
            public void processRow(ResultSet rst)
                throws SQLException
            {
                getUser(user, rst);
            }
        });

        if ("".equals(user.getPassword()))
        {
            return null;
        }

        return user;
    }

    public List<User> queryCommon(User user)
    {
        String locationId = user.getLocationID();

        final List<User> list = new ArrayList<User>();

        List<User> list1 = null;

        List<User> list2 = new ArrayList<User>();

        StringBuffer buffer = new StringBuffer();

        if (locationId.equals("-1"))
        {
            Map<String, String> map = new HashMap<String, String>();

            map.put("name", user.getName());

            list1 = jdbcOperation.getIbatisDaoSupport().queryForList("UserDAO.queryCommon", map);

            for (User user2 : list1)
            {
                RoleHelper.setRole(user2, user2.getType());

                list2.add(user2);
            }

            return list2;
        }
        else
        {
            buffer.append("select * From t_center_user Where role <> 3 and name <> ? and locationId = ?");

            jdbcOperation.query(buffer.toString(), new Object[] {user.getName(), locationId},
                new RowCallbackHandler()
                {
                    public void processRow(ResultSet rst)
                        throws SQLException
                    {
                        User user = new User();

                        getUser(user, rst);

                        list.add(user);
                    }
                });
        }

        return list;
    }

    public boolean modifyPassword(String userName, String password)
    {
        String sql = "update t_center_user set password = ? where name = ?";

        int i = jdbcOperation.update(sql, new Object[] {password, userName});

        return i != 0;
    }

    public boolean modifyStatus(String userName, int status)
    {
        String sql = "update t_center_user set status = ? where name = ?";

        int i = jdbcOperation.update(sql, new Object[] {status, userName});

        return i != 0;
    }

    public boolean modifyFail(String userName, int fail)
    {
        String sql = "update t_center_user set fail = ? where name = ?";

        int i = jdbcOperation.update(sql, new Object[] {fail, userName});

        return i != 0;
    }

    public boolean modifyLogTime(String id, String logTime)
    {
        String sql = "update t_center_user set loginTime = ? where id = ?";

        int i = jdbcOperation.update(sql, new Object[] {logTime, id});

        return i != 0;
    }

    public boolean delYWY(String userName)
    {
        String sql = "delete from t_center_user where name = ?";

        int i = jdbcOperation.update(sql, new Object[] {userName});

        return i != 0;
    }

    public boolean addUser(final User user)
    {
        return jdbcOperation.save(user) > 0;
    }

    public boolean addUser2(final User user)
    {
        return jdbcOperation2.save(user) > 0;
    }

    private void getUser(User bean, ResultSet rst)
        throws SQLException
    {
        String tmp = rst.getString("name");
        bean.setName(tmp);

        tmp = rst.getString("password");
        bean.setPassword(tmp);

        int i = rst.getInt("role");

        bean.setType(i);

        RoleHelper.setRole(bean, i);

        bean.setStafferName(rst.getString("stafferName"));

        bean.setStafferId(rst.getString("stafferId"));

        bean.setLocationID(rst.getString("locationID"));

        bean.setStatus(rst.getInt("status"));

        bean.setFail(rst.getInt("fail"));

        bean.setId(rst.getString("ID"));
    }

    /**
     * default constructor
     */
    public UserDAO()
    {}

    /**
     * @return the jdbcOperation2
     */
    public JdbcOperation getJdbcOperation2()
    {
        return jdbcOperation2;
    }

    /**
     * @param jdbcOperation2
     *            the jdbcOperation2 to set
     */
    public void setJdbcOperation2(JdbcOperation jdbcOperation2)
    {
        this.jdbcOperation2 = jdbcOperation2;
    }
}
