/**
 * File Name: CustomerExamineManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-24<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;


import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;

import com.china.center.oa.examine.dao.NewCustomerExamineDAO;
import com.china.center.oa.publics.dao.CommonDAO2;


/**
 * CustomerExamineManager
 * 
 * @author zhuzhu
 * @version 2009-1-24
 * @see
 * @since
 */
@Exceptional
@Bean(name = "customerExamineManager")
public class CustomerExamineManager
{
    private NewCustomerExamineDAO newCustomerExamineDAO = null;

    private CommonDAO2 commonDAO2 = null;

    /**
     * default constructor
     */
    public CustomerExamineManager()
    {}

    public NewCustomerExamineDAO getNewCustomerExamineDAO()
    {
        return newCustomerExamineDAO;
    }

    public void setNewCustomerExamineDAO(NewCustomerExamineDAO newCustomerExamineDAO)
    {
        this.newCustomerExamineDAO = newCustomerExamineDAO;
    }

    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
    }
}
