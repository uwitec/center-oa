/**
 * File Name: BudgetApplyDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.dao;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.budget.bean.BudgetApplyBean;
import com.china.center.oa.budget.vo.BudgetApplyVO;


/**
 * BudgetApplyDAo
 * 
 * @author zhuzhu
 * @version 2009-6-14
 * @see BudgetApplyDAo
 * @since 1.0
 */
@Bean(name = "budgetApplyDAO")
public class BudgetApplyDAO extends BaseDAO2<BudgetApplyBean, BudgetApplyVO>
{
    /**
     * updateStatus
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(Serializable id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, claz);

        return true;
    }
}
