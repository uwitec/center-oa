/*
 * File Name: DepotpartDAO.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-12-15 Grant: open source to
 * everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.centet.yongyin.bean.DepotpartBean;


/**
 * 仓区的dao
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class DepotpartDAO
{
    private JdbcOperation jdbcOperation = null;

    private JdbcOperation jdbcOperation2 = null;

    /**
     * default constructor
     */
    public DepotpartDAO()
    {}

    public boolean addDepotpart(DepotpartBean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    public boolean modfiyDepotpart(DepotpartBean bean)
    {
        return jdbcOperation.update(bean) > 0;
    }

    /**
     * Description: 事务里面删除仓区，同时还要删除仓区下的储位
     * 
     * @param id
     * @return
     * @since <IVersion>
     */
    public boolean delDepotpart(String id)
    {
        return jdbcOperation2.delete(id, DepotpartBean.class) > 0;
    }

    public List<DepotpartBean> listDepotpart()
    {
        return jdbcOperation.queryForList("where 1 = 1", DepotpartBean.class);
    }

    public List<DepotpartBean> queryDepotpartByCondition(ConditionParse condition)
    {
        condition.addWhereStr();

        return jdbcOperation.queryForList(condition.toString(), DepotpartBean.class);
    }

    public DepotpartBean findDepotpartById(String id)
    {
        return jdbcOperation.find(id, DepotpartBean.class);
    }

    public DepotpartBean findDepotpartByName(String name)
    {
        return jdbcOperation.find(name, "name", DepotpartBean.class);
    }

    public int countByName(String name, String locationId)
    {
        return jdbcOperation.queryForInt("select count(1) from "
                                         + BeanTools.getTableName(DepotpartBean.class)
                                         + " where NAME = ? and locationId = ?", new Object[] {
            name, locationId});
    }

    public int countByType(int type, String locationId)
    {
        return jdbcOperation.queryForInt("select count(1) from "
                                         + BeanTools.getTableName(DepotpartBean.class)
                                         + " where type =? and locationId = ?", new Object[] {
            type, locationId});
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

    /**
     * @return 返回 jdbcOperation2
     */
    public JdbcOperation getJdbcOperation2()
    {
        return jdbcOperation2;
    }

    /**
     * @param 对jdbcOperation2进行赋值
     */
    public void setJdbcOperation2(JdbcOperation jdbcOperation2)
    {
        this.jdbcOperation2 = jdbcOperation2;
    }
}
