/**
 * File Name: StafferVSCustomerVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-3-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.vo;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.customer.vs.StafferVSCustomerBean;

/**
 * StafferVSCustomerVO
 * 
 * @author zhuzhu
 * @version 2009-3-15
 * @see StafferVSCustomerVO
 * @since 1.0
 */
@Entity(inherit = true)
public class StafferVSCustomerVO extends StafferVSCustomerBean
{
    @Relationship(tagField="name", relationField = "customerId")
    private String customerName = "";
    
    @Relationship(tagField="code", relationField = "customerId")
    private String customerCode = "";

    /**
     * default constructor
     */
    public StafferVSCustomerVO()
    {}

    /**
     * @return the customerName
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode()
    {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode)
    {
        this.customerCode = customerCode;
    }
}
