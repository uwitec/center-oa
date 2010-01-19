/*
 * File Name: DepotpartDAO.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-12-15 Grant: open source to
 * everybody
 */
package com.china.centet.yongyin.manager;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.centet.yongyin.bean.DepotpartBean;
import com.china.centet.yongyin.bean.StorageBean;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.DepotpartDAO;
import com.china.centet.yongyin.dao.StorageDAO;


/**
 * 仓区的逻辑处理
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class DepotpartManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private DepotpartDAO depotpartDAO = null;

    private StorageDAO storageDAO = null;

    private DataSourceTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public DepotpartManager()
    {}

    /**
     * Description:
     * 
     * @param bean
     * @return
     * @throws MYException
     * @since <IVersion>
     */
    public boolean addDepotpart(DepotpartBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        JudgeTools.judgeParameterIsNull(bean.getName());

        if (depotpartDAO.countByName(bean.getName(), bean.getLocationId()) > 0)
        {
            throw new MYException(DepotpartBean.class, "已经存在!");
        }

        // 良品仓的逻辑
        if (bean.getType() == Constant.TYPE_DEPOTPART_OK)
        {
            // 良品仓可以有多个
            if (false && depotpartDAO.countByType(Constant.TYPE_DEPOTPART_OK, bean.getLocationId()) > 0)
            {
                throw new MYException("区域下已经存在良品仓!");
            }
        }

        try
        {
            depotpartDAO.addDepotpart(bean);
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("内部错误");
        }

        _logger.info("成功增加仓区:" + bean.getName());
        return true;
    }

    public boolean updateDepotpart(DepotpartBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        JudgeTools.judgeParameterIsNull(bean.getName(), bean.getId());

        DepotpartBean old = depotpartDAO.findDepotpartById(bean.getId());

        if (old == null)
        {
            throw new MYException(DepotpartBean.class, "不存在!");
        }

        if ( !old.getName().equals(bean.getName()))
        {
            if (depotpartDAO.countByName(bean.getName(), bean.getLocationId()) > 0)
            {
                throw new MYException(DepotpartBean.class, "已经存在!");
            }
        }

        if (old.getType() != bean.getType())
        {
            throw new MYException(DepotpartBean.class, "不能修改类型!");
        }

        try
        {
            depotpartDAO.modfiyDepotpart(bean);
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("内部错误");
        }

        _logger.info("成功修改仓区:" + bean.getName());
        return true;

    }

    public boolean delDepotpart(final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id, true);

        final DepotpartBean bean = depotpartDAO.findDepotpartById(id);

        if (bean == null)
        {
            throw new MYException(StorageBean.class, "不存在!");
        }

        List<StorageBean> list = storageDAO.queryStorageByDepotpartId(id);

        if ( !ListTools.isEmptyOrNull(list))
        {
            throw new MYException(StorageBean.class, "仍有储位，不能删除!");
        }

        // 操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    depotpartDAO.delDepotpart(id);

                    return Boolean.TRUE;
                }

            });
        }
        catch (TransactionException e)
        {
            _logger.error(e, e);
            throw new MYException("内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("内部错误");
        }

        _logger.info("成功删除仓区:" + bean.getName());

        return true;
    }

    public List<DepotpartBean> listDepotpart()
    {
        return depotpartDAO.listDepotpart();
    }

    public List<DepotpartBean> queryDepotpartByCondition(ConditionParse condition)
    {
        return depotpartDAO.queryDepotpartByCondition(condition);
    }

    public List<DepotpartBean> queryDepotpartByLocationId(String locationId)
    {
        ConditionParse condition = new ConditionParse();

        condition.addCondition("locationId", "=", locationId);

        return depotpartDAO.queryDepotpartByCondition(condition);
    }

    public DepotpartBean findDepotpartById(String id)
    {
        return depotpartDAO.findDepotpartById(id);
    }

    public DepotpartBean findDepotpartByName(String name)
    {
        return depotpartDAO.findDepotpartByName(name);
    }

    /**
     * @return the depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param depotpartDAO
     *            the depotpartDAO to set
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
    }

    /**
     * @return 返回 transactionManager
     */
    public DataSourceTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param 对transactionManager进行赋值
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    /**
     * @return 返回 storageDAO
     */
    public StorageDAO getStorageDAO()
    {
        return storageDAO;
    }

    /**
     * @param 对storageDAO进行赋值
     */
    public void setStorageDAO(StorageDAO storageDAO)
    {
        this.storageDAO = storageDAO;
    }
}
