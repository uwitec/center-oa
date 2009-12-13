/**
 * File Name: WorkLogDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.constant.WorkLogConstant;
import com.china.center.oa.worklog.bean.WorkLogBean;
import com.china.center.oa.worklog.vo.WorkLogVO;
import com.china.center.oa.worklog.wrap.StatWorkLogWrap;
import com.china.center.tools.StringTools;


/**
 * WorkLogDAO
 * 
 * @author zhuzhu
 * @version 2009-2-15
 * @see WorkLogDAO
 * @since 1.0
 */
@Bean(name = "workLogDAO")
public class WorkLogDAO extends BaseDAO2<WorkLogBean, WorkLogVO>
{
    /**
     * ÐÞ¸Ä×´Ì¬
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

    public int countByWorkDate(String stafferId, String workDate)
    {
        return this.jdbcOperation.queryForInt("where stafferId = ? and workDate = ?", claz,
            stafferId, workDate);
    }

    /**
     * queryWorkLogByConstion
     * 
     * @param condition
     * @param page
     * @return
     */
    public List<WorkLogVO> queryWorkLogByConstion(ConditionParse condition, PageSeparate page)
    {
        return jdbcOperation.queryObjectsBySqlAndPageSeparate(getLastQuerySelfSql(condition),
            page, WorkLogVO.class);
    }

    /**
     * list distinct staffer
     * 
     * @return
     */
    public List<String> listDistinctStaffer()
    {
        final List<String> result = new ArrayList();

        jdbcOperation.query(
            "select distinct(WorkLogBean.stafferId) from T_CENTER_WORKLOG WorkLogBean where 1 = 1",
            new RowCallbackHandler()
            {
                public void processRow(ResultSet rst)
                    throws SQLException
                {
                    result.add(rst.getString("stafferId"));
                }
            });

        return result;
    }

    /**
     * queryStatWorkLogWrap
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<StatWorkLogWrap> queryStatWorkLogWrap(String beginDate, String endDate,
                                                      String locationId, String stafferId)
    {
        Map map = new HashMap();

        map.put("beginDate", beginDate);

        map.put("endDate", endDate);

        // may be null
        map.put("locationId", locationId);

        map.put("stafferId", stafferId);

        map.put("status", WorkLogConstant.WORKLOG_STATUS_SUBMIT);

        return this.jdbcOperation.getIbatisDaoSupport().queryForList(
            "WorkLogDAO.queryStatWorkLogWrap", map);

    }

    /**
     * countSubmitWorkLogByStafferAndPeriodDate
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @param result
     * @return
     */
    public int countSubmitWorkLogByStafferAndPeriodDate(String stafferId, String beginDate,
                                                        String endDate, int result)
    {
        return this.countByCondition(
            "where stafferId = ? and workDate >= ? and workDate <= ? and status = ? and result = ?",
            stafferId, beginDate, endDate, WorkLogConstant.WORKLOG_STATUS_SUBMIT, result);
    }

    /**
     * countWorkLogByConstion
     * 
     * @param stafferId
     * @param condition
     * @return
     */
    public int countWorkLogByConstion(ConditionParse condition)
    {
        return jdbcOperation.queryObjectsBySql(getLastQuerySelfSql(condition)).getCount();
    }

    private String getQuerySelfSql()
    {
        return "select distinct (WorkLogBean.id) "
               + "from T_CENTER_WORKLOG WorkLogBean, t_center_worklog_visit visit "
               + "where WorkLogBean.id = visit.parentId";
    }

    private String getLastQuerySelfSql(ConditionParse condition)
    {
        condition.removeWhereStr();

        if (StringTools.isNullOrNone(condition.toString()))
        {
            condition.addString("1 = 1");
        }

        return "select tt.*, staffer.name as stafferName from T_CENTER_WORKLOG tt, ("
               + getQuerySelfSql() + " and " + condition.toString() + ") t1,"
               + " t_center_oastaffer staffer where staffer.id = tt.stafferId and t1.id = tt.id";
    }
}
