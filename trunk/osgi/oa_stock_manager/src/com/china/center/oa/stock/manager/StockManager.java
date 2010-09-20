/**
 * File Name: StockManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-9-20<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.stock.manager;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stock.vo.StockVO;


/**
 * StockManager
 * 
 * @author ZHUZHU
 * @version 2010-9-20
 * @see StockManager
 * @since 1.0
 */
public interface StockManager
{
    boolean addStockBean(final User user, final StockBean bean)
        throws MYException;

    StockVO findStockVO(String id);

    boolean stockItemAskChange(String itemId, String providerId)
        throws MYException;

    boolean stockItemAsk(StockItemBean bean)
        throws MYException;

    boolean stockItemAskForNet(StockItemBean oldItem, List<StockItemBean> newItemList)
        throws MYException;

    boolean updateStockBean(final User user, final StockBean bean)
        throws MYException;

    String endStock(final User user, final String id)
        throws MYException;

    boolean delStockBean(final User user, final String id)
        throws MYException;

    boolean passStock(final User user, final String id)
        throws MYException;

    boolean rejectStock(final User user, final String id, String reason)
        throws MYException;

    boolean rejectStockToAsk(final User user, final String id, String reason)
        throws MYException;
}
