/*
 * File Name: LocationManager.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-12-15 Grant: open source to
 * everybody
 */
package com.china.centet.yongyin.manager;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.DepotpartBean;
import com.china.centet.yongyin.bean.HotProductBean;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.LogBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.ProductAmount;
import com.china.centet.yongyin.bean.StorageLogBean;
import com.china.centet.yongyin.bean.StorageRelationBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.LogBeanHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.LockConstant;
import com.china.centet.yongyin.constant.OutConstanst;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.DepotpartDAO;
import com.china.centet.yongyin.dao.HotProductDAO;
import com.china.centet.yongyin.dao.LocationDAO;
import com.china.centet.yongyin.dao.ProductDAO;
import com.china.centet.yongyin.dao.StorageDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.wrap.ProductWrap;


/**
 * <描述>
 * 
 * @author ZHUZHU
 * @version 2007-12-15
 * @see
 * @since
 */
public class ProductManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log monitorLog = LogFactory.getLog("bill");

    private LocationDAO locationDAO = null;

    private CommonDAO commonDAO = null;

    private ProductDAO productDAO = null;

    private UserDAO userDAO = null;

    private HotProductDAO hotProductDAO = null;

    private StorageDAO storageDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private DataSourceTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public ProductManager()
    {}

    /**
     * 增加产品
     * 
     * @param locationBean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public String addProduct(final Product product)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(product, product.getName());

        String id = null;

        ConditionParse condtion = new ConditionParse();

        condtion.addCondition("name", "=", product.getName());

        if (productDAO.countProductByCondition(condtion) > 0)
        {
            throw new MYException("产品名称已经存在!");
        }

        ConditionParse condtion1 = new ConditionParse();
        condtion1.addCondition("code", "=", product.getCode());
        if (productDAO.countProductByCondition(condtion1) > 0)
        {
            throw new MYException("产品编码已经存在!");
        }

        id = commonDAO.getSquenceString();

        product.setId(id);

        productDAO.addProduct2(product);

        List<LocationBean> list = locationDAO.listLocation();

        ProductAmount amount = new ProductAmount();

        for (LocationBean locationBean : list)
        {
            amount.setLocationId(locationBean.getId());

            amount.setProductId(product.getId());

            amount.setProductName(product.getName());

            productDAO.addProductAmount2(amount);
        }

        return id;
    }

    /**
     * composeProduct
     * 
     * @param product
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean composeProduct(User user, List<ProductWrap> in, ProductWrap out)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(in, out);

        checkCommon(in, out);

        // NOTE synchronized handle product
        synchronized (LockConstant.CENTER_PRODUCT_AMOUNT_LOCK)
        {
            StorageRelationBean outRelation = checkDir(out, true);

            int preAmount = outRelation.getAmount();

            // do DB
            outRelation.setAmount(outRelation.getAmount() + out.getAmount());

            storageDAO.updateStorageRelation(outRelation);

            logStorage(user, out, outRelation, preAmount, true);

            for (ProductWrap productWrap : in)
            {
                StorageRelationBean each = checkIn(productWrap, false);

                int preAmountEach = each.getAmount();

                each.setAmount(each.getAmount() - productWrap.getAmount());

                storageDAO.updateStorageRelation(each);

                logStorage(user, productWrap, each, preAmountEach, false);
            }
        }

        return true;
    }

    /**
     * checkCommon
     * 
     * @param in
     * @param out
     * @throws MYException
     */
    private void checkCommon(List<ProductWrap> in, ProductWrap out)
        throws MYException
    {
        List<ProductWrap> temp = new ArrayList();

        temp.addAll(in);

        temp.add(out);

        if (ListTools.hasEqualsObject(temp))
        {
            throw new MYException("合成/分拆不能有相同的仓区下同样的产品,请确认操作");
        }
    }

    /**
     * log
     * 
     * @param user
     * @param out
     * @param outRelation
     * @param preAmount
     * @param add
     *            whether add or not
     * @throws MYException
     */
    private void logStorage(User user, ProductWrap out, StorageRelationBean outRelation,
                            int preAmount, boolean add)
        throws MYException
    {
        // 记录仓区的产品异动数量
        StorageLogBean log = new StorageLogBean();

        BeanUtil.copyProperties(log, outRelation);

        log.setType(Constant.OPR_STORAGE_COMPOSE);

        log.setPreAmount(preAmount);

        log.setAfterAmount(outRelation.getAmount());

        if (add)
        {
            log.setChangeAmount(out.getAmount());
        }
        else
        {
            log.setChangeAmount( -out.getAmount());
        }

        log.setLogTime(TimeTools.now());

        log.setUser(user.getStafferName());

        storageDAO.addStorageLog(log);

        // NOTE 合并拆分后同步到库存
        synchronizedToKuCun(user, out, outRelation, log);
    }

    /**
     * synchronizedToKuCun
     * 
     * @param user
     * @param out
     * @param outRelation
     * @param log
     * @throws MYException
     */
    private void synchronizedToKuCun(User user, ProductWrap out, StorageRelationBean outRelation,
                                     StorageLogBean log)
        throws MYException
    {
        DepotpartBean tempDepotpart = depotpartDAO.findDepotpartById(outRelation.getDepotpartId());

        if (tempDepotpart == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 如果是良品仓的发生了变动，需要同步到库存里面
        if (tempDepotpart.getType() != Constant.TYPE_DEPOTPART_OK)
        {
            return;
        }

        OutBean outTemp = new OutBean();

        outTemp.setFullId(log.getId());

        outTemp.setType(OutConstanst.OUT_TYPE_COMPOSE);

        outTemp.setOutType(OutConstanst.OUTTYPES_COMPOSE);

        BaseBean base = new BaseBean();

        Product product = productDAO.findProductById(out.getProductId());

        if (product != null)
        {
            base.setProductName(product.getName());
        }

        base.setAmount(log.getChangeAmount());

        // log in file
        LogBean bean = LogBeanHelper.getLogBean(outTemp, base, Constant.LOG_OPR_SUBMIT,
            user.getStafferName());

        bean.setLocationId(Constant.SYSTEM_LOCATION);

        // 当前销售库存
        int current = productDAO.getTotal(out.getProductId(), bean.getLocationId());

        int count = current + log.getChangeAmount();

        if (count < 0)
        {
            throw new MYException("销售库存产品的数量变动后会小于0，确认实际库存和销售库存");
        }

        bean.setBeforCount(current);

        // 同步到销售库存里面
        productDAO.modifyTatol(out.getProductId(), count, bean.getLocationId());

        bean.setAfterCount(count);

        monitorLog.info(bean);
    }

    /**
     * checkIn
     * 
     * @param productWrap
     * @return
     * @throws MYException
     */
    private StorageRelationBean checkIn(ProductWrap productWrap, boolean add)
        throws MYException
    {
        StorageRelationBean each = storageDAO.findStorageRelationByDepotpartAndProcut(
            productWrap.getDepotpartId(), productWrap.getProductId());

        if (each == null)
        {
            throw new MYException("仓区下没有指定的产品,请确认操作");
        }

        int resultAmount = 0;

        if (add)
        {
            resultAmount = each.getAmount() + productWrap.getAmount();
        }
        else
        {
            resultAmount = each.getAmount() - productWrap.getAmount();
        }

        if (resultAmount < 0)
        {
            throw new MYException("仓区下指定的产品数量不足,请确认操作");
        }

        return each;
    }

    /**
     * checkDir
     * 
     * @param out
     * @return
     * @throws MYException
     */
    private StorageRelationBean checkDir(ProductWrap out, boolean add)
        throws MYException
    {
        StorageRelationBean outRelation = storageDAO.findStorageRelationByDepotpartAndProcut(
            out.getDepotpartId(), out.getProductId());

        if (outRelation == null)
        {
            if (outRelation == null)
            {
                throw new MYException("仓区下没有指定的产品,请确认操作");
            }
        }

        int resultAmount = 0;

        if (add)
        {
            resultAmount = outRelation.getAmount() + out.getAmount();
        }
        else
        {
            resultAmount = outRelation.getAmount() - out.getAmount();
        }

        if (resultAmount < 0)
        {
            throw new MYException("仓区下指定的产品数量不足,请确认操作");
        }

        return outRelation;
    }

    /**
     * composeProduct
     * 
     * @param product
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean decomposeProduct(User user, ProductWrap in, List<ProductWrap> out)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(in, out);

        checkCommon(out, in);

        // in lock compose product
        synchronized (LockConstant.CENTER_PRODUCT_AMOUNT_LOCK)
        {
            StorageRelationBean outRelation = checkDir(in, false);

            int preAmount = outRelation.getAmount();

            // do DB
            outRelation.setAmount(outRelation.getAmount() - in.getAmount());

            storageDAO.updateStorageRelation(outRelation);

            logStorage(user, in, outRelation, preAmount, false);

            for (ProductWrap productWrap : out)
            {
                StorageRelationBean each = checkIn(productWrap, true);

                int preAmountEach = each.getAmount();

                each.setAmount(each.getAmount() + productWrap.getAmount());

                storageDAO.updateStorageRelation(each);

                logStorage(user, productWrap, each, preAmountEach, true);
            }
        }

        return true;
    }

    /**
     * 修改产品
     * 
     * @param product
     * @return
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateProduct(Product product)
        throws MYException
    {
        Product old = productDAO.findProductById(product.getId());

        if (old == null)
        {
            throw new MYException("产品不存在");
        }

        boolean updateName = false;

        if ( !old.getName().equals(product.getName().trim()))
        {
            updateName = true;

            ConditionParse condtion = new ConditionParse();

            condtion.addCondition("name", "=", product.getName().trim());

            if (productDAO.countProductByCondition(condtion) > 0)
            {
                throw new MYException("产品名称已经存在:" + product.getName());
            }
        }
        productDAO.updateProduct(product);

        if (updateName)
        {
            productDAO.updateProductInBase(product.getId(), product.getName().trim());

            productDAO.updateProductInProductNum(product.getId(), product.getName().trim());
        }

        return true;
    }

    /**
     * 保存采购单
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addHotProduct(HotProductBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        int count = hotProductDAO.countProduct(bean.getProductId());

        if (count > 0)
        {
            throw new MYException("热点产品已经存在");
        }

        try
        {
            hotProductDAO.saveEntityBean(bean);
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("系统内部错误");
        }

        return true;
    }

    /**
     * 保存采购单
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean delHotProduct(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        try
        {
            hotProductDAO.deleteEntityBean(id);
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("系统内部错误");
        }

        return true;
    }

    public List<LocationBean> listLocation()
    {
        return locationDAO.listLocation();
    }

    public LocationBean findLocationById(String id)
    {
        return locationDAO.findLocation(id);
    }

    public Product findProductById(String id)
    {
        return productDAO.findProductById(id);
    }

    /**
     * @return the locationDAO
     */
    public LocationDAO getLocationDAO()
    {
        return locationDAO;
    }

    /**
     * @return the transactionManager
     */
    public DataSourceTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param locationDAO
     *            the locationDAO to set
     */
    public void setLocationDAO(LocationDAO locationDAO)
    {
        this.locationDAO = locationDAO;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @return the productDAO
     */
    public ProductDAO getProductDAO()
    {
        return productDAO;
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
     * @param productDAO
     *            the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    /**
     * @return the hotProductDAO
     */
    public HotProductDAO getHotProductDAO()
    {
        return hotProductDAO;
    }

    /**
     * @param hotProductDAO
     *            the hotProductDAO to set
     */
    public void setHotProductDAO(HotProductDAO hotProductDAO)
    {
        this.hotProductDAO = hotProductDAO;
    }

    /**
     * @return the storageDAO
     */
    public StorageDAO getStorageDAO()
    {
        return storageDAO;
    }

    /**
     * @param storageDAO
     *            the storageDAO to set
     */
    public void setStorageDAO(StorageDAO storageDAO)
    {
        this.storageDAO = storageDAO;
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
}
