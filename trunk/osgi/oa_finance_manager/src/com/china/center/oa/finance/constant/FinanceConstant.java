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
     * 管理属性
     */
    @Defined(key = "bankType", value = "管理属性")
    int BANK_TYPE_NOTDUTY = 0;

    /**
     * 非管理属性
     */
    @Defined(key = "bankType", value = "非管理属性")
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
     * 坏账
     */
    @Defined(key = "inbillType", value = "销售坏账")
    int INBILL_TYPE_BADOUT = 4;

    /**
     * 预收转费用
     */
    @Defined(key = "inbillType", value = "预收转费用")
    int INBILL_TYPE_FEE = 5;

    /**
     * 个人还款
     */
    @Defined(key = "inbillType", value = "个人还款")
    int INBILL_TYPE_UNBORROW = 6;

    /**
     * 转账
     */
    @Defined(key = "inbillType", value = "转账")
    int INBILL_TYPE_TRANSFER = 98;

    /**
     * 其他
     */
    @Defined(key = "inbillType", value = "其他")
    int INBILL_TYPE_OTHER = 99;

    /**
     * 可使用
     */
    @Defined(key = "paymentUseall", value = "未使用")
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
     * 待稽核
     */
    @Defined(key = "payApplyStatus", value = "待稽核")
    int PAYAPPLY_STATUS_CHECK = 3;

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
     * 回款转收款(就是回款转预收)
     */
    @Defined(key = "payApplyType", value = "回款转收款")
    int PAYAPPLY_TYPE_PAYMENT = 0;

    /**
     * 销售单绑定
     */
    @Defined(key = "payApplyType", value = "销售单绑定")
    int PAYAPPLY_TYPE_BING = 1;

    /**
     * 临时
     */
    int PAYAPPLY_TYPE_TEMP = 2;

    /**
     * 预收转费用
     */
    @Defined(key = "payApplyType", value = "预收转费用")
    int PAYAPPLY_TYPE_CHANGEFEE = 3;

    /**
     * 正常
     */
    @Defined(key = "outbillStatus", value = "正常")
    int OUTBILL_STATUS_INIT = 0;

    /**
     * 转账中
     */
    @Defined(key = "outbillStatus", value = "转账中")
    int OUTBILL_STATUS_SUBMIT = 1;

    /**
     * 转账结束
     */
    @Defined(key = "outbillStatus", value = "转账结束")
    int OUTBILL_STATUS_END = 2;

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
     * 自动
     */
    @Defined(key = "billCreateType", value = "自动")
    int BILL_CREATETYPE_AUTO = 0;

    /**
     * 人工
     */
    @Defined(key = "billCreateType", value = "人工")
    int BILL_CREATETYPE_HAND = 1;

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

    /**
     * 销售/预收退款
     */
    @Defined(key = "outbillType", value = "销售/预收退款")
    int OUTBILL_TYPE_OUTBACK = 10;

    /**
     * 手续费
     */
    @Defined(key = "outbillType", value = "手续费")
    int OUTBILL_TYPE_HANDLING = 11;

    /**
     * 转账
     */
    @Defined(key = "outbillType", value = "转账")
    int OUTBILL_TYPE_TRANSFER = 98;

    /**
     * 其他
     */
    @Defined(key = "outbillType", value = "其他")
    int OUTBILL_TYPE_OTHER = 99;

    /**
     * 销售单
     */
    @Defined(key = "invsoutType", value = "销售单")
    int INSVSOUT_TYPE_OUT = 0;

    /**
     * 委托结算
     */
    @Defined(key = "invsoutType", value = "委托结算")
    int INSVSOUT_TYPE_BALANCE = 1;

    /**
     * 初始
     */
    @Defined(key = "invoiceinsStatus", value = "初始")
    int INVOICEINS_STATUS_INIT = 0;

    /**
     * 待财务审核
     */
    @Defined(key = "invoiceinsStatus", value = "待财务审核")
    int INVOICEINS_STATUS_SUBMIT = 1;

    /**
     * 待稽核
     */
    @Defined(key = "invoiceinsStatus", value = "待稽核")
    int INVOICEINS_STATUS_CHECK = 2;

    /**
     * 结束
     */
    @Defined(key = "invoiceinsStatus", value = "结束")
    int INVOICEINS_STATUS_END = 99;

    /**
     * 普通
     */
    @Defined(key = "invoiceinsType", value = "普通")
    int INVOICEINS_TYPE_COMMON = 0;

    /**
     * 对分公司
     */
    @Defined(key = "invoiceinsType", value = "对分公司")
    int INVOICEINS_TYPE_DUTY = 1;

}
