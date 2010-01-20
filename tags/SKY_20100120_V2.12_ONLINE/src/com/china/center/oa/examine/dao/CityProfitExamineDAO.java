/**
 * File Name: CityProfitExamineDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-30<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.examine.bean.CityProfitExamineBean;
import com.china.center.oa.examine.vo.CityProfitExamineVO;
import com.china.center.tools.TimeTools;


/**
 * CityProfitExamineDAO
 * 
 * @author zhuzhu
 * @version 2009-1-30
 * @see
 * @since
 */
@Bean(name = "cityProfitExamineDAO")
public class CityProfitExamineDAO extends BaseDAO2<CityProfitExamineBean, CityProfitExamineVO>
{
    /**
     * 查询指定时间内的区域利润总和(拓展用户，终端用户的不算)
     * 
     * @param cityId
     * @param beginDate
     * @param endDate
     * @return
     */
    public double queryCityProfit(String cityId, String beginDate, String endDate)
    {
        // 先格式化一下
        beginDate = TimeTools.changeTimeToDate(beginDate);

        endDate = TimeTools.changeTimeToDate(endDate);

        String sql = "select sum(t1.profit) from t_center_profit t1, t_center_customer_now t2 "
                     + "where t1.customerId = t2.id and t1.cityId = ? and t1.sellType = ? "
                     + "and t1.orgDate >= ? and t1.orgDate <= ? ";
        
        return this.jdbcOperation.queryForDouble(sql, cityId, CustomerConstant.SELLTYPE_EXPEND,
            beginDate, endDate);
    }

    /**
     * 查询配置的区域id
     * 
     * @param parentId
     * @return
     */
    public List<String> queryDistinctCityByparentId(String parentId)
    {
        String sql = "select distinct cityId from " + BeanTools.getTableName(this.getClaz())
                     + " where parentId = ?";

        final List<String> result = new ArrayList<String>();

        jdbcOperation.query(sql, new Object[] {parentId}, new RowCallbackHandler()
        {
            public void processRow(ResultSet rst)
                throws SQLException
            {
                result.add(rst.getString("cityId"));
            }
        });

        return result;
    }
}
