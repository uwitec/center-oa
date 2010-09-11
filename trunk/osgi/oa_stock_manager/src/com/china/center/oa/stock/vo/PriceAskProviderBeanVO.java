/**
 *
 */
package com.china.center.oa.stock.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.stock.bean.PriceAskProviderBean;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class PriceAskProviderBeanVO extends PriceAskProviderBean
{
    @Relationship(relationField = "productId", tagField = "name")
    private String productName = "";

    @Relationship(relationField = "productId", tagField = "code")
    private String productCode = "";

    @Relationship(relationField = "providerId", tagField = "name")
    private String providerName = "";

    @Relationship(relationField = "userId", tagField = "name")
    private String userName = "";

    @Ignore
    private String pid = "";

    /**
     * 剩余的产品数量
     */
    @Ignore
    private int remainmount = 0;

    /**
     *
     */
    public PriceAskProviderBeanVO()
    {
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
     * @return the pid
     */
    public String getPid()
    {
        return pid;
    }

    /**
     * @param pid
     *            the pid to set
     */
    public void setPid(String pid)
    {
        this.pid = pid;
    }

    /**
     * @return the remainmount
     */
    public int getRemainmount()
    {
        return remainmount;
    }

    /**
     * @param remainmount
     *            the remainmount to set
     */
    public void setRemainmount(int remainmount)
    {
        this.remainmount = remainmount;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("PriceAskProviderBeanVO ( ")
            .append(super.toString())
            .append(TAB)
            .append("productName = ")
            .append(this.productName)
            .append(TAB)
            .append("productCode = ")
            .append(this.productCode)
            .append(TAB)
            .append("providerName = ")
            .append(this.providerName)
            .append(TAB)
            .append("userName = ")
            .append(this.userName)
            .append(TAB)
            .append("pid = ")
            .append(this.pid)
            .append(TAB)
            .append("remainmount = ")
            .append(this.remainmount)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
