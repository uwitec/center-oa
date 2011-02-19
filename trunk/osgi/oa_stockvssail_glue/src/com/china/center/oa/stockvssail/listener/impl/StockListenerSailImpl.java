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
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stock.constant.StockConstant;
import com.china.center.oa.stock.dao.StockItemDAO;
import com.china.center.oa.stock.listener.StockListener;
import com.china.center.oa.stock.vo.StockItemVO;
import com.china.center.oa.stock.vo.StockVO;
import com.china.center.tools.StringTools;
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

    private DepotpartDAO depotpartDAO = null;

    /**
     * default constructor
     */
    public StockListenerSailImpl()
    {
    }

    /**
     * 生成入库单
     */
    public void onEndStockItem(User user, StockBean bean, final StockItemBean each)
        throws MYException
    {
        autoToOut(user, bean, each);
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
    private void autoToOut(final User user, StockBean bean, final StockItemBean each)
        throws MYException
    {
        List<StockItemVO> items = stockItemDAO.queryEntityVOsByFK(bean.getId());

        for (StockItemVO item : items)
        {
            if ( !item.getId().equals(each.getId()))
            {
                continue;
            }

            List<BaseBean> baseList = new ArrayList<BaseBean>();

            OutBean out = new OutBean();

            out.setStatus(OutConstant.STATUS_SAVE);

            out.setStafferName(user.getStafferName());

            out.setStafferId(user.getStafferId());

            out.setType(OutConstant.OUT_TYPE_INBILL);

            out.setOutType(OutConstant.OUTTYPE_IN_COMMON);

            out.setOutTime(TimeTools.now_short());

            out.setDepartment("采购部");

            out.setCustomerId(item.getProviderId());

            out.setCustomerName(item.getProviderName());

            // 所在区域
            out.setLocationId(user.getLocationId());

            // 目的仓库
            out.setLocation(DepotConstant.STOCK_DEPOT_ID);

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

            String on = ((StockVO)bean).getOwerName();

            if (StringTools.isNullOrNone(on))
            {
                baseBean.setOwnerName("公共");
            }
            else
            {
                baseBean.setOwnerName(on);
            }

            // 来源于入库的仓区
            baseBean.setDepotpartId(each.getDepotpartId());

            DepotpartBean deport = depotpartDAO.find(each.getDepotpartId());

            if (deport != null)
            {
                baseBean.setDepotpartName(deport.getName());
            }
            else
            {
                throw new MYException("仓区不存在");
            }

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

    /**
     * @return the depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param depotpartDAO
     *            the depotpartDAO to set
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
    }

}
