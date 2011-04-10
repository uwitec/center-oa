/**
 * File Name: StockPayApplyManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.OutBillBean;


/**
 * StockPayApplyManager
 * 
 * @author ZHUZHU
 * @version 2011-2-18
 * @see StockPayApplyManager
 * @since 3.0
 */
public interface StockPayApplyManager
{
    /**
     * 通过(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean submitStockPayApply(User user, String id, double payMoney, String reason)
        throws MYException;

    /**
     * 通过(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean passStockPayByCEO(User user, String id, String reason)
        throws MYException;

    /**
     * 结束申请付款(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean endStockPayBySEC(User user, String id, String reason, List<OutBillBean> outBillList)
        throws MYException;

    /**
     * 驳回(需要同步)
     * 
     * @param stafferId
     * @param id
     * @return
     * @throws MYException
     */
    boolean rejectStockPayApply(User user, String id, String reason)
        throws MYException;

    /**
     * 强制关闭
     * 
     * @param user
     * @param id
     * @param reason
     * @return
     * @throws MYException
     */
    boolean closeStockPayApply(User user, String id, String reason)
        throws MYException;
}
