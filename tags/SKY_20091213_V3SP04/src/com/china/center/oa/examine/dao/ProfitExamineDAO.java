/**
 * File Name: ProfitExamineDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.examine.bean.ProfitExamineBean;


/**
 * ProfitExamineDAO
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see ProfitExamineDAO
 * @since 1.0
 */
@Bean(name = "profitExamineDAO")
public class ProfitExamineDAO extends BaseDAO2<ProfitExamineBean, ProfitExamineBean>
{
    /**
     * ¸ù¾Ýstep²éÑ¯
     * 
     * @param parentId
     * @param step
     * @return
     */
    public ProfitExamineBean findByParentAndStep(String parentId, int step)
    {
        List<ProfitExamineBean> list = this.jdbcOperation.queryForList(
            "where parentId = ? and step = ?", claz, parentId, step);

        if (list.size() > 0)
        {
            return list.get(0);
        }

        return null;
    }
}
