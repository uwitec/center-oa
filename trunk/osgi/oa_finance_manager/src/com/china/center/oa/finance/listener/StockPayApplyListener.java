/**
 * File Name: PaymentListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener;


import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.StockPayApplyBean;


/**
 * PaymentListener
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see StockPayApplyListener
 * @since 3.0
 */
public interface StockPayApplyListener extends ParentListener
{
    /**
     * 采购付款--会计付款
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    void onEndStockPayBySEC(User user, StockPayApplyBean bean)
        throws MYException;
}
