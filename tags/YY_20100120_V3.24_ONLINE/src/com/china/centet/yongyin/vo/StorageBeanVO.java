/**
 * 文 件 名: StorageBeanVO.java <br>
 * 版 权: centerchina Technologies Co., Ltd. Copyright YYYY-YYYY, All rights reserved
 * <br>
 * 描 述: <描述> <br>
 * 修 改 人: admin <br>
 * 修改时间: 2008-1-6 <br>
 * 跟踪单号: <跟踪单号> <br>
 * 修改单号: <修改单号> <br>
 * 修改内容: <修改内容> <br>
 */
package com.china.centet.yongyin.vo;


import com.china.centet.yongyin.bean.StorageBean;


/**
 * VO
 * 
 * @author admin
 * @version [版本号, 2008-1-6]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StorageBeanVO extends StorageBean
{
    private String depotpartName = "";

    private String productName = "";

    /**
     * 默认构造函数
     */
    public StorageBeanVO()
    {}

    /**
     * @return 返回 depotpartName
     */
    public String getDepotpartName()
    {
        return depotpartName;
    }

    /**
     * @param 对depotpartName进行赋值
     */
    public void setDepotpartName(String depotpartName)
    {
        this.depotpartName = depotpartName;
    }

    /**
     * @return 返回 productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param 对productName进行赋值
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

}
