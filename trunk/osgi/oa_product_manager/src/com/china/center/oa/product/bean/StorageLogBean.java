/**
 * 文 件 名: StorageBean.java <br>
 * 版 权: centerchina Technologies Co., Ltd. Copyright YYYY-YYYY, All rights reserved
 * <br>
 * 描 述: <描述> <br>
 * 修 改 人: admin <br>
 * 修改时间: 2008-1-5 <br>
 * 跟踪单号: <跟踪单号> <br>
 * 修改单号: <修改单号> <br>
 * 修改内容: <修改内容> <br>
 */
package com.china.center.oa.product.bean;


import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.product.constant.StorageConstant;


/**
 * 储位变动日志
 * 
 * @author ZHUZHU
 * @version [版本号, 2008-1-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity(name = "储位记录")
@Table(name = "T_CENTER_STORAGELOG")
public class StorageLogBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private String serializeId = "";

    private int type = StorageConstant.OPR_STORAGE_INIT;

    private int preAmount = 0;

    private int afterAmount = 0;

    /**
     * 仓区下整个产品的数量
     */
    private int preAmount1 = 0;

    /**
     * 仓区下整个产品的数量
     */
    private int afterAmount1 = 0;

    private int changeAmount = 0;

    @Join(tagClass = DepotpartBean.class)
    private String depotpartId = "";

    @Join(tagClass = StorageBean.class)
    private String storageId = "";

    @Join(tagClass = ProductBean.class)
    private String productId = "";

    @Join(tagClass = DepotBean.class)
    private String locationId = "";

    private String logTime = "";

    private double price = 0.0d;

    /**
     * 操作的用户
     */
    private String user = "";

    private String description = "";

    public StorageLogBean()
    {
    }

    /**
     * @return 返回 id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param 对id进行赋值
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return 返回 depotpartId
     */
    public String getDepotpartId()
    {
        return depotpartId;
    }

    /**
     * @param 对depotpartId进行赋值
     */
    public void setDepotpartId(String depotpartId)
    {
        this.depotpartId = depotpartId;
    }

    /**
     * @return 返回 description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param 对description进行赋值
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return 返回 productId
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * @param 对productId进行赋值
     */
    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * @return 返回 type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param 对type进行赋值
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @return 返回 preAmount
     */
    public int getPreAmount()
    {
        return preAmount;
    }

    /**
     * @param 对preAmount进行赋值
     */
    public void setPreAmount(int preAmount)
    {
        this.preAmount = preAmount;
    }

    /**
     * @return 返回 afterAmount
     */
    public int getAfterAmount()
    {
        return afterAmount;
    }

    /**
     * @param 对afterAmount进行赋值
     */
    public void setAfterAmount(int afterAmount)
    {
        this.afterAmount = afterAmount;
    }

    /**
     * @return 返回 changeAmount
     */
    public int getChangeAmount()
    {
        return changeAmount;
    }

    /**
     * @param 对changeAmount进行赋值
     */
    public void setChangeAmount(int changeAmount)
    {
        this.changeAmount = changeAmount;
    }

    /**
     * @return 返回 storageId
     */
    public String getStorageId()
    {
        return storageId;
    }

    /**
     * @param 对storageId进行赋值
     */
    public void setStorageId(String storageId)
    {
        this.storageId = storageId;
    }

    /**
     * @return 返回 logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param 对logTime进行赋值
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    /**
     * @return 返回 locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param 对locationId进行赋值
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    /**
     * @return the user
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * @return the serializeId
     */
    public String getSerializeId()
    {
        return serializeId;
    }

    /**
     * @param serializeId
     *            the serializeId to set
     */
    public void setSerializeId(String serializeId)
    {
        this.serializeId = serializeId;
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
     * @return the preAmount1
     */
    public int getPreAmount1()
    {
        return preAmount1;
    }

    /**
     * @param preAmount1
     *            the preAmount1 to set
     */
    public void setPreAmount1(int preAmount1)
    {
        this.preAmount1 = preAmount1;
    }

    /**
     * @return the afterAmount1
     */
    public int getAfterAmount1()
    {
        return afterAmount1;
    }

    /**
     * @param afterAmount1
     *            the afterAmount1 to set
     */
    public void setAfterAmount1(int afterAmount1)
    {
        this.afterAmount1 = afterAmount1;
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
            .append("StorageLogBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("serializeId = ")
            .append(this.serializeId)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("preAmount = ")
            .append(this.preAmount)
            .append(TAB)
            .append("afterAmount = ")
            .append(this.afterAmount)
            .append(TAB)
            .append("preAmount1 = ")
            .append(this.preAmount1)
            .append(TAB)
            .append("afterAmount1 = ")
            .append(this.afterAmount1)
            .append(TAB)
            .append("changeAmount = ")
            .append(this.changeAmount)
            .append(TAB)
            .append("depotpartId = ")
            .append(this.depotpartId)
            .append(TAB)
            .append("storageId = ")
            .append(this.storageId)
            .append(TAB)
            .append("productId = ")
            .append(this.productId)
            .append(TAB)
            .append("locationId = ")
            .append(this.locationId)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("price = ")
            .append(this.price)
            .append(TAB)
            .append("user = ")
            .append(this.user)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
