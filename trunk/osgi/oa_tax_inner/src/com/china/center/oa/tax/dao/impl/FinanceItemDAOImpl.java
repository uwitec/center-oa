/**
 * File Name: FinanceItemDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.dao.impl;


import java.math.BigDecimal;
import java.util.Map;

import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.vo.FinanceItemVO;


/**
 * FinanceItemDAOImpl
 * 
 * @author ZHUZHU
 * @version 2011-2-6
 * @see FinanceItemDAOImpl
 * @since 1.0
 */
public class FinanceItemDAOImpl extends BaseDAO<FinanceItemBean, FinanceItemVO> implements FinanceItemDAO
{
    public long sumInByCondition(ConditionParse condition)
    {
        String sql = BeanTools.getSumHead(claz, "inmoney") + condition.toString();

        return this.jdbcOperation.queryForLong(sql);
    }

    public long sumOutByCondition(ConditionParse condition)
    {
        String sql = BeanTools.getSumHead(claz, "outmoney") + condition.toString();

        return this.jdbcOperation.queryForLong(sql);
    }

    public long[] sumMoneryByCondition(ConditionParse condition)
    {
        String sql = "select sum(inmoney) as inmoney, sum(outmoney) as outmoney  from "
                     + BeanTools.getTableName(claz) + " FinanceItemBean " + condition.toString();

        Map queryForMap = this.jdbcOperation.queryForMap(sql);

        long[] result = new long[2];

        if (queryForMap.get("inmoney") == null)
        {
            result[0] = 0;
        }
        else
        {
            result[0] = ((BigDecimal)queryForMap.get("inmoney")).longValue();
        }

        if (queryForMap.get("outmoney") == null)
        {
            result[1] = 0;
        }
        else
        {
            result[1] = ((BigDecimal)queryForMap.get("outmoney")).longValue();
        }

        return result;
    }
}
