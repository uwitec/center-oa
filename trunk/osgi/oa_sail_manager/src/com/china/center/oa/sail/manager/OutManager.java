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

import com.center.china.osgi.publics.ListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.listener.OutListener;


/**
 * OutManager
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see OutManager
 * @since 1.0
 */
public interface OutManager extends ListenerManager<OutListener>
{
    String addOut(final OutBean outBean, final Map dataMap, final User user)
        throws MYException;

    /**
     * coloneOutAndSubmitAffair(自动生成入库单)
     * 
     * @param outBean
     * @param user
     * @return
     * @throws MYException
     */
    String coloneOutAndSubmitAffair(final OutBean outBean, final User user, int type)
        throws MYException;

    /**
     * coloneOutWithAffair(事务提交,入库单)
     * 
     * @param outBean
     * @param user
     * @param type
     * @return
     * @throws MYException
     */
    String coloneOutWithAffair(final OutBean outBean, final User user, int type)
        throws MYException;

    /**
     * coloneOutWithOutAffair(自动生成个人领样的退货入库单,且在保存态)
     * 
     * @param outBean
     * @param user
     * @return
     * @throws MYException
     */
    String coloneOutWithoutAffair(final OutBean outBean, final User user, int type)
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

    /**
     * submit
     * 
     * @param fullId
     * @param user
     * @param storageType
     *            库存变动类型
     * @return 修改后的单据状态
     * @throws MYException
     */
    int submit(final String fullId, final User user, int storageType)
        throws MYException;

    /**
     * submitWithOutAffair(采购入库的时候使用)
     * 
     * @param fullId
     * @param user
     * @return
     * @throws MYException
     */
    int submitWithOutAffair(final String fullId, final User user, int type)
        throws MYException;

    /**
     * reject
     * 
     * @param fullId
     * @param user
     * @param reason
     * @return 修改后的单据状态
     * @throws MYException
     */
    int reject(final String fullId, final User user, final String reason)
        throws MYException;

    /**
     * pass
     * 
     * @param fullId
     * @param user
     * @param nextStatus
     * @param reason
     * @param depotpartId
     * @return 修改后的单据状态
     * @throws MYException
     */
    int pass(final String fullId, final User user, final int nextStatus, final String reason, final String depotpartId)
        throws MYException;

    boolean check(final String fullId, final User user, final String checks)
        throws MYException;

    OutBean findOutById(final String fullId);

    boolean delOut(User user, final String fullId)
        throws MYException;

    boolean updateOut(final OutBean out)
        throws MYException;

    /**
     * 增加代销结算
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addOutBalance(final User user, OutBalanceBean bean)
        throws MYException;

    /**
     * passOutBalance
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean passOutBalance(final User user, String id)
        throws MYException;

    /**
     * deleteOutBalance
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean deleteOutBalance(final User user, String id)
        throws MYException;

    /**
     * rejectOutBalance
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    boolean rejectOutBalance(final User user, String id, String reason)
        throws MYException;

    /**
     * 销售单付款
     * 
     * @param user
     * @param fullId
     * @param reason
     * @return
     */
    boolean payOut(final User user, String fullId, String reason)
        throws MYException;

    /**
     * payBaddebts
     * 
     * @param user
     * @param fullId
     * @return
     * @throws MYException
     */
    boolean payBaddebts(final User user, String fullId, double bad)
        throws MYException;

    /**
     * updateInvoice
     * 
     * @param user
     * @param fullId
     * @return
     * @throws MYException
     */
    boolean updateInvoice(final User user, String fullId, String invoiceId)
        throws MYException;

    /**
     * initPayOut(返回付款状态且坏账为0)
     * 
     * @param user
     * @param fullId
     * @return
     * @throws MYException
     */
    boolean initPayOut(final User user, String fullId)
        throws MYException;

    boolean mark(String fullId, boolean status);

    boolean modifyReDate(String fullId, String reDate);

    /**
     * 获得out在日志里面的状态
     * 
     * @param fullId
     * @return
     */
    int findOutStatusInLog(String fullId);

    /**
     * 是否是领样转销售
     * 
     * @param fullId
     * @return
     */
    boolean isSwatchToSail(String fullId);

    String addSwatchToSail(final User user, final OutBean outBean)
        throws MYException;
}
