/**
 * File Name: StorageManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager.impl;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.User;
import com.center.china.osgi.publics.file.writer.WriteFile;
import com.center.china.osgi.publics.file.writer.WriteFileFactory;
import com.china.center.common.MYException;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.jdbc.expression.Expression;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.StorageBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.manager.StorageManager;
import com.china.center.oa.product.vo.StorageRelationVO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.TimeTools;


/**
 * StorageManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-22
 * @see StorageManagerImpl
 * @since 1.0
 */
@Exceptional
public class StorageManagerImpl implements StorageManager
{
    private final Log triggerLog = LogFactory.getLog("trigger");

    private StorageDAO storageDAO = null;

    private StorageRelationDAO storageRelationDAO = null;

    private CommonDAO commonDAO = null;

    private DepotDAO depotDAO = null;

    /**
     * default constructor
     */
    public StorageManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.StorageManager#addBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.StorageBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addBean(User user, StorageBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        Expression exp = new Expression(bean, this);

        exp.check("#name && #depotpartId &unique @storageDAO", "储位名称已经存在");

        storageDAO.saveEntityBean(bean);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.StorageManager#deleteBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StorageBean old = storageDAO.find(id);

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (old.getId().equals(DepotConstant.STOCK_STORAGE_ID))
        {
            throw new MYException("初始化数据不能删除,请确认操作");
        }

        // 储位里面有产品是不能删除的(这里是统计的产品的合计数量)
        if (storageRelationDAO.sumAllProductInStorage(id) != 0)
        {
            throw new MYException("储位下仍有产品不能删除,请确认操作");
        }

        storageDAO.deleteEntityBean(id);

        storageRelationDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.manager.StorageManager#updateBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.StorageBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateBean(User user, StorageBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        StorageBean old = storageDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        Expression exp = new Expression(bean, this);

        exp.check("#name && #depotpartId &unique2 @storageDAO", "储位名称已经存在");

        bean.setDepotpartId(old.getDepotpartId());

        bean.setLocationId(old.getLocationId());

        storageDAO.updateEntityBean(bean);

        return true;
    }

    public void exportAllStorageRelation()
    {
        triggerLog.info("begin exportAllStorageRelation...");

        WriteFile write = null;

        OutputStream out = null;

        try
        {
            out = new FileOutputStream(getProductStorePath() + "/ProductAmount_"
                                       + TimeTools.now("yyyyMMddHHmmss") + ".csv");

            ConditionParse condtion = new ConditionParse();

            List<DepotBean> lList = depotDAO.listEntityBeans();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            write.writeLine("日期,仓库,仓区,仓区属性,储位,产品名称,产品编码,产品数量,产品价格");

            String now = TimeTools.now("yyyy-MM-dd");

            for (DepotBean locationBean : lList)
            {
                condtion.clear();

                condtion.addCondition("StorageRelationBean.locationId", "=", locationBean.getId());

                condtion.addIntCondition("StorageRelationBean.amount", ">", 0);

                List<StorageRelationVO> list = storageRelationDAO
                    .queryEntityVOsByCondition(condtion);

                for (StorageRelationVO each : list)
                {
                    if (each.getAmount() > 0)
                    {
                        String typeName = DefinedCommon.getValue("depotpartType", each
                            .getDepotpartType());

                        write.writeLine(now
                                        + ','
                                        + locationBean.getName()
                                        + ','
                                        + each.getDepotpartName()
                                        + ','
                                        + typeName
                                        + ','
                                        + each.getStorageName()
                                        + ','
                                        + each.getProductName().replaceAll(",", " ").replaceAll(
                                            "\r\n", "") + ',' + each.getProductCode() + ','
                                        + String.valueOf(each.getAmount()) + ','
                                        + MathTools.formatNum(each.getPrice()));
                    }
                }

            }

            write.close();

        }
        catch (Throwable e)
        {
            triggerLog.error(e, e);
        }
        finally
        {
            if (write != null)
            {
                try
                {
                    write.close();
                }
                catch (IOException e1)
                {
                }
            }

            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                }
            }
        }

        triggerLog.info("end exportAllStorageRelation...");
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
     * @return the mailAttchmentPath
     */
    public String getProductStorePath()
    {
        return ConfigLoader.getProperty("productStore");
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

}
