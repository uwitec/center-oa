/**
 *
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.PriceBean;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class PriceBeanVO extends PriceBean
{
    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Relationship(relationField = "productId", tagField = "name")
    private String productName = "";

    @Relationship(relationField = "productId", tagField = "code")
    private String productCode = "";

    @Relationship(relationField = "priceWebId", tagField = "name")
    private String priceWebName = "";

    /**
     *
     */
    public PriceBeanVO()
    {}

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
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
     * @return the priceWebName
     */
    public String getPriceWebName()
    {
        return priceWebName;
    }

    /**
     * @param priceWebName
     *            the priceWebName to set
     */
    public void setPriceWebName(String priceWebName)
    {
        this.priceWebName = priceWebName;
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
}
