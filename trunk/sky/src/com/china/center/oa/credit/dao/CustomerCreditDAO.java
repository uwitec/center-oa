/**
 * File Name: CustomerCreditDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.CreditConstant;
import com.china.center.oa.credit.vo.CustomerCreditVO;
import com.china.center.oa.credit.vs.CustomerCreditBean;


/**
 * CustomerCreditDAO
 * 
 * @author ZHUZHU
 * @version 2009-11-8
 * @see CustomerCreditDAO
 * @since 1.0
 */
@Bean(name = "customerCreditDAO")
public class CustomerCreditDAO extends BaseDAO2<CustomerCreditBean, CustomerCreditVO>
{
    public double sumValByFK(Serializable cid)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "val")
                                                 + "where cid = ?", cid);
    }

    /**
     * лчЁЩ
     * 
     * @param cid
     * @param exceptionId
     * @return
     */
    public double sumValExceptionByFK(Serializable cid, String exceptionId)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "val")
                                                 + "where cid = ? and itemId <> ?", cid,
            exceptionId);
    }

    public double sumValExceptPersonByFK(Serializable cid)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "val")
                                                 + "where cid = ? and itemId <> ?", cid,
            CreditConstant.SET_DRECT);
    }

    /**
     * countByValueId
     * 
     * @param valueId
     * @return
     */
    public int countByValueId(Serializable valueId)
    {
        return this.jdbcOperation.queryForInt(BeanTools.getCountHead(claz) + "where valueid = ?",
            valueId);
    }
}
