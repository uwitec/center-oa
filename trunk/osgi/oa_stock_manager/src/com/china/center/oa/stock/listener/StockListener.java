/**
 * File Name: StockListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-5<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.stock.listener;


import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.stock.bean.StockBean;


/**
 * StockListener
 * 
 * @author ZHUZHU
 * @version 2010-12-5
 * @see StockListener
 * @since 3.0
 */
public interface StockListener extends ParentListener
{
    /**
     * 结束采购的事件通知(主要是生成入库单)
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    void onEndStock(final User user, final StockBean bean)
        throws MYException;;
}
