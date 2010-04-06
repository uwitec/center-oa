/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.io.Serializable;
import java.util.List;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.PriceAskBean;
import com.china.centet.yongyin.constant.PriceConstant;
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
    public boolean updateAmount(Serializable id, int newAmount)
    {
        this.jdbcOperation.updateField("amount", newAmount, id, claz);

        return true;
    }

    public boolean updateAmountStatus(Serializable id, int newStatus)
    {
        this.jdbcOperation.updateField("amountStatus", newStatus, id, claz);

        return true;
    }

    /**
     * 定时更新询价超时
     * 
     * @return
     */
    public boolean checkAndUpdateOverTime()
    {
        // 超期询价的直接设置成超期且结束
        String sql = "update " + BeanTools.getTableName(this.claz)
                     + " set overtime = 1, status = ? where status = ? and processtime <= ?";

        this.jdbcOperation.update(sql, PriceConstant.PRICE_ASK_STATUS_END,
            PriceConstant.PRICE_ASK_STATUS_INIT, TimeTools.now());

        // 自动结束正常询价
        sql = "update " + BeanTools.getTableName(this.claz)
              + " set status = ? where status = ? and processtime <= ?";

        this.jdbcOperation.update(sql, PriceConstant.PRICE_ASK_STATUS_END,
            PriceConstant.PRICE_ASK_STATUS_PROCESSING, TimeTools.now());

        return true;
    }

    /**
     * findAbsByProductIdAndProcessTime
     * 
     * @param productId
     * @param processTime
     * @return
     */
    public PriceAskBean findAbsByProductIdAndProcessTime(String productId, String processTime)
    {
        return this.findUnique(
            "where PriceAskBean.productId = ? and PriceAskBean.processTime = ? and PriceAskBean.saveType = ?",
            productId, processTime, PriceConstant.PRICE_ASK_SAVE_TYPE_ABS);
    }

    /**
     * queryByParentId
     * 
     * @param parentId
     * @return
     */
    public List<PriceAskBean> queryByParentId(String parentId)
    {
        return this.queryEntityBeansByCondition("where parentAsk = ?", parentId);
    }
}
