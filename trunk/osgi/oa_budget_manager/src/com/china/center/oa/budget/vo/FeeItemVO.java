/**
 * File Name: FeeItemVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.budget.bean.FeeItemBean;


/**
 * FeeItemVO
 * 
 * @author ZHUZHU
 * @version 2011-7-9
 * @see FeeItemVO
 * @since 3.0
 */
@Entity(inherit = true)
public class FeeItemVO extends FeeItemBean
{
    @Relationship(relationField = "taxId")
    private String taxName = "";

    @Relationship(relationField = "taxId", tagField = "code")
    private String taxCode = "";

    /**
     * default constructor
     */
    public FeeItemVO()
    {
    }

    /**
     * @return the taxName
     */
    public String getTaxName()
    {
        return taxName;
    }

    /**
     * @param taxName
     *            the taxName to set
     */
    public void setTaxName(String taxName)
    {
        this.taxName = taxName;
    }

    /**
     * @return the taxCode
     */
    public String getTaxCode()
    {
        return taxCode;
    }

    /**
     * @param taxCode
     *            the taxCode to set
     */
    public void setTaxCode(String taxCode)
    {
        this.taxCode = taxCode;
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
            .append("FeeItemVO ( ")
            .append(super.toString())
            .append(TAB)
            .append("taxName = ")
            .append(this.taxName)
            .append(TAB)
            .append("taxCode = ")
            .append(this.taxCode)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
