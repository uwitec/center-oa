/**
 * File Name: DepotManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager.impl;


import java.util.Collection;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.expression.Expression;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.listener.DepotListener;
import com.china.center.oa.product.manager.DepotManager;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.tools.JudgeTools;


/**
 * DepotManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-22
 * @see DepotManagerImpl
 * @since 1.0
 */
@Exceptional
public class DepotManagerImpl extends AbstractListenerManager<DepotListener> implements DepotManager
{
    private DepotDAO depotDAO = null;

    private CommonDAO commonDAO = null;

    /**
     * default constructor
     */
    public DepotManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.DepotManager#addDepotBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.DepotBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addDepotBean(User user, DepotBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        Expression exp = new Expression(bean, this);

        exp.check("#name &unique @depotDAO", "仓库名称已经存在");

        depotDAO.saveEntityBean(bean);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.DepotManager#deleteDepotBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteDepotBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        DepotBean old = depotDAO.find(id);

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        Collection<DepotListener> values = this.listenerMap.values();

        for (DepotListener depotListener : values)
        {
            // 主要是仓区
            depotListener.onDeleteDepot(user, old);
        }

        depotDAO.deleteEntityBean(id);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.DepotManager#updateDepotBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.DepotBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateDepotBean(User user, DepotBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        DepotBean old = depotDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        Expression exp = new Expression(bean, this);

        exp.check("#name &unique2 @depotDAO", "仓库名称已经存在");

        depotDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * @return the depotDAO
     */
    public DepotDAO getDepotDAO()
    {
        return depotDAO;
    }

    /**
     * @param depotDAO
     *            the depotDAO to set
     */
    public void setDepotDAO(DepotDAO depotDAO)
    {
        this.depotDAO = depotDAO;
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
