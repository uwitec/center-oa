/**
 * File Name: PlanDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.plan.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.plan.bean.PlanBean;


/**
 * PlanDAO
 * 
 * @author zhuzhu
 * @version 2009-1-11
 * @see PlanDAO
 * @since 1.0
 */
@Bean(name = "planDAO")
public class PlanDAO extends BaseDAO2<PlanBean, PlanBean>
{
    /**
     * 同步计划和考核项的状态
     * 
     * @return
     */
    public int synchronizationPlanAndItemStatus()
    {
        int result = 0;
        
        for (int i = 1; i <= 7; i++ )
        {
            result += this.jdbcOperation.getIbatisDaoSupport().update("Syn.synItem" + i, null);
        }

        return result;
    }
}
