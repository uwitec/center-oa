/**
 * File Name: StockListenerFinanceImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.StockPayApplyBean;
import com.china.center.oa.finance.constant.StockPayApplyConstant;
import com.china.center.oa.finance.dao.StockPayApplyDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stock.listener.StockListener;
import com.china.center.tools.TimeTools;


/**
 * StockListenerFinanceImpl
 * 
 * @author ZHUZHU
 * @version 2011-2-17
 * @see StockListenerFinanceImpl
 * @since 3.0
 */
public class StockListenerFinanceImpl implements StockListener
{
    private StockPayApplyDAO stockPayApplyDAO = null;

    private CommonDAO commonDAO = null;

    /**
     * default constructor
     */
    public StockListenerFinanceImpl()
    {
    }

    /**
     * 生成付款申请
     */
    public void onEndStockItem(User user, StockBean bean, StockItemBean item)
        throws MYException
    {
        StockPayApplyBean apply = new StockPayApplyBean();

        apply.setId(commonDAO.getSquenceString20());

        apply.setDescription("采购自动生成付款申请:" + bean.getId() + "/" + item.getId());

        apply.setDutyId(item.getDutyId());

        apply.setInvoiceId(item.getInvoiceType());

        apply.setLocationId(bean.getLocationId());

        apply.setLogTime(TimeTools.now());

        apply.setMoneys(item.getTotal());

        apply.setPayDate(item.getNearlyPayDate());

        apply.setProvideId(item.getProviderId());

        apply.setStatus(StockPayApplyConstant.APPLY_STATUS_INIT);

        apply.setStockId(bean.getId());

        apply.setStockItemId(item.getId());

        apply.setStafferId(bean.getStafferId());

        stockPayApplyDAO.saveEntityBean(apply);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "StockListener.FinanceImpl";
    }

    /**
     * @return the stockPayApplyDAO
     */
    public StockPayApplyDAO getStockPayApplyDAO()
    {
        return stockPayApplyDAO;
    }

    /**
     * @param stockPayApplyDAO
     *            the stockPayApplyDAO to set
     */
    public void setStockPayApplyDAO(StockPayApplyDAO stockPayApplyDAO)
    {
        this.stockPayApplyDAO = stockPayApplyDAO;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }
}
