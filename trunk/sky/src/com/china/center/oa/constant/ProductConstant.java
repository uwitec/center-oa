/**
 * 
 */
package com.china.center.oa.constant;


import com.china.center.annotation.Defined;


/**
 * @author ZHUZHU
 */
public interface ProductConstant
{
    /**
     * 正常
     */
    int STATUS_COMMON = 0;

    /**
     * 锁定
     */
    int STATUS_LOCK = 1;

    /**
     * 注销
     */
    int STATUS_LOGOUT = 2;

    /**
     * 自有
     */
    int TEMP_SELF = 0;

    /**
     * 非自有
     */
    int TEMP_PUBLIC = 1;

    /**
     * 正常态
     */
    @Defined(key = "productStatStatus", value = "正常态")
    int STAT_STATUS_COMMON = 0;

    /**
     * 预警态
     */
    @Defined(key = "productStatStatus", value = "预警态")
    int STAT_STATUS_ALERT = 1;

    /**
     * 正常
     */
    @Defined(key = "productOrderStatus", value = "预定")
    int ORDER_STATUS_COMMON = 0;

    /**
     * 结束
     */
    @Defined(key = "productOrderStatus", value = "结束")
    int ORDER_STATUS_END = 1;

    /**
     * 统计周期
     */
    int STAT_DAYS = 15;
}
