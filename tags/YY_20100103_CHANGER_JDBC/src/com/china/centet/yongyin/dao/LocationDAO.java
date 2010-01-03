/*
 * File Name: LocationDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.jdbc.inter.JdbcOperation;
import com.china.centet.yongyin.bean.LocationBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class LocationDAO
{
    private JdbcOperation jdbcOperation2 = null;

    /**
     * default constructor
     */
    public LocationDAO()
    {}

    public boolean addLocation(LocationBean bean)
    {
        return jdbcOperation2.save(bean) > 0;
    }

    public boolean delLocation(String id)
    {
        return jdbcOperation2.delete(id, LocationBean.class) > 0;
    }

    public List<LocationBean> listLocation()
    {
        return jdbcOperation2.queryForList("where 1 = 1", LocationBean.class);
    }

    public LocationBean findLocation(String id)
    {
        return jdbcOperation2.find(id, LocationBean.class);
    }

    public int countByName(String name)
    {
        return jdbcOperation2.queryForInt(
            "select count(1) from t_center_location where LOCATIONNAME = ?", new Object[] {name});
    }

    public int countByCode(String code)
    {
        return jdbcOperation2.queryForInt(
            "select count(1) from t_center_location where LOCATIONCODE = ?", new Object[] {code});
    }

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
