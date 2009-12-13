/**
 * File Name: StorageDAO.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-12-15 Grant: open source to
 * everybody
 */
package com.china.centet.yongyin.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.centet.yongyin.bean.StorageBean;
import com.china.centet.yongyin.bean.StorageLogBean;
import com.china.centet.yongyin.bean.StorageRelationBean;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.vo.StorageBeanVO;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2008-1-19
 * @see
 * @since
 */
public class StorageDAO
{
    private JdbcOperation jdbcOperation2 = null;

    private CommonDAO commonDAO = null;

    /**
     * default constructor
     */
    public StorageDAO()
    {}

    public boolean addStorage(StorageBean bean)
    {
        bean.setId(commonDAO.getSquenceString());

        return jdbcOperation2.save(bean) > 0;
    }

    public String addStorageLog(StorageLogBean bean)
    {
        String seq = bean.getSerializeId();

        if (StringTools.isNullOrNone(bean.getSerializeId()))
        {
            seq = SequenceTools.getSequence("PT", 5);

            bean.setSerializeId(seq);
        }

        jdbcOperation2.save(bean);

        return seq;
    }

    public boolean addStorageRelation(StorageRelationBean bean)
    {
        return jdbcOperation2.save(bean) > 0;
    }

    public boolean updateStorageRelation(StorageRelationBean bean)
    {
        return jdbcOperation2.update(bean) > 0;
    }

    public int countProcutBydepotpart(String depotpartId, String productId)
    {
        return jdbcOperation2.queryForInt(
            "select count(1) from T_CENTER_STORAGERALATION where depotpartId = ? and productId = ?",
            new Object[] {depotpartId, productId});
    }

    public int sumProcutInOKDepotpart(String productId)
    {
        return jdbcOperation2.queryForInt(
            "select sum(t1.amount) from T_CENTER_STORAGERALATION t1 , T_CENTER_DEPOTPART t2 where t1.DEPOTPARTID = t2.ID and t1.productId = ? and t2.type = ?",
            productId, Constant.TYPE_DEPOTPART_OK);
    }

    public boolean modfiyStorage(StorageBean bean)
    {
        return jdbcOperation2.update(bean) > 0;
    }

    /**
     * Description: 事务里面删除仓区，同时还要删除仓区下的储位
     * 
     * @param id
     * @return
     * @since <IVersion>
     */
    public boolean delStorage(String id)
    {
        return jdbcOperation2.delete(id, StorageBean.class) > 0;
    }

    public boolean delStorageRelation(String id)
    {
        return jdbcOperation2.delete(id, StorageRelationBean.class) > 0;
    }

    /**
     * 根据储位删除关系
     * 
     * @param storageId
     * @return
     */
    public boolean delStorageRelationByStorageId(String storageId)
    {
        return jdbcOperation2.update("delete from T_CENTER_STORAGERALATION where storageId = ?",
            storageId) > 0;
    }

    public List<StorageBean> listStorage(String depotpartId)
    {
        return jdbcOperation2.queryForList("where 1 = 1 and depotpartId = ?", StorageBean.class,
            depotpartId);
    }

    public List<StorageBeanVO> listStorageVO(String depotpartId)
    {
        String sql = "select t1.*, t0.name as depotpartName from t_center_depotpart t0, "
                     + "T_CENTER_STORAGE t1 "
                     + "where t0.id = t1.depotpartId and t1.depotpartId = ?";

        List<StorageBeanVO> lList = jdbcOperation2.queryForListBySql(sql, StorageBeanVO.class,
            depotpartId);

        return lList;
    }

    /**
     * queryStorageRelationByCondition
     * 
     * @param condition
     * @param isLimit
     * @return
     */
    public List<StorageRelationBean> queryStorageRelationByCondition(ConditionParse condition,
                                                                     boolean isLimit)
    {
        condition.removeWhereStr();

        String sql = "select t1.*, t0.name as productName, t0.code as productCode, t3.name as depotpartName, "
                     + "t2.name as storageName from t_center_product t0, "
                     + "t_center_storageralation t1, t_center_storage t2, t_center_depotpart t3 "
                     + "where t0.id = t1.productId and t1.storageId = t2.id and t1.depotpartId = t3.id "
                     + condition.toString() + " order by t1.amount desc,  t1.productId desc";

        if (isLimit)
        {
            return jdbcOperation2.queryObjectsBySql(sql).setMaxResults(200).list(
                StorageRelationBean.class);
        }
        else
        {
            return jdbcOperation2.queryForListBySql(sql, StorageRelationBean.class);
        }
    }

    /**
     * queryProductIdListInOKDepotpart
     * 
     * @return
     */
    public List<String> listProductIdListInOKDepotpart()
    {
        final List<String> result = new ArrayList();

        String sql = "select DISTINCT(t1.productid) from T_CENTER_STORAGERALATION t1, "
                     + "t_center_depotpart t2 where t1.depotpartid= t2.id and t2.type = 1";

        jdbcOperation2.query(sql, new RowCallbackHandler()
        {
            public void processRow(ResultSet rs)
                throws SQLException
            {
                result.add(rs.getString(1));
            }
        });

        return result;
    }

    /**
     * 当前仓库里面的库存
     * 
     * @param productId
     * @return
     */
    public int sumProductInOKDepotpart(String productId)
    {
        String sql = "select sum(t1.amount) from T_CENTER_STORAGERALATION t1, t_center_depotpart t2 "
                     + "where t1.depotpartid= t2.id and t2.type = 1 and t1.productid = ?";

        return jdbcOperation2.queryForInt(sql, productId);
    }

    /**
     * queryStorageLogByCondition
     * 
     * @param condition
     * @return
     */
    public List<StorageLogBean> queryStorageLogByCondition(ConditionParse condition)
    {
        condition.addWhereStr();

        return jdbcOperation2.queryForListBySql(
            "select t1.* from T_CENTER_STORAGELOG t1, t_center_product t2 " + condition.toString(),
            StorageLogBean.class);
    }

    public List<StorageRelationBean> queryStorageRelationByStorageId(String storageId)
    {
        return jdbcOperation2.queryForList("where storageId = ?", StorageRelationBean.class,
            storageId);
    }

    public List<StorageRelationBean> queryStorageRelationByStorageId(String depId, String storageId)
    {
        return jdbcOperation2.queryForList("where storageId = ? and depotpartId = ?",
            StorageRelationBean.class, storageId, depId);
    }

    public List<StorageBean> queryStorageByCondition(ConditionParse condition)
    {
        condition.addWhereStr();

        return jdbcOperation2.queryForList(condition.toString(), StorageBean.class);
    }

    public List<StorageBean> queryStorageByDepotpartId(String depotpartId)
    {
        return jdbcOperation2.queryForList("where depotpartId = ?", StorageBean.class, depotpartId);
    }

    public StorageBean findStorageById(String id)
    {
        return jdbcOperation2.find(id, StorageBean.class);
    }

    public StorageRelationBean findStorageRelationById(String id)
    {
        return jdbcOperation2.find(id, StorageRelationBean.class);
    }

    public StorageRelationBean findStorageRelationByDepotpartAndProcut(String depotpartId,
                                                                       String productId)
    {
        List<StorageRelationBean> list = jdbcOperation2.queryForList(
            "where depotpartId = ? and productId = ?", StorageRelationBean.class, depotpartId,
            productId);

        if (list.size() != 1)
        {
            return null;
        }

        return list.get(0);
    }

    public int countByName(String name, String depotpartId)
    {
        return jdbcOperation2.queryForInt("select count(1) from "
                                          + BeanTools.getTableName(StorageBean.class)
                                          + " where NAME = ? and depotpartId = ?", new Object[] {
            name, depotpartId});
    }

    /**
     * @return 返回 jdbcOperation2
     */
    public JdbcOperation getJdbcOperation2()
    {
        return jdbcOperation2;
    }

    /**
     * @param 对jdbcOperation2进行赋值
     */
    public void setJdbcOperation2(JdbcOperation jdbcOperation2)
    {
        this.jdbcOperation2 = jdbcOperation2;
    }

    /**
     * @return 返回 commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param 对commonDAO进行赋值
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }
}
