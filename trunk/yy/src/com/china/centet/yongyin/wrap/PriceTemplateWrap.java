/**
 * File Name: PriceTemplateWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wrap;


import java.io.Serializable;
import java.util.List;

import com.china.centet.yongyin.bean.PriceTemplateBean;
import com.china.centet.yongyin.bean.PriceWebBean;


/**
 * PriceTemplate的包装类
 * 
 * @author zhuzhu
 * @version 2008-8-3
 * @see
 * @since
 */
public class PriceTemplateWrap implements Serializable
{
    private String productId = "";

    private String productName = "";

    private String productCode = "";

    private List<PriceWebBean> items = null;

    /**
     * default constructor
     */
    public PriceTemplateWrap()
    {}

    /**
     * default constructor
     */
    public PriceTemplateWrap(PriceTemplateBean bean)
    {
        this(bean.getProductId());
    }

    /**
     * default constructor
     */
    public PriceTemplateWrap(String productId)
    {
        this.productId = productId;
    }

    /**
     * @return the productId
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    public String getWebs()
    {
        String webs = "";
        for (PriceWebBean temp : this.items)
        {
            webs += temp.getName() + " ";
        }

        return webs;
    }

    /**
     * @return the productCode
     */
    public String getProductCode()
    {
        return productCode;
    }

    /**
     * @return the items
     */
    public List<PriceWebBean> getItems()
    {
        return items;
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
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    /**
     * @param productCode
     *            the productCode to set
     */
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    /**
     * @param items
     *            the items to set
     */
    public void setItems(List<PriceWebBean> items)
    {
        this.items = items;
    }
}
