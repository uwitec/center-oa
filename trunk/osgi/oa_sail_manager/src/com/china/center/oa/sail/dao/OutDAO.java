/**
 * File Name: OutDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao;


import java.util.Map;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.vo.OutVO;


/**
 * OutDAO
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see OutDAO
 * @since 1.0
 */
public interface OutDAO extends DAO<OutBean, OutVO>
{
    boolean modifyOutStatus2(String fullId, int status);

    boolean modifyManagerTime(String fullId, String managerTime);

    /**
     * 更新信用
     * 
     * @param fullId
     * @param reserve4
     * @param reserve6
     * @return
     */
    boolean updateOutReserve2(String fullId, int reserve4, String reserve6);

    boolean updateCurcredit(String fullId, double curcredit);

    boolean updateStaffcredit(String fullId, double staffcredit);

    /**
     * 更新分公司经理的担保的额度
     * 
     * @param fullId
     * @param managercredit
     * @return
     */
    boolean updateManagercredit(String fullId, double managercredit);

    Integer sumPreassignAmount(Map parMap);

    boolean modifyChecks(String fullId, String checks);

    boolean modifyData(String fullId, String date);

    boolean updataInWay(String fullId, int inway);

    boolean modifyOutHadPay2(String fullId, String hadPay);

    boolean modifyReDate2(String fullId, String reDate);

    boolean modifyPay2(String fullId, int pay);

    boolean modifyTempType(String fullId, int tempType);

    boolean mark2(String fullId, boolean status);

    /**
     * sumNoPayBusiness(客户还没有付款的单据)
     * 
     * @param cid
     * @param beginDate
     * @param endDate
     * @return
     */
    double sumNoPayBusiness(String cid, String beginDate, String endDate);

    /**
     * sumNoPayAndAvouchBusinessByStafferId(职员信用)
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    double sumNoPayAndAvouchBusinessByStafferId(String stafferId, String beginDate, String endDate);

    /**
     * 统计一个产品在系统的销售单没有发货单据数量
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    Integer countNotEndProductInOut(String productId, String beginDate, String endDate);

    /**
     * 统计一个产品在系统的入库单在途的数量
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    Integer countNotEndProductInIn(String productId, String beginDate, String endDate);

    /**
     * sumNoPayAndAvouchBusinessByStafferId(分公司经理信用)
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    double sumNoPayAndAvouchBusinessByManagerId(String stafferId, String beginDate, String endDate);
}
