/**
 * File Name: FinanceFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.facade.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.manager.BankManager;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.facade.AbstarctFacade;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * FinanceFacadeImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-12
 * @see FinanceFacadeImpl
 * @since 3.0
 */
public class FinanceFacadeImpl extends AbstarctFacade implements FinanceFacade
{
    private BankManager bankManager = null;

    private UserManager userManager = null;

    /**
     * default constructor
     */
    public FinanceFacadeImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.facade.FinanceFacade#addBankBean(java.lang.String,
     *      com.china.center.oa.finance.bean.BankBean)
     */
    public boolean addBankBean(String userId, BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BANK_OPR))
        {
            return bankManager.addBean(user, bean);
        }
        else
        {
            throw new MYException("没有权限");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.facade.FinanceFacade#deleteBankBean(java.lang.String, java.lang.String)
     */
    public boolean deleteBankBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BANK_OPR))
        {
            return bankManager.deleteBean(user, id);
        }
        else
        {
            throw new MYException("没有权限");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.facade.FinanceFacade#updateBankBean(java.lang.String,
     *      com.china.center.oa.finance.bean.BankBean)
     */
    public boolean updateBankBean(String userId, BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BANK_OPR))
        {
            return bankManager.updateBean(user, bean);
        }
        else
        {
            throw new MYException("没有权限");
        }
    }

    /**
     * @return the bankManager
     */
    public BankManager getBankManager()
    {
        return bankManager;
    }

    /**
     * @param bankManager
     *            the bankManager to set
     */
    public void setBankManager(BankManager bankManager)
    {
        this.bankManager = bankManager;
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

}
