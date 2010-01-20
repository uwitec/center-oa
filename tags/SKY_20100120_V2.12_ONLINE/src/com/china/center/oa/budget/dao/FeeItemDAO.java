/**
 * File Name: BudgetDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.budget.bean.FeeItemBean;


/**
 * BudgetDAO
 * 
 * @author zhuzhu
 * @version 2008-12-2
 * @see FeeItemDAO
 * @since 1.0
 */
@Bean(name = "feeItemDAO")
public class FeeItemDAO extends BaseDAO2<FeeItemBean, FeeItemBean>
{

}
