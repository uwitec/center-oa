/**
 * File Name: ExamineDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;

import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.examine.bean.ExamineBean;
import com.china.center.oa.examine.vo.ExamineVO;
import com.china.center.tools.ListTools;

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
     * 根据职员查询当前财务年度的已经审核通过的个人考评
     * @param stafferId
     * @param year
     * @return
     */
    public ExamineBean findByStafferIdAndYear(String stafferId, int year)
    {
        List<ExamineBean> result = this.queryEntityBeansByCondition("where stafferId = ? and year = ? and attType = ? and status = ? and abs = ?",
                stafferId,
                year,
                ExamineConstant.EXAMINE_ATTTYPE_PERSONAL,
                ExamineConstant.EXAMINE_STATUS_PASS,
                ExamineConstant.EXAMINE_ABS_FALSE);
        
        if (ListTools.isEmptyOrNull(result))
        {
            return null;
        }
        
        return result.get(0);
    }
    
    /**
     * 分公司考核的count
     * @param stafferId
     * @param year
     * @param attType
     * @param type
     * @return
     */
    public int countInLocationType(String stafferId, int year, int attType,
            int type)
    {
        return this.jdbcOperation.queryForInt(BeanTools.getCountHead(claz)
                + "WHERE stafferId = ? and year = ? and attType = ? and type = ?",
                stafferId,
                year,
                attType,
                type);
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
        return this.jdbcOperation.update("set status = ? where year = ?",
                claz,
                ExamineConstant.EXAMINE_STATUS_END,
                year);
    }
}
