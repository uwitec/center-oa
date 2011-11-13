/**
 * File Name: InBillVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.finance.bean.InBillBean;


/**
 * InBillVO
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see InBillVO
 * @since 3.0
 */
@Entity(inherit = true)
public class InBillVO extends InBillBean
{
    @Relationship(relationField = "bankId")
    private String bankName = "";

    @Relationship(relationField = "customerId")
    private String customerName = "";

    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "ownerId")
    private String ownerName = "";

    @Relationship(relationField = "paymentId", tagField = "description")
    private String paymentDes = "";

    /**
     * default constructor
     */
    public InBillVO()
    {
    }

    /**
     * @return the bankName
     */
    public String getBankName()
    {
        return bankName;
    }

    /**
     * @param bankName
     *            the bankName to set
     */
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

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
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

    /**
     * @return the ownerName
     */
    public String getOwnerName()
    {
        return ownerName;
    }

    /**
     * @param ownerName
     *            the ownerName to set
     */
    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("InBillVO ( ")
            .append(super.toString())
            .append(TAB)
            .append("bankName = ")
            .append(this.bankName)
            .append(TAB)
            .append("customerName = ")
            .append(this.customerName)
            .append(TAB)
            .append("stafferName = ")
            .append(this.stafferName)
            .append(TAB)
            .append("ownerName = ")
            .append(this.ownerName)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

    /**
     * @return the paymentDes
     */
    public String getPaymentDes()
    {
        return paymentDes;
    }

    /**
     * @param paymentDes
     *            the paymentDes to set
     */
    public void setPaymentDes(String paymentDes)
    {
        this.paymentDes = paymentDes;
    }

}
