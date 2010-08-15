/**
 * File Name: ProductFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.facade.impl;


import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.facade.ProductFacade;
import com.china.center.oa.product.manager.ProductManager;
import com.china.center.oa.product.vs.ProductVSLocationBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.facade.AbstarctFacade;
import com.china.center.tools.JudgeTools;


/**
 * ProductFacadeImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-15
 * @see ProductFacadeImpl
 * @since 1.0
 */
public class ProductFacadeImpl extends AbstarctFacade implements ProductFacade
{
    private ProductManager productManager = null;

    /**
     * default constructor
     */
    public ProductFacadeImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.facade.ProductFacade#addProductBean(java.lang.String,
     *      com.china.center.oa.product.bean.ProductBean)
     */
    public boolean addProductBean(String userId, ProductBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PRODUCT_OPR))
        {
            return productManager.addProductBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.facade.ProductFacade#changeProductStatus(java.lang.String, java.lang.String,
     *      int, int)
     */
    public boolean changeProductStatus(String userId, String productId, int oldStatus, int newStatus)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, productId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PRODUCT_CHECK))
        {
            return productManager.changeProductStatus(user, productId, oldStatus, newStatus);
        }
        else
        {
            throw noAuth();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.facade.ProductFacade#configProductVSLocation(java.lang.String, java.lang.String,
     *      java.util.List)
     */
    public boolean configProductVSLocation(String userId, String productId, List<ProductVSLocationBean> vsList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, productId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PRODUCT_OPR))
        {
            return productManager.configProductVSLocation(user, productId, vsList);
        }
        else
        {
            throw noAuth();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.facade.ProductFacade#deleteProductBean(java.lang.String, java.lang.String)
     */
    public boolean deleteProductBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PRODUCT_OPR))
        {
            return productManager.deleteProductBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.facade.ProductFacade#updateProductBean(java.lang.String,
     *      com.china.center.oa.product.bean.ProductBean)
     */
    public boolean updateProductBean(String userId, ProductBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.PRODUCT_OPR))
        {
            return productManager.updateProductBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * @return the productManager
     */
    public ProductManager getProductManager()
    {
        return productManager;
    }

    /**
     * @param productManager
     *            the productManager to set
     */
    public void setProductManager(ProductManager productManager)
    {
        this.productManager = productManager;
    }

}
