/*
 * File Name: SaleConstant.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wokflow.sale;

/**
 * 销售单的常量定义
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public interface SaleConstant
{
    /**
     * 保存动作(保存和提交)
     */
    String SAVE_OPRRATION = "saves";

    String FLOW_OPRRATION = "needFlow";

    /**
     * 保存
     */
    String VALUE_SAVE = "0";

    /**
     * 提交
     */
    String VALUE_SUBMIT = "1";

    /**
     * 流程中决定保存
     */
    String FLOW_DECISION_SAVE = "保存";

    /**
     * 流程中决定提交
     */
    String FLOW_DECISION_SUBMIT = "提交";

    String YES = "是";

    String NO = "否";
}
