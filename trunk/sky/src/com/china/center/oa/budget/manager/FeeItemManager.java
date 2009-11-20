/**
 * File Name: FeeItemManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-24<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.manager;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.annosql.constant.AnoConstant;
import com.china.center.common.MYException;
import com.china.center.oa.budget.bean.FeeItemBean;
import com.china.center.oa.budget.dao.BudgetItemDAO;
import com.china.center.oa.budget.dao.FeeItemDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.tools.JudgeTools;


/**
 * FeeItemManager
 * 
 * @author zhuzhu
 * @version 2009-5-24
 * @see FeeItemManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "feeItemManager")
public class FeeItemManager
{
    private FeeItemDAO feeItemDAO = null;

    private BudgetItemDAO budgetItemDAO = null;

    private CommonDAO2 commonDAO2 = null;

    /**
     * default constructor
     */
    public FeeItemManager()
    {}

    /**
     * add fee item
     * 
     * @param user
     * @param bean
     * @return true
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, FeeItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO2.getSquenceString20());

        feeItemDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(FeeItemBean bean)
        throws MYException
    {
        if (feeItemDAO.countByUnique(bean.getName().trim()) > 0)
        {
            throw new MYException("预算项已经存在");
        }
    }

    /**
     * updateBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, FeeItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkUpdateBean(bean);

        feeItemDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * checkUpdateBean
     * 
     * @param bean
     * @throws MYException
     */
    private void checkUpdateBean(FeeItemBean bean)
        throws MYException
    {
        FeeItemBean old = feeItemDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("预算项不存在");
        }

        if ( !old.getName().equalsIgnoreCase(bean.getName()))
        {
            if (feeItemDAO.countByUnique(bean.getName().trim()) > 0)
            {
                throw new MYException("预算项已经存在");
            }
        }
    }

    /**
     * deleteBean
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean deleteBean(User user, Serializable id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDeleteBean(user, id);

        feeItemDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * checkDeleteBean
     * 
     * @param id
     * @throws MYException
     */
    private void checkDeleteBean(User user, Serializable id)
        throws MYException
    {
        // ref other
        int countByFK = budgetItemDAO.countByFK(id, AnoConstant.FK_FIRST);

        if (countByFK > 0)
        {
            throw new MYException("预算项已经被使用");
        }
    }

    /**
     * @return the feeItemDAO
     */
    public FeeItemDAO getFeeItemDAO()
    {
        return feeItemDAO;
    }

    /**
     * @param feeItemDAO
     *            the feeItemDAO to set
     */
    public void setFeeItemDAO(FeeItemDAO feeItemDAO)
    {
        this.feeItemDAO = feeItemDAO;
    }

    /**
     * @return the budgetItemDAO
     */
    public BudgetItemDAO getBudgetItemDAO()
    {
        return budgetItemDAO;
    }

    /**
     * @param budgetItemDAO
     *            the budgetItemDAO to set
     */
    public void setBudgetItemDAO(BudgetItemDAO budgetItemDAO)
    {
        this.budgetItemDAO = budgetItemDAO;
    }

    /**
     * @return the commonDAO2
     */
    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    /**
     * @param commonDAO2
     *            the commonDAO2 to set
     */
    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
    }
}
