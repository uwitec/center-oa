/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.JoinType;
import com.china.centet.yongyin.constant.PriceConstant;


/**
 * 询价供应商
 * 
 * @author Administrator
 */
@Entity(name = "询价供应商")
@Table(name = "T_CENTER_PRICEASKPROVIDER")
public class PriceAskProviderBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Join(tagClass = Product.class)
    private String productId = "";

    @FK(index = 0)
    private String askId = "";

    @Join(tagClass = User.class, type = JoinType.LEFT)
    private String userId = "";

    @Join(tagClass = ProviderBean.class, type = JoinType.EQUAL)
    private String providerId = "";

    /**
     * 0:满足 1：不满足
     */
    private int hasAmount = PriceConstant.HASAMOUNT_NO;

    /**
     * 可以提供的数量
     */
    private int supportAmount = 0;

    private double price = 0.0d;

    private String logTime = "";

    private String description = "";

    /**
     *
     */
    public PriceAskProviderBean()
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
     * @return the askId
     */
    public String getAskId()
    {
        return askId;
    }

    /**
     * @param askId
     *            the askId to set
     */
    public void setAskId(String askId)
    {
        this.askId = askId;
    }

    /**
     * @return the providerId
     */
    public String getProviderId()
    {
        return providerId;
    }

    /**
     * @param providerId
     *            the providerId to set
     */
    public void setProviderId(String providerId)
    {
        this.providerId = providerId;
    }

    /**
     * @return the hasAmount
     */
    public int getHasAmount()
    {
        return hasAmount;
    }

    /**
     * @param hasAmount
     *            the hasAmount to set
     */
    public void setHasAmount(int hasAmount)
    {
        this.hasAmount = hasAmount;
    }

    /**
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(double price)
    {
        this.price = price;
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
        if (description != null)
        {
            this.description = description;
        }
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
     * @return the supportAmount
     */
    public int getSupportAmount()
    {
        return supportAmount;
    }

    /**
     * @param supportAmount
     *            the supportAmount to set
     */
    public void setSupportAmount(int supportAmount)
    {
        this.supportAmount = supportAmount;
    }

}
