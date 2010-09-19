/**
 * File Name: PriceAskProviderDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-9-10<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.stock.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.stock.bean.PriceAskProviderBean;
import com.china.center.oa.stock.constant.PriceConstant;
import com.china.center.oa.stock.dao.PriceAskProviderDAO;
import com.china.center.oa.stock.vo.PriceAskProviderBeanVO;


/**
 * PriceAskProviderDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-9-10
 * @see PriceAskProviderDAOImpl
 * @since 1.0
 */
public class PriceAskProviderDAOImpl extends BaseDAO<PriceAskProviderBean, PriceAskProviderBeanVO> implements PriceAskProviderDAO
{
    public PriceAskProviderBean findBeanByAskIdAndProviderId(String askId, String providerId, int type)
    {
        return this.jdbcOperation.queryObjects("where askId = ? and providerId = ? and type = ?", this.claz, askId,
            providerId, type).uniqueResult(this.claz);
    }

    /**
     * queryByCondition
     * 
     * @param userId
     * @param productId
     * @param askDate
     * @return
     */
    public List<PriceAskProviderBeanVO> queryByCondition(String askDate, String productId)
    {
        Map<String, Object> paramterMap = new HashMap();

        paramterMap.put("productId", productId);

        // 虚拟存储
        paramterMap.put("saveType", PriceConstant.PRICE_ASK_SAVE_TYPE_ABS);

        paramterMap.put("type", PriceConstant.PRICE_ASK_TYPE_NET);

        paramterMap.put("askDate", askDate);

        return (List)this.jdbcOperation.getIbatisDaoSupport().queryForList("PriceAskProviderDAO.queryByCondition",
            paramterMap);
    }

    /**
     * deleteByProviderId
     * 
     * @param askId
     * @param providerId
     * @return
     */
    public boolean deleteByProviderId(String askId, String providerId, int type)
    {
        this.jdbcOperation.delete("where askId = ? and providerId = ? and type = ?", claz, askId, providerId, type);

        return true;
    }
}