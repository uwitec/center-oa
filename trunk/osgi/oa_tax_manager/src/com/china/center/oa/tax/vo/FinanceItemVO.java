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
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.tools.MathTools;


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

    @Relationship(relationField = "taxId0")
    private String taxName0 = "";

    @Relationship(relationField = "taxId1")
    private String taxName1 = "";

    @Relationship(relationField = "taxId2")
    private String taxName2 = "";

    @Relationship(relationField = "taxId3")
    private String taxName3 = "";

    @Ignore
    private String unitName = "";

    @Ignore
    private String departmentName = "";

    @Ignore
    private String stafferName = "";

    @Ignore
    private String depotName = "";

    @Ignore
    private String productName = "";

    @Ignore
    private String duty2Name = "";

    /**
     * 显示金额
     */
    @Ignore
    private String showInmoney = "";

    /**
     * 显示金额
     */
    @Ignore
    private String showOutmoney = "";

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
     * @return the showInmoney
     */
    public String getShowInmoney()
    {
        double inM = super.getInmoney() + 0.0d;

        this.showInmoney = MathTools.formatNum(inM / TaxConstanst.DOUBLE_TO_INT);

        return showInmoney;
    }

    /**
     * @param showInmoney
     *            the showInmoney to set
     */
    public void setShowInmoney(String showInmoney)
    {
        this.showInmoney = showInmoney;
    }

    /**
     * @return the showOutmoney
     */
    public String getShowOutmoney()
    {
        double outM = super.getOutmoney() + 0.0d;

        this.showOutmoney = MathTools.formatNum(outM / TaxConstanst.DOUBLE_TO_INT);

        return showOutmoney;
    }

    /**
     * @param showOutmoney
     *            the showOutmoney to set
     */
    public void setShowOutmoney(String showOutmoney)
    {
        this.showOutmoney = showOutmoney;
    }

    /**
     * @return the depotName
     */
    public String getDepotName()
    {
        return depotName;
    }

    /**
     * @param depotName
     *            the depotName to set
     */
    public void setDepotName(String depotName)
    {
        this.depotName = depotName;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    /**
     * @return the duty2Name
     */
    public String getDuty2Name()
    {
        return duty2Name;
    }

    /**
     * @param duty2Name
     *            the duty2Name to set
     */
    public void setDuty2Name(String duty2Name)
    {
        this.duty2Name = duty2Name;
    }

    /**
     * @return the taxName0
     */
    public String getTaxName0()
    {
        return taxName0;
    }

    /**
     * @param taxName0
     *            the taxName0 to set
     */
    public void setTaxName0(String taxName0)
    {
        this.taxName0 = taxName0;
    }

    /**
     * @return the taxName1
     */
    public String getTaxName1()
    {
        return taxName1;
    }

    /**
     * @param taxName1
     *            the taxName1 to set
     */
    public void setTaxName1(String taxName1)
    {
        this.taxName1 = taxName1;
    }

    /**
     * @return the taxName2
     */
    public String getTaxName2()
    {
        return taxName2;
    }

    /**
     * @param taxName2
     *            the taxName2 to set
     */
    public void setTaxName2(String taxName2)
    {
        this.taxName2 = taxName2;
    }

    /**
     * @return the taxName3
     */
    public String getTaxName3()
    {
        return taxName3;
    }

    /**
     * @param taxName3
     *            the taxName3 to set
     */
    public void setTaxName3(String taxName3)
    {
        this.taxName3 = taxName3;
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
            .append("FinanceItemVO ( ")
            .append(super.toString())
            .append(TAB)
            .append("taxName = ")
            .append(this.taxName)
            .append(TAB)
            .append("taxName0 = ")
            .append(this.taxName0)
            .append(TAB)
            .append("taxName1 = ")
            .append(this.taxName1)
            .append(TAB)
            .append("taxName2 = ")
            .append(this.taxName2)
            .append(TAB)
            .append("taxName3 = ")
            .append(this.taxName3)
            .append(TAB)
            .append("unitName = ")
            .append(this.unitName)
            .append(TAB)
            .append("departmentName = ")
            .append(this.departmentName)
            .append(TAB)
            .append("stafferName = ")
            .append(this.stafferName)
            .append(TAB)
            .append("depotName = ")
            .append(this.depotName)
            .append(TAB)
            .append("productName = ")
            .append(this.productName)
            .append(TAB)
            .append("duty2Name = ")
            .append(this.duty2Name)
            .append(TAB)
            .append("showInmoney = ")
            .append(this.showInmoney)
            .append(TAB)
            .append("showOutmoney = ")
            .append(this.showOutmoney)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
