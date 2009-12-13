/**
 *
 */
package com.china.centet.yongyin.vo;


import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.StockItemBean;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class StockItemBeanVO extends StockItemBean
{
    @Relationship(relationField = "productId", tagField = "name")
    private String productName = "";

    @Relationship(relationField = "productId", tagField = "code")
    private String productCode = "";

    @Relationship(relationField = "providerId", tagField = "name")
    private String providerName = "";

    @Ignore
    private List<PriceAskProviderBeanVO> asksVo = null;

    /**
     *
     */
    public StockItemBeanVO()
    {}

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
     * @return the asksVo
     */
    public List<PriceAskProviderBeanVO> getAsksVo()
    {
        return asksVo;
    }

    /**
     * @param asksVo
     *            the asksVo to set
     */
    public void setAsksVo(List<PriceAskProviderBeanVO> asksVo)
    {
        this.asksVo = asksVo;
    }

    /**
     * @return the productCode
     */
    public String getProductCode()
    {
        return productCode;
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
}
