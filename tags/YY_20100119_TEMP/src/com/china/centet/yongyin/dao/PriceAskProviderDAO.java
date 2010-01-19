/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.PriceAskProviderBean;
import com.china.centet.yongyin.constant.PriceConstant;
import com.china.centet.yongyin.vo.PriceAskProviderBeanVO;


/**
 * @author ZHUZHU
 * @version 2007-12-15
 * @see
 * @since
 */
public class PriceAskProviderDAO extends BaseDAO2<PriceAskProviderBean, PriceAskProviderBeanVO>
{
    /**
     * default constructor
     */
    public PriceAskProviderDAO()
    {}

    public PriceAskProviderBean findBeanByAskIdAndProviderId(String askId, String providerId)
    {
        return this.jdbcOperation.queryObjects("where askId = ? and providerId = ?", this.claz,
            askId, providerId).uniqueResult(this.claz);
    }

    /**
     * queryByCondition
     * 
     * @param userId
     * @param productId
     * @param askDate
     * @return
     */
    public List<PriceAskProviderBeanVO> queryByCondition(String userId, String askDate)
    {
        Map<String, Object> paramterMap = new HashMap();

        paramterMap.put("userId", userId);
        paramterMap.put("type", PriceConstant.PRICE_ASK_TYPE_NET);
        paramterMap.put("askDate", askDate);

        return (List)this.jdbcOperation2.getIbatisDaoSupport().queryForList(
            "PriceAskProviderDAO.queryByCondition", paramterMap);
    }

    /**
     * deleteByProviderId
     * 
     * @param askId
     * @param providerId
     * @return
     */
    public boolean deleteByProviderId(String askId, String providerId)
    {
        this.jdbcOperation.delete("where askId = ? and providerId = ?", claz, askId, providerId);

        return true;
    }
}
