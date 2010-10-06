/**
 * File Name: ComposeConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * ComposeConstant
 * 
 * @author ZHUZHU
 * @version 2010-10-2
 * @see ComposeConstant
 * @since 1.0
 */
public interface ComposeConstant
{
    /**
     * 合成
     */
    @Defined(key = "composeType", value = "合成")
    int COMPOSE_TYPE_COMPOSE = 0;

    /**
     * 分解
     */
    @Defined(key = "composeType", value = "分解")
    int COMPOSE_TYPE_DECOMPOSE = 1;
}
