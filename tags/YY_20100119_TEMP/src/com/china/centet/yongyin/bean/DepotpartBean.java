/**
 * 文 件 名: DepotpartBean.java <br>
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
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;


/**
 * 仓区bean
 * 
 * @author admin
 * @version [版本号, 2008-1-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity(name = "仓区", cache = true)
@Table(name = "T_CENTER_DEPOTPART")
public class DepotpartBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Html(title = "仓区名称", must = true, maxLength = 40)
    private String name = "";

    private String locationId = "";

    /**
     * 1可发 0and2不可以
     */
    @Html(title = "仓区类型", type = Element.SELECT)
    private int type = 0;

    @Html(title = "描述", type = Element.TEXTAREA)
    private String description = "";

    public DepotpartBean()
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

}
