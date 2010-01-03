/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.PriceAskProviderBean;
import com.china.centet.yongyin.vo.PriceAskProviderBeanVO;


/**
 * @author zhuzhu
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
}
