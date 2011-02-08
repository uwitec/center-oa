/**
 * File Name: TaxTypeBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-30<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.Unique;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.tax.constanst.TaxConstanst;


/**
 * 科目表
 * 
 * @author ZHUZHU
 * @version 2011-1-30
 * @see TaxBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_TAX")
public class TaxBean implements Serializable
{
    @Id
    private String id = "";

    @Unique
    @Html(title = "编码", must = true, maxLength = 40)
    private String code = "";

    @Html(title = "名称", must = true, maxLength = 40)
    private String name = "";

    /**
     * 关联银行
     */
    @Join(tagClass = BankBean.class, type = JoinType.LEFT)
    @Html(title = "关联银行", type = Element.SELECT)
    private String refId = "";

    @FK
    @Join(tagClass = TaxTypeBean.class)
    @Html(title = "分类", must = true, type = Element.SELECT)
    private String ptype = "";

    @Html(title = "类型", must = true, type = Element.SELECT)
    private int type = TaxConstanst.TAX_TYPE_CASH;

    private int status = 0;

    @Html(title = "余额方向", must = true, type = Element.SELECT)
    private int forward = TaxConstanst.TAX_FORWARD_IN;

    private int unit = TaxConstanst.TAX_CHECK_NO;

    private int department = TaxConstanst.TAX_CHECK_NO;

    private int staffer = TaxConstanst.TAX_CHECK_NO;

    /**
     * default constructor
     */
    public TaxBean()
    {
    }

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
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return the ptype
     */
    public String getPtype()
    {
        return ptype;
    }

    /**
     * @param ptype
     *            the ptype to set
     */
    public void setPtype(String ptype)
    {
        this.ptype = ptype;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /**
     * @return the forward
     */
    public int getForward()
    {
        return forward;
    }

    /**
     * @param forward
     *            the forward to set
     */
    public void setForward(int forward)
    {
        this.forward = forward;
    }

    /**
     * @return the unit
     */
    public int getUnit()
    {
        return unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(int unit)
    {
        this.unit = unit;
    }

    /**
     * @return the department
     */
    public int getDepartment()
    {
        return department;
    }

    /**
     * @param department
     *            the department to set
     */
    public void setDepartment(int department)
    {
        this.department = department;
    }

    /**
     * @return the staffer
     */
    public int getStaffer()
    {
        return staffer;
    }

    /**
     * @param staffer
     *            the staffer to set
     */
    public void setStaffer(int staffer)
    {
        this.staffer = staffer;
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

        retValue.append("TaxBean ( ").append(super.toString()).append(TAB).append("id = ").append(this.id).append(TAB).append(
            "code = ").append(this.code).append(TAB).append("name = ").append(this.name).append(TAB).append("ptype = ").append(
            this.ptype).append(TAB).append("type = ").append(this.type).append(TAB).append("status = ").append(
            this.status).append(TAB).append("forward = ").append(this.forward).append(TAB).append("unit = ").append(
            this.unit).append(TAB).append("department = ").append(this.department).append(TAB).append("staffer = ").append(
            this.staffer).append(TAB).append(" )");

        return retValue.toString();
    }

    /**
     * @return the refId
     */
    public String getRefId()
    {
        return refId;
    }

    /**
     * @param refId
     *            the refId to set
     */
    public void setRefId(String refId)
    {
        this.refId = refId;
    }

}
