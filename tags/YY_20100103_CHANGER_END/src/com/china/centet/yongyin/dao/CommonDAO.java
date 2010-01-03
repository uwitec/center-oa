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


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.centet.yongyin.bean.RoleBean;
import com.china.centet.yongyin.constant.Constant;


/**
 * @author zhu
 * @version 2006-7-16
 * @see CommonDAO
 * @since
 */

public class CommonDAO
{
    private JdbcOperation jdbcOperation = null;

    public List<String> listAll(String tableName)
    {
        final List<String> list = new ArrayList<String>();

        StringBuffer buffer = new StringBuffer();

        buffer.append("select name From " + tableName);

        jdbcOperation.query(buffer.toString(), new RowCallbackHandler()
        {
            public void processRow(ResultSet rst)
                throws SQLException
            {
                list.add(rst.getString("name"));
            }
        });

        return list;
    }

    public List<String> listAllStaffer()
    {
        final List<String> list = new ArrayList<String>();

        StringBuffer buffer = new StringBuffer();

        buffer.append("select name From t_center_oastaffer");

        jdbcOperation.query(buffer.toString(), new RowCallbackHandler()
        {
            public void processRow(ResultSet rst)
                throws SQLException
            {
                list.add(rst.getString("name"));
            }
        });

        return list;
    }

    public List<String> queryStafferByCondition(ConditionParse condtion)
    {
        final List<String> list = new ArrayList<String>();

        StringBuffer buffer = new StringBuffer();

        buffer.append("select name From t_center_oastaffer ").append(condtion);

        jdbcOperation.query(buffer.toString(), new RowCallbackHandler()
        {
            public void processRow(ResultSet rst)
                throws SQLException
            {
                list.add(rst.getString("name"));
            }
        });

        return list;
    }

    public List<String> queryStafferByName(String name)
    {
        final List<String> list = new ArrayList<String>();

        StringBuffer buffer = new StringBuffer();

        buffer.append("select name From t_center_oastaffer where name like '%" + name + "%'");

        jdbcOperation.query(buffer.toString(), new RowCallbackHandler()
        {
            public void processRow(ResultSet rst)
                throws SQLException
            {
                list.add(rst.getString("name"));
            }
        });

        return list;
    }

    /**
     * 查询可见的角色
     * 
     * @return
     */
    public List<RoleBean> listVisibleRole()
    {
        return jdbcOperation.queryForList("where visible = ?", RoleBean.class,
            Constant.ROLE_VISIBLE_YES);
    }

    /**
     * 查询可见的角色
     * 
     * @return
     */
    public List<RoleBean> listAllRole()
    {
        return jdbcOperation.queryForList("where 1 = 1", RoleBean.class);
    }

    public RoleBean findRoleById(String id)
    {
        return jdbcOperation.find(id, RoleBean.class);
    }

    public boolean add(String name, String tableName)
    {
        String sql = "insert into " + tableName + "(name) values(?)";

        int i = jdbcOperation.update(sql, new Object[] {name});

        return i != 0;
    }

    public synchronized int getSquence()
    {
        String sql = "select max(id) from T_CENTER_SEQUENCE";

        int tem = jdbcOperation.queryForInt(sql);

        int kk = tem;

        if (tem > (Integer.MAX_VALUE - 1000))
        {
            kk = 0;
        }

        sql = "update T_CENTER_SEQUENCE set id = ?  where id = ?";

        jdbcOperation.update(sql, new Object[] {kk + 1, tem});

        return tem + 1;
    }

    public String getSquenceString()
    {
        return String.valueOf(this.getSquence());
    }

    /**
     * @return the jdbcOperation
     */
    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation;
    }

    /**
     * @param jdbcOperation
     *            the jdbcOperation to set
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }

}
