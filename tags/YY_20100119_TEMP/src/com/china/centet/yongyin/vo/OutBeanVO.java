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
package com.china.centet.yongyin.vo;


import com.china.centet.yongyin.bean.OutBean;


/**
 * Out的展现
 * 
 * @author admin
 * @version [版本号, 2008-1-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class OutBeanVO extends OutBean
{
    private String locationName = "";

    private String destinationName = "";

    private String depotpartName = "";

    private String customerAddress = "";

    public OutBeanVO()
    {}

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
}
