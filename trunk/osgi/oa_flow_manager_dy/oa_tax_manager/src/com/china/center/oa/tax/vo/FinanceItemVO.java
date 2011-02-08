/**
 * File Name: FinanceVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.tax.bean.FinanceItemBean;


/**
 * FinanceVO
 * 
 * @author ZHUZHU
 * @version 2011-2-6
 * @see FinanceItemVO
 * @since 1.0
 */
@Entity(inherit = true)
public class FinanceItemVO extends FinanceItemBean
{
    @Relationship(relationField = "taxId")
    private String taxName = "";

    @Ignore
    private String unitName = "";

    @Ignore
    private String departmentName = "";

    @Ignore
    private String stafferName = "";

    /**
     * default constructor
     */
    public FinanceItemVO()
    {
    }

    /**
     * @return the unitName
     */
    public String getUnitName()
    {
        return unitName;
    }

    /**
     * @param unitName
     *            the unitName to set
     */
    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName()
    {
        return departmentName;
    }

    /**
     * @param departmentName
     *            the departmentName to set
     */
    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuffer retValue = new StringBuffer();

        retValue.append("FinanceItemVO ( ").append(super.toString()).append(TAB).append("taxName = ").append(
            this.taxName).append(TAB).append("unitName = ").append(this.unitName).append(TAB).append(
            "departmentName = ").append(this.departmentName).append(TAB).append("stafferName = ").append(
            this.stafferName).append(TAB).append(" )");

        return retValue.toString();
    }

}
