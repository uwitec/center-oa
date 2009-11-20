/**
 * File Name: LockConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-9-26<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.constant;

/**
 * lock
 * 
 * @author ZHUZHU
 * @version 2009-9-26
 * @see LockConstant
 * @since 1.0
 */
public interface LockConstant
{
    /**
     * 总部的产品数量全局锁
     */
    Object CENTER_PRODUCT_AMOUNT_LOCK = new Object();
}
