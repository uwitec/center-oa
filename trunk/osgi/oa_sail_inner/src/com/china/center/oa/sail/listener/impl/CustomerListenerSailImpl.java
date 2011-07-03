/**
 * File Name: CustomerCreditListenerSailImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-20<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.listener.impl;


import com.china.center.common.MYException;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.listener.CustomerListener;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.helper.YYTools;


/**
 * CustomerCreditListenerSailImpl
 * 
 * @author ZHUZHU
 * @version 2010-11-20
 * @see CustomerListenerSailImpl
 * @since 3.0
 */
public class CustomerListenerSailImpl implements CustomerListener
{
    private OutDAO outDAO = null;

    /**
     * default constructor
     */
    public CustomerListenerSailImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.customer.listener.CustomerCreditListener#onNoPayBusiness(com.china.center.oa.customer.bean.CustomerBean)
     */
    public double onNoPayBusiness(CustomerBean bean)
    {
        return outDAO.sumNoPayBusiness(bean.getId(), YYTools.getFinanceBeginDate(), YYTools
            .getFinanceEndDate());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "CustomerListener.SailImpl";
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    public void onDelete(CustomerBean bean)
        throws MYException
    {
        if (outDAO.countCustomerInOut(bean.getId()) > 0)
        {
            throw new MYException("客户[%s]名下存在销售单,不能删除", bean.getName());
        }
    }

}
