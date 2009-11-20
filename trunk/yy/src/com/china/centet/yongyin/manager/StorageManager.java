/*
 * File Name: StorageDAO.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-12-15 Grant: open source to
 * everybody
 */
package com.china.centet.yongyin.manager;


import java.util.ArrayList;
import java.util.Iterator;
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
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.DepotpartBean;
import com.china.centet.yongyin.bean.LogBean;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.StorageBean;
import com.china.centet.yongyin.bean.StorageLogBean;
import com.china.centet.yongyin.bean.StorageRelationBean;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.LockConstant;
import com.china.centet.yongyin.constant.OutConstanst;
import com.china.centet.yongyin.dao.DepotpartDAO;
import com.china.centet.yongyin.dao.ProductDAO;
import com.china.centet.yongyin.dao.StorageDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.vo.StorageBeanVO;


/**
 * 储位的逻辑处理
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class StorageManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log logger1 = LogFactory.getLog("sec");

    private final Log monitorLog = LogFactory.getLog("bill");

    private StorageDAO storageDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private ProductDAO productDAO = null;

    private UserDAO userDAO = null;

    private DataSourceTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public StorageManager()
    {}

    /**
     * Description:
     * 
     * @param bean
     * @return
     * @throws MYException
     * @since <IVersion>
     */
    public synchronized boolean addStorage(final StorageBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        JudgeTools.judgeParameterIsNull(bean.getName());

        if (storageDAO.countByName(bean.getName(), bean.getDepotpartId()) > 0)
        {
            throw new MYException(StorageBean.class, "已经存在!");
        }

        // if (bean.getAmount() < 0)
        // {
        // throw new MYException(StorageBean.class, "中产品数量不能小于0!");
        // }

        // if (StringTools.isNullOrNone(bean.getProductId()))
        // {
        // if (bean.getAmount() != 0)
        // {
        // throw new MYException("没有产品的时候不能指定数量");
        // }
        // }

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    storageDAO.addStorage(bean);

                    String ids = bean.getProductId();

                    String[] ss = ids.split("#");

                    StorageRelationBean relation = new StorageRelationBean();

                    relation.setLocationId(bean.getLocationId());

                    relation.setStorageId(bean.getId());

                    relation.setDepotpartId(bean.getDepotpartId());

                    for (String string : ss)
                    {
                        if ( !StringTools.isNullOrNone(string))
                        {
                            if (storageDAO.countProcutBydepotpart(relation.getDepotpartId(),
                                string) > 0)
                            {
                                Product product = productDAO.findProductById(string);
                                throw new RuntimeException("产品【" + product.getName()
                                                           + "】已经存在仓区中，请确认");
                            }

                            relation.setProductId(string);

                            storageDAO.addStorageRelation(relation);
                        }
                    }

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
        catch (RuntimeException e)
        {
            _logger.error(e, e);
            throw new MYException(e.getMessage());
        }

        _logger.info("成功增加储位:" + bean.getName());

        return true;
    }

    /**
     * Description:处理销售库存的变动
     * 
     * @param temp
     * @param bean
     *            操作的储位
     * @since <IVersion>
     */
    private void saleProductChange(StorageRelationBean bean, StorageRelationBean des_relation,
                                   int change, LogBean logBean, String seq)
    {
        DepotpartBean temp = depotpartDAO.findDepotpartById(bean.getDepotpartId());

        // 可以是ok到ok
        if (des_relation != null)
        {
            DepotpartBean temp1 = depotpartDAO.findDepotpartById(des_relation.getDepotpartId());

            // ok到ok
            if (temp.getType() == Constant.TYPE_DEPOTPART_OK
                && temp1.getType() == Constant.TYPE_DEPOTPART_OK)
            {
                return;
            }
        }

        // 如果是良品仓的发生了变动，需要同步到库存里面
        if (temp.getType() == Constant.TYPE_DEPOTPART_OK)
        {
            // NOTE synchronized handle product
            synchronized (LockConstant.CENTER_PRODUCT_AMOUNT_LOCK)
            {
                Product product = productDAO.findProductById(bean.getProductId());

                if (product == null)
                {
                    throw new RuntimeException("产品不存在");
                }

                // 当前销售库存
                int current = productDAO.getTotal(bean.getProductId(), bean.getLocationId());

                int count = current + change;

                if (count < 0)
                {
                    throw new RuntimeException("销售库存产品的数量变动后会小于0，确认实际库存和销售库存");
                }

                // 同步到销售库存里面
                productDAO.modifyTatol(bean.getProductId(), count, bean.getLocationId());

                logger1.info("saleProductChange里面修改库存[" + product.getName() + "]数量:[" + current
                             + "-->" + count + "](如果事务执行失败此不成立)");

                logBean.setOutId(seq);
                logBean.setType(OutConstanst.OUT_TYPE_MOVE);
                logBean.setOutType(OutConstanst.OUT_TYPE_MOVE);
                logBean.setProductName(product.getName());
                logBean.setBeforCount(current);
                logBean.setAfterCount(count);
                logBean.setCurrent(change);
                logBean.setPreStatus(99);
                logBean.setAfterStatus(99);
                logBean.setLocationId(bean.getLocationId());
            }
        }
    }

    /**
     * updateStorage
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    public synchronized boolean updateStorage(final StorageBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        JudgeTools.judgeParameterIsNull(bean.getName(), bean.getId());

        final StorageBean old = storageDAO.findStorageById(bean.getId());

        if (old == null)
        {
            throw new MYException(StorageBean.class, "不存在!");
        }

        if ( !old.getName().equals(bean.getName()))
        {
            if (storageDAO.countByName(bean.getName(), bean.getDepotpartId()) > 0)
            {
                throw new MYException(StorageBean.class, "已经存在!");
            }
        }

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    storageDAO.modfiyStorage(bean);

                    updateRelationInner(bean);

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
        catch (RuntimeException e)
        {
            _logger.error(e, e);
            throw new MYException(e.getMessage());
        }

        _logger.info("成功修改储位:" + bean.getName());
        return true;
    }

    /**
     * 修改储位间的产品
     * 
     * @param depotpartId
     * @param productIds
     * @param dirStorage
     * @return
     * @throws MYException
     */
    public boolean changeDefaultStorage(final String depotpartId, final String[] productIds,
                                        final String dirStorage)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(depotpartId, dirStorage);

        for (String productId : productIds)
        {
            StorageRelationBean bean = storageDAO.findStorageRelationByDepotpartAndProcut(
                depotpartId, productId);

            if (bean == null)
            {
                throw new MYException("缺少对应关系");
            }
        }

        StorageBean sbean = storageDAO.findStorageById(dirStorage);

        if (sbean == null)
        {
            throw new MYException("缺少目的储位");
        }

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    for (String productId : productIds)
                    {
                        StorageRelationBean bean = storageDAO.findStorageRelationByDepotpartAndProcut(
                            depotpartId, productId);

                        if (bean == null)
                        {
                            throw new RuntimeException("缺少对应关系");
                        }

                        bean.setStorageId(dirStorage);

                        storageDAO.updateStorageRelation(bean);

                        _logger.info("修改储位产品到目的储位成功:" + productId + "|" + dirStorage);
                    }

                    return Boolean.TRUE;
                }

            });
        }
        catch (TransactionException e)
        {
            _logger.error(e, e);
            throw new MYException("系统错误");
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("系统错误");
        }
        catch (RuntimeException e)
        {
            _logger.error(e, e);
            throw new MYException(e.getMessage());
        }

        return true;
    }

    /**
     * Description:
     * 
     * @param src
     * @param dest
     * @param src_Depotpart
     * @param dest_Depotpart
     * @param bean
     * @return
     * @throws MYException
     * @since <IVersion>
     */
    public synchronized String moveStorage(String src_Depotpart, String dest_Depotpart,
                                           final String productId, final int amount,
                                           final String stafferName)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(src_Depotpart, dest_Depotpart);

        JudgeTools.judgeParameterIsNull(amount, true);

        if (amount <= 0)
        {
            throw new MYException("移动的数量必须大于0");
        }

        if (src_Depotpart.equals(dest_Depotpart))
        {
            throw new MYException("同一个仓区不能转移产品");
        }

        final DepotpartBean srcBean = depotpartDAO.findDepotpartById(src_Depotpart);

        final DepotpartBean destBean = depotpartDAO.findDepotpartById(dest_Depotpart);

        Product product = productDAO.findProductById(productId);

        if (storageDAO.countProcutBydepotpart(destBean.getId(), productId) <= 0)
        {
            throw new MYException(destBean.getName() + "仓区下没有 " + product.getName());
        }

        final StorageRelationBean relation = storageDAO.findStorageRelationByDepotpartAndProcut(
            srcBean.getId(), productId);

        final StorageRelationBean des_relation = storageDAO.findStorageRelationByDepotpartAndProcut(
            destBean.getId(), productId);

        if (relation == null)
        {
            throw new MYException("储位和产品的关系不存在，请重新操作!");
        }

        if (des_relation == null)
        {
            throw new MYException("储位和产品的关系不存在，请重新操作!");
        }

        if (relation.getAmount() - amount < 0)
        {
            throw new MYException("仓区下的产品【" + product.getName() + "】数量不足，请重新操作!");
        }

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        String rst = null;
        try
        {
            rst = (String)tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    int old = relation.getAmount();

                    relation.setAmount(relation.getAmount() - amount);

                    // 更新源仓区的数量
                    LogBean logBean = new LogBean();

                    String rs = updateRelationInner(relation, des_relation, old, -amount,
                        stafferName, Constant.OPR_STORAGE_MOVE, null, logBean);

                    if ( !StringTools.isNullOrNone(logBean.getProductName()))
                    {
                        monitorLog.info(logBean);
                    }

                    old = des_relation.getAmount();

                    des_relation.setAmount(des_relation.getAmount() + amount);

                    logBean = new LogBean();

                    // 更新目的仓区的数量
                    rs = updateRelationInner(des_relation, relation, old, amount, stafferName,
                        Constant.OPR_STORAGE_MOVE, rs, logBean);

                    if ( !StringTools.isNullOrNone(logBean.getProductName()))
                    {
                        monitorLog.info(logBean);
                    }

                    return rs;
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
        catch (RuntimeException e)
        {
            _logger.error(e, e);
            throw new MYException(e.getCause().toString());
        }

        return rst;
    }

    /**
     * Description:
     * 
     * @param src
     * @param dest
     * @param src_Storage
     * @param dest_Storage
     * @param bean
     * @return
     * @throws MYException
     * @since <IVersion>
     */
    public synchronized boolean changeStorage(final String id, final int amount,
                                              final String stafferName)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        if (amount < 0)
        {
            throw new MYException("数量必须大于0");
        }

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    StorageRelationBean relation = storageDAO.findStorageRelationById(id);

                    int old = relation.getAmount();

                    relation.setAmount(amount);

                    if (relation == null)
                    {
                        return Boolean.TRUE;
                    }

                    LogBean logBean = new LogBean();

                    updateRelationInner(relation, null, old, amount - old, stafferName,
                        Constant.OPR_STORAGE_UPDATE, null, logBean);

                    if ( !StringTools.isNullOrNone(logBean.getProductName()))
                    {
                        monitorLog.info(logBean);
                    }

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
        catch (RuntimeException e)
        {
            _logger.error(e, e);
            throw new MYException(e.getMessage());
        }

        return true;
    }

    /**
     * Description: 内部修改储位的产品数量
     * 
     * @param bean
     * @param old
     * @param change
     * @param logSeqId
     *            日志的序列
     * @since <IVersion>
     */
    private String updateRelationInner(StorageRelationBean bean, StorageRelationBean des_relation,
                                       int old, int change, String stafferName, int type,
                                       String logSeqId, LogBean logBean)
    {
        // 修改关系的产品数量
        storageDAO.updateStorageRelation(bean);

        StorageLogBean log = new StorageLogBean();

        log = new StorageLogBean();

        BeanUtil.copyProperties(log, bean);

        log.setStorageId(bean.getStorageId());

        log.setType(type);

        log.setPreAmount(old);

        log.setAfterAmount(bean.getAmount());

        log.setChangeAmount(change);

        log.setLogTime(TimeTools.now());

        log.setUser(stafferName);

        if ( !StringTools.isNullOrNone(logSeqId))
        {
            log.setSerializeId(logSeqId);
        }

        String seq = storageDAO.addStorageLog(log);

        logBean.setStaffer(stafferName);
        logBean.setUser(stafferName);

        saleProductChange(bean, des_relation, log.getChangeAmount(), logBean, seq);

        return seq;
    }

    /**
     * 删除储位
     * 
     * @param id
     * @return
     * @throws MYException
     */
    public boolean delStorage(final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id, true);

        if ("0".equals(id))
        {
            throw new MYException("默认储位不能删除");
        }

        final StorageBean bean = storageDAO.findStorageById(id);

        if (bean == null)
        {
            throw new MYException(StorageBean.class, "不存在!");
        }

        List<StorageRelationBean> rs = storageDAO.queryStorageRelationByStorageId(id);

        for (StorageRelationBean storageRelationBean : rs)
        {
            if (storageRelationBean.getAmount() > 0)
            {
                throw new MYException("储位下暂时有产品且数量不为0,不能删除储位!");
            }
        }

        // 操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    storageDAO.delStorage(id);

                    storageDAO.delStorageRelationByStorageId(id);

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

        _logger.info("成功删除储位:" + bean.getName());

        return true;
    }

    public List<StorageBean> queryStorageByDepotpartId(String depotpartId)
    {
        return storageDAO.listStorage(depotpartId);
    }

    public List<StorageBeanVO> queryStorageVOByDepotpartId(String depotpartId)
    {
        return storageDAO.listStorageVO(depotpartId);
    }

    public List<StorageBean> queryStorageByCondition(ConditionParse condition)
    {
        return storageDAO.queryStorageByCondition(condition);
    }

    public StorageBean findStorageById(String id)
    {
        return storageDAO.findStorageById(id);
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
     * @return 返回 depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param 对depotpartDAO进行赋值
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
    }

    /**
     * @return 返回 productDAO
     */
    public ProductDAO getProductDAO()
    {
        return productDAO;
    }

    /**
     * @param 对productDAO进行赋值
     */
    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    /**
     * @param bean
     */
    private void updateRelationInner(final StorageBean bean)
    {
        List<StorageRelationBean> relations = storageDAO.queryStorageRelationByStorageId(bean.getId());

        List<String> news = new ArrayList<String>();

        String ids = bean.getProductId();

        String[] ss = ids.split("#");

        StorageRelationBean relation = new StorageRelationBean();

        relation.setStorageId(bean.getId());

        relation.setDepotpartId(bean.getDepotpartId());

        relation.setLocationId(bean.getLocationId());

        for (String string : ss)
        {
            if ( !StringTools.isNullOrNone(string))
            {
                boolean del = false;
                StorageRelationBean item = null;
                // 循环关系把新增的和删除的找出
                for (Iterator<StorageRelationBean> it = relations.iterator(); it.hasNext();)
                {
                    item = it.next();

                    if (item.getProductId().equals(string))
                    {
                        it.remove();
                        del = true;
                        break;
                    }
                }

                if ( !del)
                {
                    news.add(string);
                }
            }
        }

        // 循环删除更新的关系
        for (StorageRelationBean item : relations)
        {
            if (item.getAmount() != 0)
            {
                Product product = productDAO.findProductById(item.getProductId());

                throw new RuntimeException("产品【" + product.getName() + "】已经存在仓区中仍有数量，不能操作!");
            }

            storageDAO.delStorageRelation(item.getId());
        }

        // 增加新增的产品关系
        for (String string2 : news)
        {
            if (storageDAO.countProcutBydepotpart(relation.getDepotpartId(), string2) > 0)
            {
                Product product = productDAO.findProductById(string2);
                throw new RuntimeException("产品【" + product.getName() + "】已经存在仓区中，请确认");
            }

            relation.setProductId(string2);

            storageDAO.addStorageRelation(relation);
        }
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
}
