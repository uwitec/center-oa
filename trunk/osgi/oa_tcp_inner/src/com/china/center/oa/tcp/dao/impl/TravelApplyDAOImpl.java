/**
 * File Name: TravelApplyDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-10<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.tcp.bean.TravelApplyBean;
import com.china.center.oa.tcp.dao.TravelApplyDAO;
import com.china.center.oa.tcp.vo.TravelApplyVO;


/**
 * TravelApplyDAOImpl
 * 
 * @author ZHUZHU
 * @version 2011-7-10
 * @see TravelApplyDAOImpl
 * @since 3.0
 */
public class TravelApplyDAOImpl extends BaseDAO<TravelApplyBean, TravelApplyVO> implements TravelApplyDAO
{
    public int updateStatus(String id, int status)
    {
        return this.jdbcOperation.updateField("status", status, id, this.claz);
    }
}
