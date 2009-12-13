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
import com.china.center.oa.budget.bean.BudgetApplyBean;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.bean.FeeItemBean;
import com.china.center.oa.budget.manager.BudgetApplyManager;
import com.china.center.oa.budget.manager.BudgetManager;
import com.china.center.oa.budget.manager.FeeItemManager;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.constant.BudgetConstant;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * customerFacade(权限控制)
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see BudgetFacade
 * @since 1.0
 */
@Bean(name = "budgetFacade")
public class BudgetFacade extends AbstarctFacade
{
    private BudgetManager budgetManager = null;

    private BudgetApplyManager budgetApplyManager = null;

    private UserManager userManager = null;

    private FeeItemManager feeItemManager = null;

    /**
     * default constructor
     */
    public BudgetFacade()
    {}

    /**
     * applyAddCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addBudget(String userId, BudgetBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        String opr = AuthConstant.BUDGET_OPR;

        if (BudgetConstant.BUDGET_ROOT.equals(bean.getParentId()))
        {
            opr = AuthConstant.BUDGET_ADDROOT;
        }

        if (containAuth(user, opr))
        {
            return budgetManager.addBean(user, bean);
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
    public boolean delBudget(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BUDGET_OPR)
            || containAuth(user, AuthConstant.BUDGET_ADDROOT))
        {
            return budgetManager.delBean(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateBudget
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateBudget(String userId, BudgetBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        String opr = AuthConstant.BUDGET_OPR;

        if (BudgetConstant.BUDGET_ROOT.equals(bean.getParentId()))
        {
            opr = AuthConstant.BUDGET_ADDROOT;
        }

        if (containAuth(user, opr))
        {
            return budgetManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateBudgetItem
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateBudgetItem(String userId, BudgetItemBean bean, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BUDGET_OPR))
        {
            return budgetManager.updateItemBean(user, bean, reason);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * delBudgetItem
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean delBudgetItem(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BUDGET_OPR))
        {
            return budgetManager.delItemBean(user, id);
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
    public boolean rejectBudget(String userId, String cid, String reson)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        String auth = AuthConstant.BUDGET_CHECK;

        if (isRootBudget(cid))
        {
            auth = AuthConstant.BUDGET_OPRROOT;
        }

        if (containAuth(user, auth))
        {
            return budgetManager.rejectBean(user, cid, reson);
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
    public boolean passBudget(String userId, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        String auth = AuthConstant.BUDGET_CHECK;

        if (isRootBudget(cid))
        {
            auth = AuthConstant.BUDGET_OPRROOT;
        }

        if (containAuth(user, auth))
        {
            return budgetManager.passBean(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * passBudgetApply
     * 
     * @param userId
     * @param cid
     * @return
     * @throws MYException
     */
    public boolean passBudgetApply(String userId, String mode, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        String auth = whetherAuth(user, mode);

        if (containAuth(user, auth))
        {
            return budgetApplyManager.passBean(user, cid);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * rejectBudgetApply
     * 
     * @param userId
     * @param cid
     * @param reson
     * @return
     * @throws MYException
     */
    public boolean rejectBudgetApply(String userId, String mode, String cid, String reson)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cid);

        User user = userManager.findUser(userId);

        checkUser(user);

        String auth = whetherAuth(user, mode);

        if (containAuth(user, auth))
        {
            return budgetApplyManager.rejectBean(user, cid, reson);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * whetherAuth
     * 
     * @param user
     * @param mode
     * @return
     */
    private String whetherAuth(User user, String mode)
    {
        String auth = AuthConstant.BUDGET_CHANGE_APPROVE_CFO;

        if ("0".equals(mode))
        {
            auth = AuthConstant.BUDGET_CHANGE_APPROVE_CFO;
        }

        if ("1".equals(mode))
        {
            auth = AuthConstant.BUDGET_CHANGE_APPROVE_COO;
        }

        if ("2".equals(mode))
        {
            auth = AuthConstant.BUDGET_CHANGE_APPROVE_CEO;
        }

        return auth;
    }

    /**
     * addFeeItem
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addFeeItem(String userId, FeeItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BUDGET_OPR))
        {
            return feeItemManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateFeeItem
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateFeeItem(String userId, FeeItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BUDGET_OPR))
        {
            return feeItemManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * deleteFeeItem
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean deleteFeeItem(String userId, Serializable id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BUDGET_OPR))
        {
            return feeItemManager.deleteBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * addBudgetApply
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addBudgetApply(String userId, BudgetApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.BUDGET_CHANGE_APPLY))
        {
            return budgetApplyManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    private boolean isRootBudget(String id)
        throws MYException
    {
        BudgetBean bean = this.budgetManager.findBudget(id);

        if (bean == null)
        {
            throw new MYException("预算不存在");
        }

        return BudgetConstant.BUDGET_ROOT.equals(bean.getParentId());
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
     * @return the budgetManager
     */
    public BudgetManager getBudgetManager()
    {
        return budgetManager;
    }

    /**
     * @param budgetManager
     *            the budgetManager to set
     */
    public void setBudgetManager(BudgetManager budgetManager)
    {
        this.budgetManager = budgetManager;
    }

    /**
     * @return the feeItemManager
     */
    public FeeItemManager getFeeItemManager()
    {
        return feeItemManager;
    }

    /**
     * @param feeItemManager
     *            the feeItemManager to set
     */
    public void setFeeItemManager(FeeItemManager feeItemManager)
    {
        this.feeItemManager = feeItemManager;
    }

    /**
     * @return the budgetApplyManager
     */
    public BudgetApplyManager getBudgetApplyManager()
    {
        return budgetApplyManager;
    }

    /**
     * @param budgetApplyManager
     *            the budgetApplyManager to set
     */
    public void setBudgetApplyManager(BudgetApplyManager budgetApplyManager)
    {
        this.budgetApplyManager = budgetApplyManager;
    }
}
