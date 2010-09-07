/**
 * File Name: StorageRelationDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao.impl;


import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.vo.StorageRelationVO;
import com.china.center.oa.product.vs.StorageRelationBean;


/**
 * StorageRelationDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-22
 * @see StorageRelationDAOImpl
 * @since 1.0
 */
public class StorageRelationDAOImpl extends BaseDAO<StorageRelationBean, StorageRelationVO> implements StorageRelationDAO
{
    public int sumAllProductInStorage(String storageId)
    {
        String sql = BeanTools.getSumHead(claz, "amount") + "where storageId = ?";

        return this.jdbcOperation.queryForInt(sql, storageId);
    }

    public int sumAllProductByProductId(String productId)
    {
        String sql = BeanTools.getSumHead(claz, "amount") + "where productId = ?";

        return this.jdbcOperation.queryForInt(sql, productId);
    }

    public StorageRelationBean findByDepotpartIdAndProductIdAndPriceKey(String depotpartId, String productId,
                                                                        String priceKey)
    {
        return findUnique("where depotpartId = ? and productId = ? and priceKey = ?", depotpartId, productId, priceKey);
    }

    public int sumProductInDepotpartId(String productId, String depotpartId)
    {
        String sql = BeanTools.getSumHead(claz, "amount") + "where productId = ? and depotpartId = ?";

        return this.jdbcOperation.queryForInt(sql, productId, depotpartId);
    }

    public int sumProductInStorage(String productId, String storageId)
    {
        String sql = BeanTools.getSumHead(claz, "amount") + "where productId = ? and storageId = ?";

        return this.jdbcOperation.queryForInt(sql, productId, storageId);
    }

    public int sumProductInLocationId(String productId, String locationId)
    {
        String sql = BeanTools.getSumHead(claz, "amount") + "where productId = ? and locationId = ?";

        return this.jdbcOperation.queryForInt(sql, productId, locationId);
    }

    public int sumProductInDepotpartIdAndPriceKey(String productId, String depotpartId, String priceKey)
    {
        String sql = BeanTools.getSumHead(claz, "amount") + "where productId = ? and depotpartId = ? and priceKey = ?";

        return this.jdbcOperation.queryForInt(sql, productId, depotpartId, priceKey);
    }

    public int sumProductInLocationIdAndPriceKey(String productId, String locationId, String priceKey)
    {
        String sql = BeanTools.getSumHead(claz, "amount") + "where productId = ? and locationId = ? and priceKey = ?";

        return this.jdbcOperation.queryForInt(sql, productId, locationId, priceKey);
    }
}
