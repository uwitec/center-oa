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
    boolean addInBillBean(User user, InBillBean bean)
        throws MYException;

    boolean updateInBillBean(User user, InBillBean bean)
        throws MYException;

    boolean deleteInBillBean(User user, String id)
        throws MYException;
}
