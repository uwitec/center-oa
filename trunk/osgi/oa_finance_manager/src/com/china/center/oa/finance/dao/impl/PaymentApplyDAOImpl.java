/**
 * File Name: PayApplyDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.PaymentApplyDAO;
import com.china.center.oa.finance.vo.PaymentApplyVO;


/**
 * PayApplyDAOImpl
 * 
 * @author ZHUZHU
 * @version 2011-1-8
 * @see PaymentApplyDAOImpl
 * @since 3.0
 */
public class PaymentApplyDAOImpl extends BaseDAO<PaymentApplyBean, PaymentApplyVO> implements PaymentApplyDAO
{
    public int countApplyByOutId(String outId)
    {
        return this.jdbcOperation
            .queryForInt(
                "select count(t1.id) from T_CENTER_PAYAPPLY t1, T_CENTER_VS_OUTPAY t2 where t1.id = t2.parentId and t1.status = ? and t2.outId = ?",
                FinanceConstant.PAYAPPLY_STATUS_INIT, outId);
    }
}
