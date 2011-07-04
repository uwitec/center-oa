/**
 * File Name: ProductDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.vo.ProductVO;


/**
 * ProductDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-15
 * @see ProductDAOImpl
 * @since 1.0
 */
public class ProductDAOImpl extends BaseDAO<ProductBean, ProductVO> implements ProductDAO
{
    public boolean updateStatus(String id, int status)
    {
        return this.jdbcOperation.updateField("status", status, id, claz) > 0;
    }
}
