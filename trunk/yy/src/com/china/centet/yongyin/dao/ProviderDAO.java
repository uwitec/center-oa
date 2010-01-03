/*
 * File Name: LogDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.ProviderBean;


/**
 * 供应商的DAO
 * 
 * @author ZHUZHU
 * @version 2007-12-15
 * @see
 * @since
 */
@Bean(name = "providerDAO")
public class ProviderDAO extends BaseDAO2<ProviderBean, ProviderBean>
{}
