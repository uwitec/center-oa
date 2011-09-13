/**
 * File Name: TcpPayListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-9-13<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.listener;


import java.util.List;

import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.tcp.bean.TravelApplyBean;


/**
 * TcpPayListener
 * 
 * @author ZHUZHU
 * @version 2011-9-13
 * @see TcpPayListener
 * @since 1.0
 */
public interface TcpPayListener extends ParentListener
{
    /**
     * 出差申请财务支付的监听
     * 
     * @param user
     * @param bean
     * @param outBillList
     * @throws MYException
     */
    void onPayTravelApply(User user, TravelApplyBean bean, List<OutBillBean> outBillList)
        throws MYException;
}
