/**
 *
 */
package com.china.centet.yongyin.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.BaseBean;


/**
 * @author Administrator
 */
public class BaseBeanDAO extends BaseDAO2<BaseBean, BaseBean>
{
    /**
     * 查询采购退货的金额总计
     * 
     * @param providerId
     * @param beginDate
     * @param endDate
     * @return
     */
    public float queryTotalBaseByCondition(String providerId, String beginDate, String endDate)
    {
        String sql = "select sum(t.value) as total from t_center_base t, t_center_out a "
                     + "where t.outId = a.fullId and a.outtype = 7 and a.customerId = ? "
                     + "and a.outTime >= ? and a.outTime <= ?";

        final List<Float> list = new ArrayList<Float>();

        this.jdbcOperation.query(sql, new Object[] {providerId, beginDate, endDate},
            new RowCallbackHandler()
            {
                public void processRow(ResultSet rs)
                    throws SQLException
                {
                    list.add(rs.getFloat("total"));
                }
            });

        return list.get(0);
    }
}
