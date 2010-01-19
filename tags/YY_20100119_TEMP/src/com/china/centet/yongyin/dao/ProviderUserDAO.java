/**
 * File Name: ProviderUserDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-27<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.ProviderUserBean;


/**
 * ProviderUserDAO
 * 
 * @author ZHUZHU
 * @version 2009-12-27
 * @see ProviderUserDAO
 * @since 1.0
 */
@Bean(name = "providerUserDAO")
public class ProviderUserDAO extends BaseDAO2<ProviderUserBean, ProviderUserBean>
{}
