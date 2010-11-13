/**
 * File Name: OutManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.manager;


import java.util.Map;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.sail.bean.OutBean;


/**
 * OutManager
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see OutManager
 * @since 1.0
 */
public interface OutManager
{
    String addOut(final OutBean outBean, final Map dataMap, final User user)
        throws MYException;

    /**
     * coloneOutAndSubmitAffair(自动生成调入的库单)
     * 
     * @param outBean
     * @param user
     * @return
     * @throws MYException
     */
    String coloneOutAndSubmitAffair(final OutBean outBean, final User user)
        throws MYException;

    /**
     * 采购入库的操作(没有事务)
     * 
     * @param outBean
     * @param user
     * @param type
     * @return
     * @throws MYException
     */
    String coloneOutAndSubmitWithOutAffair(OutBean outBean, User user, int type)
        throws MYException;

    boolean submit(final String fullId, final User user)
        throws MYException;

    /**
     * submitWithOutAffair(采购入库的时候使用)
     * 
     * @param fullId
     * @param user
     * @return
     * @throws MYException
     */
    boolean submitWithOutAffair(final String fullId, final User user, int type)
        throws MYException;

    boolean reject(final String fullId, final User user, final String reason)
        throws MYException;

    boolean pass(final String fullId, final User user, final int nextStatus, final String reason,
                 final String depotpartId)
        throws MYException;

    boolean check(final String fullId, final User user, final String checks)
        throws MYException;

    OutBean findOutById(final String fullId);

    boolean delOut(final String fullId)
        throws MYException;

    boolean updateOut(final OutBean out)
        throws MYException;

    boolean modifyPay(String fullId, int pay);

    boolean mark(String fullId, boolean status);

    boolean modifyReDate(String fullId, String reDate);

    boolean modifyOutHadPay(String fullId, String hadPay);
}
