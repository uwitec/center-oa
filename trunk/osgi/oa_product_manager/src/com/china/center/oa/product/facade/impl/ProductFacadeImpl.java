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
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.product.bean.ProviderUserBean;
import com.china.center.oa.product.facade.ProductFacade;
import com.china.center.oa.product.manager.ProductManager;
import com.china.center.oa.product.manager.ProviderManager;
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

    private ProviderManager providerManager = null;

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
     * checkHisProvider
     * 
     * @param userId
     * @param cid
     * @return
     * @throws MYException
     */
    public boolean checkHisProvider(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_HIS_CHECK))
        {
            return providerManager.checkHisProvider(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 增加供应商
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addProvider(String userId, ProviderBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR_PROVIDER))
        {
            return providerManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * addOrUpdateUserBean
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean bingProductTypeToCustmer(String userId, String pid, String[] productTypeIds)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, pid, productTypeIds);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR_PROVIDER))
        {
            return providerManager.bingProductTypeToCustmer(user, pid, productTypeIds);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * addOrUpdateUserBean
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addOrUpdateUserBean(String userId, ProviderUserBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR_PROVIDER))
        {
            return providerManager.addOrUpdateUserBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateUserPassword
     * 
     * @param userId
     * @param id
     * @param newpwd
     * @return
     * @throws MYException
     */
    public boolean updateUserPassword(String userId, String id, String newpwd)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id, newpwd);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR_PROVIDER))
        {
            return providerManager.updateUserPassword(user, id, newpwd);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 修改供应商
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateProvider(String userId, ProviderBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR_PROVIDER))
        {
            return providerManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 删除供应商
     * 
     * @param userId
     * @param providerId
     * @return
     * @throws MYException
     */
    public boolean delProvider(String userId, String providerId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, providerId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR_PROVIDER))
        {
            return providerManager.delBean(user, providerId);
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

    /**
     * @return the providerManager
     */
    public ProviderManager getProviderManager()
    {
        return providerManager;
    }

    /**
     * @param providerManager
     *            the providerManager to set
     */
    public void setProviderManager(ProviderManager providerManager)
    {
        this.providerManager = providerManager;
    }

}
