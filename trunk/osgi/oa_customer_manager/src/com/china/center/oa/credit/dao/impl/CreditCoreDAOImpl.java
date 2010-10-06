/**
 * File Name: CreditCoreDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao.impl;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.credit.bean.CreditCoreBean;
import com.china.center.oa.credit.dao.CreditCoreDAO;


/**
 * CreditCoreDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see CreditCoreDAOImpl
 * @since 1.0
 */
public class CreditCoreDAOImpl extends BaseDAO<CreditCoreBean, CreditCoreBean> implements CreditCoreDAO
{
    /**
     * 把数据付给历史字段
     * 
     * @param oldYear
     * @return
     */
    public int synMaxBusinessToOld(int oldYear)
    {
        return this.jdbcOperation.update("set oldMaxBusiness = maxBusiness, oldSumTotal = sumTotal where 1 = 1", claz);
    }
}
