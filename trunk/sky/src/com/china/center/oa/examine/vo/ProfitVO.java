/**
 * File Name: ProfitVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.examine.bean.ProfitBean;


/**
 * ProfitVO
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see ProfitVO
 * @since 1.0
 */
@Entity(inherit = true)
public class ProfitVO extends ProfitBean
{
    @Relationship(relationField = "customerId")
    private String customerName = "";
    
    @Relationship(relationField = "customerId" , tagField = "code")
    private String customerCode = "";
    
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    public ProfitVO()
    {}

    /**
     * @return the customerName
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * @param customerName
     *            the customerName to set
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

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
