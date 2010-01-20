/**
 * File Name: BudgetDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.vo.BudgetItemVO;
import com.china.center.tools.ListTools;


/**
 * BudgetDAO
 * 
 * @author zhuzhu
 * @version 2008-12-2
 * @see BudgetItemDAO
 * @since 1.0
 */
@Bean(name = "budgetItemDAO")
public class BudgetItemDAO extends BaseDAO2<BudgetItemBean, BudgetItemVO>
{
    public double sumBudgetTotal(String budgetId)
    {
        final List<Double> ruslt = new ArrayList<Double>();
        jdbcOperation.query(
            "select sum(budget) as rst from T_CENTER_BUDGETITEM where budgetId = ?",
            new Object[] {budgetId}, new RowCallbackHandler()
            {
                public void processRow(ResultSet rst)
                    throws SQLException
                {
                    ruslt.add(rst.getDouble("rst"));
                }
            });

        if (ListTools.isEmptyOrNull(ruslt))
        {
            return 0.0;
        }

        return ruslt.get(0);
    }

    /**
     * countRealTotal
     * 
     * @param budgetId
     * @return
     */
    public double sumRealTotal(String budgetId)
    {
        final List<Double> ruslt = new ArrayList<Double>();
        jdbcOperation.query(
            "select sum(realMonery) as rst from T_CENTER_BUDGETITEM where budgetId = ?",
            new Object[] {budgetId}, new RowCallbackHandler()
            {
                public void processRow(ResultSet rst)
                    throws SQLException
                {
                    ruslt.add(rst.getDouble("rst"));
                }
            });

        if (ListTools.isEmptyOrNull(ruslt))
        {
            return 0.0;
        }

        return ruslt.get(0);
    }

    /**
     * @param budgetId
     * @param feeItemId
     * @return
     */
    public double sumRealTotalInSubBudget(String budgetId, String feeItemId)
    {
        final List<Double> ruslt = new ArrayList<Double>();
        jdbcOperation.query(
            "select sum(t1.realMonery) as rst from T_CENTER_BUDGETITEM t1, T_CENTER_BUDGET t2 "
                + "where t1.budgetId = t2.id and t1.feeItemId = ? and  t2.id in (select id from T_CENTER_BUDGET t3 where t3.parentId = ?)",
            new Object[] {feeItemId, budgetId}, new RowCallbackHandler()
            {
                public void processRow(ResultSet rst)
                    throws SQLException
                {
                    ruslt.add(rst.getDouble("rst"));
                }
            });

        if (ListTools.isEmptyOrNull(ruslt))
        {
            return 0.0;
        }

        return ruslt.get(0);
    }

    /**
     * findByBudgetIdAndFeeItemId
     * 
     * @param budgetId
     * @param feeItemId
     * @return
     */
    public BudgetItemBean findByBudgetIdAndFeeItemId(String budgetId, String feeItemId)
    {
        return this.findUnique("where budgetId = ? and feeItemId = ?", budgetId, feeItemId);
    }

    /**
     * vo
     * 
     * @param budgetId
     * @param feeItemId
     * @return
     */
    public BudgetItemVO findVOByBudgetIdAndFeeItemId(String budgetId, String feeItemId)
    {
        return this.findUniqueVO("where budgetId = ? and feeItemId = ?", budgetId, feeItemId);
    }

    /**
     * @param budgetId
     * @return
     */
    public boolean updateUseMoneyEqualsRealMoney(String budgetId)
    {
        this.jdbcOperation.update(BeanTools.getUpdateHead(claz)
                                  + "set useMonery = realMonery where budgetId = ?", budgetId);
        return true;
    }
}
