/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.vs.ProductTypeVSCustomer;


/**
 * ÒøÐÐµÄdao
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class ProductTypeVSCustomerDAO extends BaseDAO2<ProductTypeVSCustomer, ProductTypeVSCustomer>
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
