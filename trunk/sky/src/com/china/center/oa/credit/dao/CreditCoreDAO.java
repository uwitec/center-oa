/**
 * File Name: CreditCoreDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.credit.bean.CreditCoreBean;


/**
 * CreditCoreDAO
 * 
 * @author ZHUZHU
 * @version 2009-12-7
 * @see CreditCoreDAO
 * @since 1.0
 */
@Bean(name = "creditCoreDAO")
public class CreditCoreDAO extends BaseDAO2<CreditCoreBean, CreditCoreBean>
{
    /**
     * 把数据付给历史字段
     * 
     * @param oldYear
     * @return
     */
    public int synMaxBusinessToOld(int oldYear)
    {
        return this.jdbcOperation.update(
            "set oldMaxBusiness = maxBusiness, oldSumTotal = sumTotal where 1 = 1", claz);
    }
}
