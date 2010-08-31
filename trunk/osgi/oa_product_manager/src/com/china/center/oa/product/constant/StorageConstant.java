/**
 * File Name: StorageConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * StorageConstant
 * 
 * @author ZHUZHU
 * @version 2010-8-25
 * @see StorageConstant
 * @since 1.0
 */
public interface StorageConstant
{
    /**
     * 初始化
     */
    @Defined(key = "storageType", value = "初始化")
    int OPR_STORAGE_INIT = 0;

    /**
     * 修改
     */
    @Defined(key = "storageType", value = "修改")
    int OPR_STORAGE_UPDATE = 1;

    /**
     * 库单通过
     */
    @Defined(key = "storageType", value = "销售出库")
    int OPR_STORAGE_OUTBILL = 2;

    /**
     * 入库单
     */
    @Defined(key = "storageType", value = "采购入库")
    int OPR_STORAGE_OUTBILLIN = 3;

    /**
     * 移动
     */
    @Defined(key = "storageType", value = "仓区移动")
    int OPR_STORAGE_MOVE = 4;

    /**
     * 合成
     */
    @Defined(key = "storageType", value = "合成/分解")
    int OPR_STORAGE_COMPOSE = 5;
}
