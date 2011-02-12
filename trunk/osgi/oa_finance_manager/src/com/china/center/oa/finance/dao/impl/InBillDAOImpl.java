/**
 * File Name: InBillDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.dao.impl;


import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.vo.InBillVO;


/**
 * InBillDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see InBillDAOImpl
 * @since 3.0
 */
public class InBillDAOImpl extends BaseDAO<InBillBean, InBillVO> implements InBillDAO
{
    public double sumByPaymentId(String paymentId)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "moneys")
                                                 + "where InBillBean.paymentId = ?", paymentId);
    }

    public double sumByOutId(String outId)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "moneys")
                                                 + "where InBillBean.outId = ?", outId);
    }

    public double sumByCondition(ConditionParse condition)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "moneys")
                                                 + condition.toString());
    }

    public int lockByCondition(ConditionParse condition)
    {
        return this.jdbcOperation.update("set InBillBean.ulock = ? " + condition.toString(), claz,
            FinanceConstant.BILL_LOCK_YES);
    }

    public double sumByOutBalanceId(String outBalanceId)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "moneys")
                                                 + "where InBillBean.outBalanceId = ?",
            outBalanceId);
    }
}
