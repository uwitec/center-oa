/**
 * File Name: StockListenerTaxImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stock.listener.StockListener;


/**
 * TODO_OSGI 采购-拿货
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see StockListenerTaxGlueImpl
 * @since 3.0
 */
public class StockListenerTaxGlueImpl implements StockListener
{
    public void onEndStockItem(User user, StockBean bean, StockItemBean item)
        throws MYException
    {

    }

    public String getListenerType()
    {
        return "StockListener.TaxGlueImpl";
    }

}
