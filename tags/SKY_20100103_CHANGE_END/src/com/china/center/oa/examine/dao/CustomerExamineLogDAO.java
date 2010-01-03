/**
 * File Name: NewCustomerExamineLogDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.examine.bean.CustomerExamineLogBean;


/**
 * CustomerExamineLogDAO
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see CustomerExamineLogDAO
 * @since 1.0
 */
@Bean(name = "customerExamineLogDAO")
public class CustomerExamineLogDAO extends BaseDAO2<CustomerExamineLogBean, CustomerExamineLogBean>
{

}
