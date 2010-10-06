/**
 * File Name: ComposeFeeDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao.impl;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.product.bean.ComposeFeeBean;
import com.china.center.oa.product.dao.ComposeFeeDAO;
import com.china.center.oa.product.vo.ComposeFeeVO;


/**
 * ComposeFeeDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-2
 * @see ComposeFeeDAOImpl
 * @since 1.0
 */
public class ComposeFeeDAOImpl extends BaseDAO<ComposeFeeBean, ComposeFeeVO> implements ComposeFeeDAO
{
    public List<ComposeFeeVO> queryEntityVOsByFK(Serializable id)
    {
        String sql = "select t1.*, t2.val as feeItemName from T_CENTER_COMPOSE_FEE t1, T_CENTER_ENUM t2 "
                     + "where t1.parentId = ? and t1.feeItemId = t2.keyss and t2.type = ?";

        return jdbcOperation.queryForListBySql(sql, this.clazVO, id, 122);
    }
}
