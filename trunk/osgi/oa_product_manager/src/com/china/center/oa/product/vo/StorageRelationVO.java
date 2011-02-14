/**
 * File Name: StorageRelationVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.product.vs.StorageRelationBean;


/**
 * StorageRelationVO
 * 
 * @author ZHUZHU
 * @version 2010-8-22
 * @see StorageRelationVO
 * @since 1.0
 */
@Entity(inherit = true)
public class StorageRelationVO extends StorageRelationBean
{
    @Relationship(relationField = "productId")
    private String productName = "";

    @Relationship(relationField = "productId", tagField = "code")
    private String productCode = "";

    @Relationship(relationField = "storageId")
    private String storageName = "";

    @Relationship(relationField = "depotpartId")
    private String depotpartName = "";

    @Relationship(relationField = "locationId")
    private String locationName = "";

    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Ignore
    private int mayAmount = 0;

    /**
     * 预先分配的,即销售单未审批的
     */
    @Ignore
    private int preassignAmount = 0;

    /**
     * 差误的数量
     */
    @Ignore
    private int errorAmount = 0;

    @Ignore
    private double batchPrice = 0.0d;

    /**
     * default constructor
     */
    public StorageRelationVO()
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
     * @return the storageName
     */
    public String getStorageName()
    {
        return storageName;
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

    /**
     * @return the locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param locationName
     *            the locationName to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * @return the mayAmount
     */
    public int getMayAmount()
    {
        return mayAmount;
    }

    /**
     * @param mayAmount
     *            the mayAmount to set
     */
    public void setMayAmount(int mayAmount)
    {
        this.mayAmount = mayAmount;
    }

    /**
     * @return the preassignAmount
     */
    public int getPreassignAmount()
    {
        return preassignAmount;
    }

    /**
     * @param preassignAmount
     *            the preassignAmount to set
     */
    public void setPreassignAmount(int preassignAmount)
    {
        this.preassignAmount = preassignAmount;
    }

    /**
     * @return the errorAmount
     */
    public int getErrorAmount()
    {
        return errorAmount;
    }

    /**
     * @param errorAmount
     *            the errorAmount to set
     */
    public void setErrorAmount(int errorAmount)
    {
        this.errorAmount = errorAmount;
    }

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

    /**
     * @return the batchPrice
     */
    public double getBatchPrice()
    {
        return batchPrice;
    }

    /**
     * @param batchPrice
     *            the batchPrice to set
     */
    public void setBatchPrice(double batchPrice)
    {
        this.batchPrice = batchPrice;
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
            .append("StorageRelationVO ( ")
            .append(super.toString())
            .append(TAB)
            .append("productName = ")
            .append(this.productName)
            .append(TAB)
            .append("productCode = ")
            .append(this.productCode)
            .append(TAB)
            .append("storageName = ")
            .append(this.storageName)
            .append(TAB)
            .append("depotpartName = ")
            .append(this.depotpartName)
            .append(TAB)
            .append("locationName = ")
            .append(this.locationName)
            .append(TAB)
            .append("stafferName = ")
            .append(this.stafferName)
            .append(TAB)
            .append("mayAmount = ")
            .append(this.mayAmount)
            .append(TAB)
            .append("preassignAmount = ")
            .append(this.preassignAmount)
            .append(TAB)
            .append("errorAmount = ")
            .append(this.errorAmount)
            .append(TAB)
            .append("batchPrice = ")
            .append(this.batchPrice)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
