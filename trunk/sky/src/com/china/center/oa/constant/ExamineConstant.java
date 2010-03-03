/**
 * File Name: ExamineConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;


import com.china.center.annotation.Defined;


/**
 * ExamineConstant
 * 
 * @author zhuzhu
 * @version 2009-1-7
 * @see ExamineConstant
 * @since 1.0
 */
public interface ExamineConstant
{
    /**
     * 初始化
     */
    @Defined(key = "examineStatus", value = "初始化")
    int EXAMINE_STATUS_INIT = 0;

    /**
     * 待业务员确认
     */
    @Defined(key = "examineStatus", value = "待业务员确认")
    int EXAMINE_STATUS_SUBMIT = 1;

    /**
     * 业务员驳回
     */
    @Defined(key = "examineStatus", value = "业务员驳回")
    int EXAMINE_STATUS_REJECT = 2;

    /**
     * 考核运行
     */
    @Defined(key = "examineStatus", value = "考核运行")
    int EXAMINE_STATUS_PASS = 3;

    /**
     * 考核结束
     */
    @Defined(key = "examineStatus", value = "考核结束")
    int EXAMINE_STATUS_END = 4;

    /**
     * 终端
     */
    @Defined(key = "examineType", value = "终端")
    int EXAMINE_TYPE_TER = CustomerConstant.SELLTYPE_TER;

    /**
     * 拓展
     */
    @Defined(key = "examineType", value = "拓展")
    int EXAMINE_TYPE_EXPEND = CustomerConstant.SELLTYPE_EXPEND;

    @Defined(key = "examineAbs", value = "非抽象")
    int EXAMINE_ABS_FALSE = 0;

    /**
     * 拓展
     */
    @Defined(key = "examineAbs", value = "抽象")
    int EXAMINE_ABS_TRUE = 1;

    /**
     * 分公司经理考核
     */
    @Defined(key = "attType", value = "分公司经理考核")
    int EXAMINE_ATTTYPE_LOCATION = 0;

    /**
     * 部门经理考核
     */
    @Defined(key = "attType", value = "部门经理考核")
    int EXAMINE_ATTTYPE_DEPARTMENT = 1;

    /**
     * 个人考核
     */
    @Defined(key = "attType", value = "个人考核")
    int EXAMINE_ATTTYPE_PERSONAL = 2;

    /**
     * 考核项--新客户考核
     */
    int EXAMINE_ITEM_TYPE_NEWCUSTOMER = 1;

    /**
     * 考核项--老客户考核
     */
    int EXAMINE_ITEM_TYPE_OLDCUSTOMER = 2;

    /**
     * 考核项--利润
     */
    int EXAMINE_ITEM_TYPE_PROFIT = 3;

    /**
     * 考核项--区域利润
     */
    int EXAMINE_ITEM_TYPE_CPROFIT = 4;

    /**
     * 考核项--产品考核(废弃)
     */
    int EXAMINE_ITEM_TYPE_PRODUCT = 5;
    
    /**
     * 考核项--产品考核
     */
    int EXAMINE_ITEM_TYPE_PRODUCT2 = 9995;

    /**
     * 考核项--新客户考核(终端)
     */
    int EXAMINE_ITEM_TYPE_TER_NEWCUSTOMER = 6;

    /**
     * 考核项--老客户考核(终端)
     */
    int EXAMINE_ITEM_TYPE_TER_OLDCUSTOMER = 7;

    /**
     * 初始化
     */
    @Defined(key = "examineResult", value = "初始")
    int EXAMINE_RESULT_INIT = 0;

    /**
     * 考核结果--刚好
     */
    @Defined(key = "examineResult", value = "达标")
    int EXAMINE_RESULT_OK = 1;

    /**
     * 考核结果--超过预期
     */
    @Defined(key = "examineResult", value = "超过预期")
    int EXAMINE_RESULT_GOOD = 2;

    @Defined(key = "examineResult", value = "未达标")
    int EXAMINE_RESULT_LESS = 3;

    /**
     * 考核项状态--初始
     */
    @Defined(key = "examineItemStatus", value = "初始")
    int EXAMINE_ITEM_STATUS_INIT = 0;

    /**
     * 考核项状态--结束
     */
    @Defined(key = "examineItemStatus", value = "结束")
    int EXAMINE_ITEM_STATUS_END = 1;
}
