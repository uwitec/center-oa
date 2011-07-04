/**
 * File Name: StorageApplyManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-28<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.bean.StorageApplyBean;


/**
 * StorageApplyManager
 * 
 * @author ZHUZHU
 * @version 2010-10-28
 * @see StorageApplyManager
 * @since 1.0
 */
public interface StorageApplyManager
{
    boolean addBean(User user, StorageApplyBean bean)
        throws MYException;

    boolean passBean(User user, String id)
        throws MYException;

    boolean rejectBean(User user, String id)
        throws MYException;
}
