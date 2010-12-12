/**
 * File Name: FinanceFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.facade;


import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BankBean;


/**
 * FinanceFacade
 * 
 * @author ZHUZHU
 * @version 2010-12-12
 * @see FinanceFacade
 * @since 3.0
 */
public interface FinanceFacade
{
    boolean addBankBean(String userId, BankBean bean)
        throws MYException;

    boolean updateBankBean(String userId, BankBean bean)
        throws MYException;

    boolean deleteBankBean(String userId, String id)
        throws MYException;
}
