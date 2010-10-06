/**
 * File Name: OutStatDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import java.util.List;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.credit.bean.OutBean;
import com.china.center.oa.customer.wrap.NotPayWrap;


/**
 * OutStatDAO
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see OutStatDAO
 * @since 1.0
 */
public interface OutStatDAO extends DAO<OutBean, OutBean>
{
    List<OutBean> queryNoneStatByCid(String cid);

    List<String> listCustomerIdList();

    OutBean findNearestById(String id, String cid);

    boolean updateReserve1ByFullId(String fullId, int reserve1, String reserve4);

    boolean updateReserve5ByFullId(String fullId, String reserve5);

    double queryMaxBusiness(String cid, String beginDate, String endDate);

    double sumNotPayByCid(String cid);

    double sumBusiness(String cid, String beginDate, String endDate);

    List<NotPayWrap> listNotPayWrap();
}
