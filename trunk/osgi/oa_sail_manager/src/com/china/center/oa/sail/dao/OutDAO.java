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
    boolean modifyOutStatus(String fullId, int status);

    /**
     * 获得当前数据库内真是的out存储
     * 
     * @param fullId
     * @return
     */
    OutBean findRealOut(String fullId);

    boolean modifyManagerTime(String fullId, String managerTime);

    /**
     * 更新信用
     * 
     * @param fullId
     * @param reserve4
     * @param reserve6
     * @return
     */
    boolean updateOutReserve(String fullId, int reserve4, String reserve6);

    boolean updateCurcredit(String fullId, double curcredit);

    boolean updateManagerId(String fullId, String managerId);

    boolean updateStaffcredit(String fullId, double staffcredit);

    /**
     * 更新分公司经理的担保的额度
     * 
     * @param fullId
     * @param managercredit
     * @return
     */
    boolean updateManagercredit(String fullId, String managerId, double managercredit);

    Integer sumPreassignAmount(Map parMap);

    boolean modifyChecks(String fullId, String checks);

    boolean modifyData(String fullId, String date);

    boolean updataInWay(String fullId, int inway);

    boolean modifyOutHadPay(String fullId, String hadPay);

    boolean modifyReDate(String fullId, String reDate);

    boolean modifyPay(String fullId, int pay);

    boolean modifyTempType(String fullId, int tempType);

    boolean mark(String fullId, boolean status);

    /**
     * sumNoPayBusiness(客户还没有付款的单据,包括预占的)
     * 
     * @param cid
     * @param beginDate
     * @param endDate
     * @return
     */
    double sumNoPayBusiness(String cid, String beginDate, String endDate);

    /**
     * 查询职员整个销售体系里面的信用使用(包括开单占用和自己担保他人的)
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    double sumAllNoPayAndAvouchBusinessByStafferId(String stafferId, String beginDate,
                                                   String endDate);

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
     * 统计一个具体库存的产品被单据占据多少配额(销售单里面)
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    Integer sumNotEndProductInOutByStorageRelation(String productId, String depotpartId,
                                                   String priceKey, String ower);

    /**
     * 统计一个具体库存的产品被单据占据多少配额(入库单里面)
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    Integer sumNotEndProductInInByStorageRelation(String productId, String depotpartId,
                                                  String priceKey, String ower);

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

    int countCustomerInOut(String customerId);
}
