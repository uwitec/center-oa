/**
 * File Name: BudgetLogDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.dao.impl;


import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.budget.bean.BudgetLogBean;
import com.china.center.oa.budget.dao.BudgetLogDAO;
import com.china.center.oa.budget.vo.BudgetLogVO;


/**
 * BudgetLogDAO
 * 
 * @author ZHUZHU
 * @version 2009-6-26
 * @see BudgetLogDAOImpl
 * @since 1.0
 */
public class BudgetLogDAOImpl extends BaseDAO<BudgetLogBean, BudgetLogVO> implements BudgetLogDAO
{
    public long sumBudgetLogByBudgetItemId(String budgetItemId)
    {
        String sql = BeanTools.getSumHead(this.claz, "monery") + "where budgetItemId = '"
                     + budgetItemId + "'";

        return (long)this.jdbcOperation.queryForDouble(sql);
    }

    public int updateUserTypeByRefId(String refId, int useType, String billIds)
    {
        String sql = BeanTools.getUpdateHead(this.claz)
                     + "set userType = ? , billIds = ? where refId = ?";

        return this.jdbcOperation.update(sql, useType, billIds, refId);
    }

}
