/**
 * File Name: AssignApplyVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.vo;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.customer.bean.AssignApplyBean;

/**
 * AssignApplyVO
 * 
 * @author zhuzhu
 * @version 2008-11-12
 * @see AssignApplyVO
 * @since 1.0
 */
@Entity(inherit = true)
public class AssignApplyVO extends AssignApplyBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "customerId")
    private String customerName = "";
    
    @Relationship(relationField = "customerId", tagField = "code")
    private String customerCode = "";
    
    @Relationship(relationField = "customerId", tagField = "sellType")
    private String customerSellType = "";

    /**
     * default constructor
     */
    public AssignApplyVO()
    {}

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

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
     * @return the customerSellType
     */
    public String getCustomerSellType()
    {
        return customerSellType;
    }

    /**
     * @param customerSellType the customerSellType to set
     */
    public void setCustomerSellType(String customerSellType)
    {
        this.customerSellType = customerSellType;
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
