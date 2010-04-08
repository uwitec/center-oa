/**
 * 
 */
package com.china.center.oa.product.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.ProductConstant;
import com.china.center.oa.product.bean.ProductStatBean;
import com.china.center.tools.TimeTools;


/**
 * ProductStatDAO
 * 
 * @author ZHUZHU
 */
@Bean(name = "productStatDAO")
public class ProductStatDAO extends BaseDAO2<ProductStatBean, ProductStatBean>
{
    public List<ProductStatBean> queryStatProductByCondition()
    {
        Map map = new HashMap();

        // 获得两周的产品销售数量
        map.put("beginDate", TimeTools.getSpecialDateString( -ProductConstant.STAT_DAYS,
            TimeTools.SHORT_FORMAT));

        map.put("endDate", TimeTools.getSpecialDateString( -1, TimeTools.SHORT_FORMAT));

        List<ProductStatBean> resultList = this.jdbcOperation.getIbatisDaoSupport().queryForList(
            "ProductStatDAO.queryStatProductByCondition", map);

        // 这里统计订货的产品
        List<ProductStatBean> newList = this.jdbcOperation.getIbatisDaoSupport().queryForList(
            "ProductStatDAO.queryOutOrderProductByCondition", map);

        for (ProductStatBean productStatBean : newList)
        {
            if ( !resultList.contains(productStatBean))
            {
                resultList.add(productStatBean);
            }
        }

        return resultList;
    }

    /**
     * 查询产品的库存(物流中心的)
     * 
     * @param productId
     * @return
     */
    public int sumProductAmountByProductId(String productId)
    {
        return this.jdbcOperation.queryForInt(
            "select sum(num) from t_center_productnumber  where productId = ?", productId);
    }

    /**
     * 已知订单（销售未发货）(15天以内的)(已经废弃)
     * 
     * @param productId
     * @return double
     */
    @Deprecated
    public int sumNotSailProduct(String productId)
    {
        Map<String, String> paramterMap = new HashMap();

        // 获得两周的产品销售数量
        paramterMap.put("beginDate", TimeTools.getSpecialDateString( -ProductConstant.STAT_DAYS,
            TimeTools.SHORT_FORMAT));

        paramterMap.put("endDate", TimeTools.getSpecialDateString( -1, TimeTools.SHORT_FORMAT));

        paramterMap.put("productId", productId);

        Object sum = this.jdbcOperation.getIbatisDaoSupport().queryForObject(
            "ProductStatDAO.sumNotSailProduct", paramterMap);

        if (sum == null)
        {
            return 0;
        }

        return (Integer)sum;
    }
}
