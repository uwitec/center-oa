/**
 * File Name: CustomerLocationJobImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.job;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.publics.dao.LocationVSCityDAO;
import com.china.center.oa.publics.trigger.CommonJob;
import com.china.center.oa.publics.vs.LocationVSCityBean;


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

    private LocationVSCityDAO locationVSCityDAO = null;

    private CustomerDAO customerDAO = null;

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
    @Transactional(rollbackFor = {MYException.class})
    public void excuteJob()
    {
        try
        {
            List<LocationVSCityBean> list = locationVSCityDAO.listEntityBeans();

            customerDAO.initCustomerLocation();

            for (LocationVSCityBean locationVSCityBean : list)
            {
                customerDAO.updateCustomerLocationByCity(locationVSCityBean.getCityId(),
                    locationVSCityBean.getLocationId());
            }
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
    @Override
    public String getJobName()
    {
        return "CustomerLocationJob.Impl";
    }

}
