/**
 * File Name: StockPayItemBeanVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-1-17<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.StockPayItemBean;


/**
 * StockPayItemBeanVO
 * 
 * @author ZHUZHU
 * @version 2010-1-17
 * @see StockPayItemBeanVO
 * @since 1.0
 */
@Entity(inherit = true)
public class StockPayItemBeanVO extends StockPayItemBean
{
    @Relationship(relationField = "providerId")
    private String providerName = "";

    @Relationship(relationField = "providerId", tagField = "code")
    private String providerCode = "";

    @Relationship(relationField = "productId")
    private String productName = "";

    /**
     * default constructor
     */
    public StockPayItemBeanVO()
    {}

    /**
     * @return the providerName
     */
    public String getProviderName()
    {
        return providerName;
    }

    /**
     * @param providerName
     *            the providerName to set
     */
    public void setProviderName(String providerName)
    {
        this.providerName = providerName;
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
     * @return the providerCode
     */
    public String getProviderCode()
    {
        return providerCode;
    }

    /**
     * @param providerCode
     *            the providerCode to set
     */
    public void setProviderCode(String providerCode)
    {
        this.providerCode = providerCode;
    }
}
