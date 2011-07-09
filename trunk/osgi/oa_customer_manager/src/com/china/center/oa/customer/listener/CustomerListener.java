/**
 * File Name: CustomerCreditListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-20<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.listener;


import com.center.china.osgi.publics.ParentListener;
import com.china.center.common.MYException;
import com.china.center.oa.customer.bean.CustomerBean;


/**
 * 客户信用监听,主要是实现信用的使用
 * 
 * @author ZHUZHU
 * @version 2010-11-20
 * @see CustomerListener
 * @since 3.0
 */
public interface CustomerListener extends ParentListener
{
    /**
     * 客户没有支付的金额(每个模块独立核算自己的，多个模块最终合计)
     * 
     * @param bean
     * @return
     */
    double onNoPayBusiness(CustomerBean bean);

    /**
     * 删除监听
     * 
     * @param bean
     * @throws MYException
     */
    void onDelete(CustomerBean bean)
        throws MYException;
}
