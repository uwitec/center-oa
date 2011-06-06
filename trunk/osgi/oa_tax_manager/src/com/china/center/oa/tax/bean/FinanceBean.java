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
import java.util.List;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.tax.constanst.TaxConstanst;


/**
 * 凭证
 * 
 * @author ZHUZHU
 * @version 2011-2-6
 * @see FinanceBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_FINANCE")
public class FinanceBean implements Serializable
{
    @Id
    private String id = "";

    private String name = "";

    /**
     * 凭证类型(管理还是税务)
     */
    private int type = TaxConstanst.FINANCE_TYPE_MANAGER;

    /**
     * 0:未人工核对 1:已核对(锁定)
     */
    private int status = TaxConstanst.FINANCE_STATUS_NOCHECK;

    /**
     * 凭证创建的类型(0:手工创建 其他是系统创建/也有不同分类)
     */
    private int createType = TaxConstanst.FINANCE_CREATETYPE_HAND;

    /**
     * 主关联单据
     */
    private String refId = "";

    /**
     * 管理凭证是不分纳税实体的(都在总部),税务凭证是分纳税实体的
     */
    @Join(tagClass = DutyBean.class)
    private String dutyId = PublicConstant.DEFAULR_DUTY_ID;

    @Join(tagClass = StafferBean.class, type = JoinType.LEFT)
    private String createrId = "";

    /**
     * 准确到微分,就是小数点后四位
     */
    private long inmoney = 0;

    /**
     * 准确到微分,就是小数点后四位
     */
    private long outmoney = 0;

    private String description = "";

    /**
     * 凭证时间
     */
    private String financeDate = "";

    /**
     * 入库时间
     */
    private String logTime = "";

    @Ignore
    private List<FinanceItemBean> itemList = null;

    /**
     * default constructor
     */
    public FinanceBean()
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
     * @return the createType
     */
    public int getCreateType()
    {
        return createType;
    }

    /**
     * @param createType
     *            the createType to set
     */
    public void setCreateType(int createType)
    {
        this.createType = createType;
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
     * @return the createrId
     */
    public String getCreaterId()
    {
        return createrId;
    }

    /**
     * @param createrId
     *            the createrId to set
     */
    public void setCreaterId(String createrId)
    {
        this.createrId = createrId;
    }

    /**
     * @return the inmoney
     */
    public long getInmoney()
    {
        return inmoney;
    }

    /**
     * @param inmoney
     *            the inmoney to set
     */
    public void setInmoney(long inmoney)
    {
        this.inmoney = inmoney;
    }

    /**
     * @return the outmoney
     */
    public long getOutmoney()
    {
        return outmoney;
    }

    /**
     * @param outmoney
     *            the outmoney to set
     */
    public void setOutmoney(long outmoney)
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
     * @return the itemList
     */
    public List<FinanceItemBean> getItemList()
    {
        return itemList;
    }

    /**
     * @param itemList
     *            the itemList to set
     */
    public void setItemList(List<FinanceItemBean> itemList)
    {
        this.itemList = itemList;
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
     * @return the financeDate
     */
    public String getFinanceDate()
    {
        return financeDate;
    }

    /**
     * @param financeDate
     *            the financeDate to set
     */
    public void setFinanceDate(String financeDate)
    {
        this.financeDate = financeDate;
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
            .append("FinanceBean ( ")
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
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("createType = ")
            .append(this.createType)
            .append(TAB)
            .append("refId = ")
            .append(this.refId)
            .append(TAB)
            .append("dutyId = ")
            .append(this.dutyId)
            .append(TAB)
            .append("createrId = ")
            .append(this.createrId)
            .append(TAB)
            .append("inmoney = ")
            .append(this.inmoney)
            .append(TAB)
            .append("outmoney = ")
            .append(this.outmoney)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append("financeDate = ")
            .append(this.financeDate)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("itemList = ")
            .append(this.itemList)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
