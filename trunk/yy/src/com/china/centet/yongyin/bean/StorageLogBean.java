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
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * 储位表
 * 
 * @author admin
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

    private int type = 0;

    private int preAmount = 0;

    private int afterAmount = 0;

    private int changeAmount = 0;

    private String depotpartId = "";

    private String storageId = "";

    private String productId = "";

    private String locationId = "";

    private String logTime = "";

    /**
     * 操作的用户
     */
    private String user = "";

    private String description = "";

    public StorageLogBean()
    {}

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
}
