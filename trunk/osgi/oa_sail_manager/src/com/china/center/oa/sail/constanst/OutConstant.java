/**
 * File Name: OutConstanst.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-10-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.constanst;


import com.china.center.jdbc.annotation.Defined;


/**
 * OutConstanst
 * 
 * @author ZHUZHU
 * @version 2008-10-4
 * @see
 * @since
 */
public interface OutConstant
{
    /**
     * 销售单
     */
    int OUT_TYPE_OUTBILL = 0;

    /**
     * 入库单
     */
    int OUT_TYPE_INBILL = 1;

    /**
     * 信用正常
     */
    int OUT_CREDIT_COMMON = 0;

    /**
     * 信用超支(临时的)
     */
    int OUT_CREDIT_OVER = 1;

    /**
     * 价格为0
     */
    int OUT_CREDIT_MIN = 2;

    /**
     * 货到收款
     */
    int OUT_SAIL_TYPE_COMMON = 0;

    /**
     * 款到发货
     */
    int OUT_SAIL_TYPE_MONEY = 1;

    /**
     * 使用业务员信用额度和客户信用额度(客户信用额度优先)
     */
    int OUT_SAIL_TYPE_CREDIT_AND_CUR = 2;

    /**
     * 使用业务员信用额度和客户信用额度还不够的时候,分公司总经理担保
     */
    int OUT_SAIL_TYPE_LOCATION_MANAGER = 3;

    /**
     * 保存
     */
    @Defined(key = "outStatus", value = "保存")
    int STATUS_SAVE = 0;

    /**
     * 业务员提交
     */
    @Defined(key = "outStatus", value = "提交")
    int STATUS_SUBMIT = 1;

    /**
     * 驳回
     */
    @Defined(key = "outStatus", value = "驳回")
    int STATUS_REJECT = 2;

    /**
     * 库管发货(这里的销售单库存就变动了)(一般此通过即是销售单已经OK status in (3, 4))
     */
    @Defined(key = "outStatus", value = "库管发货")
    int STATUS_PASS = 3;

    /**
     * 财务核对(单据结束)
     */
    @Defined(key = "outStatus", value = "财务核对(结束)")
    int STATUS_SEC_PASS = 4;

    /**
     * 结算中心通过
     */
    @Defined(key = "outStatus", value = "结算中心通过")
    int STATUS_MANAGER_PASS = 6;

    /**
     * 物流管理员通过
     */
    @Defined(key = "outStatus", value = "物流通过")
    int STATUS_FLOW_PASS = 7;

    /**
     * 分公司总经理审批
     */
    @Defined(key = "outStatus", value = "待分公司经理审批")
    int STATUS_LOCATION_MANAGER_CHECK = 8;

    /**
     * 待总裁审批
     */
    @Defined(key = "outStatus", value = "待总裁审批")
    int STATUS_CEO_CHECK = 9;

    /**
     * 待董事长审批(入库单专用)
     */
    @Defined(key = "outStatus", value = "待董事长审批")
    int STATUS_CHAIRMA_CHECK = 10;

    /**
     * 未付款
     */
    @Defined(key = "outPay", value = "未付款")
    int PAY_NOT = 0;

    /**
     * 付款
     */
    @Defined(key = "outPay", value = "已付款")
    int PAY_YES = 1;

    /**
     * 过期
     */
    @Defined(key = "outPay", value = "过期未付款")
    int PAY_OVER = 2;

    /**
     * 非在途
     */
    @Defined(key = "inway", value = "非在途")
    int IN_WAY_NO = 0;

    /**
     * 在途中
     */
    @Defined(key = "inway", value = "在途中")
    int IN_WAY = 1;

    /**
     * 在途结束
     */
    @Defined(key = "inway", value = "在途结束")
    int IN_WAY_OVER = 2;

    /**
     * 销售出库
     */
    @Defined(key = "outType_out", value = "销售出库")
    int OUTTYPE_OUT_COMMON = 0;

    /**
     * 个人领样
     */
    @Defined(key = "outType_out", value = "个人领样")
    int OUTTYPE_OUT_SWATCH = 1;

    /**
     * 零售
     */
    @Defined(key = "outType_out", value = "零售")
    int OUTTYPE_OUT_RETAIL = 2;

    /**
     * 委托代销
     */
    @Defined(key = "outType_out", value = "委托代销")
    int OUTTYPE_OUT_CONSIGN = 3;

    /**
     * 赠送
     */
    @Defined(key = "outType_out", value = "赠送")
    int OUTTYPE_OUT_PRESENT = 4;

    /**
     * 采购入库
     */
    @Defined(key = "outType_in", value = "采购入库")
    int OUTTYPE_IN_COMMON = 0;

    /**
     * 调拨
     */
    @Defined(key = "outType_in", value = "调拨")
    int OUTTYPE_IN_MOVEOUT = 1;

    /**
     * 报废出库
     */
    @Defined(key = "outType_in", value = "报废")
    int OUTTYPE_IN_DROP = 2;

    /**
     * 系统纠正
     */
    @Defined(key = "outType_in", value = "系统纠正")
    int OUTTYPE_IN_ERRORP = 3;

    /**
     * 采购退货
     */
    int OUTTYPE_IN_RECEDE = 7;

    /**
     * 不需要发票
     */
    int HASINVOICE_NO = 0;

    /**
     * 需要
     */
    int HASINVOICE_YES = 1;

    /**
     * 流程中决定保存
     */
    String FLOW_DECISION_SAVE = "保存";

    /**
     * 流程中决定提交
     */
    String FLOW_DECISION_SUBMIT = "提交";

    /**
     * 临时的状态
     */
    int STATUS_TEMP = 99;

    /**
     * 没有开票
     */
    @Defined(key = "invoiceStatus", value = "没有开票")
    int INVOICESTATUS_INIT = 0;

    /**
     * 开票中
     */
    @Defined(key = "invoiceStatus", value = "开票中")
    int INVOICESTATUS_PROCESS = 1;

    /**
     * 全部开票
     */
    @Defined(key = "invoiceStatus", value = "全部开票中")
    int INVOICESTATUS_END = 2;

    /**
     * 提交
     */
    @Defined(key = "outBalanceStatus", value = "提交")
    int OUTBALANCE_STATUS_SUBMIT = 0;

    /**
     * 结算中心通过
     */
    @Defined(key = "outBalanceStatus", value = "结算中心通过")
    int OUTBALANCE_STATUS_PASS = 1;

    /**
     * 驳回
     */
    @Defined(key = "outBalanceStatus", value = "驳回")
    int OUTBALANCE_STATUS_REJECT = 2;

    /**
     * 结算单
     */
    @Defined(key = "outBalanceType", value = "结算单")
    int OUTBALANCE_TYPE_SAIL = 0;

    /**
     * 退货单
     */
    @Defined(key = "outBalanceType", value = "退货单")
    int OUTBALANCE_TYPE_BACK = 1;

    /**
     * 调出
     */
    @Defined(key = "moveOut", value = "调出")
    int MOVEOUT_OUT = 0;

    /**
     * 调入
     */
    @Defined(key = "moveOut", value = "调入")
    int MOVEOUT_IN = 1;

}
