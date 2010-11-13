/**
 * 文 件 名: OutVO.java <br>
 * 版 权: centerchina Technologies Co., Ltd. Copyright YYYY-YYYY, All rights reserved
 * <br>
 * 描 述: <描述> <br>
 * 修 改 人: admin <br>
 * 修改时间: 2008-1-5 <br>
 * 跟踪单号: <跟踪单号> <br>
 * 修改单号: <修改单号> <br>
 * 修改内容: <修改内容> <br>
 */
package com.china.center.oa.sail.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.sail.bean.OutBean;


/**
 * Out的展现
 * 
 * @author ZHUZHU
 * @version
 * @see
 * @since 1.0
 */
@Entity(inherit = true)
public class OutVO extends OutBean
{
    @Relationship(relationField = "locationId")
    private String locationName = "";

    @Relationship(relationField = "location")
    private String depotName = "";

    /**
     * 入库单的时候,目的仓库
     */
    @Relationship(relationField = "destinationId")
    private String destinationName = "";

    @Relationship(relationField = "depotpartId")
    private String depotpartName = "";

    @Relationship(relationField = "customerId", tagField = "address")
    private String customerAddress = "";

    public OutVO()
    {
    }

    /**
     * @return 返回 locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param 对locationName进行赋值
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * @return the destinationName
     */
    public String getDestinationName()
    {
        return destinationName;
    }

    /**
     * @param destinationName
     *            the destinationName to set
     */
    public void setDestinationName(String destinationName)
    {
        this.destinationName = destinationName;
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
     * @return the customerAddress
     */
    public String getCustomerAddress()
    {
        return customerAddress;
    }

    /**
     * @param customerAddress
     *            the customerAddress to set
     */
    public void setCustomerAddress(String customerAddress)
    {
        this.customerAddress = customerAddress;
    }

    /**
     * @return the depotName
     */
    public String getDepotName()
    {
        return depotName;
    }

    /**
     * @param depotName
     *            the depotName to set
     */
    public void setDepotName(String depotName)
    {
        this.depotName = depotName;
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
            .append("OutBeanVO ( ")
            .append(super.toString())
            .append(TAB)
            .append("locationName = ")
            .append(this.locationName)
            .append(TAB)
            .append("depotName = ")
            .append(this.depotName)
            .append(TAB)
            .append("destinationName = ")
            .append(this.destinationName)
            .append(TAB)
            .append("depotpartName = ")
            .append(this.depotpartName)
            .append(TAB)
            .append("customerAddress = ")
            .append(this.customerAddress)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
