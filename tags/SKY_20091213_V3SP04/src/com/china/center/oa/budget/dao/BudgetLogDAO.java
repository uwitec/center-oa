/**
 * File Name: BudgetLogDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.budget.bean.BudgetLogBean;
import com.china.center.oa.budget.vo.BudgetLogVO;


/**
 * BudgetLogDAO
 * 
 * @author zhuzhu
 * @version 2009-6-26
 * @see BudgetLogDAO
 * @since 1.0
 */
@Bean(name = "budgetLogDAO")
public class BudgetLogDAO extends BaseDAO2<BudgetLogBean, BudgetLogVO>
{

}
