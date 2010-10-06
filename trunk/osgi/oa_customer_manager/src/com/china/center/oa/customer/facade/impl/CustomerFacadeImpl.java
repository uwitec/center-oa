/**
 * File Name: CustomerFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.facade.impl;


import java.io.Serializable;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.credit.bean.CreditItemBean;
import com.china.center.oa.credit.bean.CreditItemSecBean;
import com.china.center.oa.credit.bean.CreditItemThrBean;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.credit.manager.CreditItemManager;
import com.china.center.oa.credit.manager.CustomerCreditManager;
import com.china.center.oa.customer.bean.AssignApplyBean;
import com.china.center.oa.customer.bean.CustomerApplyBean;
import com.china.center.oa.customer.bean.CustomerCheckBean;
import com.china.center.oa.customer.bean.CustomerCheckItemBean;
import com.china.center.oa.customer.facade.CustomerFacade;
import com.china.center.oa.customer.manager.CustomerCheckManager;
import com.china.center.oa.customer.manager.CustomerManager;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.facade.AbstarctFacade;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * CustomerFacadeImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see CustomerFacadeImpl
 * @since 1.0
 */
public class CustomerFacadeImpl extends AbstarctFacade implements CustomerFacade
{
    private CustomerManager customerManager = null;

    private UserManager userManager = null;

    private CustomerCreditManager customerCreditManager = null;

    private CustomerCheckManager customerCheckManager = null;

    private CreditItemManager creditItemManager = null;

    /**
     * default constructor
     */
    public CustomerFacadeImpl()
    {
    }

    /**
     * applyAddCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean applyAddCustomer(String userId, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR))
        {
            return customerManager.applyAddCustomer(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * delApplyCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean delApplyCustomer(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR))
        {
            return customerManager.delApply(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * applyUpdateCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean applyUpdateCustomer(String userId, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR))
        {
            return customerManager.applyUpdateCustomer(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * applyUpdateCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean applyDelCustomer(String userId, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_OPR))
        {
            return customerManager.applyDelCustomer(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * rejectApplyCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean rejectApplyCustomer(String userId, String cid, String reson)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK))
        {
            return customerManager.rejectApplyCustomer(user, cid, reson);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * passApplyCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean passApplyCustomer(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK))
        {
            return customerManager.passApplyCustomer(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * assignApplyCustomerCode
     * 
     * @param userId
     * @param cid
     * @param code
     * @return
     * @throws MYException
     */
    public boolean assignApplyCustomerCode(String userId, String cid, String code)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid, code);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_ASSIGN_CODE))
        {
            return customerManager.assignApplyCustomerCode(user, cid, code);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * addAssignApply
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addAssignApply(String userId, AssignApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_APPLY_ASSIGN))
        {
            return customerManager.addAssignApply(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * passAssignApply
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean passAssignApply(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK))
        {
            return customerManager.passAssignApply(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * checkHisCustomer
     * 
     * @param userId
     * @param cid
     * @return
     * @throws MYException
     */
    public boolean checkHisCustomer(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_HIS_CHECK))
        {
            return customerManager.checkHisCustomer(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * passAssignApply
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean rejectAssignApply(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK))
        {
            return customerManager.rejectAssignApply(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * passAssignApply
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean reclaimAssignCustomer(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_RECLAIM))
        {
            return customerManager.reclaimAssignCustomer(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 回收职员下的客户
     * 
     * @param userId
     * @param stafferId
     * @param flag
     * @return
     * @throws MYException
     */
    public boolean reclaimStafferAssignCustomer(String userId, String stafferId, int flag)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, stafferId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_RECLAIM))
        {
            return customerManager.reclaimStafferAssignCustomer(user, stafferId, flag);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * addCheckBean
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addCheckBean(String userId, CustomerCheckBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK_COMMON))
        {
            return customerCheckManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * goonBean
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean goonBean(String userId, CustomerCheckBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK_COMMON))
        {
            return customerCheckManager.goonBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * passCheckBean
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean passCheckBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK_CHECK))
        {
            return customerCheckManager.passBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateCheckItem
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCheckItem(String userId, CustomerCheckItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK_COMMON))
        {
            return customerCheckManager.updateCheckItem(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * rejectCheckBean
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean rejectCheckBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK_CHECK))
        {
            return customerCheckManager.rejectBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * delCheckBean
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean delCheckBean(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CHECK_COMMON))
        {
            return customerCheckManager.delBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * addCreditItemThr
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addCreditItemThr(String userId, CreditItemThrBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CREDIT_OPR))
        {
            return creditItemManager.addCreditItemThr(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateCreditItemThr
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCreditItemThr(String userId, CreditItemThrBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CREDIT_OPR))
        {
            return creditItemManager.updateCreditItemThr(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateCreditItem
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCreditItem(String userId, CreditItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CREDIT_OPR))
        {
            return creditItemManager.updateCreditItem(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateCreditItemSec
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCreditItemSec(String userId, CreditItemSecBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CREDIT_OPR))
        {
            return creditItemManager.updateCreditItemSec(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * deleteCreditItemThr
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean deleteCreditItemThr(String userId, Serializable id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CREDIT_OPR))
        {
            return creditItemManager.deleteCreditItemThr(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * doPassApplyConfigStaticCustomerCredit
     * 
     * @param userId
     * @param cid
     * @return
     * @throws MYException
     */
    public boolean doPassApplyConfigStaticCustomerCredit(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CREDIT_CHECK))
        {
            return customerCreditManager.doPassApplyConfigStaticCustomerCredit(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * doRejectApplyConfigStaticCustomerCredit
     * 
     * @param userId
     * @param cid
     * @return
     * @throws MYException
     */
    public boolean doRejectApplyConfigStaticCustomerCredit(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CUSTOMER_CREDIT_CHECK))
        {
            return customerCreditManager.doRejectApplyConfigStaticCustomerCredit(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateCreditLevel
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCreditLevel(String userId, CreditLevelBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CREDIT_LEVEL_OPR))
        {
            return creditItemManager.updateCreditLevel(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * interposeCredit
     * 
     * @param userId
     * @param cid
     * @param newCreditVal
     * @return
     * @throws MYException
     */
    public boolean interposeCredit(String userId, String cid, double newCreditVal)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CREDIT_FORCE_UPDATE))
        {
            return customerCreditManager.interposeCredit(user, cid, newCreditVal);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * @return the customerManager
     */
    public CustomerManager getCustomerManager()
    {
        return customerManager;
    }

    /**
     * @param customerManager
     *            the customerManager to set
     */
    public void setCustomerManager(CustomerManager customerManager)
    {
        this.customerManager = customerManager;
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
     * @return the customerCheckManager
     */
    public CustomerCheckManager getCustomerCheckManager()
    {
        return customerCheckManager;
    }

    /**
     * @param customerCheckManager
     *            the customerCheckManager to set
     */
    public void setCustomerCheckManager(CustomerCheckManager customerCheckManager)
    {
        this.customerCheckManager = customerCheckManager;
    }

    /**
     * @return the creditItemManager
     */
    public CreditItemManager getCreditItemManager()
    {
        return creditItemManager;
    }

    /**
     * @param creditItemManager
     *            the creditItemManager to set
     */
    public void setCreditItemManager(CreditItemManager creditItemManager)
    {
        this.creditItemManager = creditItemManager;
    }

    /**
     * @return the customerCreditManager
     */
    public CustomerCreditManager getCustomerCreditManager()
    {
        return customerCreditManager;
    }

    /**
     * @param customerCreditManager
     *            the customerCreditManager to set
     */
    public void setCustomerCreditManager(CustomerCreditManager customerCreditManager)
    {
        this.customerCreditManager = customerCreditManager;
    }
}
