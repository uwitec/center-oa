/**
 * File Name: BankManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BankBean;


/**
 * BankManager
 * 
 * @author ZHUZHU
 * @version 2010-12-12
 * @see BankManager
 * @since 3.0
 */
public interface BankManager
{
    boolean addBean(User user, BankBean bean)
        throws MYException;

    boolean updateBean(User user, BankBean bean)
        throws MYException;

    boolean deleteBean(User user, String id)
        throws MYException;
}
