/**
 * File Name: FinanceBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;


/**
 * 凭证项
 * 
 * @author ZHUZHU
 * @version 2011-2-6
 * @see FinanceItemBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_FINANCEITEM")
public class FinanceItemBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    private String pid = "";

    private String pareId = "";

    private String name = "";

    private int type = 0;

    private int forward = 0;

    @Join(tagClass = TaxBean.class)
    private String taxId = "";

    private String dutyId = "";

    private double inmoney = 0.0d;

    private double outmoney = 0.0d;

    private String description = "";

    private String logTime = "";

    private String unitId = "";

    private int unitType = 0;

    private String departmentId = "";

    private String stafferId = "";

    /**
     * default constructor
     */
    public FinanceItemBean()
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
     * @return the pid
     */
    public String getPid()
    {
        return pid;
    }

    /**
     * @param pid
     *            the pid to set
     */
    public void setPid(String pid)
    {
        this.pid = pid;
    }

    /**
     * @return the pareId
     */
    public String getPareId()
    {
        return pareId;
    }

    /**
     * @param pareId
     *            the pareId to set
     */
    public void setPareId(String pareId)
    {
        this.pareId = pareId;
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
     * @return the taxId
     */
    public String getTaxId()
    {
        return taxId;
    }

    /**
     * @param taxId
     *            the taxId to set
     */
    public void setTaxId(String taxId)
    {
        this.taxId = taxId;
    }

    /**
     * @return the inmoney
     */
    public double getInmoney()
    {
        return inmoney;
    }

    /**
     * @param inmoney
     *            the inmoney to set
     */
    public void setInmoney(double inmoney)
    {
        this.inmoney = inmoney;
    }

    /**
     * @return the outmoney
     */
    public double getOutmoney()
    {
        return outmoney;
    }

    /**
     * @param outmoney
     *            the outmoney to set
     */
    public void setOutmoney(double outmoney)
    {
        this.outmoney = outmoney;
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
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime
     *            the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    /**
     * @return the unitId
     */
    public String getUnitId()
    {
        return unitId;
    }

    /**
     * @param unitId
     *            the unitId to set
     */
    public void setUnitId(String unitId)
    {
        this.unitId = unitId;
    }

    /**
     * @return the unitType
     */
    public int getUnitType()
    {
        return unitType;
    }

    /**
     * @param unitType
     *            the unitType to set
     */
    public void setUnitType(int unitType)
    {
        this.unitType = unitType;
    }

    /**
     * @return the departmentId
     */
    public String getDepartmentId()
    {
        return departmentId;
    }

    /**
     * @param departmentId
     *            the departmentId to set
     */
    public void setDepartmentId(String departmentId)
    {
        this.departmentId = departmentId;
    }

    /**
     * @return the stafferId
     */
    public String getStafferId()
    {
        return stafferId;
    }

    /**
     * @param stafferId
     *            the stafferId to set
     */
    public void setStafferId(String stafferId)
    {
        this.stafferId = stafferId;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuffer retValue = new StringBuffer();

        retValue.append("FinanceItemBean ( ").append(super.toString()).append(TAB).append("id = ").append(this.id).append(
            TAB).append("pid = ").append(this.pid).append(TAB).append("pareId = ").append(this.pareId).append(TAB).append(
            "name = ").append(this.name).append(TAB).append("type = ").append(this.type).append(TAB).append(
            "forward = ").append(this.forward).append(TAB).append("taxId = ").append(this.taxId).append(TAB).append(
            "dutyId = ").append(this.dutyId).append(TAB).append("inmoney = ").append(this.inmoney).append(TAB).append(
            "outmoney = ").append(this.outmoney).append(TAB).append("description = ").append(this.description).append(
            TAB).append("logTime = ").append(this.logTime).append(TAB).append("unitId = ").append(this.unitId).append(
            TAB).append("unitType = ").append(this.unitType).append(TAB).append("departmentId = ").append(
            this.departmentId).append(TAB).append("stafferId = ").append(this.stafferId).append(TAB).append(" )");

        return retValue.toString();
    }

}
