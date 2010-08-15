/**
 * File Name: ProductFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.facade;


import java.util.List;

import com.china.center.common.MYException;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.vs.ProductVSLocationBean;


/**
 * ProductFacade
 * 
 * @author ZHUZHU
 * @version 2010-8-15
 * @see ProductFacade
 * @since 1.0
 */
public interface ProductFacade
{
    boolean addProductBean(String userId, ProductBean bean)
        throws MYException;

    boolean updateProductBean(String userId, ProductBean bean)
        throws MYException;

    boolean deleteProductBean(String userId, String id)
        throws MYException;

    boolean configProductVSLocation(String userId, String productId, List<ProductVSLocationBean> vsList)
        throws MYException;

    boolean changeProductStatus(String userId, String productId, int oldStatus, int newStatus)
        throws MYException;
}
