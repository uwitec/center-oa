/**
 * File Name: PriceTemplateBeanVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.PriceTemplateBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2008-8-3
 * @see
 * @since
 */
@Entity(inherit = true, cache = true)
public class PriceTemplateBeanVO extends PriceTemplateBean
{
    @Relationship(relationField = "productId", tagField = "name")
    private String productName = "";

    @Relationship(relationField = "productId", tagField = "code")
    private String productCode = "";

    @Relationship(relationField = "priceWebId", tagField = "name")
    private String priceWebName = "";

    /**
     * default constructor
     */
    public PriceTemplateBeanVO()
    {}

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @return the productCode
     */
    public String getProductCode()
    {
        return productCode;
    }

    /**
     * @return the priceWebName
     */
    public String getPriceWebName()
    {
        return priceWebName;
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
     * @param priceWebName
     *            the priceWebName to set
     */
    public void setPriceWebName(String priceWebName)
    {
        this.priceWebName = priceWebName;
    }
}
