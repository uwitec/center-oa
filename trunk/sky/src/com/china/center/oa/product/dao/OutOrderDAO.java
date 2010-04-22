/**
 * File Name: OutOrderDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-3-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.ProductConstant;
import com.china.center.oa.product.bean.OutOrderBean;
import com.china.center.oa.product.vo.OutOrderVO;
import com.china.center.tools.TimeTools;


/**
 * OutOrderDAO
 * 
 * @author ZHUZHU
 * @version 2010-3-12
 * @see OutOrderDAO
 * @since 1.0
 */
@Bean(name = "outOrderDAO")
public class OutOrderDAO extends BaseDAO2<OutOrderBean, OutOrderVO>
{
    public int sumOrderByProductId(String productId)
    {
        return this.jdbcOperation.queryForInt(BeanTools.getSumHead(claz, "orderAmount")
                                              + "where status = ? and productId = ?",
            ProductConstant.ORDER_STATUS_COMMON, productId);
    }

    public boolean autoEndOrderBean()
    {
        this.jdbcOperation.update(BeanTools.getUpdateHead(claz)
                                  + "set status = ? where status = ? and endTime <= ?",
            ProductConstant.ORDER_STATUS_END, ProductConstant.ORDER_STATUS_COMMON,
            TimeTools.now_short());

        return true;
    }
}
