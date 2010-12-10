/**
 * File Name: StockListenerSailImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-5<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.stockvssail.listener.impl;


import java.util.ArrayList;
import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.constant.StockConstant;
import com.china.center.oa.stock.dao.StockItemDAO;
import com.china.center.oa.stock.listener.StockListener;
import com.china.center.oa.stock.vo.StockItemVO;
import com.china.center.tools.TimeTools;


/**
 * StockListenerSailImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-5
 * @see StockListenerSailImpl
 * @since 3.0
 */
public class StockListenerSailImpl implements StockListener
{
    private OutManager outManager = null;

    private StockItemDAO stockItemDAO = null;

    /**
     * default constructor
     */
    public StockListenerSailImpl()
    {
    }

    /**
     * 生成入库单
     */
    public void onEndStock(User user, StockBean bean)
        throws MYException
    {
        autoToOut(user, bean);
    }

    /**
     * 根据采购单生成自动入库单，每个采购单体生成一个入库单
     * 
     * @param user
     * @param id
     * @param bean
     * @param out
     * @throws MYException
     */
    private void autoToOut(final User user, StockBean bean)
        throws MYException
    {
        List<StockItemVO> items = stockItemDAO.queryEntityVOsByFK(bean.getId());

        for (StockItemVO item : items)
        {
            List<BaseBean> baseList = new ArrayList<BaseBean>();

            OutBean out = new OutBean();

            out.setStatus(OutConstant.STATUS_SAVE);

            out.setStafferName(user.getStafferName());

            out.setStafferId(user.getStafferId());

            out.setType(OutConstant.OUT_TYPE_INBILL);

            out.setOutTime(TimeTools.now());

            out.setDepartment("采购部");

            out.setCustomerId(item.getProviderId());

            out.setCustomerName(item.getProviderName());

            // 所在区域
            out.setLocationId(user.getLocationId());

            // 目的仓库
            out.setLocation(DepotConstant.CENTER_DEPOT_ID);

            out.setTotal(item.getTotal());

            out.setInway(OutConstant.IN_WAY_NO);

            out.setDescription("采购单自动转换成入库单,采购单单号:" + bean.getId());

            BaseBean baseBean = new BaseBean();

            baseBean.setValue(item.getTotal());
            baseBean.setLocationId(out.getLocation());
            baseBean.setAmount(item.getAmount());
            baseBean.setProductName(item.getProductName());
            baseBean.setUnit("套");
            baseBean.setPrice(item.getPrice());
            baseBean.setValue(item.getTotal());
            baseBean.setShowId(item.getShowId());

            baseBean.setCostPrice(item.getPrice());

            baseBean.setProductId(item.getProductId());
            baseBean.setCostPriceKey(StorageRelationHelper.getPriceKey(item.getPrice()));
            baseBean.setOwner(bean.getOwerId());
            baseBean.setDepotpartId(DepotConstant.CENTER_DEPOTPART_ID);

            baseBean.setDepotpartName("采购仓区");
            // 成本
            baseBean.setDescription(String.valueOf(item.getPrice()));
            baseList.add(baseBean);

            out.setBaseList(baseList);

            // CORE 采购单生成入库单
            String fullId = outManager.coloneOutAndSubmitWithOutAffair(out, user,
                StorageConstant.OPR_STORAGE_OUTBILLIN);

            item.setHasRef(StockConstant.STOCK_ITEM_HASREF_YES);

            item.setRefOutId(fullId);

            // 修改采购项的属性
            stockItemDAO.updateEntityBean(item);
        }
    }

    public String getListenerType()
    {
        return "StockListener.SailImpl";
    }

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }

    /**
     * @return the stockItemDAO
     */
    public StockItemDAO getStockItemDAO()
    {
        return stockItemDAO;
    }

    /**
     * @param stockItemDAO
     *            the stockItemDAO to set
     */
    public void setStockItemDAO(StockItemDAO stockItemDAO)
    {
        this.stockItemDAO = stockItemDAO;
    }

}
