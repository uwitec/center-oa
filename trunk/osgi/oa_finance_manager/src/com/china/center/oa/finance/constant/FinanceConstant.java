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
}
