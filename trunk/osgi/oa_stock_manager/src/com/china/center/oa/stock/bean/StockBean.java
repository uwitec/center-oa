/**
 *
 */
package com.china.center.oa.stock.bean;


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
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.bean.UserBean;
import com.china.center.oa.stock.constant.PriceConstant;
import com.china.center.oa.stock.constant.StockConstant;


/**
 * @author Administrator
 */
@Entity(name = "采购单")
@Table(name = "T_CENTER_STOCK")
public class StockBean implements Serializable
{
    @Id(autoIncrement = false)
    private String id = "";

    @Join(tagClass = UserBean.class, type = JoinType.LEFT)
    private String userId = "";

    @Join(tagClass = StafferBean.class, type = JoinType.LEFT)
    private String stafferId = "";

    @Join(tagClass = LocationBean.class)
    private String locationId = "";

    private int status = StockConstant.STOCK_STATUS_INIT;

    private int exceptStatus = StockConstant.EXCEPTSTATUS_COMMON;

    @Html(title = "到货时间", type = Element.DATE, must = true, maxLength = 100)
    private String needTime = "";

    @Html(title = "预期帐期", type = Element.DATE, must = true, maxLength = 100)
    private String willDate = "";

    /**
     * 采购主管必填,早于这个日期是不能付款的
     */
    @Html(title = "最早付款日期", type = Element.DATE, must = true, maxLength = 100)
    private String nearlyPayDate = "";

    @Html(title = "询价方式", type = Element.SELECT, must = true)
    private int type = PriceConstant.PRICE_ASK_TYPE_INNER;

    /**
     * 采购属性【公卖、自卖】
     */
    private int stockType = StockConstant.STOCK_SAILTYPE_PUBLIC;

    private String logTime = "";

    @Html(title = "物流走向", must = true, maxLength = 100, type = Element.SELECT)
    private String flow = "";

    private double total = 0.0d;

    /**
     * 是否付款
     */
    private int pay = StockConstant.STOCK_PAY_NO;

    @Html(title = "备注", type = Element.TEXTAREA, maxLength = 100)
    private String description = "";

    @Ignore
    private List<StockItemBean> item = null;

    /**
     *
     */
    public StockBean()
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
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
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
     * @return the needTime
     */
    public String getNeedTime()
    {
        return needTime;
    }

    /**
     * @param needTime
     *            the needTime to set
     */
    public void setNeedTime(String needTime)
    {
        this.needTime = needTime;
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
     * @return the flow
     */
    public String getFlow()
    {
        return flow;
    }

    /**
     * @param flow
     *            the flow to set
     */
    public void setFlow(String flow)
    {
        this.flow = flow;
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
     * @return the item
     */
    public List<StockItemBean> getItem()
    {
        return item;
    }

    /**
     * @param item
     *            the item to set
     */
    public void setItem(List<StockItemBean> item)
    {
        this.item = item;
    }

    /**
     * @return the exceptStatus
     */
    public int getExceptStatus()
    {
        return exceptStatus;
    }

    /**
     * @param exceptStatus
     *            the exceptStatus to set
     */
    public void setExceptStatus(int exceptStatus)
    {
        this.exceptStatus = exceptStatus;
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
     * @return the pay
     */
    public int getPay()
    {
        return pay;
    }

    /**
     * @param pay
     *            the pay to set
     */
    public void setPay(int pay)
    {
        this.pay = pay;
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
     * @return the willDate
     */
    public String getWillDate()
    {
        return willDate;
    }

    /**
     * @param willDate
     *            the willDate to set
     */
    public void setWillDate(String willDate)
    {
        this.willDate = willDate;
    }

    /**
     * @return the nearlyPayDate
     */
    public String getNearlyPayDate()
    {
        return nearlyPayDate;
    }

    /**
     * @param nearlyPayDate
     *            the nearlyPayDate to set
     */
    public void setNearlyPayDate(String nearlyPayDate)
    {
        this.nearlyPayDate = nearlyPayDate;
    }

    /**
     * @return the stockType
     */
    public int getStockType()
    {
        return stockType;
    }

    /**
     * @param stockType
     *            the stockType to set
     */
    public void setStockType(int stockType)
    {
        this.stockType = stockType;
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
            .append("StockBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("userId = ")
            .append(this.userId)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("locationId = ")
            .append(this.locationId)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("exceptStatus = ")
            .append(this.exceptStatus)
            .append(TAB)
            .append("needTime = ")
            .append(this.needTime)
            .append(TAB)
            .append("willDate = ")
            .append(this.willDate)
            .append(TAB)
            .append("nearlyPayDate = ")
            .append(this.nearlyPayDate)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("stockType = ")
            .append(this.stockType)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("flow = ")
            .append(this.flow)
            .append(TAB)
            .append("total = ")
            .append(this.total)
            .append(TAB)
            .append("pay = ")
            .append(this.pay)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append("item = ")
            .append(this.item)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}