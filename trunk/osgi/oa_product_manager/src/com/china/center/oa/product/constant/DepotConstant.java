/**
 * File Name: DepotConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * DepotConstant
 * 
 * @author ZHUZHU
 * @version 2010-8-22
 * @see DepotConstant
 * @since 1.0
 */
public interface DepotConstant
{
    /**
     * 区域仓库
     */
    @Defined(key = "depotType", value = "区域仓库")
    int DEPOT_TYPE_LOCATION = 0;

    /**
     * 中心仓库
     */
    @Defined(key = "depotType", value = "中心仓库")
    int DEPOT_TYPE_CENTER = 1;

    /**
     * 良品仓
     */
    @Defined(key = "depotpartType", value = "良品仓")
    int DEPOTPART_TYPE_OK = 0;

    /**
     * 次品仓
     */
    @Defined(key = "depotpartType", value = "次品仓")
    int DEPOTPART_TYPE_INFERIOR = 1;

    /**
     * 报废仓
     */
    @Defined(key = "depotpartType", value = "报废仓")
    int DEPOTPART_TYPE_SUPERSESSION = 2;

    /**
     * 物流中心仓库
     */
    String CENTER_DEPOT_ID = "99000000000000000001";

    /**
     * 采购仓区
     */
    String CENTER_DEPOTPART_ID = "10000000000000000002";

    /**
     * 采购储位
     */
    String CENTER_STORAGE_ID = "11000000000000000003";
}
