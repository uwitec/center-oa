package com.china.center.oa.product.wrap;


import com.china.center.oa.product.bean.StorageLogBean;


/**
 * 盘点报表
 * 
 * @author ZHUZHU
 * @version 2007-9-2
 * @see
 * @since
 */
public class StatProductBean extends StorageLogBean
{
    /**
     * 储位
     */
    private String storageName = "";

    private String depotpartName = "";

    /**
     * 产品名称
     */
    private String productName = "";

    /**
     * 当前数量
     */
    private int currentAmount = 0;

    /**
     * default constructor
     */
    public StatProductBean()
    {
    }

    /**
     * @return the storageName
     */
    public String getStorageName()
    {
        return storageName;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param storageName
     *            the storageName to set
     */
    public void setStorageName(String storageName)
    {
        this.storageName = storageName;
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
     * @return the currentAmount
     */
    public int getCurrentAmount()
    {
        return currentAmount;
    }

    /**
     * @param currentAmount
     *            the currentAmount to set
     */
    public void setCurrentAmount(int currentAmount)
    {
        this.currentAmount = currentAmount;
    }

    /**
     * @return the depotpartName
     */
    public String getDepotpartName()
    {
        return depotpartName;
    }

    /**
     * @param depotpartName
     *            the depotpartName to set
     */
    public void setDepotpartName(String depotpartName)
    {
        this.depotpartName = depotpartName;
    }
}
