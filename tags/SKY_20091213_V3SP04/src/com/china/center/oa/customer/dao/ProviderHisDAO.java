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
import com.china.center.oa.customer.bean.ProviderHisBean;


/**
 * DepartmentDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see ProviderHisDAO
 * @since 1.0
 */
@Bean(name = "providerHisDAO")
public class ProviderHisDAO extends BaseDAO2<ProviderHisBean, ProviderHisBean>
{
    /**
     * updateCheckStatus
     * 
     * @param id
     * @param checkStatus
     * @return
     */
    public boolean updateCheckStatus(String id, int checkStatus)
    {
        this.jdbcOperation.updateField("checkStatus", checkStatus, id, claz);

        return true;
    }
}
