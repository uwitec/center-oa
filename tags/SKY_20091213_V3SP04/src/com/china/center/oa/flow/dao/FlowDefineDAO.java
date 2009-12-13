/**
 * File Name: FlowDefineDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.flow.bean.FlowDefineBean;
import com.china.center.oa.flow.vo.FlowDefineVO;


/**
 * FlowDefineDAO
 * 
 * @author zhuzhu
 * @version 2009-4-26
 * @see FlowDefineDAO
 * @since 1.0
 */
@Bean(name = "flowDefineDAO")
public class FlowDefineDAO extends BaseDAO2<FlowDefineBean, FlowDefineVO>
{
    /**
     * updateStatus
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(Serializable id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, claz);

        return true;
    }
}
