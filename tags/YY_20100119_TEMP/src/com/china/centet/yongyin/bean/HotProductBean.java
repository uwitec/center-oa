/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;


/**
 * @author Administrator
 */
@Entity(name = "热点产品")
@Table(name = "T_CENTER_HOTPRODUCT")
public class HotProductBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Join(tagClass = Product.class)
    @Html(title = "热点产品", name = "productName", readonly = true, must = true, maxLength = 100)
    private String productId = "";

    @Html(title = "热点排名", must = true, oncheck = JCheck.ONLY_NUMBER)
    private int orders = 99;

    private String logTime = "";

    /**
     *
     */
    public HotProductBean()
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
     * @return the orders
     */
    public int getOrders()
    {
        return orders;
    }

    /**
     * @param orders
     *            the orders to set
     */
    public void setOrders(int orders)
    {
        this.orders = orders;
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
}
