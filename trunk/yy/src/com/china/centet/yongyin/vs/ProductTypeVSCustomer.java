/**
 *
 */
package com.china.centet.yongyin.vs;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Table;


/**
 * VS bean
 * 
 * @author Administrator
 */
@Entity
@Table(name = "T_CENTER_PRODUCTTYPEVSCUSTOMER")
public class ProductTypeVSCustomer implements Serializable
{
    @FK(index = 0)
    private String productTypeId = "";

    @FK(index = 1)
    private String customerId = "";

    /**
     *
     */
    public ProductTypeVSCustomer()
    {}

    /**
     * @return the productTypeId
     */
    public String getProductTypeId()
    {
        return productTypeId;
    }

    /**
     * @param productTypeId
     *            the productTypeId to set
     */
    public void setProductTypeId(String productTypeId)
    {
        this.productTypeId = productTypeId;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
}
