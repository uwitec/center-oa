/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.PriceAskBean;
import com.china.centet.yongyin.vo.PriceAskBeanVO;


/**
 * 询价的dao
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class PriceAskDAO extends BaseDAO2<PriceAskBean, PriceAskBeanVO>
{
    /**
     * default constructor
     */
    public PriceAskDAO()
    {}

    /**
     * 定时更新询价超时
     * 
     * @return
     */
    public boolean checkAndUpdateOverTime()
    {
        String sql = "update " + BeanTools.getTableName(this.claz)
                     + " set overtime = 1 where status = 0 and processtime <= ?";

        this.jdbcOperation.update(sql, TimeTools.now());

        return true;
    }
}
