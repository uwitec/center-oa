/**
 * File Name: ComposeProductManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager.impl;


import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.expression.Expression;
import com.china.center.oa.product.bean.ComposeFeeBean;
import com.china.center.oa.product.bean.ComposeFeeDefinedBean;
import com.china.center.oa.product.bean.ComposeItemBean;
import com.china.center.oa.product.bean.ComposeProductBean;
import com.china.center.oa.product.constant.ComposeConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.ComposeFeeDAO;
import com.china.center.oa.product.dao.ComposeFeeDefinedDAO;
import com.china.center.oa.product.dao.ComposeItemDAO;
import com.china.center.oa.product.dao.ComposeProductDAO;
import com.china.center.oa.product.listener.ComposeProductListener;
import com.china.center.oa.product.manager.ComposeProductManager;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.vo.ComposeFeeDefinedVO;
import com.china.center.oa.product.vo.ComposeProductVO;
import com.china.center.oa.product.wrap.ProductChangeWrap;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.message.MessageConstant;
import com.china.center.oa.publics.message.PublishMessage;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * ComposeProductManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-2
 * @see ComposeProductManagerImpl
 * @since 1.0
 */
@Exceptional
public class ComposeProductManagerImpl extends AbstractListenerManager<ComposeProductListener> implements ComposeProductManager
{
    private ComposeProductDAO composeProductDAO = null;

    private ComposeItemDAO composeItemDAO = null;

    private ComposeFeeDAO composeFeeDAO = null;

    private ComposeFeeDefinedDAO composeFeeDefinedDAO = null;

    private StorageRelationManager storageRelationManager = null;

    private PublishMessage publishMessage = null;

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

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rollbackComposeProduct(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        ComposeProductBean bean = findBeanById(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !bean.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能操作自己的单据,请确认操作");
        }

        if (bean.getType() != ComposeConstant.COMPOSE_TYPE_COMPOSE)
        {
            throw new MYException("数据错误,请确认操作");
        }

        int count = composeProductDAO.countByFK(id);

        if (count > 0)
        {
            throw new MYException("此合成单存在回滚申请,请先结束此申请或者删除(或者分解已经成功)");
        }

        bean.setRefId(id);

        bean.setType(ComposeConstant.COMPOSE_TYPE_DECOMPOSE);

        bean.setLogTime(TimeTools.now());

        List<ComposeFeeBean> feeList = bean.getFeeList();

        if ( !ListTools.isEmptyOrNull(feeList))
        {
            for (ComposeFeeBean composeFeeBean : feeList)
            {
                composeFeeBean.setParentId(bean.getId());

                // 费用是负数
                composeFeeBean.setPrice( -composeFeeBean.getPrice());
            }
        }

        saveInner(bean);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean lastPassComposeProduct(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        ComposeProductBean bean = findBeanById(id);

        if (bean == null)
        {
            throw new MYException("合成单不存在");
        }

        if (bean.getStatus() != ComposeConstant.STATUS_MANAGER_PASS)
        {
            throw new MYException("合成单状态不正确,不能最终合成产品");
        }

        composeProductDAO.updateStatus(id, ComposeConstant.STATUS_CRO_PASS);

        // 结束时间
        composeProductDAO.updateLogTime(id, TimeTools.now());

        // 修改库存(合成)
        if (bean.getType() == ComposeConstant.COMPOSE_TYPE_COMPOSE)
        {
            processCompose(user, bean);
        }
        else
        {
            // 分解
            processDecompose(user, bean);
        }

        // TAX_ADD 产品合成/分解-运营总监通过
        Collection<ComposeProductListener> listenerMapValues = this.listenerMapValues();

        for (ComposeProductListener composeProductListener : listenerMapValues)
        {
            composeProductListener.onConfirmCompose(user, bean);
        }

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean passComposeProduct(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        composeProductDAO.updateStatus(id, ComposeConstant.STATUS_MANAGER_PASS);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectComposeProduct(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        composeProductDAO.deleteEntityBean(id);

        composeItemDAO.deleteEntityBeansByFK(id);

        composeFeeDAO.deleteEntityBeansByFK(id);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addComposeFeeDefinedBean(User user, ComposeFeeDefinedBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString());

        // TEMPLATE DAO校验表达式
        Expression exp = new Expression(bean, this);

        exp.check("#name &unique @composeFeeDefinedDAO", "名称已经存在");

        return composeFeeDefinedDAO.saveEntityBean(bean);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteComposeFeeDefinedBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        int count = composeFeeDAO.countByFK(id, AnoConstant.FK_FIRST);

        if (count > 0)
        {
            throw new MYException("费用项已经被使用,不能删除");
        }

        return composeFeeDefinedDAO.deleteEntityBean(id);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateComposeFeeDefinedBean(User user, ComposeFeeDefinedBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        Expression exp = new Expression(bean, this);

        exp.check("#name &unique2 @composeFeeDefinedDAO", "名称已经存在");

        return composeFeeDefinedDAO.updateEntityBean(bean);
    }

    public ComposeFeeDefinedVO findComposeFeeDefinedVO(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        ComposeFeeDefinedVO vo = composeFeeDefinedDAO.findVO(id);

        if (vo == null)
        {
            return vo;
        }

        if ( !StringTools.isNullOrNone(vo.getTaxId()))
        {
            Map<String, String> result = publishMessage.publicP2PMessage(
                MessageConstant.FINDCOMPOSEFEEDEFINEDVO, vo.getTaxId());

            if (result.size() > 0)
            {
                vo.setTaxName(result.get(MessageConstant.RESULT));
            }
        }

        return vo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.ComposeProductManager#findById(java.lang.String)
     */
    public ComposeProductVO findById(String id)
    {
        ComposeProductVO vo = composeProductDAO.findVO(id);

        if (vo == null)
        {
            return null;
        }

        vo.setItemVOList(composeItemDAO.queryEntityVOsByFK(id));

        vo.setFeeVOList(composeFeeDAO.queryEntityVOsByFK(id));

        return vo;
    }

    private ComposeProductBean findBeanById(String id)
    {
        ComposeProductBean bean = composeProductDAO.find(id);

        if (bean == null)
        {
            return null;
        }

        bean.setItemList(composeItemDAO.queryEntityBeansByFK(id));

        bean.setFeeList(composeFeeDAO.queryEntityBeansByFK(id));

        return bean;
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
        wrap.setRefId(sid);

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
            eachWrap.setRefId(sid);

            storageRelationManager.changeStorageRelationWithoutTransaction(user, eachWrap, true);
        }
    }

    private void processDecompose(User user, ComposeProductBean bean)
        throws MYException
    {
        String sid = commonDAO.getSquenceString();

        ProductChangeWrap wrap = new ProductChangeWrap();

        wrap.setStafferId(StorageConstant.PUBLIC_STAFFER);
        wrap.setChange( -bean.getAmount());
        wrap.setDepotpartId(bean.getDepotpartId());
        wrap.setDescription("分解产品异动(分解后减少):" + bean.getId());
        wrap.setPrice(bean.getPrice());
        wrap.setProductId(bean.getProductId());
        wrap.setType(StorageConstant.OPR_STORAGE_COMPOSE);
        wrap.setSerializeId(sid);
        wrap.setRefId(sid);

        storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, false);

        List<ComposeItemBean> itemList = bean.getItemList();

        for (ComposeItemBean composeItemBean : itemList)
        {
            ProductChangeWrap eachWrap = new ProductChangeWrap();

            eachWrap.setStorageId(composeItemBean.getStorageId());
            eachWrap.setStafferId(StorageConstant.PUBLIC_STAFFER);
            eachWrap.setChange(composeItemBean.getAmount());
            eachWrap.setDepotpartId(composeItemBean.getDepotpartId());
            eachWrap.setDescription("分解产品(合成项增加):" + bean.getId());
            eachWrap.setPrice(composeItemBean.getPrice());
            eachWrap.setProductId(composeItemBean.getProductId());
            eachWrap.setType(StorageConstant.OPR_STORAGE_COMPOSE);
            eachWrap.setRelationId(eachWrap.getRelationId());
            eachWrap.setSerializeId(sid);
            eachWrap.setRefId(sid);

            storageRelationManager.changeStorageRelationWithoutTransaction(user, eachWrap, true);
        }
    }

    private void saveInner(ComposeProductBean bean)
    {
        bean.setId(commonDAO.getSquenceString20());

        bean.setStatus(ComposeConstant.STATUS_SUBMIT);

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

    /**
     * @return the composeFeeDefinedDAO
     */
    public ComposeFeeDefinedDAO getComposeFeeDefinedDAO()
    {
        return composeFeeDefinedDAO;
    }

    /**
     * @param composeFeeDefinedDAO
     *            the composeFeeDefinedDAO to set
     */
    public void setComposeFeeDefinedDAO(ComposeFeeDefinedDAO composeFeeDefinedDAO)
    {
        this.composeFeeDefinedDAO = composeFeeDefinedDAO;
    }

    /**
     * @return the publishMessage
     */
    public PublishMessage getPublishMessage()
    {
        return publishMessage;
    }

    /**
     * @param publishMessage
     *            the publishMessage to set
     */
    public void setPublishMessage(PublishMessage publishMessage)
    {
        this.publishMessage = publishMessage;
    }
}
