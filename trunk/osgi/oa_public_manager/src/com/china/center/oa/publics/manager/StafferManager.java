/**
 * File Name: StafferManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-23<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import java.util.Collection;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.listener.StafferListener;
import com.china.center.oa.publics.vo.StafferVO;


/**
 * StafferManager
 * 
 * @author ZHUZHU
 * @version 2010-6-23
 * @see StafferManager
 * @since 1.0
 */
public interface StafferManager
{
    boolean addBean(User user, StafferBean bean)
        throws MYException;

    boolean updateBean(User user, StafferBean bean)
        throws MYException;

    boolean updatePwkey(User user, StafferBean bean)
        throws MYException;

    boolean delBean(User user, String stafferId)
        throws MYException;

    /**
     * 获得本职员的上级
     * 
     * @param stafferId
     * @return
     */
    Collection<StafferVO> querySuperiorStaffer(String stafferId);

    /**
     * putListener
     * 
     * @param listener
     */
    void putListener(StafferListener listener);

    /**
     * removeListener
     * 
     * @param listener
     */
    void removeListener(String listener);
}
