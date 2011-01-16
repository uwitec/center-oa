/**
 * File Name: InvoiceDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-9-19<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao.impl;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.dao.InvoiceDAO;


/**
 * InvoiceDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-9-19
 * @see InvoiceDAOImpl
 * @since 1.0
 */
public class InvoiceDAOImpl extends BaseDAO<InvoiceBean, InvoiceBean> implements InvoiceDAO
{
    public List<InvoiceBean> listForwardIn()
    {
        ConditionParse condition = new ConditionParse();

        condition.addIntCondition("forward", "=", InvoiceConstant.INVOICE_FORWARD_IN);

        return this.queryEntityBeansByCondition(condition);
    }

    public List<InvoiceBean> listForwardOut()
    {
        ConditionParse condition = new ConditionParse();

        condition.addIntCondition("forward", "=", InvoiceConstant.INVOICE_FORWARD_OUT);

        return this.queryEntityBeansByCondition(condition);
    }
}
