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

import com.china.center.jdbc.inter.IbatisDaoSupport;
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
    private IbatisDaoSupport ibatisDaoSupport = null;

    public PriceAskProviderBean findBeanByAskIdAndProviderId(String askId, String providerId, int type)
    {
        return this.jdbcOperation.queryObjects("where askId = ? and providerId = ? and type = ?", this.claz, askId,
            providerId, type).uniqueResult(this.claz);
    }

    public PriceAskProviderBeanVO findVOByAskIdAndProviderId(String askId, String providerId, int type)
    {
        return this.jdbcOperation.queryObjects(
            "where PriceAskProviderBean.askId = ? and  PriceAskProviderBean.providerId = ? and  PriceAskProviderBean.type = ?",
            this.clazVO, askId, providerId, type).uniqueResult(this.clazVO);
    }

    /**
     * queryByCondition
     * 
     * @param userId
     * @param productId
     * @param askDate
     * @return
     */
    public List<PriceAskProviderBeanVO> queryByCondition(String askDate, String productId, String stockId)
    {
        Map<String, Object> paramterMap = new HashMap();

        paramterMap.put("productId", productId);

        // 虚拟存储
        // paramterMap.put("saveType", PriceConstant.PRICE_ASK_SAVE_TYPE_ABS);

        // 只查询采购询价的
        paramterMap.put("src", PriceConstant.PRICE_ASK_SRC_STOCK);

        paramterMap.put("askDate", askDate);

        paramterMap.put("stockId", stockId);

        return (List)this.ibatisDaoSupport.queryForList("PriceAskProviderDAOImpl.queryByCondition", paramterMap);
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

    /**
     * @return the ibatisDaoSupport
     */
    public IbatisDaoSupport getIbatisDaoSupport()
    {
        return ibatisDaoSupport;
    }

    /**
     * @param ibatisDaoSupport
     *            the ibatisDaoSupport to set
     */
    public void setIbatisDaoSupport(IbatisDaoSupport ibatisDaoSupport)
    {
        this.ibatisDaoSupport = ibatisDaoSupport;
    }
}
