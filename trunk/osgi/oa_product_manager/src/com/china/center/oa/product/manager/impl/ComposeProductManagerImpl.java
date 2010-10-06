/**
 * File Name: ComposeProductManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager.impl;


import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.bean.ComposeFeeBean;
import com.china.center.oa.product.bean.ComposeItemBean;
import com.china.center.oa.product.bean.ComposeProductBean;
import com.china.center.oa.product.constant.ComposeConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.ComposeFeeDAO;
import com.china.center.oa.product.dao.ComposeItemDAO;
import com.china.center.oa.product.dao.ComposeProductDAO;
import com.china.center.oa.product.manager.ComposeProductManager;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.vo.ComposeProductVO;
import com.china.center.oa.product.wrap.ProductChangeWrap;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;


/**
 * ComposeProductManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-2
 * @see ComposeProductManagerImpl
 * @since 1.0
 */
@Exceptional
public class ComposeProductManagerImpl implements ComposeProductManager
{
    private ComposeProductDAO composeProductDAO = null;

    private ComposeItemDAO composeItemDAO = null;

    private ComposeFeeDAO composeFeeDAO = null;

    private StorageRelationManager storageRelationManager = null;

    private CommonDAO commonDAO = null;

    /**
     * default constructor
     */
    public ComposeProductManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.ComposeProductManager#addComposeProduct(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.ComposeProductBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addComposeProduct(User user, ComposeProductBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        saveInner(bean);

        // 修改库存(合成)
        if (bean.getType() == ComposeConstant.COMPOSE_TYPE_COMPOSE)
        {
            processCompose(user, bean);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.ComposeProductManager#findById(java.lang.String)
     */
    public ComposeProductVO findById(String id)
    {
        ComposeProductVO vo = composeProductDAO.findVO(id);

        vo.setItemVOList(composeItemDAO.queryEntityVOsByFK(id));

        vo.setFeeVOList(composeFeeDAO.queryEntityVOsByFK(id));

        return vo;
    }

    /**
     * processCompose
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void processCompose(User user, ComposeProductBean bean)
        throws MYException
    {
        String sid = commonDAO.getSquenceString();

        ProductChangeWrap wrap = new ProductChangeWrap();

        wrap.setStafferId(StorageConstant.PUBLIC_STAFFER);
        wrap.setChange(bean.getAmount());
        wrap.setDepotpartId(bean.getDepotpartId());
        wrap.setDescription("合成产品异动(合成后增加):" + bean.getId());
        wrap.setPrice(bean.getPrice());
        wrap.setProductId(bean.getProductId());
        wrap.setType(StorageConstant.OPR_STORAGE_COMPOSE);
        wrap.setSerializeId(sid);

        storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, false);

        List<ComposeItemBean> itemList = bean.getItemList();

        for (ComposeItemBean composeItemBean : itemList)
        {
            ProductChangeWrap eachWrap = new ProductChangeWrap();

            eachWrap.setStorageId(composeItemBean.getStorageId());
            eachWrap.setStafferId(StorageConstant.PUBLIC_STAFFER);
            eachWrap.setChange( -composeItemBean.getAmount());
            eachWrap.setDepotpartId(composeItemBean.getDepotpartId());
            eachWrap.setDescription("合成产品移动(合成项减少):" + bean.getId());
            eachWrap.setPrice(composeItemBean.getPrice());
            eachWrap.setProductId(composeItemBean.getProductId());
            eachWrap.setType(StorageConstant.OPR_STORAGE_COMPOSE);
            eachWrap.setRelationId(eachWrap.getRelationId());
            eachWrap.setSerializeId(sid);

            storageRelationManager.changeStorageRelationWithoutTransaction(user, eachWrap, true);
        }
    }

    private void saveInner(ComposeProductBean bean)
    {
        bean.setId(commonDAO.getSquenceString20());

        composeProductDAO.saveEntityBean(bean);

        List<ComposeItemBean> itemList = bean.getItemList();

        if ( !ListTools.isEmptyOrNull(itemList))
        {
            for (ComposeItemBean composeItemBean : itemList)
            {
                composeItemBean.setParentId(bean.getId());
            }
        }

        composeItemDAO.saveAllEntityBeans(itemList);

        List<ComposeFeeBean> feeList = bean.getFeeList();

        if ( !ListTools.isEmptyOrNull(feeList))
        {
            for (ComposeFeeBean composeFeeBean : feeList)
            {
                composeFeeBean.setParentId(bean.getId());
            }
        }

        composeFeeDAO.saveAllEntityBeans(feeList);
    }

    /**
     * @return the composeProductDAO
     */
    public ComposeProductDAO getComposeProductDAO()
    {
        return composeProductDAO;
    }

    /**
     * @param composeProductDAO
     *            the composeProductDAO to set
     */
    public void setComposeProductDAO(ComposeProductDAO composeProductDAO)
    {
        this.composeProductDAO = composeProductDAO;
    }

    /**
     * @return the composeItemDAO
     */
    public ComposeItemDAO getComposeItemDAO()
    {
        return composeItemDAO;
    }

    /**
     * @param composeItemDAO
     *            the composeItemDAO to set
     */
    public void setComposeItemDAO(ComposeItemDAO composeItemDAO)
    {
        this.composeItemDAO = composeItemDAO;
    }

    /**
     * @return the composeFeeDAO
     */
    public ComposeFeeDAO getComposeFeeDAO()
    {
        return composeFeeDAO;
    }

    /**
     * @param composeFeeDAO
     *            the composeFeeDAO to set
     */
    public void setComposeFeeDAO(ComposeFeeDAO composeFeeDAO)
    {
        this.composeFeeDAO = composeFeeDAO;
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

    /**
     * @return the storageRelationManager
     */
    public StorageRelationManager getStorageRelationManager()
    {
        return storageRelationManager;
    }

    /**
     * @param storageRelationManager
     *            the storageRelationManager to set
     */
    public void setStorageRelationManager(StorageRelationManager storageRelationManager)
    {
        this.storageRelationManager = storageRelationManager;
    }
}
