/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.annotation.enums.JoinType;
import com.china.centet.yongyin.constant.PriceConstant;
import com.china.centet.yongyin.constant.StockConstant;


/**
 * @author Administrator
 */
@Entity(name = "采购单")
@Table(name = "T_CENTER_STOCK")
public class StockBean implements Serializable
{
    @Id(autoIncrement = false)
    private String id = "";

    @Join(tagClass = User.class, type = JoinType.LEFT)
    private String userId = "";

    @Join(tagClass = LocationBean.class)
    private String locationId = "";

    private int status = StockConstant.STOCK_STATUS_INIT;

    private int exceptStatus = StockConstant.EXCEPTSTATUS_COMMON;

    @Html(title = "到货时间", type = Element.DATE, must = true, maxLength = 100)
    private String needTime = "";

    @Html(title = "询价方式", type = Element.SELECT, must = true)
    private int type = PriceConstant.PRICE_ASK_TYPE_INNER;

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
    {}

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
}
