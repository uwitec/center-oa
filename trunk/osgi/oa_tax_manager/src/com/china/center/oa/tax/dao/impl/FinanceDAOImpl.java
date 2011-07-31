/**
 * File Name: FinanceDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.dao.impl;


import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.vo.FinanceVO;


/**
 * FinanceDAOImpl
 * 
 * @author ZHUZHU
 * @version 2011-2-6
 * @see FinanceDAOImpl
 * @since 1.0
 */
public class FinanceDAOImpl extends BaseDAO<FinanceBean, FinanceVO> implements FinanceDAO
{
    public boolean updateCheck(String id, String reason)
    {
        String sql = BeanTools.getUpdateHead(claz) + "set status = ?, checks = ? where id = ?";

        this.jdbcOperation.update(sql, TaxConstanst.FINANCE_STATUS_CHECK, reason, id);

        return true;
    }

    public int updateLockToEnd(String beginTime, String endTime)
    {
        String sql = BeanTools.getUpdateHead(claz)
                     + "set locks = ? where financeDate >= ? and financeDate <= ?";

        return this.jdbcOperation.update(sql, TaxConstanst.FINANCE_LOCK_YES, beginTime, endTime);
    }
}
