/**
 * File Name: ProfitDAo.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.examine.bean.ProfitBean;
import com.china.center.oa.examine.vo.ProfitVO;
import com.china.center.tools.TimeTools;


/**
 * ProfitDAo
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see ProfitDAo
 * @since 1.0
 */
@Bean(name = "profitDAO")
public class ProfitDAO extends BaseDAO2<ProfitBean, ProfitVO>
{
    /**
     * 求一定时间内的职员的利润之和
     * 
     * @param stafferId
     * @param beginTime
     * @param endTime
     * @return
     */
    public double countProfitByTime(String stafferId, String beginTime, String endTime)
    {
        // 先格式化一下
        beginTime = TimeTools.changeTimeToDate(beginTime);

        endTime = TimeTools.changeTimeToDate(endTime);

        return this.jdbcOperation.queryForDouble(
            "select sum(profit) from " + BeanTools.getTableName(claz)
                + " where stafferId = ? and orgDate >= ? and orgDate <= ?", stafferId, beginTime,
            endTime);
    }

    /**
     * 统计客户同一天的利润
     * 
     * @param cid
     * @param logDate
     * @return
     */
    public int countByCusotmerIdAndLogDate(String cid, String logDate)
    {
        return this.jdbcOperation.queryForInt("where customerId = ? and orgDate = ?", claz, cid,
            logDate);
    }

    /**
     * 根据customerId和logTime修改
     * 
     * @param cid
     * @param logDate
     * @param profit
     * @return
     */
    public boolean updateProfitByCusotmerIdAndLogDate(String cid, String logDate, double profit)
    {
        this.jdbcOperation.update(
            "set profit = ? , logTime = ? where customerId = ? and orgDate = ?", claz, profit,
            TimeTools.now(), cid, logDate);

        return true;
    }

    public int deleteByOrgDate(String beginDate, String endDate)
    {
        return this.jdbcOperation.delete("where orgDate >= ? and orgDate <= ?", claz, beginDate,
            endDate);
    }
}
