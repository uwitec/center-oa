/**
 * File Name: AssignApplyDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.customer.bean.AssignApplyBean;
import com.china.center.oa.customer.dao.AssignApplyDAO;
import com.china.center.oa.customer.vo.AssignApplyVO;


/**
 * AssignApplyDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see AssignApplyDAOImpl
 * @since 1.0
 */
public class AssignApplyDAOImpl extends BaseDAO<AssignApplyBean, AssignApplyVO> implements AssignApplyDAO
{
    public boolean delAssignByCityId(String cityId)
    {
        this.jdbcOperation.getIbatisDaoSupport().delete("CustomerDAOImpl.delAssignByCityId", cityId);

        return true;
    }
}
