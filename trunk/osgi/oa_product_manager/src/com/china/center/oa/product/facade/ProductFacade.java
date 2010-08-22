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
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.product.bean.ProviderUserBean;
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

    boolean checkHisProvider(String userId, String cid)
        throws MYException;

    boolean addProvider(String userId, ProviderBean bean)
        throws MYException;

    boolean bingProductTypeToCustmer(String userId, String pid, String[] productTypeIds)
        throws MYException;

    boolean addOrUpdateUserBean(String userId, ProviderUserBean bean)
        throws MYException;

    boolean updateUserPassword(String userId, String id, String newpwd)
        throws MYException;

    boolean updateProvider(String userId, ProviderBean bean)
        throws MYException;

    boolean delProvider(String userId, String providerId)
        throws MYException;

    boolean addDepotBean(String userId, DepotBean bean)
        throws MYException;

    boolean updateDepotBean(String userId, DepotBean bean)
        throws MYException;

    boolean deleteDepotBean(String userId, String id)
        throws MYException;
}
