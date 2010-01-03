/**
 * File Name: DepartmentDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.ProviderBean;
import com.china.center.oa.customer.vo.ProviderVO;


/**
 * DepartmentDAO
 * 
 * @author ZHUZHU
 * @version 2008-11-2
 * @see ProviderDAO
 * @since 1.0
 */
@Bean(name = "providerDAO")
public class ProviderDAO extends BaseDAO2<ProviderBean, ProviderVO>
{
    /**
     * 是否供应商在out里面被引用
     * 
     * @param providerId
     * @return
     */
    public int countProviderInOut(String providerId)
    {
        String sql = "select count(1) from t_center_out where type = 1 and customerId = ?";

        return this.jdbcOperation.queryForInt(sql, providerId);
    }

    public int countProviderByName(String name)
    {
        return this.jdbcOperation.queryForInt("where name = ?", claz, name);
    }
}
