/**
 * File Name: StorageApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-28<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager.impl;


import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.bean.StorageApplyBean;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.StorageApplyDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.product.manager.StorageApplyManager;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.vo.StorageApplyVO;
import com.china.center.oa.product.vs.StorageRelationBean;
import com.china.center.oa.product.wrap.ProductChangeWrap;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * StorageApplyManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-28
 * @see StorageApplyManagerImpl
 * @since 1.0
 */
@Exceptional
public class StorageApplyManagerImpl implements StorageApplyManager
{
    private StorageApplyDAO storageApplyDAO = null;

    private StorageRelationDAO storageRelationDAO = null;

    private CommonDAO commonDAO = null;

    private UserManager userManager = null;

    private StorageRelationManager storageRelationManager = null;

    /**
     * default constructor
     */
    public StorageApplyManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.StorageApplyManager#addBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.StorageApplyBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addBean(User user, StorageApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAdd(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        return storageApplyDAO.saveEntityBean(bean);
    }

    /**
     * checkAdd
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void checkAdd(User user, StorageApplyBean bean)
        throws MYException
    {
        StorageRelationBean relation = storageRelationDAO.find(bean.getStorageRelationId());

        if (relation == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        boolean hasPublicTranc = userManager.containAuth(user, AuthConstant.STORAGE_TRANS);

        if ("0".equals(relation.getStafferId()))
        {
            if ( !hasPublicTranc)
            {
                throw new MYException("没有操作权限");
            }
        }
        else
        {
            if ( !user.getStafferId().equals(relation.getStafferId()))
            {
                throw new MYException("只能转移自己名下的库存");
            }

            if (user.getStafferId().equals(bean.getReveiver()))
            {
                throw new MYException("不能自己转移给自己");
            }
        }

        // 删除以前的申请
        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addCondition("storageRelationId", "=", bean.getStorageRelationId());

        condition.addIntCondition("status", "=", StorageConstant.STORAGEAPPLY_STATUS_SUBMIT);

        storageApplyDAO.deleteEntityBeansByCondition(condition);

        // Expression exp = new Expression(bean, this);
        // exp.check("#storageRelationId && #status &unique @storageApplyDAO", "库存申请已经存在,请重新操作");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.StorageApplyManager#passBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean passBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StorageApplyVO bean = storageApplyDAO.findVO(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !bean.getReveiver().equals(user.getStafferId()))
        {
            throw new MYException("只有接收者可以通过申请");
        }

        if (bean.getStatus() != StorageConstant.STORAGEAPPLY_STATUS_SUBMIT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        StorageRelationBean relation = storageRelationDAO.find(bean.getStorageRelationId());

        if (relation == null)
        {
            throw new MYException("库存不存在,请废弃此申请");
        }

        if (relation.getAmount() < bean.getAmount())
        {
            throw new MYException("库存不够,请废弃此申请");
        }

        // 库存的变动
        moveRelation(user, bean, relation);

        // 修改申请的状态
        bean.setStatus(StorageConstant.STORAGEAPPLY_STATUS_PASS);

        return storageApplyDAO.updateEntityBean(bean);
    }

    /**
     * moveRelation
     * 
     * @param user
     * @param bean
     * @param relation
     * @throws MYException
     */
    private void moveRelation(User user, StorageApplyVO bean, StorageRelationBean relation)
        throws MYException
    {
        String sequence = commonDAO.getSquenceString();

        ProductChangeWrap deleteWrap = new ProductChangeWrap();

        deleteWrap.setRelationId(bean.getStorageRelationId());

        deleteWrap.setType(StorageConstant.OPR_DDEPOTPART_APPLY_MOVE);

        deleteWrap.setSerializeId(sequence);

        deleteWrap.setChange( -bean.getAmount());

        deleteWrap.setRefId(sequence);

        deleteWrap.setDescription("职员[" + bean.getApplyName() + "]转移名下产品到[" + bean.getReveiveName()
                                  + "]");

        storageRelationManager.changeStorageRelationWithoutTransaction(user, deleteWrap, true);

        ProductChangeWrap addWrap = StorageRelationHelper.createProductChangeWrap(relation);

        addWrap.setType(StorageConstant.OPR_DDEPOTPART_APPLY_MOVE);

        addWrap.setSerializeId(sequence);

        addWrap.setStafferId(bean.getReveiver());

        addWrap.setChange(bean.getAmount());

        addWrap.setRefId(sequence);

        addWrap.setDescription("职员[" + bean.getReveiveName() + "]接受名下产品[" + bean.getApplyName()
                               + "]");

        storageRelationManager.changeStorageRelationWithoutTransaction(user, addWrap, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.StorageApplyManager#rejectBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean rejectBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        return storageApplyDAO.deleteEntityBean(id);
    }

    /**
     * @return the storageApplyDAO
     */
    public StorageApplyDAO getStorageApplyDAO()
    {
        return storageApplyDAO;
    }

    /**
     * @param storageApplyDAO
     *            the storageApplyDAO to set
     */
    public void setStorageApplyDAO(StorageApplyDAO storageApplyDAO)
    {
        this.storageApplyDAO = storageApplyDAO;
    }

    /**
     * @return the storageRelationDAO
     */
    public StorageRelationDAO getStorageRelationDAO()
    {
        return storageRelationDAO;
    }

    /**
     * @param storageRelationDAO
     *            the storageRelationDAO to set
     */
    public void setStorageRelationDAO(StorageRelationDAO storageRelationDAO)
    {
        this.storageRelationDAO = storageRelationDAO;
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

}
