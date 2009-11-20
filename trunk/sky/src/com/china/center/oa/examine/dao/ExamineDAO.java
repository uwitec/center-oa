/**
 * File Name: ExamineDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.examine.bean.ExamineBean;
import com.china.center.oa.examine.vo.ExamineVO;


/**
 * ExamineDAO
 * 
 * @author zhuzhu
 * @version 2009-1-7
 * @see ExamineDAO
 * @since 1.0
 */
@Bean(name = "examineDAO")
public class ExamineDAO extends BaseDAO2<ExamineBean, ExamineVO>
{
    /**
     * updateStatus
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(String id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, claz);

        return true;
    }

    /**
     * 分公司考核的count
     * @param stafferId
     * @param year
     * @param attType
     * @param type
     * @return
     */
    public int countInLocationType(String stafferId, int year, int attType, int type)
    {
        return this.jdbcOperation.queryForInt(
            BeanTools.getCountHead(claz)
                + "WHERE stafferId = ? and year = ? and attType = ? and type = ?", stafferId,
            year, attType, type);
    }

    /**
     * 更新总额
     * 
     * @param id
     * @param totalProfit
     * @return
     */
    public boolean updateTotalProfit(String id, double totalProfit)
    {
        this.jdbcOperation.updateField("totalProfit", totalProfit, id, claz);

        return true;
    }

    /**
     * 结束所有的年度考评
     * 
     * @param year
     * @return
     */
    public int updateExamineStatusToEnd(int year)
    {
        return this.jdbcOperation.update("set status = ? where year = ?", claz,
            ExamineConstant.EXAMINE_STATUS_END, year);
    }
}
