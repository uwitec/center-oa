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

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.credit.vo.CustomerCreditApplyVO;
import com.china.center.oa.credit.vs.CustomerCreditApplyBean;


/**
 * CustomerCreditDAO
 * 
 * @author ZHUZHU
 * @version 2009-11-8
 * @see CustomerCreditApplyDAO
 * @since 1.0
 */
@Bean(name = "customerCreditApplyDAO")
public class CustomerCreditApplyDAO extends BaseDAO2<CustomerCreditApplyBean, CustomerCreditApplyVO>
{
    public boolean hasUpdate(Serializable cid)
    {
        return super.countByFK(cid) > 0;
    }
}
