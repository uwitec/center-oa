/**
 * File Name: ProviderDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao.impl;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.product.vo.ProviderVO;


/**
 * ProviderDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-21
 * @see ProviderDAOImpl
 * @since 1.0
 */
public class ProviderDAOImpl extends BaseDAO<ProviderBean, ProviderVO> implements ProviderDAO
{
    /**
     * 是否供应商在out里面被引用
     * 
     * @param providerId
     * @return
     */
    public int countProviderInOut(String providerId)
    {
        String sql = "select count(1) from t_center_out where type = 1 and customerId = ?";

        return this.jdbcOperation.queryForInt(sql, providerId);
    }

    public int countProviderByName(String name)
    {
        return this.jdbcOperation.queryForInt("where name = ?", claz, name);
    }

    public List<ProviderBean> queryByLimit(ConditionParse condition, int limit)
    {
        condition.removeWhereStr();

        String sql = "select t1.* from T_CENTER_PROVIDE t1, T_CENTER_PRODUCTTYPEVSCUSTOMER t2 where t1.id = t2.customerId "
                     + condition.toString();

        PageSeparate page = new PageSeparate(limit, limit);

        return this.jdbcOperation.queryObjectsBySqlAndPageSeparate(sql, page, claz);
    }
}
