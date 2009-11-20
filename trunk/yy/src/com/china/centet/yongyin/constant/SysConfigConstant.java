/*
 * File Name: Constant.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.constant;

/**
 * <描述>
 * 
 * @author zhuzhu
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
     * 是否启动加密锁
     */
    String NEED_SUPER_ENC_LOCK = "NEED_SUPER_ENC_LOCK";

}
