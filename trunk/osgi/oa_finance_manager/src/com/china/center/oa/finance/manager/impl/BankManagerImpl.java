/**
 * File Name: BankManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.Collection;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.expression.Expression;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.listener.BankListener;
import com.china.center.oa.finance.manager.BankManager;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.tools.JudgeTools;


/**
 * BankManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-12
 * @see BankManagerImpl
 * @since 3.0
 */
@Exceptional
public class BankManagerImpl extends AbstractListenerManager<BankListener> implements BankManager
{
    private BankDAO bankDAO = null;

    private CommonDAO commonDAO = null;

    /**
     * default constructor
     */
    public BankManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.BankManager#addBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.BankBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addBean(User user, BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        Expression exp = new Expression(bean, this);

        exp.check("#name &unique @bankDAO", "名称已经存在");

        bankDAO.saveEntityBean(bean);

        Collection<BankListener> listenerMapValues = this.listenerMapValues();

        for (BankListener bankListener : listenerMapValues)
        {
            bankListener.onAddBank(user, bean);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.BankManager#deleteBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        BankBean old = bankDAO.find(id);

        if (old == null)
        {
            throw new MYException("银行不能被删除,请确认操作");
        }

        Collection<BankListener> listenerMapValues = this.listenerMapValues();

        for (BankListener bankListener : listenerMapValues)
        {
            bankListener.onDeleteBank(user, old);
        }

        bankDAO.deleteEntityBean(id);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.BankManager#updateBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.BankBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateBean(User user, BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        BankBean old = bankDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        Expression exp = new Expression(bean, this);

        exp.check("#name &unique2 @bankDAO", "名称已经存在");

        bankDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * @return the bankDAO
     */
    public BankDAO getBankDAO()
    {
        return bankDAO;
    }

    /**
     * @param bankDAO
     *            the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO)
    {
        this.bankDAO = bankDAO;
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
