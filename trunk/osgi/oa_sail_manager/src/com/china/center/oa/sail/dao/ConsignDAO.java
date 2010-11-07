/**
 * File Name: ConsignDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao;


import java.util.List;

import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.sail.bean.ConsignBean;
import com.china.center.oa.sail.bean.TransportBean;


/**
 * ConsignDAO
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see ConsignDAO
 * @since 1.0
 */
public interface ConsignDAO
{
    boolean addTransport(TransportBean bean);

    boolean addConsign(ConsignBean bean);

    boolean updateConsign(ConsignBean bean);

    boolean delConsign(String id);

    boolean delTransport(String id);

    List<TransportBean> listTransport();

    List<ConsignBean> queryConsignByCondition(ConditionParse condition);

    int countTransport(String transportId);

    ConsignBean findConsignById(String id);

    List<TransportBean> queryTransportByType(int type);

    List<TransportBean> queryTransportByParentId(String parentId);

    TransportBean findTransportById(String id);

    TransportBean findTransportByName(String name);

    int countByName(String name, int type);
}
