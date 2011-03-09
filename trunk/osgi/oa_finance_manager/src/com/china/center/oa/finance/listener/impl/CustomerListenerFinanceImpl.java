/**
 * File Name: CustomerListenerFinanceImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-3-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener.impl;


import com.china.center.common.MYException;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.listener.CustomerListener;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;


/**
 * CustomerListenerFinanceImpl
 * 
 * @author ZHUZHU
 * @version 2011-3-9
 * @see CustomerListenerFinanceImpl
 * @since 3.0
 */
public class CustomerListenerFinanceImpl implements CustomerListener
{
    private InBillDAO inBillDAO = null;

    private OutBillDAO outBillDAO = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.customer.listener.CustomerListener#onDelete(com.china.center.oa.customer.bean.CustomerBean)
     */
    public void onDelete(CustomerBean bean)
        throws MYException
    {
        if (inBillDAO.countUnitInBill(bean.getId()) > 0)
        {
            throw new MYException("客户[%s]已经被收款单使用,不能删除", bean.getName());
        }

        if (outBillDAO.countUnitInBill(bean.getId()) > 0)
        {
            throw new MYException("客户[%s]已经被付款单使用,不能删除", bean.getName());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.customer.listener.CustomerListener#onNoPayBusiness(com.china.center.oa.customer.bean.CustomerBean)
     */
    public double onNoPayBusiness(CustomerBean bean)
    {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "CustomerListener.FinanceImpl";
    }

    /**
     * @return the inBillDAO
     */
    public InBillDAO getInBillDAO()
    {
        return inBillDAO;
    }

    /**
     * @param inBillDAO
     *            the inBillDAO to set
     */
    public void setInBillDAO(InBillDAO inBillDAO)
    {
        this.inBillDAO = inBillDAO;
    }

    /**
     * @return the outBillDAO
     */
    public OutBillDAO getOutBillDAO()
    {
        return outBillDAO;
    }

    /**
     * @param outBillDAO
     *            the outBillDAO to set
     */
    public void setOutBillDAO(OutBillDAO outBillDAO)
    {
        this.outBillDAO = outBillDAO;
    }

}
