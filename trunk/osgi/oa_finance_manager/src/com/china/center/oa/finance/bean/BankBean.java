/*
 * File Name: BankBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.publics.bean.DutyBean;


/**
 * 银行
 * 
 * @author ZHUZHU
 * @version 2007-12-16
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_BANK")
public class BankBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "帐户", must = true, maxLength = 40)
    private String name = "";

    @Html(title = "类型", type = Element.SELECT)
    private int type = FinanceConstant.BANK_TYPE_NOTDUTY;

    @FK
    @Join(tagClass = DutyBean.class)
    @Html(title = "纳税实体", type = Element.SELECT)
    private String dutyId = "";

    /**
     * 生成科目所使用
     */
    @Ignore
    @Html(title = "科目编码", must = true, maxLength = 40)
    private String code = "";

    @Ignore
    private String parentTaxId = "";

    @Ignore
    private int unit = 0;

    @Ignore
    private int department = 0;

    @Ignore
    private int staffer = 0;

    /**
     * 余额
     */
    private double total = 0.0d;

    @Html(title = "备注", maxLength = 100, type = Element.TEXTAREA)
    private String description = "";

    /**
     * default constructor
     */
    public BankBean()
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
     * @return the dutyId
     */
    public String getDutyId()
    {
        return dutyId;
    }

    /**
     * @param dutyId
     *            the dutyId to set
     */
    public void setDutyId(String dutyId)
    {
        this.dutyId = dutyId;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the total
     */
    public double getTotal()
    {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(double total)
    {
        this.total = total;
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
     * @return the parentTaxId
     */
    public String getParentTaxId()
    {
        return parentTaxId;
    }

    /**
     * @param parentTaxId
     *            the parentTaxId to set
     */
    public void setParentTaxId(String parentTaxId)
    {
        this.parentTaxId = parentTaxId;
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
            .append("BankBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("name = ")
            .append(this.name)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("dutyId = ")
            .append(this.dutyId)
            .append(TAB)
            .append("code = ")
            .append(this.code)
            .append(TAB)
            .append("parentTaxId = ")
            .append(this.parentTaxId)
            .append(TAB)
            .append("unit = ")
            .append(this.unit)
            .append(TAB)
            .append("department = ")
            .append(this.department)
            .append(TAB)
            .append("staffer = ")
            .append(this.staffer)
            .append(TAB)
            .append("total = ")
            .append(this.total)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
