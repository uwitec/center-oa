/**
 * File Name: PaymentBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import java.io.Serializable;

import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.FK;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * PaymentBean
 * 
 * @author ZHUZHU
 * @version 2010-12-22
 * @see PaymentBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_PAYMENT")
public class PaymentBean implements Serializable
{
    @Id
    private String id = "";

    private String name = "";

    private String fromer = "";

    @FK(index = AnoConstant.FK_DEFAULT)
    @Join(tagClass = BankBean.class)
    private String bankId = "";

    @FK(index = AnoConstant.FK_FIRST)
    @Join(tagClass = StafferBean.class, type = JoinType.LEFT)
    private String stafferId = "";

    private int type = FinanceConstant.PAYMENT_PAY_PUBLIC;

    private int status = FinanceConstant.PAYMENT_STATUS_INIT;

    private int useall = FinanceConstant.PAYMENT_USEALL_INIT;

    /**
     * 已经被使用金额
     */
    private double useMoney = 0.0d;

    private double money = 0.0d;

    /**
     * 回款日期
     */
    private String receiveTime = "";

    /**
     * 导入时间
     */
    private String logTime = "";

    private String description = "";

    /**
     * default constructor
     */
    public PaymentBean()
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
     * @return the fromer
     */
    public String getFromer()
    {
        return fromer;
    }

    /**
     * @param fromer
     *            the fromer to set
     */
    public void setFromer(String fromer)
    {
        this.fromer = fromer;
    }

    /**
     * @return the bankId
     */
    public String getBankId()
    {
        return bankId;
    }

    /**
     * @param bankId
     *            the bankId to set
     */
    public void setBankId(String bankId)
    {
        this.bankId = bankId;
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
     * @return the money
     */
    public double getMoney()
    {
        return money;
    }

    /**
     * @param money
     *            the money to set
     */
    public void setMoney(double money)
    {
        this.money = money;
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
     * @return the receiveTime
     */
    public String getReceiveTime()
    {
        return receiveTime;
    }

    /**
     * @param receiveTime
     *            the receiveTime to set
     */
    public void setReceiveTime(String receiveTime)
    {
        this.receiveTime = receiveTime;
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
     * @return the useall
     */
    public int getUseall()
    {
        return useall;
    }

    /**
     * @param useall
     *            the useall to set
     */
    public void setUseall(int useall)
    {
        this.useall = useall;
    }

    /**
     * @return the useMoney
     */
    public double getUseMoney()
    {
        return useMoney;
    }

    /**
     * @param useMoney
     *            the useMoney to set
     */
    public void setUseMoney(double useMoney)
    {
        this.useMoney = useMoney;
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
            .append("PaymentBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("name = ")
            .append(this.name)
            .append(TAB)
            .append("fromer = ")
            .append(this.fromer)
            .append(TAB)
            .append("bankId = ")
            .append(this.bankId)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("useall = ")
            .append(this.useall)
            .append(TAB)
            .append("useMoney = ")
            .append(this.useMoney)
            .append(TAB)
            .append("money = ")
            .append(this.money)
            .append(TAB)
            .append("receiveTime = ")
            .append(this.receiveTime)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
