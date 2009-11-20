/**
 * File Name: AuthConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;

/**
 * AuthConstant
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see AuthConstant
 * @since 1.0
 */
public interface AuthConstant
{
    /**
     * 默认都有的
     */
    String PUNLIC_AUTH = "0000";

    /**
     * 操作区域
     */
    String LOCATION_OPR = "010101";

    /**
     * 操作人员
     */
    String STAFFER_OPR = "010201";

    /**
     * 操作角色(暂时和操作用户一样的权限)
     */
    String ROLE_OPR = "010401";

    /**
     * 操作用户
     */
    String USER_OPR = "010401";

    /**
     * 组织结构管理
     */
    String ORG_MANAGER = "0105";

    /**
     * 部门管理
     */
    String DEPARTMENT_MANAGER = "0106";

    /**
     * 职务管理
     */
    String POST_MANAGER = "0107";

    /**
     * 申请增删改客户
     */
    String CUSTOMER_OPR = "0202";

    /**
     * 审批客户
     */
    String CUSTOMER_CHECK = "0203";

    /**
     * 查看非本人的客户联系方式
     */
    String CUSTOMER_OQUERY = "0204";

    /**
     * 申请分配客户
     */
    String CUSTOMER_APPLY_ASSIGN = "0205";

    /**
     * 查询历史
     */
    String CUSTOMER_QUERY_HIS = "0206";

    /**
     * 查询分公司所有客户
     */
    String CUSTOMER_QUERY_LOCATION = "0207";

    /**
     * 导入客户
     */
    String CUSTOMER_UPLOAD = "0208";

    /**
     * 客户分布
     */
    String CUSTOMER_DISTRIBUTE = "0209";

    /**
     * 客户分公司全量同步
     */
    String CUSTOMER_SYNCHRONIZATION = "0210";

    /**
     * 客户回收
     */
    String CUSTOMER_RECLAIM = "0211";

    /**
     * 查询供应商
     */
    String CUSTOMER_QUERY_PROVIDER = "0212";

    /**
     * 操作供应商
     */
    String CUSTOMER_OPR_PROVIDER = "0213";

    /**
     * 客户编码分配
     */
    String CUSTOMER_ASSIGN_CODE = "0214";

    /**
     * 客户/供应商 修改核对
     */
    String CUSTOMER_HIS_CHECK = "0215";

    /**
     * 客户审计
     */
    String CUSTOMER_CHECK_PARENT = "0216";

    /**
     * 客户审计
     */
    String CUSTOMER_CHECK_COMMON = "021601";

    /**
     * 客户审计审批
     */
    String CUSTOMER_CHECK_CHECK = "021602";

    /**
     * 所有审计结果查看
     */
    String CUSTOMER_CHECK_ALLQUERY = "021603";

    /**
     * 客户信用审核
     */
    String CUSTOMER_CREDIT_CHECK = "0217";

    /**
     * 查询地市考核配置
     */
    String CITYCONFIG_QUERY = "0301";

    /**
     * 操作地市考核配置
     */
    String CITYCONFIG_OPR = "0302";

    /**
     * 查询考核
     */
    String EXAMINE_QUERY = "0303";

    /**
     * 操作考核
     */
    String EXAMINE_OPR = "0304";

    /**
     * 考核变更确认
     */
    String EXAMINE_UPDATE_COMMIT = "0305";

    /**
     * 客户利润查询
     */
    String EXAMINE_PROFIT_QUERY = "0306";

    /**
     * 客户利润操作
     */
    String EXAMINE_PROFIT_OPR = "0307";

    /**
     * 业务员的日志
     */
    String WORKLOG_OPR = "0401";

    /**
     * 日志分析
     */
    String WORKLOG_ANY = "0402";

    /**
     * 日志客户全局查询
     */
    String WORKLOG_GBOAL_QUERY = "0403";

    /**
     * 预算查询
     */
    String BUDGET_QUERY = "0501";

    /**
     * 预算操作
     */
    String BUDGET_OPR = "0502";

    /**
     * 预算审核
     */
    String BUDGET_CHECK = "0503";

    /**
     * 根预算的增加
     */
    String BUDGET_ADDROOT = "0504";

    /**
     * 根预算的审核(包括预算)
     */
    String BUDGET_OPRROOT = "0505";

    /**
     * 预算变更
     */
    String BUDGET_CHANGE = "0506";

    /**
     * 预算变更--申请变更
     */
    String BUDGET_CHANGE_APPLY = "050601";

    /**
     * 预算变更审批--财务总监
     */
    String BUDGET_CHANGE_APPROVE_CFO = "050602";

    /**
     * 预算变更审批--总经理
     */
    String BUDGET_CHANGE_APPROVE_COO = "050603";

    /**
     * 预算变更审批--董事长
     */
    String BUDGET_CHANGE_APPROVE_CEO = "050604";

    /**
     * 私有群组操作
     */
    String GROUP_OPR = "0601";

    /**
     * 公共群组操作
     */
    String GROUP_PUBLIC_OPR = "0602";

    /**
     * 系统群组操作
     */
    String GROUP_SYSTEM_OPR = "0603";

    /**
     * 流程模板操作
     */
    String FLOW_TEMPLATE_OPR = "0701";

    /**
     * 流程定义操作
     */
    String FLOW_DEFINED_OPR = "0702";

    /**
     * 强制废弃
     */
    String FLOW_DEFINED_FORCE_DROP = "0703";

    /**
     * 信用等级操作
     */
    String CREDIT_OPR = "0901";

    /**
     * 信用等级操作
     */
    String CREDIT_LEVEL_OPR = "0902";

    /**
     * 强制修改
     */
    String CREDIT_FORCE_UPDATE = "0903";
}
