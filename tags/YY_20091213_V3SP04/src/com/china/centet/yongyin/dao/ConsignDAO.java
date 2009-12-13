/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.centet.yongyin.bean.ConsignBean;
import com.china.centet.yongyin.bean.TransportBean;


/**
 * 发货单和运输方式
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class ConsignDAO
{
    private JdbcOperation jdbcOperation = null;

    private JdbcOperation jdbcOperation2 = null;

    /**
     * default constructor
     */
    public ConsignDAO()
    {}

    public boolean addTransport(TransportBean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    public boolean addConsign(ConsignBean bean)
    {
        return jdbcOperation2.save(bean) > 0;
    }

    public boolean updateConsign(ConsignBean bean)
    {
        return jdbcOperation.update(bean) > 0;
    }

    public boolean delConsign(String id)
    {
        return jdbcOperation2.delete(id, ConsignBean.class) > 0;
    }

    public boolean delTransport(String id)
    {
        return jdbcOperation.delete(id, TransportBean.class) > 0;
    }

    public List<TransportBean> listTransport()
    {
        return jdbcOperation.queryForList("where 1 = 1", TransportBean.class);
    }

    public List<ConsignBean> queryConsignByCondition(ConditionParse condition)
    {
        condition.removeWhereStr();

        return jdbcOperation.queryForListBySql(
            "select * from T_CENTER_OUTPRODUCT t1, t_center_out t2 where t1.fullId = t2.fullId "
                + condition, ConsignBean.class);
    }

    public int countTransport(String transportId)
    {
        return jdbcOperation.queryForInt(
            "select count(1) from T_CENTER_OUTPRODUCT where transport = ?", transportId);
    }

    public ConsignBean findConsignById(String id)
    {
        List<ConsignBean> list = jdbcOperation.queryForListBySql(
            "select * from T_CENTER_OUTPRODUCT t1, t_center_out t2 where t1.fullId = ? and t1.fullId = t2.fullId",
            ConsignBean.class, id);

        if (list.size() != 1)
        {
            return null;
        }

        return list.get(0);
    }

    public List<TransportBean> queryTransportByType(int type)
    {
        return jdbcOperation.queryForList("where type = ? order by parent", TransportBean.class,
            type);
    }

    public List<TransportBean> queryTransportByParentId(String parentId)
    {
        return jdbcOperation.queryForList("where PARENT = ?", TransportBean.class, parentId);
    }

    public TransportBean findTransportById(String id)
    {
        return jdbcOperation.find(id, TransportBean.class);
    }

    public TransportBean findTransportByName(String name)
    {
        return jdbcOperation.find(name, "name", TransportBean.class);
    }

    public int countByName(String name, int type)
    {
        return jdbcOperation.queryForInt("select count(1) from "
                                         + BeanTools.getTableName(TransportBean.class)
                                         + " where NAME = ? and type = ?", new Object[] {name,
            type});
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
