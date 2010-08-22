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
     * 良品仓
     */
    @Defined(key = "depotpartType", value = "次品仓")
    int DEPOTPART_TYPE_INFERIOR = 0;

    /**
     * 良品仓
     */
    @Defined(key = "depotpartType", value = "良品仓")
    int DEPOTPART_TYPE_OK = 1;

    /**
     * 报废仓
     */
    @Defined(key = "depotpartType", value = "报废仓")
    int DEPOTPART_TYPE_SUPERSESSION = 2;
}
