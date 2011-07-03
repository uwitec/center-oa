/**
 * File Name: OutBalanceDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao.impl;


import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.vo.OutBalanceVO;


/**
 * OutBalanceDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-4
 * @see OutBalanceDAOImpl
 * @since 3.0
 */
public class OutBalanceDAOImpl extends BaseDAO<OutBalanceBean, OutBalanceVO> implements OutBalanceDAO
{
    public boolean updateInvoiceStatus(String id, double invoiceMoney, int invoiceStatus)
    {
        String sql = BeanTools.getUpdateHead(claz)
                     + "set invoiceMoney = ?, invoiceStatus = ? where id = ?";

        jdbcOperation.update(sql, invoiceMoney, invoiceStatus, id);

        return true;
    }

    public boolean updateHadPay(String id, double hadPay)
    {
        String sql = BeanTools.getUpdateHead(claz) + "set payMoney = ? where id = ?";

        jdbcOperation.update(sql, hadPay, id);

        return true;
    }

    public boolean updatePay(String id, int pay)
    {
        String sql = BeanTools.getUpdateHead(claz) + "set pay = ? where id = ?";

        jdbcOperation.update(sql, pay, id);

        return true;
    }

    public boolean updateCheck(String id, int checkStatus, String checks)
    {
        String sql = BeanTools.getUpdateHead(claz) + "set checkStatus = ?, checks = ? where id = ?";

        jdbcOperation.update(sql, checkStatus, checks, id);

        return true;
    }
}
