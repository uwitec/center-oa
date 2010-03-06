/**
 * File Name: PriceTemplateBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.JoinType;


/**
 * PriceTemplateBean
 * 
 * @author ZHUZHU
 * @version 2008-8-3
 * @see
 * @since
 */
@Entity(name = "网站价格录入模板", cache = true)
@Table(name = "T_CENTER_PRICETEMPLATE")
public class PriceTemplateBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    @Join(tagClass = Product.class, type = JoinType.LEFT)
    @Html(title = "产品", name = "productName", readonly = true)
    private String productId = "";

    @Join(tagClass = PriceWebBean.class)
    private String priceWebId = "";

    /**
     * default constructor
     */
    public PriceTemplateBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the productId
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * @return the priceWebId
     */
    public String getPriceWebId()
    {
        return priceWebId;
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
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * @param priceWebId
     *            the priceWebId to set
     */
    public void setPriceWebId(String priceWebId)
    {
        this.priceWebId = priceWebId;
    }
}
