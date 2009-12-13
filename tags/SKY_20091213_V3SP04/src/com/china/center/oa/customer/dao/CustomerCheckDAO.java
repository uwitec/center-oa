/**
 * File Name: CustomerCheckDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-3-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.CustomerCheckBean;
import com.china.center.oa.customer.vo.CustomerCheckVO;


/**
 * CustomerCheckDAO
 * 
 * @author zhuzhu
 * @version 2009-3-15
 * @see CustomerCheckDAO
 * @since 1.0
 */
@Bean(name = "customerCheckDAO")
public class CustomerCheckDAO extends BaseDAO2<CustomerCheckBean, CustomerCheckVO>
{

}
