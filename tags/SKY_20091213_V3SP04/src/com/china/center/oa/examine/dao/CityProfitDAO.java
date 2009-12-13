/**
 * File Name: CityProfitDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-29<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.examine.bean.CityProfitBean;
import com.china.center.oa.examine.vo.CityProfitVO;


/**
 * CityProfitDAO
 * 
 * @author zhuzhu
 * @version 2009-1-29
 * @see
 * @since
 */
@Bean(name = "cityProfitDAO")
public class CityProfitDAO extends BaseDAO2<CityProfitBean, CityProfitVO>
{
    /**
     * 根据区域和月份进行更新
     * 
     * @param cityId
     * @param month
     * @param profit
     * @return
     */
    public boolean updateByCityAndMonth(String cityId, int month, double profit)
    {
        this.jdbcOperation.update(BeanTools.getUpdateHead(claz)
                                  + "set profit = ? where cityId = ? and month = ?", profit,
            cityId, month);

        return true;
    }
}
