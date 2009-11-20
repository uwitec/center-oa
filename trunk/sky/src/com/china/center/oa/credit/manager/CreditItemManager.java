/**
 * File Name: CreditItemManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.manager;


import java.io.Serializable;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.CreditConstant;
import com.china.center.oa.credit.bean.CreditItemBean;
import com.china.center.oa.credit.bean.CreditItemSecBean;
import com.china.center.oa.credit.bean.CreditItemThrBean;
import com.china.center.oa.credit.dao.CreditItemDAO;
import com.china.center.oa.credit.dao.CreditItemSecDAO;
import com.china.center.oa.credit.dao.CreditItemThrDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.tools.JudgeTools;


/**
 * CreditItemManager
 * 
 * @author ZHUZHU
 * @version 2009-10-27
 * @see CreditItemManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "creditItemManager")
public class CreditItemManager
{
    private CreditItemDAO creditItemDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private CreditItemSecDAO creditItemSecDAO = null;

    private CreditItemThrDAO creditItemThrDAO = null;

    /**
     * default constructor
     */
    public CreditItemManager()
    {}

    /**
     * updateCreditItemSec
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateCreditItemSec(User user, CreditItemSecBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        CreditItemSecBean old = checkUpdate(bean);

        bean.setPid(old.getPid());

        creditItemSecDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * updateCreditItem
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateCreditItem(User user, CreditItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        CreditItemBean old = checkUpdateCreditItem(bean);

        bean.setName(old.getName());

        creditItemDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * addCreditItemThr
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addCreditItemThr(User user, CreditItemThrBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddThr(bean);

        bean.setId(commonDAO2.getSquenceString20());

        creditItemThrDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * updateCreditItemThr
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateCreditItemThr(User user, CreditItemThrBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        CreditItemThrBean old = checkUpdateThr(bean);

        bean.setPid(old.getPid());

        bean.setUnit(old.getUnit());

        creditItemThrDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * deleteCreditItemThr
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean deleteCreditItemThr(User user, Serializable id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDelThr(id);

        creditItemThrDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * checkDelThr
     * 
     * @param id
     * @throws MYException
     */
    private void checkDelThr(Serializable id)
        throws MYException
    {
    // TODO check FK
    }

    /**
     * checkUpdateThr
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    private CreditItemThrBean checkUpdateThr(CreditItemThrBean bean)
        throws MYException
    {
        CreditItemThrBean old = creditItemThrDAO.find(bean.getId());

        if ( !old.getName().equals(bean.getName()))
        {
            if (creditItemThrDAO.countByUnique(bean.getName(), bean.getPid()) > 0)
            {
                throw new MYException("%s已经存在", bean.getName());
            }
        }

        if (old.getIndexPos() != bean.getIndexPos())
        {
            if (creditItemThrDAO.countByPidAndIndexPos(bean.getPid(), bean.getIndexPos()) > 0)
            {
                throw new MYException("%s索引位置重复", bean.getName());
            }
        }

        // count per
        if (bean.getPer() > 100.0d)
        {
            throw new MYException("得分不能超过100");
        }

        return old;
    }

    /**
     * checkAddThr
     * 
     * @param bean
     * @throws MYException
     */
    private void checkAddThr(CreditItemThrBean bean)
        throws MYException
    {
        if (creditItemThrDAO.countByUnique(bean.getName(), bean.getPid()) > 0)
        {
            throw new MYException("%s已经存在", bean.getName());
        }

        if (creditItemThrDAO.countByPidAndIndexPos(bean.getPid(), bean.getIndexPos()) > 0)
        {
            throw new MYException("%s索引位置重复", bean.getName());
        }

        if (bean.getPer() > 100.0d)
        {
            throw new MYException("得分不能超过100");
        }

        // process unit
        CreditItemSecBean parent = creditItemSecDAO.find(bean.getPid());

        if (parent == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setUnit(parent.getUnit());
    }

    /**
     * checkUpdate
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    private CreditItemSecBean checkUpdate(CreditItemSecBean bean)
        throws MYException
    {
        CreditItemSecBean old = creditItemSecDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !old.getName().equals(bean.getName()))
        {
            if (creditItemSecDAO.countByUnique(bean.getName()) > 0)
            {
                throw new MYException("%s已经存在", bean.getName());
            }
        }

        // percent must check amount
        if (old.getType() == CreditConstant.CREDIT_ITEM_TYPE_PERCENT)
        {
            // count per
            List<CreditItemSecBean> list = creditItemSecDAO.queryEntityBeansByFK(bean.getPid());

            double count = 0.0d;

            for (CreditItemSecBean creditItemSecBean : list)
            {
                if (creditItemSecBean.getId().equals(bean.getId()))
                {
                    count += bean.getPer();
                }
                else
                {
                    count += creditItemSecBean.getPer();
                }
            }

            if (count > 100.0d)
            {
                throw new MYException("百分比之和超过100");
            }
        }

        return old;
    }

    /**
     * checkUpdateCreditItem
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    private CreditItemBean checkUpdateCreditItem(CreditItemBean bean)
        throws MYException
    {
        CreditItemBean old = creditItemDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        List<CreditItemBean> list = creditItemDAO.listEntityBeans();

        double count = 0.0d;

        for (CreditItemBean creditItemSecBean : list)
        {
            if (creditItemSecBean.getId().equals(bean.getId()))
            {
                count += bean.getPer();
            }
            else
            {
                count += creditItemSecBean.getPer();
            }
        }

        if (count > 100.0d)
        {
            throw new MYException("百分比之和超过100");
        }

        return old;
    }

    /**
     * @return the creditItemDAO
     */
    public CreditItemDAO getCreditItemDAO()
    {
        return creditItemDAO;
    }

    /**
     * @param creditItemDAO
     *            the creditItemDAO to set
     */
    public void setCreditItemDAO(CreditItemDAO creditItemDAO)
    {
        this.creditItemDAO = creditItemDAO;
    }

    /**
     * @return the creditItemSecDAO
     */
    public CreditItemSecDAO getCreditItemSecDAO()
    {
        return creditItemSecDAO;
    }

    /**
     * @param creditItemSecDAO
     *            the creditItemSecDAO to set
     */
    public void setCreditItemSecDAO(CreditItemSecDAO creditItemSecDAO)
    {
        this.creditItemSecDAO = creditItemSecDAO;
    }

    /**
     * @return the creditItemThrDAO
     */
    public CreditItemThrDAO getCreditItemThrDAO()
    {
        return creditItemThrDAO;
    }

    /**
     * @param creditItemThrDAO
     *            the creditItemThrDAO to set
     */
    public void setCreditItemThrDAO(CreditItemThrDAO creditItemThrDAO)
    {
        this.creditItemThrDAO = creditItemThrDAO;
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
