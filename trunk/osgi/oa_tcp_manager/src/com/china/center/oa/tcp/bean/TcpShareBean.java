package com.china.center.oa.tcp.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * 费用分担
 * 
 * @author ZHUZHU
 * @version 2011-7-10
 * @see TcpShareBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_TCPSHARE")
public class TcpShareBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    private String refId = "";

    @Join(tagClass = BudgetBean.class)
    private String budgetId = "";

    @Join(tagClass = PrincipalshipBean.class)
    private String departmentId = "";

    /**
     * 权签人
     */
    @Join(tagClass = StafferBean.class)
    private String approverId = "";

    private int ratio = 0;

    /**
     * default constructor
     */
    public TcpShareBean()
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

    /**
     * @return the budgetId
     */
    public String getBudgetId()
    {
        return budgetId;
    }

    /**
     * @param budgetId
     *            the budgetId to set
     */
    public void setBudgetId(String budgetId)
    {
        this.budgetId = budgetId;
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
     * @return the approverId
     */
    public String getApproverId()
    {
        return approverId;
    }

    /**
     * @param approverId
     *            the approverId to set
     */
    public void setApproverId(String approverId)
    {
        this.approverId = approverId;
    }

    /**
     * @return the ratio
     */
    public int getRatio()
    {
        return ratio;
    }

    /**
     * @param ratio
     *            the ratio to set
     */
    public void setRatio(int ratio)
    {
        this.ratio = ratio;
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
            .append("TcpShareBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("refId = ")
            .append(this.refId)
            .append(TAB)
            .append("budgetId = ")
            .append(this.budgetId)
            .append(TAB)
            .append("departmentId = ")
            .append(this.departmentId)
            .append(TAB)
            .append("approverId = ")
            .append(this.approverId)
            .append(TAB)
            .append("ratio = ")
            .append(this.ratio)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
