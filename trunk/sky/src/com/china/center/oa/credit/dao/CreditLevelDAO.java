/**
 * File Name: CreditLevelDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-11-1<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.credit.vo.CreditLevelVO;


/**
 * CreditLevelDAO
 * 
 * @author ZHUZHU
 * @version 2009-11-1
 * @see CreditLevelDAO
 * @since 1.0
 */
@Bean(name = "creditLevelDAO")
public class CreditLevelDAO extends BaseDAO2<CreditLevelBean, CreditLevelVO>
{
    public CreditLevelBean findByVal(int val)
    {
        return this.findUnique("where min <= ? and max >= ?", (double)val, (double)val);
    }
}
