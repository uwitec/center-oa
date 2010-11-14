/**
 * File Name: OutConstanst.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-10-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.constanst;

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
     * 自主调入(从其他区域调出，但是单据属于其他区域)
     */
    int INBILL_SELF_IN = 8;

    /**
     * 销售单
     */
    int OUT_TYPE_OUTBILL = 0;

    /**
     * 入库单
     */
    int OUT_TYPE_INBILL = 1;

    /**
     * 合成单
     */
    int OUT_TYPE_COMPOSE = 2;

    /**
     * 产品合分
     */
    int OUTTYPES_COMPOSE = 7;

    /**
     * 仓区移动
     */
    int OUT_TYPE_MOVE = 8;

    /**
     * 自动修正
     */
    int OUT_TYPE_MODIFY = 9;

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
     * 黑名单客户
     */
    String BLACK_LEVEL = "90000000000000000000";

    /**
     * 初始化
     */
    int STATUS_SAVE = 0;

    /**
     * 业务员提交
     */
    int STATUS_SUBMIT = 1;

    /**
     * 驳回
     */
    int STATUS_REJECT = 2;

    /**
     * 库管发货(这里的销售单库存就变动了)(一般此通过即是销售单已经OK status in (3, 4))
     */
    int STATUS_PASS = 3;

    /**
     * 财务应收
     */
    int STATUS_SEC_PASS = 4;

    /**
     * 结算中心通过
     */
    int STATUS_MANAGER_PASS = 6;

    /**
     * 物流管理员通过
     */
    int STATUS_FLOW_PASS = 7;

    /**
     * 分公司总经理审批
     */
    int STATUS_LOCATION_MANAGER_CHECK = 8;

    /**
     * 未付款
     */
    int PAY_NOT = 0;

    /**
     * 付款
     */
    int PAY_YES = 1;

    /**
     * 过期
     */
    int PAY_OVER = 2;

    /**
     * 入库单--采购入库
     */
    int INBILL_COMMON = 0;

    /**
     * 调出
     */
    int INBILL_OUT = 1;

    /**
     * 调入
     */
    int INBILL_IN = 4;

    /**
     * 非在途
     */
    int IN_WAY_NO = 0;

    /**
     * 在途
     */
    int IN_WAY = 1;

    /**
     * 在途结束
     */
    int IN_WAY_OVER = 2;

    /**
     * 销售出库
     */
    int OUTTYPE_OUT_COMMON = 0;

    /**
     * 个人领样
     */
    int OUTTYPE_OUT_SWATCH = 1;

    /**
     * 采购入库
     */
    int OUTTYPE_IN_COMMON = 0;

    /**
     * 调出
     */
    int OUTTYPE_IN_MOVEOUT = 1;

    /**
     * 盘亏出库
     */
    int OUTTYPE_IN_PANGKUI = 2;

    /**
     * 盘盈入库
     */
    int OUTTYPE_IN_PANGYING = 3;

    /**
     * 退换货入库
     */
    int OUTTYPE_IN_EXCHANGE = 5;

    /**
     * 报废出库
     */
    int OUTTYPE_IN_DROP = 6;

    /**
     * 采购退货
     */
    int OUTTYPE_IN_RECEDE = 7;

    /**
     * 调入
     */
    int OUTTYPE_IN_MOVEIN = 8;

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

}
