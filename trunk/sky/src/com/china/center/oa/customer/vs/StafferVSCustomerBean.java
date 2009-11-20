/**
 * File Name: OrgBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.vs;


import java.io.Serializable;

import com.china.center.annosql.constant.AnoConstant;
import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;
import com.china.center.oa.customer.bean.CustomerBean;


/**
 * OrgBean
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_VS_STACUS")
public class StafferVSCustomerBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    private String stafferId = "";

    @Unique
    @FK(index = AnoConstant.FK_FIRST)
    @Join(tagClass = CustomerBean.class)
    private String customerId = "";

    /**
     * default constructor
     */
    public StafferVSCustomerBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the stafferId
     */
    public String getStafferId()
    {
        return stafferId;
    }

    /**
     * @param stafferId
     *            the stafferId to set
     */
    public void setStafferId(String stafferId)
    {
        this.stafferId = stafferId;
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
