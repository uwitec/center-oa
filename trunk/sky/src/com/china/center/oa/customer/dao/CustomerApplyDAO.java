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
import com.china.center.oa.customer.bean.CustomerApplyBean;
import com.china.center.oa.customer.vo.CustomerApplyVO;


/**
 * DepartmentDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see CustomerApplyDAO
 * @since 1.0
 */
@Bean(name = "customerApplyDAO")
public class CustomerApplyDAO extends BaseDAO2<CustomerApplyBean, CustomerApplyVO>
{
    /**
     * Í³¼Æcode
     * 
     * @param code
     * @return
     */
    public int countByCode(String code)
    {
        return this.jdbcOperation.queryForInt("where code = ?", claz, code);
    }

    /**
     * ÐÞ¸ÄÉêÇë×´Ì¬
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
    
    public boolean updateCode(String id, String code)
    {
        this.jdbcOperation.updateField("code", code, id, claz);

        return true;
    }
}
