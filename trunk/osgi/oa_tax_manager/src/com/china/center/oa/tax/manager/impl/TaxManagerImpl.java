/**
 * File Name: TaxManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-30<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.manager.impl;


import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.expression.Expression;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.manager.TaxManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;


/**
 * TaxManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-1-30
 * @see TaxManagerImpl
 * @since 1.0
 */
@Exceptional
public class TaxManagerImpl implements TaxManager
{
    private TaxDAO taxDAO = null;

    private CommonDAO commonDAO = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tax.manager.TaxManager#addTaxBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.tax.bean.TaxBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addTaxBean(User user, TaxBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        Expression exp = new Expression(bean, this);

        exp.check("#name || #code &unique @taxDAO", "名称或者编码已经存在");

        taxDAO.saveEntityBean(bean);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tax.manager.TaxManager#deleteTaxBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteTaxBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        TaxBean old = taxDAO.find(id);

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !StringTools.isNullOrNone(old.getRefId()))
        {
            throw new MYException("关联银行不能删除,请确认操作");
        }

        // TODO 如果科目被使用是不能删除的

        taxDAO.deleteEntityBean(id);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tax.manager.TaxManager#updateTaxBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.tax.bean.TaxBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateTaxBean(User user, TaxBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        TaxBean old = taxDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setUnit(old.getUnit());
        bean.setDepartment(old.getDepartment());
        bean.setStaffer(old.getStaffer());

        Expression exp = new Expression(bean, this);

        exp.check("#name || #code &unique2 @taxDAO", "名称或者编码已经存在");

        taxDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * @return the taxDAO
     */
    public TaxDAO getTaxDAO()
    {
        return taxDAO;
    }

    /**
     * @param taxDAO
     *            the taxDAO to set
     */
    public void setTaxDAO(TaxDAO taxDAO)
    {
        this.taxDAO = taxDAO;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

}
