/**
 * File Name: InBillDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.dao;


import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.vo.InBillVO;


/**
 * InBillDAO
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see InBillDAO
 * @since 3.0
 */
public interface InBillDAO extends DAO<InBillBean, InBillVO>
{
    double sumByPaymentId(String paymentId);

    /**
     * sumByOutId(这里包括申请关联的)
     * 
     * @param outId
     * @return
     */
    double sumByOutId(String outId);
}
