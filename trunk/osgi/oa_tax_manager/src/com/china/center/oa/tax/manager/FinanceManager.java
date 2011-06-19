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

    boolean updateFinanceBean(User user, FinanceBean bean)
        throws MYException;

    /**
     * addFinanceBeanWithoutTransactional(无事务的)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addFinanceBeanWithoutTransactional(User user, FinanceBean bean)
        throws MYException;

    boolean deleteFinanceBean(User user, String id)
        throws MYException;

    /**
     * 没有事务的删除
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean deleteFinanceBeanWithoutTransactional(User user, String id)
        throws MYException;

    boolean checks(User user, String id, String reason)
        throws MYException;

    boolean checks2(User user, String id, int type, String reason)
        throws MYException;

    boolean deleteChecks(User user, String id)
        throws MYException;

    boolean updateFinanceCheck(User user, String id, String reason)
        throws MYException;
}
