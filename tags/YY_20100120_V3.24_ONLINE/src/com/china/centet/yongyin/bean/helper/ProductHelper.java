/*
 * File Name: ProductHelper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-9
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean.helper;


import java.sql.ResultSet;
import java.sql.SQLException;

import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.SyProductBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-9-9
 * @see
 * @since
 */
public abstract class ProductHelper
{
    public static Product getBean(ResultSet rst)
        throws SQLException
    {
        Product product = new Product();

        getBean(rst, product);
        return product;
    }

    public static Product getBean(ResultSet rst, Product product)
        throws SQLException
    {
        product.setId(rst.getString("id"));
        product.setName(rst.getString("name"));
        product.setCode(rst.getString("code"));
        product.setModify(rst.getString("modify"));
        product.setRefId(rst.getString("refId"));
        product.setTemp(rst.getInt("temp"));
        product.setCityFlag(rst.getString("cityFlag"));

        return product;
    }

    public static SyProductBean getSyBean(ResultSet rst, SyProductBean bean)
        throws SQLException
    {
        bean.setCityFlag(rst.getString("cityFlag"));
        bean.setName(rst.getString("name"));
        bean.setNewId(rst.getString("newId"));
        bean.setOldId(rst.getString("oldId"));
        bean.setDateTimes(rst.getString("dateTimes"));

        return bean;
    }

    public static SyProductBean getSyBean(ResultSet rst)
        throws SQLException
    {
        SyProductBean syProductBean = new SyProductBean();

        getSyBean(rst, syProductBean);

        return syProductBean;
    }
}
