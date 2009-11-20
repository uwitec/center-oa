/*
 * File Name: MenuItemDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.jdbc.inter.JdbcOperation;
import com.china.centet.yongyin.bean.MenuItemBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class MenuItemDAO
{
    private JdbcOperation jdbcOperation = null;

    /**
     * default constructor
     */
    public MenuItemDAO()
    {}

    public List<MenuItemBean> listMenuItem()
    {
        return jdbcOperation.queryForList("where 1 = 1", MenuItemBean.class);
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
