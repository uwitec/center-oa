/**
 *
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.ProductAmount;


/**
 * @author Administrator
 */
public class ProductAmountDAO extends BaseDAO2<ProductAmount, ProductAmount>
{
    public List<ProductAmount> queryProductAmountByProductIdAndLocationId(String productId,
                                                                          String locationId)
    {
        return this.jdbcOperation.queryForList("where productId = ? and locationId = ? ",
            this.claz, productId, locationId);
    }

    /**
     * 查询产品的库存
     * 
     * @param productId
     * @return
     */
    public int countProductAmountByProductId(String productId)
    {
        return this.jdbcOperation.queryForInt(
            "select sum(num) from t_center_productnumber  where productId = ? ", productId);
    }
}
