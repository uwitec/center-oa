/**
 * File Name: StafferVSCustomerDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.vo.StafferVSCustomerVO;
import com.china.center.oa.customer.vs.StafferVSCustomerBean;


/**
 * StafferVSCustomerDAO
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see StafferVSCustomerDAO
 * @since 1.0
 */
@Bean(name = "stafferVSCustomerDAO")
public class StafferVSCustomerDAO extends BaseDAO2<StafferVSCustomerBean, StafferVSCustomerVO>
{
    /**
     * 修改地市下的拓展客户为新客户,请是否是新的临时变量也是0(就是新的)
     * 
     * @param cityId
     * @return
     */
    public boolean updateNewByCityId(String cityId)
    {
        this.jdbcOperation.getIbatisDaoSupport().delete("CustomerDAO.updateNewByCityId", cityId);

        return true;
    }

    /**
     * 这里仅仅删除拓展的客户
     * 
     * @param cityId
     * @return
     */
    public boolean delVSByCityId(String cityId)
    {
        this.jdbcOperation.getIbatisDaoSupport().delete("CustomerDAO.delVSByCityId", cityId);

        return true;
    }

    public int countByStafferId(String stafferId)
    {
        return this.jdbcOperation.queryForInt("where stafferId = ?", claz, stafferId);
    }

    public int countByStafferIdAndCustomerId(String stafferId, String customerId)
    {
        return this.jdbcOperation.queryForInt("where stafferId = ? and customerId = ?", claz,
            stafferId, customerId);
    }
}
