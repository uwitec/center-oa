/**
 *
 */
package com.china.centet.yongyin.constant;

/**
 * 询价的常量定义
 * 
 * @author Administrator
 */
public interface PriceConstant
{
    /**
     * 正常
     */
    int PRICE_COMMON = 0;

    /**
     * 拒绝
     */
    int PRICE_REJECT = 1;

    /**
     * 询价初始化
     */
    int PRICE_ASK_STATUS_INIT = 0;

    /**
     * 询价中(询价没有结束)
     */
    int PRICE_ASK_STATUS_PROCESSING = 1;

    /**
     * 询价异常
     */
    int PRICE_ASK_STATUS_EXCEPTION = 2;

    /**
     * 询价结束
     */
    int PRICE_ASK_STATUS_END = 3;

    /**
     * 询价类型-内网
     */
    int PRICE_ASK_TYPE_INNER = 0;

    /**
     * 询价类型-外网
     */
    int PRICE_ASK_TYPE_NET = 1;

    /**
     * 没有逾期
     */
    int OVERTIME_NO = 0;

    /**
     * 逾期
     */
    int OVERTIME_YES = 1;

    /**
     * 数量是否满足-是
     */
    int HASAMOUNT_OK = 0;

    /**
     * 数量是否满足-否
     */
    int HASAMOUNT_NO = 1;

    /**
     * 询价紧急程度-一般(2小时)
     */
    int PRICE_INSTANCY_COMMON = 0;

    /**
     * 询价紧急程度-紧急(1小时)
     */
    int PRICE_INSTANCY_INSTANCY = 1;

    /**
     * 询价紧急程度-非常紧急(30分钟)
     */
    int PRICE_INSTANCY_VERYINSTANCY = 2;

    /**
     * 询价紧急程度-外网询价(4小时)
     */
    int PRICE_INSTANCY_NETWORK = 3;

}
