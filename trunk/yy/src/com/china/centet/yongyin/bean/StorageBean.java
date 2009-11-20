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
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;


/**
 * 储位表
 * 
 * @author admin
 * @version [版本号, 2008-1-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity(name = "储位")
@Table(name = "T_CENTER_STORAGE")
public class StorageBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "储位名称", must = true, maxLength = 20, oncheck = {JCheck.NOT_NONE,
        JCheck.ONLY_COMMONCHAR})
    private String name = "";

    @Html(title = "产品数量", oncheck = JCheck.ONLY_NUMBER)
    private int amount = 0;

    private String depotpartId = "";

    @Ignore
    @Html(title = "存储产品", type = Element.SELECT, name = "productName", readonly = true)
    private String productId = "";

    private String locationId = "";

    @Html(title = "描述", type = Element.TEXTAREA, maxLength = 200)
    private String description = "";

    public StorageBean()
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
     * @return 返回 amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * @param 对amount进行赋值
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * @return 返回 name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param 对name进行赋值
     */
    public void setName(String name)
    {
        this.name = name;
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
        if (productId != null)
        {
            this.productId = productId;
        }
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
}
