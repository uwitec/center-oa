/**
 * File Name: FechProductListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stockvssail.listener.FechProductListener;


/**
 * TODO_OSGI 采购-拿货
 * 
 * @author ZHUZHU
 * @version 2011-6-12
 * @see FechProductListenerTaxGlueImpl
 * @since 3.0
 */
public class FechProductListenerTaxGlueImpl implements FechProductListener
{

    /**
     * 采购拿货的时候生成凭证
     */
    public void onFechProduct(User user, StockBean bean, StockItemBean each, OutBean out)
        throws MYException
    {
        // 借:1.库存商品(1243) 2.主营业务税金即附加(5402)（供应商税点*库存商品价值）（只有一般纳税人才会有主营业务税金，其他的没有此项）(5402)
        // 贷:1.应付账款-货款(2122-01) 2.应付账款-货款（税点应付）
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "FechProductListener.TaxGlueImpl";
    }

}
