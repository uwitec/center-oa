/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.centet.yongyin.bean.BankBean;


/**
 * ÒøÐÐµÄdao
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class BankDAO extends BaseDAO<BankBean, BankBean>
{
    /**
     * default constructor
     */
    public BankDAO()
    {}

    public BankBean findBankByName(String name)
    {
        return jdbcOperation.find(name, "name", this.claz);
    }

    public int countByName(String name)
    {
        return this.countBycondition("where NAME = ?", name);
    }
}
