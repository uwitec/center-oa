/*
 * File Name: Constant.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-16
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;

/**
 * SysConfigConstant
 * 
 * @author ZHUZHU
 * @version 2007-8-16
 * @see
 * @since
 */
public interface SysConfigConstant
{
    /**
     * 超级二次密码
     */
    String SUPERPASSWORD = "SUPERPASSWORD";

    /**
     * 是否二次验证密码
     */
    String NEEDSUPERPASSWORD = "NEEDSUPERPASSWORD";

    /**
     * 采购单据预警数(单位:人民币)
     */
    String STOCK_MAX_SINGLE_MONEY = "STOCK_MAX_SINGLE_MONEY";

    /**
     * 登录暗号
     */
    String SIGN_YY_CENTER = "SIGN_YY_CENTER";

    /**
     * 询价最近的N条记录
     */
    String PRICE_NEARLY_MAX = "PRICE_NEARLY_MAX";

    /**
     * 财务年度开始时间 默认:03-01
     */
    String DATE_OF_YEAR = "DATE_OF_YEAR";

    /**
     * 日志的最早提交时间
     */
    String WORKLOG_BEGINTIME = "WORKLOG_BEGINTIME";

    /**
     * 日志的最迟提交时间
     */
    String WORKLOG_ENDTIME = "WORKLOG_ENDTIME";

    /**
     * 最小拜访次数,终端客户变成老客户
     */
    String MAX_VISITS = "MAX_VISITS";

    /**
     * 是否启动加密锁
     */
    String NEED_SUPER_ENC_LOCK = "NEED_SUPER_ENC_LOCK";

    /**
     * 产品数量快照天数
     */
    String SNAPSHO_DAYS = "SNAPSHO_DAYS";

    /**
     * static credit
     */
    String CREDIT_STATIC = "CREDIT_STATIC";

    /**
     * dynamic credit
     */
    String CREDIT_DYNAMIC = "CREDIT_DYNAMIC";

    /**
     * 是否启动客户信用额度
     */
    String OUT_CREDIT = "OUT_CREDIT";

    /**
     * 外网询价最大产品数量
     */
    String ASK_PRODUCT_AMOUNT_MAX = "ASK_PRODUCT_AMOUNT_MAX";

}
