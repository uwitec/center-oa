/**
 * File Name: FinanceManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.tax.bean.FinanceBean;


/**
 * FinanceManager
 * 
 * @author ZHUZHU
 * @version 2011-2-7
 * @see FinanceManager
 * @since 1.0
 */
public interface FinanceManager
{
    boolean addFinanceBean(User user, FinanceBean bean)
        throws MYException;

    boolean deleteFinanceBean(User user, String id)
        throws MYException;

    boolean checks(User user, String id, String reason)
        throws MYException;
}
