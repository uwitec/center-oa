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
     * 银行贷款
     */
    @Defined(key = "inbillType", value = "银行贷款")
    int INBILL_TYPE_LOAN = 1;

    /**
     * 卖出公司资产
     */
    @Defined(key = "inbillType", value = "卖出公司资产")
    int INBILL_TYPE_ASSETS = 2;

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
     * 预收
     */
    @Defined(key = "inbillStatus", value = "预收")
    int INBILL_STATUS_NOREF = 2;
}
