/**
 * File Name: ProductWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-9-26<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wrap;


import java.io.Serializable;


/**
 * ProductWrap
 * 
 * @author ZHUZHU
 * @version 2009-9-26
 * @see ProductWrap
 * @since 1.0
 */
public class ProductWrap implements Serializable
{
    private String productId = "";

    private String storageId = "";

    private String depotpartId = "";

    private int amount = 0;

    /**
     * default constructor
     */
    public ProductWrap()
    {}

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
     * @return the storageId
     */
    public String getStorageId()
    {
        return storageId;
    }

    /**
     * @param storageId
     *            the storageId to set
     */
    public void setStorageId(String storageId)
    {
        this.storageId = storageId;
    }

    /**
     * @return the depotpartId
     */
    public String getDepotpartId()
    {
        return depotpartId;
    }

    /**
     * @param depotpartId
     *            the depotpartId to set
     */
    public void setDepotpartId(String depotpartId)
    {
        this.depotpartId = depotpartId;
    }

    /**
     * @return the amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String tab = ",";

        StringBuilder retValue = new StringBuilder();

        retValue.append("ProductWrap ( ").append(super.toString()).append(tab).append(
            "productId = ").append(this.productId).append(tab).append("storageId = ").append(
            this.storageId).append(tab).append("depotpartId = ").append(this.depotpartId).append(
            tab).append("amount = ").append(this.amount).append(tab).append(" )");

        return retValue.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (depotpartId == null) ? 0 : depotpartId.hashCode());
        result = prime * result + ( (productId == null) ? 0 : productId.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if ( ! (obj instanceof ProductWrap)) return false;
        final ProductWrap other = (ProductWrap)obj;
        if (depotpartId == null)
        {
            if (other.depotpartId != null) return false;
        }
        else if ( !depotpartId.equals(other.depotpartId)) return false;
        if (productId == null)
        {
            if (other.productId != null) return false;
        }
        else if ( !productId.equals(other.productId)) return false;
        return true;
    }
}
