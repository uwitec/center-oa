/**
 * File Name: FinanceConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * FinanceConstant
 * 
 * @author ZHUZHU
 * @version 2010-12-12
 * @see FinanceConstant
 * @since 3.0
 */
public interface FinanceConstant
{
    /**
     * 不入进入税务帐套
     */
    @Defined(key = "bankType", value = "不入税务帐套")
    int BANK_TYPE_NOTDUTY = 0;

    /**
     * 入进入税务帐套
     */
    @Defined(key = "bankType", value = "进入税务帐套")
    int BANK_TYPE_INDUTY = 1;

    /**
     * 回款单状态--未认领
     */
    @Defined(key = "paymentStatus", value = "未认领")
    int PAYMENT_STATUS_INIT = 0;

    /**
     * 回款单状态--已认领
     */
    @Defined(key = "paymentStatus", value = "已认领")
    int PAYMENT_STATUS_END = 1;

    /**
     * 对公
     */
    @Defined(key = "paymentType", value = "对公")
    int PAYMENT_PAY_PUBLIC = 0;

    /**
     * 对私
     */
    @Defined(key = "paymentType", value = "对私")
    int PAYMENT_PAY_SELF = 1;

    /**
     * 销售收入
     */
    @Defined(key = "inbillType", value = "销售收入")
    int INBILL_TYPE_SAILOUT = 0;

    /**
     * 银行贷款(The loan is interest free)
     */
    @Defined(key = "inbillType", value = "银行贷款")
    int INBILL_TYPE_LOAN = 1;

    /**
     * 卖出公司资产
     */
    @Defined(key = "inbillType", value = "卖出公司资产")
    int INBILL_TYPE_ASSETS = 2;

    /**
     * 利息
     */
    @Defined(key = "inbillType", value = "利息")
    int INBILL_TYPE_INTEREST = 3;

    /**
     * 可使用
     */
    @Defined(key = "paymentUseall", value = "可使用")
    int PAYMENT_USEALL_INIT = 0;

    /**
     * 全部使用
     */
    @Defined(key = "paymentUseall", value = "全部使用")
    int PAYMENT_USEALL_END = 1;

    /**
     * 提交
     */
    @Defined(key = "payApplyStatus", value = "提交")
    int PAYAPPLY_STATUS_INIT = 0;

    /**
     * 通过
     */
    @Defined(key = "payApplyStatus", value = "通过")
    int PAYAPPLY_STATUS_PASS = 1;

    /**
     * 驳回
     */
    @Defined(key = "payApplyStatus", value = "驳回")
    int PAYAPPLY_STATUS_REJECT = 2;

    /**
     * 回款转收款
     */
    @Defined(key = "payApplyType", value = "回款转收款")
    int PAYAPPLY_TYPE_PAYMENT = 0;

    /**
     * 销售单绑定
     */
    @Defined(key = "payApplyType", value = "销售单绑定")
    int PAYAPPLY_TYPE_BING = 1;

    /**
     * 已收
     */
    @Defined(key = "inbillStatus", value = "已收")
    int INBILL_STATUS_PAYMENTS = 0;

    /**
     * 关联申请
     */
    @Defined(key = "inbillStatus", value = "关联申请")
    int INBILL_STATUS_PREPAYMENTS = 1;

    /**
     * 未锁定
     */
    @Defined(key = "billLock", value = "未锁定")
    int BILL_LOCK_NO = 0;

    /**
     * 锁定
     */
    @Defined(key = "billLock", value = "锁定")
    int BILL_LOCK_YES = 1;

    /**
     * 预收
     */
    @Defined(key = "inbillStatus", value = "预收")
    int INBILL_STATUS_NOREF = 2;

    /**
     * 现金
     */
    @Defined(key = "outbillPayType", value = "现金")
    int OUTBILL_PAYTYPE_MONEY = 0;

    /**
     * 银行
     */
    @Defined(key = "outbillPayType", value = "银行")
    int OUTBILL_PAYTYPE_BANK = 1;

    /**
     * 采购付款
     */
    @Defined(key = "outbillType", value = "采购付款")
    int OUTBILL_TYPE_STOCK = 0;

    /**
     * 买固定资产
     */
    @Defined(key = "outbillType", value = "买固定资产")
    int OUTBILL_TYPE_BUY_ASSET = 1;

    /**
     * 买低值用品(买办公品)
     */
    @Defined(key = "outbillType", value = "买办公低值品")
    int OUTBILL_TYPE_BUY_COMMON = 2;

    /**
     * 买无形资产
     */
    @Defined(key = "outbillType", value = "买无形资产")
    int OUTBILL_TYPE_BUY_ABS = 3;

    /**
     * 支付代摊费用
     */
    @Defined(key = "outbillType", value = "支付代摊费用")
    int OUTBILL_TYPE_PAY_APPORTION = 4;

    /**
     * 长期股权投资
     */
    @Defined(key = "outbillType", value = "长期股权投资")
    int OUTBILL_TYPE_BUY_STOCK = 5;

    /**
     * 个人借款
     */
    @Defined(key = "outbillType", value = "个人借款")
    int OUTBILL_TYPE_BORROW = 6;

    /**
     * 押金和定金
     */
    @Defined(key = "outbillType", value = "押金和定金")
    int OUTBILL_TYPE_CASH = 7;

    /**
     * 费用报销
     */
    @Defined(key = "outbillType", value = "费用报销")
    int OUTBILL_TYPE_WIPEOUT = 8;

    /**
     * 税金
     */
    @Defined(key = "outbillType", value = "税金")
    int OUTBILL_TYPE_DUTY = 9;

}
