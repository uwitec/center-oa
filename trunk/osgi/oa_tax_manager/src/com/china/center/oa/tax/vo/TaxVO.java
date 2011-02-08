/**
 * File Name: TaxVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-31<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.constanst.TaxConstanst;


/**
 * TaxVO
 * 
 * @author ZHUZHU
 * @version 2011-1-31
 * @see TaxVO
 * @since 1.0
 */
@Entity(inherit = true)
public class TaxVO extends TaxBean
{
    @Relationship(relationField = "ptype")
    private String ptypeName = "";

    @Relationship(relationField = "refId")
    private String refName = "";

    @Ignore
    private String other = "";

    /**
     * default constructor
     */
    public TaxVO()
    {
    }

    public String getOther()
    {
        StringBuilder sb = new StringBuilder();

        if (getUnit() == TaxConstanst.TAX_CHECK_YES)
        {
            sb.append("单位").append("/");
        }

        if (getDepartment() == TaxConstanst.TAX_CHECK_YES)
        {
            sb.append("部门").append("/");
        }

        if (getStaffer() == TaxConstanst.TAX_CHECK_YES)
        {
            sb.append("职员").append("/");
        }

        if (sb.length() > 0)
        {
            sb.delete(sb.length() - 1, sb.length());
        }

        other = sb.toString();

        return other;
    }

    /**
     * @return the ptypeName
     */
    public String getPtypeName()
    {
        return ptypeName;
    }

    /**
     * @param ptypeName
     *            the ptypeName to set
     */
    public void setPtypeName(String ptypeName)
    {
        this.ptypeName = ptypeName;
    }

    /**
     * @param other
     *            the other to set
     */
    public void setOther(String other)
    {
        this.other = other;
    }

    /**
     * @return the refName
     */
    public String getRefName()
    {
        return refName;
    }

    /**
     * @param refName
     *            the refName to set
     */
    public void setRefName(String refName)
    {
        this.refName = refName;
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

        retValue.append("TaxVO ( ").append(super.toString()).append(TAB).append("ptypeName = ").append(this.ptypeName).append(
            TAB).append("refName = ").append(this.refName).append(TAB).append("other = ").append(this.other).append(TAB).append(
            " )");

        return retValue.toString();
    }

}
