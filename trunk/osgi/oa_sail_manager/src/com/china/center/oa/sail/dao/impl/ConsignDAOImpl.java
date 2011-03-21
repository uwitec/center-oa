/**
 * File Name: ConsignDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao.impl;


import java.util.List;

import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.sail.bean.ConsignBean;
import com.china.center.oa.sail.bean.TransportBean;
import com.china.center.oa.sail.dao.ConsignDAO;


/**
 * ConsignDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see ConsignDAOImpl
 * @since 3.0
 */
public class ConsignDAOImpl implements ConsignDAO
{
    private JdbcOperation jdbcOperation = null;

    /**
     * default constructor
     */
    public ConsignDAOImpl()
    {
    }

    public boolean addTransport(TransportBean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    public boolean updateTransport(TransportBean bean)
    {
        return jdbcOperation.update(bean) > 0;
    }

    public TransportBean findTransport(String id)
    {
        return jdbcOperation.find(id, TransportBean.class);
    }

    public boolean addConsign(ConsignBean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    public boolean updateConsign(ConsignBean bean)
    {
        return jdbcOperation.update(bean) > 0;
    }

    public boolean delConsign(String id)
    {
        jdbcOperation.delete("where fullid = ?", ConsignBean.class, id);

        return true;
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

        return jdbcOperation.queryObjectsBySql(
            "select * from T_CENTER_OUTPRODUCT t1, t_center_out t2 where t1.fullId = t2.fullId"
                + condition).setMaxResults(500).list(ConsignBean.class);
    }

    public int countTransport(String transportId)
    {
        return jdbcOperation.queryForInt(
            "select count(1) from T_CENTER_OUTPRODUCT where transport = ?", transportId);
    }

    public ConsignBean findDefaultConsignByFullId(String id)
    {
        List<ConsignBean> list = jdbcOperation
            .queryForListBySql(
                "select * from T_CENTER_OUTPRODUCT t1, t_center_out t2 where t1.fullId = ? and t1.fullId = t2.fullId order by t1.gid asc",
                ConsignBean.class, id);

        if (list.size() == 0)
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
                                         + " where NAME = ? and type = ?",
            new Object[] {name, type});
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

    public ConsignBean findById(String gid)
    {
        List<ConsignBean> list = jdbcOperation
            .queryForListBySql(
                "select * from T_CENTER_OUTPRODUCT t1, t_center_out t2 where t1.gid = ? and t1.fullId = t2.fullId",
                ConsignBean.class, gid);

        if (list.size() != 1)
        {
            return null;
        }

        return list.get(0);
    }

    public List<ConsignBean> queryConsignByFullId(String fullId)
    {
        List<ConsignBean> list = jdbcOperation
            .queryForListBySql(
                "select * from T_CENTER_OUTPRODUCT t1, t_center_out t2 where t1.fullId = ? and t1.fullId = t2.fullId order by t1.gid asc",
                ConsignBean.class, fullId);

        return list;
    }
}
