/**
 * File Name: BaseDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao.impl;


import java.util.List;

import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.dao.BaseDAO;


/**
 * BaseDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see BaseDAOImpl
 * @since 1.0
 */
public class BaseDAOImpl extends com.china.center.jdbc.inter.impl.BaseDAO<BaseBean, BaseBean> implements BaseDAO
{

    public int countBaseByOutTime(String outTime)
    {
        return this.jdbcOperation
            .queryForInt(
                "select count(1) from t_center_base t1, t_center_out t2  where t1.outid = t2.fullid and t2.outtime >= ?",
                outTime);
    }

    public List<BaseBean> queryBaseByOutTime(String outTime, PageSeparate pageSeparate)
    {
        String sql = "select t1.* from t_center_base t1, t_center_out t2  where t1.outid = t2.fullid and t2.outtime >= ?";

        return this.jdbcOperation.queryObjectsBySqlAndPageSeparate(sql, pageSeparate,
            BaseBean.class, outTime);
    }

    public boolean updateCostPricekey(String id, String costPricekey)
    {
        return this.jdbcOperation.updateField("costPriceKey", costPricekey, id, claz) > 0;
    }

}
