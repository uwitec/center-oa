/**
 * File Name: OutBalanceVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.sail.bean.OutBalanceBean;


/**
 * OutBalanceVO
 * 
 * @author ZHUZHU
 * @version 2010-12-4
 * @see OutBalanceVO
 * @since 3.0
 */
@Entity(inherit = true)
public class OutBalanceVO extends OutBalanceBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "customerId")
    private String customerName = "";

    @Relationship(relationField = "dirDepot")
    private String dirDepotName = "";

    @Relationship(relationField = "outId", tagField = "outTime")
    private String outTime = "";

    /**
     * default constructor
     */
    public OutBalanceVO()
    {
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
     * @return the dirDepotName
     */
    public String getDirDepotName()
    {
        return dirDepotName;
    }

    /**
     * @param dirDepotName
     *            the dirDepotName to set
     */
    public void setDirDepotName(String dirDepotName)
    {
        this.dirDepotName = dirDepotName;
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
     * @return the outTime
     */
    public String getOutTime()
    {
        return outTime;
    }

    /**
     * @param outTime
     *            the outTime to set
     */
    public void setOutTime(String outTime)
    {
        this.outTime = outTime;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuffer retValue = new StringBuffer();

        retValue.append("OutBalanceVO ( ").append(super.toString()).append(TAB).append("stafferName = ").append(
            this.stafferName).append(TAB).append("customerName = ").append(this.customerName).append(TAB).append(
            "dirDepotName = ").append(this.dirDepotName).append(TAB).append("outTime = ").append(this.outTime).append(
            TAB).append(" )");

        return retValue.toString();
    }

}
