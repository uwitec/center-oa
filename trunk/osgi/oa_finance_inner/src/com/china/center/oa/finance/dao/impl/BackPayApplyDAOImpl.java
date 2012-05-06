/**
 * File Name: BackPayApplyDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-3-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.finance.bean.BackPayApplyBean;
import com.china.center.oa.finance.dao.BackPayApplyDAO;
import com.china.center.oa.finance.vo.BackPayApplyVO;


/**
 * BackPayApplyDAOImpl
 * 
 * @author ZHUZHU
 * @version 2011-3-3
 * @see BackPayApplyDAOImpl
 * @since 3.0
 */
public class BackPayApplyDAOImpl extends BaseDAO<BackPayApplyBean, BackPayApplyVO> implements BackPayApplyDAO
{
    public boolean updateRefIds(String id, String refIds)
    {
        return this.jdbcOperation.updateField("refIds", refIds, id, claz) > 0;
    }
}
