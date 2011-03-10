/**
 * File Name: CheckViewDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-3-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.tax.bean.CheckViewBean;
import com.china.center.oa.tax.dao.CheckViewDAO;


/**
 * CheckViewDAOImpl
 * 
 * @author ZHUZHU
 * @version 2011-3-9
 * @see CheckViewDAOImpl
 * @since 3.0
 */
public class CheckViewDAOImpl extends BaseDAO<CheckViewBean, CheckViewBean> implements CheckViewDAO
{
    public boolean updateCheck(String tableName, String id, String reason)
    {
        String sql = "update " + tableName + " set checkStatus = ?, checks = ? where id = ?";

        this.jdbcOperation.update(sql, PublicConstant.CHECK_STATUS_END, reason, id);

        return true;
    }
}
