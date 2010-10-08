/**
 * File Name: CustomerLocationJobImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.job;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.oa.customer.manager.CustomerManager;
import com.china.center.oa.publics.trigger.CommonJob;


/**
 * CustomerLocationJobImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see CustomerLocationJobImpl
 * @since 1.0
 */
public class CustomerLocationJobImpl implements CommonJob
{
    private final Log _logger = LogFactory.getLog(getClass());

    private CustomerManager customerManager = null;

    /**
     * default constructor
     */
    public CustomerLocationJobImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.publics.trigger.CommonJob#excuteJob()
     */
    public void excuteJob()
    {
        try
        {
            customerManager.synchronizationAllCustomerLocation();
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.publics.trigger.CommonJob#getJobName()
     */
    public String getJobName()
    {
        return "CustomerLocationJob.Impl";
    }

    /**
     * @return the customerManager
     */
    public CustomerManager getCustomerManager()
    {
        return customerManager;
    }

    /**
     * @param customerManager
     *            the customerManager to set
     */
    public void setCustomerManager(CustomerManager customerManager)
    {
        this.customerManager = customerManager;
    }

}
