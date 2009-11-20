/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.annotation.enums.JoinType;


/**
 * @author Administrator
 */
@Entity(name = "会员消费")
@Table(name = "T_CENTER_CONSUME")
public class ConsumeBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Join(tagClass = MemberBean.class, type = JoinType.LEFT)
    private String memberId = "";

    @Join(tagClass = Product.class, type = JoinType.LEFT)
    private String productId = "";

    @Ignore
    @Html(title = "产品", must = true)
    private String productName = "";

    @Join(tagClass = LocationBean.class, type = JoinType.LEFT)
    private String locationId = "";

    @Join(tagClass = User.class, type = JoinType.LEFT, tagField = "id")
    private String userId = "";

    private String logTime = "";

    @Html(title = "备注", type = Element.TEXTAREA, maxLength = 100)
    private String description = "";

    @Html(title = "产品数量", must = true, oncheck = JCheck.ONLY_NUMBER, maxLength = 5)
    private int amount = 0;

    private int point = 0;

    @Html(title = "单价", must = true, oncheck = JCheck.ONLY_NUMBER, maxLength = 9)
    private int price = 0;

    private int memberpoint = 0;

    @Html(title = "实际消费金额", must = true, oncheck = JCheck.ONLY_FLOAT, maxLength = 9)
    private double cost = 0.0d;

    @Html(title = "金额", must = true, oncheck = JCheck.ONLY_FLOAT, maxLength = 9)
    private double precost = 0.0d;

    @Html(title = "折扣", must = true, oncheck = JCheck.ONLY_FLOAT, maxLength = 4)
    private double rebate = 1.0d;

    /**
     *
     */
    public ConsumeBean()
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
     * @return the memberId
     */
    public String getMemberId()
    {
        return memberId;
    }

    /**
     * @param memberId
     *            the memberId to set
     */
    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    /**
     * @return the productId
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId)
    {
        this.productId = productId;
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
     * @return the amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * @return the point
     */
    public int getPoint()
    {
        return point;
    }

    /**
     * @param point
     *            the point to set
     */
    public void setPoint(int point)
    {
        this.point = point;
    }

    /**
     * @return the memberpoint
     */
    public int getMemberpoint()
    {
        return memberpoint;
    }

    /**
     * @param memberpoint
     *            the memberpoint to set
     */
    public void setMemberpoint(int memberpoint)
    {
        this.memberpoint = memberpoint;
    }

    /**
     * @return the cost
     */
    public double getCost()
    {
        return cost;
    }

    /**
     * @param cost
     *            the cost to set
     */
    public void setCost(double cost)
    {
        this.cost = cost;
    }

    /**
     * @return the precost
     */
    public double getPrecost()
    {
        return precost;
    }

    /**
     * @param precost
     *            the precost to set
     */
    public void setPrecost(double precost)
    {
        this.precost = precost;
    }

    /**
     * @return the rebate
     */
    public double getRebate()
    {
        return rebate;
    }

    /**
     * @param rebate
     *            the rebate to set
     */
    public void setRebate(double rebate)
    {
        this.rebate = rebate;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    /**
     * @return the price
     */
    public int getPrice()
    {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(int price)
    {
        this.price = price;
    }
}
