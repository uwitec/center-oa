/**
 * File Name: PaymentManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.PaymentBean;


/**
 * PaymentManager
 * 
 * @author ZHUZHU
 * @version 2010-12-22
 * @see PaymentManager
 * @since 3.0
 */
public interface PaymentManager
{
    boolean addBean(User user, PaymentBean bean)
        throws MYException;

    boolean updateBean(User user, PaymentBean bean)
        throws MYException;

    boolean deleteBean(User user, String id)
        throws MYException;

    /**
     * 领取回款(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean drawBean(String stafferId, String id)
        throws MYException;

    /**
     * 退领(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean dropBean(String stafferId, String id)
        throws MYException;
}
