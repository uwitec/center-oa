/*
 * File Name: Trigger.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.trigger;


import java.util.List;

import com.china.centet.yongyin.bean.Bill;
import com.china.centet.yongyin.bean.StatBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-8-15
 * @see
 * @since
 */
public interface Trigger
{
    void statMoney();

    void statBankEveryDay();

    StatBean statBank(List<Bill> billList, String statId, String llastId, String bank,
                      boolean isFlag);

    int synchronizedProduct();

    void printCacheEfficiency();
}
