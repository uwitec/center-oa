/*
 * 文件名：ProductionDAOImpl.java
 * 版权：Copyright by www.centerchina.com
 * 描述：
 * 修改人：zhu
 * 修改时间：2006-7-16
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.china.centet.yongyin.dao;


import java.util.List;
import java.util.Map;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.centet.yongyin.bean.ProviderBean;
import com.china.centet.yongyin.vo.CustomerVO2;


/**
 * @author zhu
 * @version 2006-7-16
 * @see CustomerDAO
 * @since
 */

public class CustomerDAO
{
    private JdbcOperation jdbcOperation = null;

    public List<CustomerBean> queryCustomerByCondtion(ConditionParse condtion)
    {
        return queryCustomerByCondtion(condtion, 100);
    }

    public List<CustomerBean> queryCustomerByCondtion(ConditionParse condtion, int limit)
    {
        condtion.removeWhereStr();

        return jdbcOperation.queryObjectsBySql(
            "select t1.* from t_center_customer_now t1, t_center_vs_stacus t2 where t1.id = t2.customerid and "
                + condtion.toString() + " order by t1.id desc").setMaxResults(limit).list(
            CustomerBean.class);
    }

    /**
     * 是否有此客户的权限
     * 
     * @param stafferId
     * @param customerId
     * @return
     */
    public boolean isCustomerOwner(String stafferId, String customerId)
    {
        String sql = "select count(1) from t_center_vs_stacus t where STAFFERID = ? and CUSTOMERID = ? ";

        return jdbcOperation.queryForInt(sql, stafferId, customerId) > 0;
    }

    public List<CustomerVO2> queryAllCustomerByCondtion(Map map)
    {
        return (List<CustomerVO2>)jdbcOperation.getIbatisDaoSupport().queryForList(
            "CustomerDAO.queryAllCustomerByCondtion", map);
    }

    /**
     * 查询产品分类下的供应商(已经修正)
     * 
     * @param condtion
     * @param productTypeId
     * @param page
     * @return
     */
    public List<ProviderBean> queryCustomerByCondtionInProductTypeVSCustomer(
                                                                             ConditionParse condtion,
                                                                             PageSeparate page)
    {
        condtion.addWhereStr();

        String sql = "select Customer.* from T_CENTER_PROVIDE Customer, T_CENTER_PRODUCTTYPEVSCUSTOMER ProductTypeVSCustomer ";

        return jdbcOperation.queryObjectsBySql(
            sql + condtion.toString() + " order by Customer.id desc").setFirstResult(
            page.getSectionFoot()).setMaxResults(page.getPageSize()).list(ProviderBean.class);
    }

    /**
     * 统计产品分类下的供应商(已经修正)
     * 
     * @param condtion
     * @param productTypeId
     * @param page
     * @return
     */
    public int countCustomerByCondtionInProductTypeVSCustomer(ConditionParse condtion)
    {
        condtion.addWhereStr();

        String sql = "select Customer.* from T_CENTER_PROVIDE Customer, "
                     + "T_CENTER_PRODUCTTYPEVSCUSTOMER ProductTypeVSCustomer ";

        return jdbcOperation.queryObjectsBySql(
            sql + condtion.toString() + " order by Customer.id desc").getCount();
    }

    /**
     * 需要修改
     * 
     * @param id
     * @return
     */
    public CustomerBean findCustomerById(String id)
    {
        return jdbcOperation.find(id, CustomerBean.class);
    }

    public int getNum(String userName, String code)
    {
        return 0;
    }

    /**
     * @return the jdbcOperation
     */
    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation;
    }

    /**
     * @param jdbcOperation
     *            the jdbcOperation to set
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }
}
