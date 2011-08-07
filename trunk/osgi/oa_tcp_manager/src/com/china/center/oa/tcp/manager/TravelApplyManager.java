/**
 * File Name: TravelApplyManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-10<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.tcp.bean.TravelApplyBean;


/**
 * TravelApplyManager
 * 
 * @author ZHUZHU
 * @version 2011-7-10
 * @see TravelApplyManager
 * @since 3.0
 */
public interface TravelApplyManager
{
    /**
     * 增加差旅费申请及借款
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addTravelApplyBean(User user, TravelApplyBean bean)
        throws MYException;

    /**
     * 修改差旅费申请及借款
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean updateTravelApplyBean(User user, TravelApplyBean bean)
        throws MYException;
}
