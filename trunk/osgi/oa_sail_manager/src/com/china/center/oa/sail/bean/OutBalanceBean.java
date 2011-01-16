/**
 * File Name: OutBalanceBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.sail.constanst.OutConstant;


/**
 * OutBalanceBean(委托结算单)
 * 
 * @author ZHUZHU
 * @version 2010-12-4
 * @see OutBalanceBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_OUTBALANCE")
public class OutBalanceBean implements Serializable
{
    @Id
    private String id = "";

    @FK
    private String outId = "";

    private double total = 0.0d;

    private int status = OutConstant.OUTBALANCE_STATUS_SUBMIT;

    private int type = OutConstant.OUTBALANCE_TYPE_SAIL;

    private String logTime = "";

    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    private String description = "";

    private String reason = "";

    /**
     * 退货库
     */
    @Join(tagClass = DepotBean.class, type = JoinType.LEFT)
    private String dirDepot = "";

    @Ignore
    private List<BaseBalanceBean> baseBalanceList = null;

    /**
     * default constructor
     */
    public OutBalanceBean()
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
     * @return the outId
     */
    public String getOutId()
    {
        return outId;
    }

    /**
     * @param outId
     *            the outId to set
     */
    public void setOutId(String outId)
    {
        this.outId = outId;
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
     * @return the baseBalanceList
     */
    public List<BaseBalanceBean> getBaseBalanceList()
    {
        return baseBalanceList;
    }

    /**
     * @param baseBalanceList
     *            the baseBalanceList to set
     */
    public void setBaseBalanceList(List<BaseBalanceBean> baseBalanceList)
    {
        this.baseBalanceList = baseBalanceList;
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
     * @return the reason
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason)
    {
        this.reason = reason;
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
     * @return the dirDepot
     */
    public String getDirDepot()
    {
        return dirDepot;
    }

    /**
     * @param dirDepot
     *            the dirDepot to set
     */
    public void setDirDepot(String dirDepot)
    {
        this.dirDepot = dirDepot;
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
            .append("OutBalanceBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("outId = ")
            .append(this.outId)
            .append(TAB)
            .append("total = ")
            .append(this.total)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append("reason = ")
            .append(this.reason)
            .append(TAB)
            .append("dirDepot = ")
            .append(this.dirDepot)
            .append(TAB)
            .append("baseBalanceList = ")
            .append(this.baseBalanceList)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
