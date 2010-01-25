/**
 *
 */
package com.china.centet.yongyin.constant;

/**
 * @author Administrator
 */
public interface StockConstant
{
    /**
     * 初始态
     */
    int STOCK_STATUS_INIT = 0;

    /**
     * 提交
     */
    int STOCK_STATUS_SUBMIT = 1;

    /**
     * 驳回
     */
    int STOCK_STATUS_REJECT = 2;

    /**
     * 区域经理通过
     */
    int STOCK_STATUS_MANAGERPASS = 3;

    /**
     * 询价员通过(外网询价员通过)
     */
    int STOCK_STATUS_PRICEPASS = 4;

    /**
     * 采购主管通过
     */
    int STOCK_STATUS_STOCKPASS = 5;

    /**
     * 采购经理通过
     */
    int STOCK_STATUS_STOCKMANAGERPASS = 6;

    /**
     * 采购结束
     */
    int STOCK_STATUS_END = 7;

    /**
     * 采购结束(到货)
     */
    int STOCK_STATUS_LASTEND = 8;

    /**
     * 采购item的开始
     */
    int STOCK_ITEM_STATUS_INIT = 0;

    /**
     * 采购item的结束
     */
    int STOCK_ITEM_STATUS_END = 1;

    /**
     * 采购item的询价结束
     */
    int STOCK_ITEM_STATUS_ASK = 2;

    /**
     * 无异常
     */
    int EXCEPTSTATUS_COMMON = 0;

    /**
     * 异常(价格不是最小的)
     */
    int EXCEPTSTATUS_EXCEPTION_MIN = 1;

    /**
     * 异常(钱过多)
     */
    int EXCEPTSTATUS_EXCEPTION_MONEY = 2;

    int DISPLAY_YES = 0;

    int DISPLAY_NO = 1;

    /**
     * 没有付款
     */
    int STOCK_PAY_NO = 0;

    /**
     * 付款
     */
    int STOCK_PAY_YES = 1;

    /**
     * 付款申请
     */
    int STOCK_PAY_APPLY = 2;

    /**
     * 驳回
     */
    int STOCK_PAY_REJECT = 3;

    /**
     * 采购的item是否被库单关联 否
     */
    int STOCK_ITEM_REF_NO = 0;

    /**
     * 采购的item是否被库单关联 是
     */
    int STOCK_ITEM_REF_YES = 1;

    /**
     * 采购未逾期
     */
    int STOCK_OVERTIME_NO = 0;

    /**
     * 采购逾期
     */
    int STOCK_OVERTIME_YES = 1;

    /**
     * 采购单付款状态--初始
     */
    int STOCK_ITEM_PAY_STATUS_INIT = 0;

    /**
     * 采购单付款状态--使用
     */
    int STOCK_ITEM_PAY_STATUS_USED = 1;

}
