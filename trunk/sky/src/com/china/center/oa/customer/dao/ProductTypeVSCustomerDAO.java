/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.vo.ProductTypeVSCustomerVO;
import com.china.center.oa.customer.vs.ProductTypeVSCustomer;


/**
 * ProductTypeVSCustomerDAO
 * 
 * @author ZHUZHU
 * @version 2007-12-15
 * @see
 * @since
 */
@Bean(name = "productTypeVSCustomerDAO")
public class ProductTypeVSCustomerDAO extends BaseDAO2<ProductTypeVSCustomer, ProductTypeVSCustomerVO>
{
    public boolean delVSByCustomerId(String customerId)
    {
        this.jdbcOperation.delete(customerId, "customerId", this.claz);

        return true;
    }

    public List<ProductTypeVSCustomer> queryVSByCustomerId(String customerId)
    {
        return this.jdbcOperation.queryForListByField("customerId", this.claz, customerId);
    }
}
