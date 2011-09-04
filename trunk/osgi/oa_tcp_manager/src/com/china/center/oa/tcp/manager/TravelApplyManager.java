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
import com.china.center.oa.tcp.vo.TravelApplyVO;


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

    /**
     * 删除
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean deleteTravelApplyBean(User user, String id)
        throws MYException;

    /**
     * findVO
     * 
     * @param id
     * @return
     */
    TravelApplyVO findVO(String id);

    /**
     * 提交
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean submitTravelApplyBean(User user, String id, String processId)
        throws MYException;

    /**
     * 通过
     * 
     * @param user
     * @param id
     * @param processId
     * @param reason
     * @return
     * @throws MYException
     */
    boolean passTravelApplyBean(User user, String id, String processId, String reason)
        throws MYException;

    /**
     * rejectTravelApplyBean
     * 
     * @param user
     * @param id
     * @param processId
     * @param reason
     * @return
     * @throws MYException
     */
    boolean rejectTravelApplyBean(User user, String id, String type, String reason)
        throws MYException;
}
