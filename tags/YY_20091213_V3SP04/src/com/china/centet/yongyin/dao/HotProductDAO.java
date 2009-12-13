/**
 *
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.jdbc.util.PageSeparate;
import com.china.centet.yongyin.bean.HotProductBean;
import com.china.centet.yongyin.vo.HotProductBeanVO;


/**
 * @author Administrator
 */
public class HotProductDAO extends BaseDAO2<HotProductBean, HotProductBeanVO>
{
    /**
     *
     */
    public HotProductDAO()
    {}

    public int countProduct(String productId)
    {
        return this.countBycondition("where productId = ?", productId);
    }

    /**
     * 根据统计查询
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<HotProductBeanVO> queryStatHotProduct(String beginTime, String endTime,
                                                      PageSeparate separate)
    {
        String sql = "select a.name as productName, a.code as productCode, "
                     + "productId, sum(amount) as amount ";

        sql += "from t_center_stockitem t, t_center_product a  "
               + "WHERE status = 1 and t.productId = a.id and t.logTime >= ? and t.logTime <= ?"
               + " group by productId order by sum(amount) desc";

        return this.jdbcOperation.queryObjectsBySqlAndPageSeparate(sql, separate,
            HotProductBeanVO.class, beginTime, endTime);
    }

    public int countStatHotProduct(String beginTime, String endTime)
    {
        String sql = "select a.name as productName, a.code as productCode, "
                     + "productId, sum(amount) as amount ";

        sql += "from t_center_stockitem t, t_center_product a  "
               + "WHERE status = 1 and t.productId = a.id and t.logTime >= ? and t.logTime <= ?"
               + " group by productId order by sum(amount) desc";

        return this.jdbcOperation.queryObjectsBySql(sql, beginTime, endTime).getCount();
    }
}
