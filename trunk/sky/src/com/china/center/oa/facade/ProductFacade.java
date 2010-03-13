/**
 * File Name: customerFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.facade;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.MYException;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.product.bean.OutOrderBean;
import com.china.center.oa.product.manager.ProductStatManager;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * MailGroupFacade(È¨ÏÞ¿ØÖÆ)
 * 
 * @author ZHUZHU
 * @version 2008-11-2
 * @see ProductFacade
 * @since 1.0
 */
@Bean(name = "productFacade")
public class ProductFacade extends AbstarctFacade
{
    private ProductStatManager productStatManager = null;

    private UserManager userManager = null;

    public ProductFacade()
    {}

    /**
     * addOutOrder
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addOutOrder(String userId, OutOrderBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        String opr = AuthConstant.PRODUCT_ORDER;

        if (containAuth(user, opr))
        {
            return productStatManager.addOutOrder(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * cancleOutOrder
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean cancleOutOrder(String userId, Serializable id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        String opr = AuthConstant.PRODUCT_ORDER;

        if (containAuth(user, opr))
        {
            return productStatManager.cancleOutOrder(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * @return the userManager
     */
    public UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * @param userManager
     *            the userManager to set
     */
    public void setUserManager(UserManager userManager)
    {
        this.userManager = userManager;
    }

    /**
     * @return the productStatManager
     */
    public ProductStatManager getProductStatManager()
    {
        return productStatManager;
    }

    /**
     * @param productStatManager
     *            the productStatManager to set
     */
    public void setProductStatManager(ProductStatManager productStatManager)
    {
        this.productStatManager = productStatManager;
    }
}
