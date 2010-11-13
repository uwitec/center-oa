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

    boolean updateOutReserve2(String fullId, int reserve4, String reserve6);

    boolean updateCurcredit(String fullId, double curcredit);

    boolean updateStaffcredit(String fullId, double staffcredit);

    Integer sumPreassignAmount(Map parMap);

    boolean modifyChecks(String fullId, String checks);

    boolean modifyData(String fullId, String date);

    boolean updataInWay(String fullId, int inway);

    boolean modifyOutHadPay2(String fullId, String hadPay);

    boolean modifyReDate2(String fullId, String reDate);

    boolean modifyPay2(String fullId, int pay);

    boolean modifyTempType(String fullId, int tempType);

    boolean mark2(String fullId, boolean status);

    double sumNoPayBusiness(String cid, String beginDate, String endDate);

    double sumNoPayAndAvouchBusinessByStafferId(String stafferId, String beginDate, String endDate);
}
