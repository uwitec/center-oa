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


import java.util.List;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.util.PageSeparate;
import com.china.centet.yongyin.bean.Bill;


/**
 * @author zhu
 * @version 2006-7-16
 * @see BillDAO
 * @since
 */

public class BillDAO
{
    private JdbcOperation jdbcOperation = null;

    public Bill findBillById(final String id)
    {
        return jdbcOperation.find(id, Bill.class);
    }

    public List<Bill> queryBillByCondition(ConditionParse condition)
    {
        condition.addWhereStr();

        return jdbcOperation.queryForList(condition.toString() + " order by id desc", Bill.class);
    }

    public List<Bill> queryBillByCondition(ConditionParse condition, PageSeparate page)
    {
        condition.addWhereStr();

        return jdbcOperation.queryObjectsByPageSeparate(
            condition.toString() + " order by id desc", page, Bill.class);
    }

    public int countBillByCondition(ConditionParse condition)
    {
        condition.addWhereStr();

        return jdbcOperation.queryObjects(condition.toString() + " order by id desc", Bill.class).getCount();
    }

    public boolean delBill(String id)
    {
        String sql = "delete from t_center_bill where id = ?";

        int i = jdbcOperation.update(sql, new Object[] {id});

        return i != 0;
    }

    public boolean mark(String fullId, String mark)
    {
        String sql = "update t_center_bill set mark = ? where id = ?";

        int i = jdbcOperation.update(sql, new Object[] {mark, fullId});

        return i != 0;
    }

    public boolean updateInway(String fullId, int inway)
    {
        String sql = "update t_center_bill set inway = ? where id = ?";

        int i = jdbcOperation.update(sql, new Object[] {inway, fullId});

        return i != 0;
    }

    public boolean addBill(final Bill bill)
    {
        return jdbcOperation.save(bill) > 0;
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
