/**
 * File Name: CustomerCheckItemDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-3-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.CustomerCheckItemBean;
import com.china.center.oa.customer.vo.CustomerCheckItemVO;


/**
 * CustomerCheckItemDAO
 * 
 * @author zhuzhu
 * @version 2009-3-15
 * @see CustomerCheckItemDAO
 * @since 1.0
 */
@Bean(name = "customerCheckItemDAO")
public class CustomerCheckItemDAO extends BaseDAO2<CustomerCheckItemBean, CustomerCheckItemVO>
{

}
