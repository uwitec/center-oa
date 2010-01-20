/**
 * File Name: VisitDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.constant.WorkLogConstant;
import com.china.center.oa.examine.wrap.CustomerWrap;
import com.china.center.oa.worklog.bean.VisitBean;
import com.china.center.oa.worklog.wrap.VisitWrap;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * VisitDAO
 * 
 * @author zhuzhu
 * @version 2009-2-15
 * @see VisitDAO
 * @since 1.0
 */
@Bean(name = "visitDAO")
public class VisitDAO extends BaseDAO2<VisitBean, VisitBean>
{
    /**
     * 根据职员和时间查询时间段内产品的开单客户
     * 
     * @param newType
     *            新/老
     * @param stafferId
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<CustomerWrap> queryCustomerInTer(int newType, String stafferId, String beginTime,
                                                 String endTime)
    {
        // 先格式化一下
        beginTime = TimeTools.changeTimeToDate(beginTime);

        endTime = TimeTools.changeTimeToDate(endTime);

        Map map = new HashMap();

        map.put("stafferId", stafferId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("newType", newType);

        return this.jdbcOperation.getIbatisDaoSupport().queryForList("Examine.queryCustomerInTer",
            map);
    }

    /**
     * 统计职员下拜访客户的总数
     * 
     * @param targerId
     * @return
     */
    public int countByCIdAndType(String stafferId, String targerId)
    {
        String sql = "select count(1) from T_CENTER_WORKLOG_VISIT t1, T_CENTER_WORKLOG t2 "
                     + "where t2.stafferId = ? and t1.targerId = ? and t1.workType = ? and t1.parentId = t2.id";

        return this.jdbcOperation.queryForInt(sql, stafferId, targerId,
            WorkLogConstant.WORKTYPE_VISIT);
    }

    /**
     * queryWorkLogByConstion
     * 
     * @param condition
     * @param page
     * @return
     */
    public List<VisitWrap> queryWorkItemByConstion(ConditionParse condition, PageSeparate page)
    {
        return jdbcOperation.queryObjectsBySqlAndPageSeparate(getLastQuerySelfSql(condition),
            page, VisitWrap.class);
    }

    /**
     * countWorkItemByConstion
     * 
     * @param condition
     *            condition for query
     * @return
     */
    public int countWorkItemByConstion(ConditionParse condition)
    {
        return jdbcOperation.queryObjectsBySql(getLastQuerySelfSql(condition)).getCount();
    }

    private String getLastQuerySelfSql(ConditionParse condition)
    {
        condition.removeWhereStr();

        if (StringTools.isNullOrNone(condition.toString()))
        {
            condition.addString("1 = 1");
        }

        return "select VisitBean.*, WorkLogBean.logTime, StafferBean.name as stafferName "
               + "from T_CENTER_WORKLOG_VISIT VisitBean, T_CENTER_WORKLOG WorkLogBean, T_CENTER_OASTAFFER StafferBean "
               + "where VisitBean.parentId = WorkLogBean.id and WorkLogBean.stafferId = StafferBean.id and "
               + condition.toString();
    }

}
