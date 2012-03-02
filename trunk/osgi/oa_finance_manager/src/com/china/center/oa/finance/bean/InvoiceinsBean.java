/**
 * File Name: InvoiceinsBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.vs.InsVSOutBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.sail.bean.UnitViewBean;


/**
 * 发票实例
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see InvoiceinsBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_INVOICEINS")
public class InvoiceinsBean implements Serializable
{
    @Id
    private String id = "";

    /**
     * 发票类型
     */
    @Html(title = "发票类型", type = Element.SELECT)
    @Join(tagClass = InvoiceBean.class)
    private String invoiceId = "";

    /**
     * status
     */
    private int status = FinanceConstant.INVOICEINS_STATUS_INIT;

    /**
     * 类型
     */
    private int type = FinanceConstant.INVOICEINS_TYPE_COMMON;

    /**
     * 特殊类型
     */
    private int stype = FinanceConstant.INVOICEINS_STYPE_AXAX;

    /**
     * mtype
     */
    private int mtype = PublicConstant.MANAGER_TYPE_COMMON;

    /**
     * 关注类型
     */
    private int vtype = PublicConstant.VTYPE_DEFAULT;

    /**
     * 纳税实例
     */
    @Html(title = "纳税实体", must = true, type = Element.SELECT)
    @Join(tagClass = DutyBean.class)
    private String dutyId = "";

    /**
     * 开票单位
     */
    @Html(title = "开票单位", must = true, maxLength = 100)
    private String unit = "";

    /**
     * 暂时不使用
     */
    private String reveive = "";

    private String locationId = "";

    @Join(tagClass = UnitViewBean.class, type = JoinType.LEFT)
    private String customerId = "";

    /**
     * 总金额
     */
    @Html(title = "开票金额", must = true, type = Element.NUMBER)
    private double moneys = 0.0d;

    @Html(title = "开票日期", must = true, type = Element.DATE)
    private String invoiceDate = "";

    @Join(tagClass = StafferBean.class, type = JoinType.LEFT)
    private String stafferId = "";

    /**
     * 审批人
     */
    @Join(tagClass = StafferBean.class, type = JoinType.LEFT, alias = "StafferBean2")
    @Html(title = "会计审核人", must = true, type = Element.SELECT)
    private String processer = "";

    private String logTime = "";

    private String refIds = "";

    @Html(title = "备注", maxLength = 500, type = Element.TEXTAREA)
    private String description = "";

    /**
     * 总部核对信息
     */
    private String checks = "";

    /**
     * 关联单据
     */
    private String checkrefId = "";

    private int checkStatus = PublicConstant.CHECK_STATUS_INIT;

    @Ignore
    private List<InvoiceinsItemBean> itemList = null;

    @Ignore
    private List<InsVSOutBean> vsList = null;

    /**
     * default constructor
     */
    public InvoiceinsBean()
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
     * @return the invoiceId
     */
    public String getInvoiceId()
    {
        return invoiceId;
    }

    /**
     * @param invoiceId
     *            the invoiceId to set
     */
    public void setInvoiceId(String invoiceId)
    {
        this.invoiceId = invoiceId;
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
     * @return the unit
     */
    public String getUnit()
    {
        return unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    /**
     * @return the reveive
     */
    public String getReveive()
    {
        return reveive;
    }

    /**
     * @param reveive
     *            the reveive to set
     */
    public void setReveive(String reveive)
    {
        this.reveive = reveive;
    }

    /**
     * @return the moneys
     */
    public double getMoneys()
    {
        return moneys;
    }

    /**
     * @param moneys
     *            the moneys to set
     */
    public void setMoneys(double moneys)
    {
        this.moneys = moneys;
    }

    /**
     * @return the invoiceDate
     */
    public String getInvoiceDate()
    {
        return invoiceDate;
    }

    /**
     * @param invoiceDate
     *            the invoiceDate to set
     */
    public void setInvoiceDate(String invoiceDate)
    {
        this.invoiceDate = invoiceDate;
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
     * @return the itemList
     */
    public List<InvoiceinsItemBean> getItemList()
    {
        return itemList;
    }

    /**
     * @param itemList
     *            the itemList to set
     */
    public void setItemList(List<InvoiceinsItemBean> itemList)
    {
        this.itemList = itemList;
    }

    /**
     * @return the vsList
     */
    public List<InsVSOutBean> getVsList()
    {
        return vsList;
    }

    /**
     * @param vsList
     *            the vsList to set
     */
    public void setVsList(List<InsVSOutBean> vsList)
    {
        this.vsList = vsList;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    /**
     * @return the refIds
     */
    public String getRefIds()
    {
        return refIds;
    }

    /**
     * @param refIds
     *            the refIds to set
     */
    public void setRefIds(String refIds)
    {
        this.refIds = refIds;
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
     * @return the processer
     */
    public String getProcesser()
    {
        return processer;
    }

    /**
     * @param processer
     *            the processer to set
     */
    public void setProcesser(String processer)
    {
        this.processer = processer;
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
     * @return the mtype
     */
    public int getMtype()
    {
        return mtype;
    }

    /**
     * @param mtype
     *            the mtype to set
     */
    public void setMtype(int mtype)
    {
        this.mtype = mtype;
    }

    /**
     * @return the vtype
     */
    public int getVtype()
    {
        return vtype;
    }

    /**
     * @param vtype
     *            the vtype to set
     */
    public void setVtype(int vtype)
    {
        this.vtype = vtype;
    }

    /**
     * @return the checks
     */
    public String getChecks()
    {
        return checks;
    }

    /**
     * @param checks
     *            the checks to set
     */
    public void setChecks(String checks)
    {
        this.checks = checks;
    }

    /**
     * @return the checkrefId
     */
    public String getCheckrefId()
    {
        return checkrefId;
    }

    /**
     * @param checkrefId
     *            the checkrefId to set
     */
    public void setCheckrefId(String checkrefId)
    {
        this.checkrefId = checkrefId;
    }

    /**
     * @return the checkStatus
     */
    public int getCheckStatus()
    {
        return checkStatus;
    }

    /**
     * @param checkStatus
     *            the checkStatus to set
     */
    public void setCheckStatus(int checkStatus)
    {
        this.checkStatus = checkStatus;
    }

    /**
     * @return the stype
     */
    public int getStype()
    {
        return stype;
    }

    /**
     * @param stype
     *            the stype to set
     */
    public void setStype(int stype)
    {
        this.stype = stype;
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

        retValue.append("InvoiceinsBean ( ").append(super.toString()).append(TAB).append("id = ").append(this.id).append(
            TAB).append("invoiceId = ").append(this.invoiceId).append(TAB).append("status = ").append(this.status).append(
            TAB).append("type = ").append(this.type).append(TAB).append("stype = ").append(this.stype).append(TAB).append(
            "mtype = ").append(this.mtype).append(TAB).append("vtype = ").append(this.vtype).append(TAB).append(
            "dutyId = ").append(this.dutyId).append(TAB).append("unit = ").append(this.unit).append(TAB).append(
            "reveive = ").append(this.reveive).append(TAB).append("locationId = ").append(this.locationId).append(TAB).append(
            "customerId = ").append(this.customerId).append(TAB).append("moneys = ").append(this.moneys).append(TAB).append(
            "invoiceDate = ").append(this.invoiceDate).append(TAB).append("stafferId = ").append(this.stafferId).append(
            TAB).append("processer = ").append(this.processer).append(TAB).append("logTime = ").append(this.logTime).append(
            TAB).append("refIds = ").append(this.refIds).append(TAB).append("description = ").append(this.description).append(
            TAB).append("checks = ").append(this.checks).append(TAB).append("checkrefId = ").append(this.checkrefId).append(
            TAB).append("checkStatus = ").append(this.checkStatus).append(TAB).append("itemList = ").append(
            this.itemList).append(TAB).append("vsList = ").append(this.vsList).append(TAB).append(" )");

        return retValue.toString();
    }

}
