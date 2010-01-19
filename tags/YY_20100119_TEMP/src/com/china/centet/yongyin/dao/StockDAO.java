/**
 *
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.StockBean;
import com.china.centet.yongyin.vo.StockBeanVO;


/**
 * @author Administrator
 */
public class StockDAO extends BaseDAO2<StockBean, StockBeanVO>
{
    /**
     * 更新状态
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(String id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, this.claz);

        return true;
    }

    public boolean updatePayStatus(String id, int pay)
    {
        this.jdbcOperation.updateField("pay", pay, id, this.claz);

        return true;
    }

    /**
     * 更新状态
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateTotal(String id, double total)
    {
        this.jdbcOperation.updateField("total", total, id, this.claz);

        return true;
    }

    /**
     * 更新状态
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateExceptStatus(String id, int exceptStatus)
    {
        this.jdbcOperation.updateField("exceptStatus", exceptStatus, id, this.claz);

        return true;
    }

    /**
     * 更新状态
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateNeedTime(String id, String date)
    {
        this.jdbcOperation.updateField("needTime", date, id, this.claz);

        return true;
    }
}
