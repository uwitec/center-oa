/*
 * 文件名：ProductionDAOImpl.java 版权：Copyright by www.centerchina.com 描述： 修改人：zhu
 * 修改时间：2006-7-16 跟踪单号： 修改单号： 修改内容：
 */

package com.china.center.oa.product.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.product.bean.ProductBean;


/**
 * ProductDAO
 * 
 * @author zhu
 * @version 2006-7-16
 * @see ProductDAO
 * @since
 */
@Bean(name = "productDAO")
public class ProductDAO extends BaseDAO2<ProductBean, ProductBean>
{

}
