/**
 * File Name: CustomerDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-13<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.ext.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.CustomerBean;


/**
 * CustomerDAO
 * 
 * @author ZHUZHU
 * @version 2009-12-13
 * @see CustomerBaseDAO
 * @since 1.0
 */
@Bean(name = "customerBaseDAO")
public class CustomerBaseDAO extends BaseDAO2<CustomerBean, CustomerBean>
{

}
