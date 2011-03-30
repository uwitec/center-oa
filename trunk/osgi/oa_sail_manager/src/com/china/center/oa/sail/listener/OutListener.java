/**
 * File Name: OutListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.listener;


import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.OutBean;


/**
 * OutListener
 * 
 * @author ZHUZHU
 * @version 2011-1-9
 * @see OutListener
 * @since 3.0
 */
public interface OutListener extends ParentListener
{
    /**
     * 驳回监听
     * 
     * @param bean
     * @throws MYException
     */
    void onReject(User user, OutBean bean)
        throws MYException;

    /**
     * 通过监听
     * 
     * @param bean
     * @throws MYException
     */
    void onPass(User user, OutBean bean)
        throws MYException;

    /**
     * 取消坏账
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    void onCancleBadDebts(User user, OutBean bean)
        throws MYException;

    /**
     * 确认收款
     * 
     * @param bean
     * @throws MYException
     */
    ResultBean onHadPay(User user, OutBean bean);

    /**
     * 销售单还剩余未付款的金额(这里坏账是在内的),仅仅是普通销售的
     * 
     * @param user
     * @param fullId
     * @return
     */
    double outNeedPayMoney(User user, String fullId);
}
