/**
 * File Name: AssignApplyDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.AssignApplyBean;
import com.china.center.oa.customer.vo.AssignApplyVO;


/**
 * AssignApplyDAO
 * 
 * @author zhuzhu
 * @version 2008-11-12
 * @see AssignApplyDAO
 * @since 1.0
 */
@Bean(name = "assignApplyDAO")
public class AssignApplyDAO extends BaseDAO2<AssignApplyBean, AssignApplyVO>
{
    public boolean delAssignByCityId(String cityId)
    {
        this.jdbcOperation.getIbatisDaoSupport().delete("CustomerDAO.delAssignByCityId", cityId);
        
        return true;
    }
}
