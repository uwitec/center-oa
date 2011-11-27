/**
 * File Name: TcpConstanst.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-10<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.constanst;


import com.china.center.jdbc.annotation.Defined;


/**
 * TcpConstanst
 * 
 * @author ZHUZHU
 * @version 2011-7-10
 * @see TcpConstanst
 * @since 3.0
 */
public interface TcpConstanst
{
    /**
     * 初始
     */
    @Defined(key = "tcpStatus", value = "初始")
    int TCP_STATUS_INIT = 0;

    /**
     * 驳回
     */
    @Defined(key = "tcpStatus", value = "驳回")
    int TCP_STATUS_REJECT = 1;

    /**
     * 待部门经理审批
     */
    @Defined(key = "tcpStatus", value = "待部门经理审批")
    int TCP_STATUS_WAIT_DEPARTMENT = 2;

    /**
     * 待大区经理审批
     */
    @Defined(key = "tcpStatus", value = "待大区经理审批")
    int TCP_STATUS_WAIT_AREA = 3;

    /**
     * 待事业部经理审批
     */
    @Defined(key = "tcpStatus", value = "待事业部经理审批")
    int TCP_STATUS_WAIT_LOCATION = 4;

    /**
     * 待财务总监审批
     */
    @Defined(key = "tcpStatus", value = "待财务总监审批")
    int TCP_STATUS_WAIT_CFO = 5;

    /**
     * 待总裁审批
     */
    @Defined(key = "tcpStatus", value = "待总裁审批")
    int TCP_STATUS_WAIT_CEO = 6;

    /**
     * 待董事长审批
     */
    @Defined(key = "tcpStatus", value = "待董事长审批")
    int TCP_STATUS_WAIT_TOP = 7;

    /**
     * 待采购货比三家
     */
    @Defined(key = "tcpStatus", value = "待采购货比三家")
    int TCP_STATUS_WAIT_BUY = 11;

    /**
     * 待中心总监
     */
    @Defined(key = "tcpStatus", value = "待中心总监审批")
    int TCP_STATUS_WAIT_CCHECK = 12;

    /**
     * 待稽核
     */
    @Defined(key = "tcpStatus", value = "待稽核")
    int TCP_STATUS_WAIT_CHECK = 20;

    /**
     * 待权签人会签
     */
    @Defined(key = "tcpStatus", value = "待权签人会签")
    int TCP_STATUS_WAIT_QQR = 21;

    /**
     * 待财务支付
     */
    @Defined(key = "tcpStatus", value = "待财务支付")
    int TCP_STATUS_WAIT_PAY = 22;

    /**
     * 待财务入账
     */
    @Defined(key = "tcpStatus", value = "待财务入账")
    int TCP_STATUS_LAST_CHECK = 23;

    /**
     * 结束
     */
    @Defined(key = "tcpStatus", value = "结束")
    int TCP_STATUS_END = 99;

    /**
     * 借款
     */
    @Defined(key = "travelApplyBorrow", value = "借款")
    int TRAVELAPPLY_BORROW_YES = 1;

    /**
     * 不借款
     */
    @Defined(key = "travelApplyBorrow", value = "不借款")
    int TRAVELAPPLY_BORROW_NO = 0;

    /**
     * 公司付款给员工
     */
    @Defined(key = "expensePayType", value = "公司付款给员工")
    int PAYTYPE_PAY_YES = 1;

    /**
     * 收支均衡
     */
    @Defined(key = "expensePayType", value = "借款报销收支平衡")
    int PAYTYPE_PAY_OK = 0;

    /**
     * 员工付款给公司
     */
    @Defined(key = "expensePayType", value = "员工付款给公司")
    int PAYTYPE_PAY_NO = 2;

    /**
     * 现金
     */
    @Defined(key = "travelApplyReceiveType", value = "现金")
    int TRAVELAPPLY_RECEIVETYPE_CASH = 0;

    /**
     * 银行
     */
    @Defined(key = "travelApplyReceiveType", value = "银行")
    int TRAVELAPPLY_RECEIVETYPE_BANK = 1;

    /**
     * 出差申请处理的URL
     */
    String TCP_TRAVELAPPLY_PROCESS_URL = "../tcp/apply.do?method=findTravelApply&update=2&id=";

    /**
     * 报销处理的URL
     */
    String TCP_EXPENSE_PROCESS_URL = "../tcp/expense.do?method=findExpense&update=2&id=";

    /**
     * 申请借款的详细URL
     */
    String TCP_TRAVELAPPLY_DETAIL_URL = "../tcp/apply.do?method=findTravelApply&id=";

    /**
     * 报销详细URL
     */
    String TCP_EXPENSE_DETAIL_URL = "../tcp/expense.do?method=findExpense&id=";

    /**
     * 归属
     */
    @Defined(key = "tcpPool", value = "归属")
    int TCP_POOL_COMMON = 0;

    /**
     * 共享池
     */
    @Defined(key = "tcpPool", value = "共享池")
    int TCP_POOL_POOL = 1;

    /**
     * 销售系
     */
    @Defined(key = "tcpStype", value = "销售系")
    int TCP_STYPE_SAIL = 0;

    /**
     * 职能系
     */
    @Defined(key = "tcpStype", value = "职能系")
    int TCP_STYPE_WORK = 1;

    /**
     * 管理系
     */
    @Defined(key = "tcpStype", value = "管理系")
    int TCP_STYPE_MANAGER = 2;

    /**
     * 差旅费申请及借款
     */
    @Defined(key = "tcpType", value = "差旅费申请及借款")
    int TCP_APPLYTYPE_TRAVEL = 0;

    /**
     * 业务招待费申请及借款
     */
    @Defined(key = "tcpType", value = "业务招待费申请及借款")
    int TCP_APPLYTYPE_ENTERTAIN = 1;

    /**
     * 日常办公和固定资产采购申请及借
     */
    @Defined(key = "tcpType", value = "日常办公和固定资产采购申请及借款")
    int TCP_APPLYTYPE_STOCK = 2;

    /**
     * 对公业务申请及借款
     */
    @Defined(key = "tcpType", value = "对公业务申请及借款")
    int TCP_APPLYTYPE_PUBLIC = 3;

    /**
     * 差旅费报销
     */
    @Defined(key = "tcpType", value = "差旅费报销")
    int TCP_EXPENSETYPE_TRAVEL = 11;

    /**
     * 业务招待费报销
     */
    @Defined(key = "tcpType", value = "业务招待费报销")
    int TCP_EXPENSETYPE_ENTERTAIN = 12;

    /**
     * 日常费用报销(日常办公和固定资产采购申请及借/对公业务申请及借款)
     */
    @Defined(key = "tcpType", value = "日常费用报销")
    int TCP_EXPENSETYPE_PUBLIC = 13;

    /**
     * 通用费用报销
     */
    @Defined(key = "tcpType", value = "通用费用报销")
    int TCP_EXPENSETYPE_COMMON = 14;

    /**
     * 未关联报销
     */
    @Defined(key = "tcpApplyFeedback", value = "未关联报销")
    int TCP_APPLY_FEEDBACK_NO = 0;

    /**
     * 已关联报销(说明借款结束)
     */
    @Defined(key = "tcpApplyFeedback", value = "已关联报销")
    int TCP_APPLY_FEEDBACK_YES = 1;

    /**
     * 公司付款给员工
     */
    @Defined(key = "tcpPayType", value = "公司付款给员工")
    int PAYTYPE_GPAY_YES = 1;

    /**
     * 收支均衡
     */
    @Defined(key = "tcpPayType", value = "借款报销收支平衡")
    int PAYTYPE_GPAY_OK = 0;

    /**
     * 员工付款给公司
     */
    @Defined(key = "tcpPayType", value = "员工付款给公司")
    int PAYTYPE_GPAY_NO = 2;

    /**
     * 员工付款给公司
     */
    @Defined(key = "tcpPayType", value = "员工申请借款")
    int PAYTYPE_GPAY_BO = 98;
}
