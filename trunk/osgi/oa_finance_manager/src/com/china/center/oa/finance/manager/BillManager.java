/**
 * File Name: BillManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;


/**
 * BillManager
 * 
 * @author ZHUZHU
 * @version 2010-12-26
 * @see BillManager
 * @since 3.0
 */
public interface BillManager
{
    /**
     * addInBillBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addInBillBean(User user, InBillBean bean)
        throws MYException;

    /**
     * addInBillBeanWithoutTransaction
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addInBillBeanWithoutTransaction(User user, InBillBean bean)
        throws MYException;

    boolean updateInBillBean(User user, InBillBean bean)
        throws MYException;

    boolean deleteInBillBean(User user, String id)
        throws MYException;

    /**
     * 分拆
     * 
     * @param user
     * @param id
     * @param newMoney
     * @return
     * @throws MYException
     */
    boolean splitInBillBean(User user, String id, double newMoney)
        throws MYException;

    boolean splitInBillBeanWithoutTransactional(User user, String id, double newMoney)
        throws MYException;

    /**
     * addOutBillBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addOutBillBean(User user, OutBillBean bean)
        throws MYException;

    /**
     * deleteOutBillBean
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean deleteOutBillBean(User user, String id)
        throws MYException;
}
