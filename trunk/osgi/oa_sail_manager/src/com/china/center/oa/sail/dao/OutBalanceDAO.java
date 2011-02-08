/**
 * File Name: OutBalanceDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao;


import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.vo.OutBalanceVO;


/**
 * OutBalanceDAO
 * 
 * @author ZHUZHU
 * @version 2010-12-4
 * @see OutBalanceDAO
 * @since 3.0
 */
public interface OutBalanceDAO extends DAO<OutBalanceBean, OutBalanceVO>
{
    boolean updateInvoiceStatus(String id, double invoiceMoney, int invoiceStatus);
}
