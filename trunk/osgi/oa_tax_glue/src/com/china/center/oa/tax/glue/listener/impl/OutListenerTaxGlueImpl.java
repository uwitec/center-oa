/**
 * File Name: OutListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import java.util.ArrayList;
import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.BaseBalanceBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.listener.OutListener;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.constanst.TaxItemConstanst;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * TODO_OSGI 入库单/销售单/结算单/坏账
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see OutListenerTaxGlueImpl
 * @since 3.0
 */
public class OutListenerTaxGlueImpl implements OutListener
{
    private DutyDAO dutyDAO = null;

    private DepartmentDAO departmentDAO = null;

    private TaxDAO taxDAO = null;

    private CommonDAO commonDAO = null;

    private ProviderDAO providerDAO = null;

    private FinanceManager financeManager = null;

    private OutDAO outDAO = null;

    private BaseDAO baseDAO = null;

    /**
     * default constructor
     */
    public OutListenerTaxGlueImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onCancleBadDebts(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onCancleBadDebts(User user, OutBean bean)
        throws MYException
    {
        // 取消坏账
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onCheck(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onCheck(User user, OutBean bean)
        throws MYException
    {
        // 

    }

    /**
     * CORE 销售/入库
     */
    public void onConfirmOutOrBuy(User user, OutBean outBean)
        throws MYException
    {
        // 销售处理
        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            // 销售-销售出库
            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_OUT_COMMON)
            {
                processOutCommon(user, outBean);

                return;
            }

            // 个人领样
            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH)
            {
                processOutSwatch(user, outBean);

                return;
            }

            // 零售
            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_OUT_RETAIL)
            {
                processOutRetail(user, outBean);

                return;
            }

            // 销售-委托代销
            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_OUT_CONSIGN)
            {
                processOutConsign(user, outBean);

                return;
            }

            // 销售-赠送
            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                processOutPresent(user, outBean);

                return;
            }

            return;
        }

        // 入库处理
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL)
        {
            // 入库单-调拨（调入接受时）
            if (OutHelper.isMoveIn(outBean))
            {
                processMoveIn(user, outBean);

                return;
            }

            // 入库单-报废 && 入库单-系统纠正
            if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                && (outBean.getOutType() == OutConstant.OUTTYPE_IN_DROP || outBean.getOutType() == OutConstant.OUTTYPE_IN_ERRORP))
            {
                // 营业外支出/库存商品
                processBuyDrop(user, outBean);

                return;
            }

            // 采购退货
            if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_IN_STOCK)
            {
                // 库存商品/应付账款-供应商（负数）
                processBuyStockBack(user, outBean);

                return;
            }

            // 入库单-库存中转
            if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_IN_OTHER)
            {
                // 库存商品/库存商品-中转
                processBuyOther(user, outBean);

                return;
            }

            // 入库单-领样退货
            if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
            {
                processBuySwatch(user, outBean);

                return;
            }

            // 入库单-销售退库
            if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                && outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK)
            {
                processBuyCommonBack(user, outBean);

                return;
            }

            return;
        }
    }

    private void processMoveIn(User user, OutBean outBean)
        throws MYException
    {
        // 库存商品
        // 库存商品
        FinanceBean financeBean = new FinanceBean();

        String name = "入库单-调拨（调入接受）:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BUY_OUT);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 库存商品/库存商品
        createOutCommonItemMoveIn(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 入库单-报废/入库单-系统纠正( 营业外支出/库存商品)
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processBuyDrop(User user, OutBean outBean)
        throws MYException
    {
        // 营业外支出/库存商品
        FinanceBean financeBean = new FinanceBean();

        String name = "入库单-报废/系统纠正:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        if (outBean.getOutType() == OutConstant.OUTTYPE_IN_DROP)
        {
            financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BUY_DROP);
        }
        else
        {
            financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BUY_ERRORP);
        }

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 营业外支出/库存商品
        createBuyDrop(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 库存商品/应付账款-供应商（负数）
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processBuyStockBack(User user, OutBean outBean)
        throws MYException
    {
        // 库存商品/应付账款-供应商（负数）
        FinanceBean financeBean = new FinanceBean();

        String name = "入库单-采购退货:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BUY_STOCKBACK);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 库存商品/应付账款-供应商（负数）
        createBuyStockBack(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 入库单-库存中转
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processBuyOther(User user, OutBean outBean)
        throws MYException
    {
        // 库存商品/库存商品-中转
        FinanceBean financeBean = new FinanceBean();

        String name = "入库单-库存中转:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BUY_OTHER);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 库存商品/库存商品-中转
        createBuyOther(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 销售-销售出库
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processOutCommon(User user, OutBean outBean)
        throws MYException
    {
        // 应收账款（销售金额，含税价）(1132) 主营业务收入：含税价
        // 主营业务成本(5401) 库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "销售出库:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_COMMON);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 应收账款（销售金额，含税价）(1132)/主营业务收入：含税价
        createOutCommonItem1(user, outBean, financeBean, itemList);

        // 主营业务成本(5401)/库存商品（成本价*数量）
        createOutCommonItem2(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 个人领样
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processOutSwatch(User user, OutBean outBean)
        throws MYException
    {
        // 其他应收款（销售金额，含税价）/主营业务收入
        // 主营业务成本(5401) /库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "销售出库:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_SWATCH);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 其他应收款（销售金额，含税价）1133-20/主营业务收入：含税价
        createOutSwatchItem1(user, outBean, financeBean, itemList);

        // 主营业务成本(5401)/库存商品（成本价*数量）
        createOutSwatchItem2(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 入库-个人领养退库<br>
     * 其他应收款（销售金额，含税价）（负数）/主营业务收入：含税价<br>
     * 主营业务成本（负数）/库存商品（成本价*数量）
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processBuySwatch(User user, OutBean outBean)
        throws MYException
    {
        // 其他应收款（销售金额，含税价）/主营业务收入
        // 主营业务成本(5401) /库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "入库-个人领样退库:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_OUT_SWATCHBACK);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // -其他应收款（销售金额，含税价）1133-20/主营业务收入：含税价
        createBuySwatchItem1(user, outBean, financeBean, itemList);

        // -主营业务成本(5401)/库存商品（成本价*数量）
        createBuySwatchItem2(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 入库-销售退库
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processBuyCommonBack(User user, OutBean outBean)
        throws MYException
    {
        // 其他应收款（销售金额，含税价）/主营业务收入
        // 主营业务成本(5401) /库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "入库-销售退库:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_OUT_SAILBACK);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 应收账款（销售金额，含税价）（负数）（1132）/主营业务收入：含税价
        createBuyCommonBackItem1(user, outBean, financeBean, itemList);

        // -主营业务成本(5401)/库存商品（成本价*数量）
        createBuyCommonBackItem2(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 销售-委托代销
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processOutConsign(User user, OutBean outBean)
        throws MYException
    {
        // 应收账款/主营业务收入
        // 主营业务成本/库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "销售-委托代销:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_CONSIGN);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 委托代销商品1261/库存商品（成本价*数量）
        createOutConsignItem(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 销售-结算退货单
     * 
     * @param user
     * @param bean
     * @param outBean
     * @throws MYException
     */
    private void processOutBalanceBack(User user, OutBalanceBean bean, OutBean outBean)
        throws MYException
    {
        // 应收账款/主营业务收入
        // 主营业务成本/库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "委托销售-结算退货单:" + bean.getId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_CONSIGN_BACK);

        financeBean.setRefId(bean.getId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 委托代销商品1261（负数）/库存商品（成本价*数量）
        createOutBalanceBackItem(user, outBean, bean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 销售-零售
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processOutRetail(User user, OutBean outBean)
        throws MYException
    {
        // 应收账款/主营业务收入
        // 主营业务成本/库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "销售出库:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_RETAIL);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 其他应收款（销售金额，含税价）1133-20/主营业务收入：含税价
        createOutRetailItem(user, outBean, financeBean, itemList);

        // 主营业务成本(5401)/库存商品（成本价*数量）
        createOutRetailItem2(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 销售-赠送
     * 
     * @param user
     * @param outBean
     * @throws MYException
     */
    private void processOutPresent(User user, OutBean outBean)
        throws MYException
    {
        // 应收账款/主营业务收入
        // 主营业务成本/库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "销售-赠送:" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_PRESENT);

        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 营业费用-业务招待费（5501-05）/库存商品（成本价*数量）
        createOutPresentItem(user, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 应收账款（销售价*结算数量）/主营业务收入
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutBalanceItem1(User user, OutBean outBean, OutBalanceBean bean,
                                       FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "委托销售-结算单:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn.setPareId(pare1);

        itemIn.setName("应收账款:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        // 应收账款
        String itemInTaxId = TaxItemConstanst.REVEIVE_PRODUCT;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn);

        double money = bean.getTotal();

        itemIn.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 客户，职员，部门
        copyDepartment(outBean, itemIn);
        itemIn.setStafferId(outBean.getStafferId());
        itemIn.setUnitId(outBean.getCustomerId());
        itemIn.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut1 = new FinanceItemBean();

        itemOut1.setPareId(pare1);

        itemOut1.setName("主营业务收入:" + name);

        itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut1);

        // 库存商品
        String itemTaxIdOut1 = TaxItemConstanst.MAIN_RECEIVE;

        TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut1);

        double outMoney = bean.getTotal();

        itemOut1.setInmoney(0);

        itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut1.setDescription(itemOut1.getName());

        // 辅助核算 部门/职员
        copyDepartment(outBean, itemOut1);
        itemOut1.setStafferId(outBean.getStafferId());

        itemList.add(itemOut1);
    }

    /**
     * 主营业务成本/委托代销商品
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutBalanceItem2(User user, OutBean outBean, OutBalanceBean bean,
                                       FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "委托销售-结算单:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn1.setPareId(pare1);

        itemIn1.setName("主营业务成本:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 主营业务成本(部门/职员)
        String itemInTaxId = TaxItemConstanst.MAIN_COST;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = getOutBalanceCost(bean, outBean);

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 客户，职员，部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());

        itemList.add(itemIn1);

        List<BaseBean> baseList = outBean.getBaseList();

        List<BaseBalanceBean> baseBalanceList = bean.getBaseBalanceList();

        // 库存商品（成本价*数量）
        for (BaseBalanceBean baseBalanceBean : baseBalanceList)
        {
            BaseBean baseBean = getBaseBean(baseBalanceBean, baseList);

            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("委托代销商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品-中转
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT_TEMP;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本*数量
            double outMoney = baseBalanceBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount(baseBalanceBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    private void createOutCommonItem1(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "销售出库:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn1.setPareId(pare1);

        itemIn1.setName("应收账款:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 库存商品
        String itemInTaxId = TaxItemConstanst.REVEIVE_PRODUCT;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = outBean.getTotal();

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 客户，职员，部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());
        itemIn1.setUnitId(outBean.getCustomerId());
        itemIn1.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemIn1);

        // 贷方
        FinanceItemBean itemOut1 = new FinanceItemBean();

        itemOut1.setPareId(pare1);

        itemOut1.setName("主营业务收入:" + name);

        itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut1);

        // 库存商品
        String itemTaxIdOut1 = TaxItemConstanst.MAIN_RECEIVE;

        TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut1);

        double outMoney = outBean.getTotal();

        itemOut1.setInmoney(0);

        itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut1.setDescription(itemOut1.getName());

        // 辅助核算 部门/职员
        copyDepartment(outBean, itemOut1);
        itemOut1.setStafferId(outBean.getStafferId());

        itemList.add(itemOut1);
    }

    /**
     * 应收账款/主营业务收入
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutRetailItem(User user, OutBean outBean, FinanceBean financeBean,
                                     List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "销售-零售:" + outBean.getFullId() + '.';

        // 其他应收款（销售金额，含税价）1133-20/主营业务收入
        String pare1 = commonDAO.getSquenceString();

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        itemIn1.setPareId(pare1);

        itemIn1.setName("应收账款:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 库存商品
        String itemInTaxId = TaxItemConstanst.REVEIVE_PRODUCT;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = outBean.getTotal();

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 客户，职员，部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());
        itemIn1.setUnitId(outBean.getCustomerId());
        itemIn1.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemIn1);

        // 贷方
        FinanceItemBean itemOut1 = new FinanceItemBean();

        itemOut1.setPareId(pare1);

        itemOut1.setName("主营业务收入:" + name);

        itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut1);

        // 库存商品
        String itemTaxIdOut1 = TaxItemConstanst.MAIN_RECEIVE;

        TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut1);

        // 总销售价
        double outMoney = outBean.getTotal();

        itemOut1.setInmoney(0);

        itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut1.setDescription(itemOut1.getName());

        // 辅助核算 部门/职员
        copyDepartment(outBean, itemOut1);
        itemOut1.setStafferId(outBean.getStafferId());

        itemList.add(itemOut1);
    }

    /**
     * 其他应收款（销售金额，含税价）1133-20/主营业务收入：含税价
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutSwatchItem1(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "个人领样:" + outBean.getFullId() + '.';

        // 其他应收款（销售金额，含税价）1133-20/主营业务收入
        String pare1 = commonDAO.getSquenceString();

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemInEach = new FinanceItemBean();

            itemInEach.setPareId(pare1);

            itemInEach.setName("其他应收款-样品(" + baseBean.getProductName() + "):" + name);

            itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

            FinanceHelper.copyFinanceItem(financeBean, itemInEach);

            // 其他应收款-样品
            String itemTaxIdIn = TaxItemConstanst.OTHER_REVEIVE_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdIn);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemInEach);

            // 含税价
            double outMoney = baseBean.getAmount() * baseBean.getPrice();

            itemInEach.setInmoney(FinanceHelper.doubleToLong(outMoney));

            itemInEach.setOutmoney(0);

            itemInEach.setDescription(itemInEach.getName());

            // 辅助核算 部门/职员/产品/仓库
            copyDepartment(outBean, itemInEach);
            itemInEach.setStafferId(outBean.getStafferId());
            itemInEach.setProductId(baseBean.getProductId());
            itemInEach.setProductAmount(baseBean.getAmount());
            itemInEach.setDepotId(outBean.getLocation());

            itemList.add(itemInEach);
        }

        // 贷方
        FinanceItemBean itemOut1 = new FinanceItemBean();

        itemOut1.setPareId(pare1);

        itemOut1.setName("主营业务收入:" + name);

        itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut1);

        // 库存商品
        String itemTaxIdOut1 = TaxItemConstanst.MAIN_RECEIVE;

        TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut1);

        // 总销售价
        double outMoney = outBean.getTotal();

        itemOut1.setInmoney(0);

        itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut1.setDescription(itemOut1.getName());

        // 辅助核算 部门/职员
        copyDepartment(outBean, itemOut1);
        itemOut1.setStafferId(outBean.getStafferId());

        itemList.add(itemOut1);
    }

    /**
     * 其他应收款-样品（销售金额，含税价）（负数）/主营业务收入：含税价
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createBuySwatchItem1(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "入库-个人领样退库:" + outBean.getFullId() + '.';

        // 其他应收款（销售金额，含税价）1133-20/主营业务收入
        String pare1 = commonDAO.getSquenceString();

        List<BaseBean> baseList = outBean.getBaseList();

        // 其他应收款-样品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemInEach = new FinanceItemBean();

            itemInEach.setPareId(pare1);

            itemInEach.setName("其他应收款-样品(" + baseBean.getProductName() + "):" + name);

            itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

            FinanceHelper.copyFinanceItem(financeBean, itemInEach);

            // 其他应收款-样品
            String itemTaxIdIn = TaxItemConstanst.OTHER_REVEIVE_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdIn);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemInEach);

            // 含税价
            double outMoney = -baseBean.getAmount() * baseBean.getPrice();

            itemInEach.setInmoney(FinanceHelper.doubleToLong(outMoney));

            itemInEach.setOutmoney(0);

            itemInEach.setDescription(itemInEach.getName());

            // 辅助核算 部门/职员/产品/仓库
            copyDepartment(outBean, itemInEach);
            itemInEach.setStafferId(outBean.getStafferId());
            itemInEach.setProductId(baseBean.getProductId());
            itemInEach.setProductAmount( -baseBean.getAmount());
            itemInEach.setDepotId(outBean.getLocation());

            itemList.add(itemInEach);
        }

        // 贷方
        FinanceItemBean itemOut1 = new FinanceItemBean();

        itemOut1.setPareId(pare1);

        itemOut1.setName("主营业务收入:" + name);

        itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut1);

        // 库存商品
        String itemTaxIdOut1 = TaxItemConstanst.MAIN_RECEIVE;

        TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut1);

        // 总销售价
        double outMoney = -outBean.getTotal();

        itemOut1.setInmoney(0);

        itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut1.setDescription(itemOut1.getName());

        // 辅助核算 部门/职员
        copyDepartment(outBean, itemOut1);
        itemOut1.setStafferId(outBean.getStafferId());

        itemList.add(itemOut1);
    }

    /**
     * 应收账款（销售金额，含税价）（负数）（1132）/主营业务收入：含税价
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createBuyCommonBackItem1(User user, OutBean outBean, FinanceBean financeBean,
                                          List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "入库-销售退库:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn.setPareId(pare1);

        itemIn.setName("应收账款:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        // 库存商品
        String itemInTaxId = TaxItemConstanst.REVEIVE_PRODUCT;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn);

        double money = -outBean.getTotal();

        itemIn.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 客户，职员，部门
        copyDepartment(outBean, itemIn);
        itemIn.setStafferId(outBean.getStafferId());
        itemIn.setUnitId(outBean.getCustomerId());
        itemIn.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut1 = new FinanceItemBean();

        itemOut1.setPareId(pare1);

        itemOut1.setName("主营业务收入:" + name);

        itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut1);

        // 库存商品
        String itemTaxIdOut1 = TaxItemConstanst.MAIN_RECEIVE;

        TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut1);

        // 总销售价
        double outMoney = -outBean.getTotal();

        itemOut1.setInmoney(0);

        itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut1.setDescription(itemOut1.getName());

        // 辅助核算 部门/职员
        copyDepartment(outBean, itemOut1);
        itemOut1.setStafferId(outBean.getStafferId());

        itemList.add(itemOut1);
    }

    /**
     * 部门辅助核算
     * 
     * @param outBean
     * @param item
     */
    private void copyDepartment(OutBean outBean, FinanceItemBean item)
    {
        if ( !StringTools.isNullOrNone(outBean.getIndustryId2()))
        {
            item.setDepartmentId(outBean.getIndustryId2());

            return;
        }

        if ( !StringTools.isNullOrNone(outBean.getIndustryId()))
        {
            item.setDepartmentId(outBean.getIndustryId());

            return;
        }
    }

    /**
     * 主营业务成本(5401)/库存商品（成本价*数量）
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutCommonItem2(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "销售出库:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn1.setPareId(pare1);

        itemIn1.setName("主营业务成本:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 主营业务成本(部门/职员)
        String itemInTaxId = TaxItemConstanst.MAIN_COST;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = getOutCost(outBean);

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 客户，职员，部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());

        itemList.add(itemIn1);

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount(baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    /**
     * 委托代销商品/库存商品
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutConsignItem(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "销售-委托代销:" + outBean.getFullId() + '.';

        String pare1 = commonDAO.getSquenceString();

        List<BaseBean> baseList = outBean.getBaseList();

        // 委托代销商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemInEach = new FinanceItemBean();

            itemInEach.setPareId(pare1);

            itemInEach.setName("委托代销商品(" + baseBean.getProductName() + "):" + name);

            itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

            FinanceHelper.copyFinanceItem(financeBean, itemInEach);

            // 委托代销商品
            String itemTaxIdIn = TaxItemConstanst.CONSIGN_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdIn);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemInEach);

            // 委托代销商品（成本*数量）
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemInEach.setInmoney(FinanceHelper.doubleToLong(outMoney));

            itemInEach.setOutmoney(0);

            itemInEach.setDescription(itemInEach.getName());

            // 辅助核算 产品/仓库
            itemInEach.setProductId(baseBean.getProductId());
            itemInEach.setProductAmount(baseBean.getAmount());
            itemInEach.setDepotId(outBean.getLocation());

            itemList.add(itemInEach);
        }

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本价*数量
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);
            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));
            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount(baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    /**
     * createOutBalanceBackItem
     * 
     * @param user
     * @param outBean
     * @param bean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutBalanceBackItem(User user, OutBean outBean, OutBalanceBean bean,
                                          FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "委托销售-结算退货单:" + outBean.getFullId() + '.';

        String pare1 = commonDAO.getSquenceString();

        List<BaseBean> baseList = outBean.getBaseList();

        List<BaseBalanceBean> baseBalanceList = bean.getBaseBalanceList();

        // 委托代销商品（成本价*数量）
        for (BaseBalanceBean baseBalanceBean : baseBalanceList)
        {
            BaseBean baseBean = getBaseBean(baseBalanceBean, baseList);

            FinanceItemBean itemInEach = new FinanceItemBean();

            itemInEach.setPareId(pare1);

            itemInEach.setName("委托代销商品(" + baseBean.getProductName() + "):" + name);

            itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

            FinanceHelper.copyFinanceItem(financeBean, itemInEach);

            // 委托代销商品
            String itemTaxIdIn = TaxItemConstanst.CONSIGN_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdIn);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemInEach);

            // 委托代销商品（成本*数量）
            double outMoney = -baseBalanceBean.getAmount() * baseBean.getCostPrice();

            itemInEach.setInmoney(FinanceHelper.doubleToLong(outMoney));

            itemInEach.setOutmoney(0);

            itemInEach.setDescription(itemInEach.getName());

            // 辅助核算 产品/仓库
            itemInEach.setProductId(baseBean.getProductId());
            itemInEach.setProductAmount( -baseBalanceBean.getAmount());
            itemInEach.setDepotId(outBean.getLocation());

            itemList.add(itemInEach);
        }

        // 库存商品（成本价*数量）
        for (BaseBalanceBean baseBalanceBean : baseBalanceList)
        {
            BaseBean baseBean = getBaseBean(baseBalanceBean, baseList);

            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本价*数量
            double outMoney = -baseBalanceBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);
            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));
            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount( -baseBalanceBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    private void createOutRetailItem2(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "销售-零售:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn1.setPareId(pare1);

        itemIn1.setName("主营业务成本:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 主营业务成本(部门/职员)
        String itemInTaxId = TaxItemConstanst.MAIN_COST;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = getOutCost(outBean);

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 职员/部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());

        itemList.add(itemIn1);

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本价*数量
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount(baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    /**
     * 销售-赠送( 营业费用-业务招待费（5501-05）/库存商品（成本价*数量）)
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutPresentItem(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "销售-赠送:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn.setPareId(pare1);

        itemIn.setName("主营业务成本:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        // 营业费用-业务招待费(部门/职员)
        String itemInTaxId = TaxItemConstanst.RECEIVE_COMMON;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn);

        double money = getOutCost(outBean);

        itemIn.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 职员/部门
        copyDepartment(outBean, itemIn);
        itemIn.setStafferId(outBean.getStafferId());

        itemList.add(itemIn);

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本价*数量
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount(baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    private void createOutSwatchItem2(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "个人领样:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn1.setPareId(pare1);

        itemIn1.setName("主营业务成本:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 主营业务成本(部门/职员)
        String itemInTaxId = TaxItemConstanst.MAIN_COST;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = getOutCost(outBean);

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 职员/部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());

        itemList.add(itemIn1);

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本价*数量
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount(baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    /**
     * 主营业务成本（负数）/库存商品（成本价*数量）
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createBuySwatchItem2(User user, OutBean outBean, FinanceBean financeBean,
                                      List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "个人领样退库:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn1.setPareId(pare1);

        itemIn1.setName("主营业务成本:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 主营业务成本(部门/职员)
        String itemInTaxId = TaxItemConstanst.MAIN_COST;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = -getOutCost(outBean);

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 职员/部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());

        itemList.add(itemIn1);

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本价*数量
            double outMoney = -baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount( -baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    private void createBuyCommonBackItem2(User user, OutBean outBean, FinanceBean financeBean,
                                          List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "入库-销售退库:" + outBean.getFullId() + '.';

        // 借:库存商品 贷:应付账款-供应商
        FinanceItemBean itemIn1 = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn1.setPareId(pare1);

        itemIn1.setName("主营业务成本:" + name);

        itemIn1.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn1);

        // 主营业务成本(部门/职员)
        String itemInTaxId = TaxItemConstanst.MAIN_COST;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn1);

        double money = -getOutCost(outBean);

        itemIn1.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn1.setOutmoney(0);

        itemIn1.setDescription(itemIn1.getName());

        // 辅助核算 职员/部门
        copyDepartment(outBean, itemIn1);
        itemIn1.setStafferId(outBean.getStafferId());

        itemList.add(itemIn1);

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            // 成本价*数量
            double outMoney = -baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount( -baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    /**
     * 营业外支出/库存商品
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createBuyDrop(User user, OutBean outBean, FinanceBean financeBean,
                               List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "入库单-报废/系统纠正:" + outBean.getFullId() + '.';

        // 营业外支出/库存商品
        FinanceItemBean itemIn = new FinanceItemBean();

        String pare1 = commonDAO.getSquenceString();

        itemIn.setPareId(pare1);

        itemIn.setName("营业外支出:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        // 营业外支出
        String itemInTaxId = TaxItemConstanst.OTHER_PAY;

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn);

        // 因为入库单是特性决定
        double money = -getOutCost(outBean);

        itemIn.setInmoney(FinanceHelper.doubleToLong(money));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算NA
        itemList.add(itemIn);

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOut1 = new FinanceItemBean();

            itemOut1.setPareId(pare1);

            itemOut1.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOut1);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOut1);

            double outMoney = -baseBean.getAmount() * baseBean.getCostPrice();

            itemOut1.setInmoney(0);

            itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOut1.setDescription(itemOut1.getName());

            // 辅助核算 产品/仓库
            itemOut1.setProductId(baseBean.getProductId());
            itemOut1.setProductAmount( -baseBean.getAmount());
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
        }
    }

    /**
     * 库存商品/应付账款-供应商（负数）
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createBuyStockBack(User user, OutBean outBean, FinanceBean financeBean,
                                    List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "入库单-采购退货:" + outBean.getFullId() + '.';

        String pare1 = commonDAO.getSquenceString();

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemInEach = new FinanceItemBean();

            itemInEach.setPareId(pare1);

            itemInEach.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

            FinanceHelper.copyFinanceItem(financeBean, itemInEach);

            // 库存商品
            String itemTaxIdIn = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdIn);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemInEach);

            // 库存商品减少(baseBean.getAmount()是负数)
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemInEach.setInmoney(FinanceHelper.doubleToLong(outMoney));

            itemInEach.setOutmoney(0);

            itemInEach.setDescription(itemInEach.getName());

            // 辅助核算 产品/仓库
            itemInEach.setProductId(baseBean.getProductId());
            itemInEach.setProductAmount(baseBean.getAmount());
            itemInEach.setDepotId(outBean.getLocation());

            itemList.add(itemInEach);
        }

        FinanceItemBean itemOut1 = new FinanceItemBean();

        itemOut1.setPareId(pare1);

        itemOut1.setName("应付账款:" + name);

        itemOut1.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut1);

        // 应付账款
        String itemTaxIdOut1 = TaxItemConstanst.PAY_PRODUCT;

        TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut1);

        // 应付账款-供应商（负数）
        double outMoney = getOutCost(outBean);

        itemOut1.setInmoney(0);

        itemOut1.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut1.setDescription(itemOut1.getName());

        // 辅助核算 单位
        itemOut1.setUnitId(outBean.getCustomerId());
        itemOut1.setUnitType(TaxConstanst.UNIT_TYPE_PROVIDE);

        itemList.add(itemOut1);
    }

    /**
     * 库存商品/库存商品-中转
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createBuyOther(User user, OutBean outBean, FinanceBean financeBean,
                                List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = "入库单-库存中转:" + outBean.getFullId() + '.';

        String pare1 = commonDAO.getSquenceString();

        List<BaseBean> baseList = outBean.getBaseList();

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemInEach = new FinanceItemBean();

            itemInEach.setPareId(pare1);

            itemInEach.setName("库存商品(" + baseBean.getProductName() + "):" + name);

            itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

            FinanceHelper.copyFinanceItem(financeBean, itemInEach);

            // 库存商品
            String itemTaxIdIn = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdIn);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemInEach);

            // 库存商品减少(baseBean.getAmount()是负数)
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemInEach.setInmoney(FinanceHelper.doubleToLong(outMoney));

            itemInEach.setOutmoney(0);

            itemInEach.setDescription(itemInEach.getName());

            // 辅助核算 产品/仓库
            itemInEach.setProductId(baseBean.getProductId());
            itemInEach.setProductAmount(baseBean.getAmount());
            itemInEach.setDepotId(outBean.getLocation());

            itemList.add(itemInEach);
        }

        // 库存商品-中转
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOutEach = new FinanceItemBean();

            itemOutEach.setPareId(pare1);

            itemOutEach.setName("库存商品-中转(" + baseBean.getProductName() + "):" + name);

            itemOutEach.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOutEach);

            // 库存商品-中转
            String itemTaxIdIn = TaxItemConstanst.DEPOR_PRODUCT_TEMP;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdIn);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOutEach);

            // 库存商品
            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemOutEach.setInmoney(0);
            itemOutEach.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOutEach.setDescription(itemOutEach.getName());

            // 辅助核算 产品/仓库
            itemOutEach.setProductId(baseBean.getProductId());
            itemOutEach.setProductAmount(baseBean.getAmount());
            itemOutEach.setDepotId(outBean.getLocation());

            itemList.add(itemOutEach);
        }
    }

    /**
     * 库存商品/库存商品
     * 
     * @param user
     * @param outBean
     *            接受的bean(数量是整数)
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createOutCommonItemMoveIn(User user, OutBean outBean, FinanceBean financeBean,
                                           List<FinanceItemBean> itemList)
        throws MYException
    {

        String name = "入库单-调拨:" + outBean.getFullId() + '.';

        String pare = commonDAO.getSquenceString();

        // 处理调出变动库存
        String moveOutFullId = outBean.getRefOutFullId();

        OutBean moveOut = outDAO.find(moveOutFullId);

        if (moveOut == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemInEach = new FinanceItemBean();

            itemInEach.setPareId(pare);

            itemInEach.setName("库存商品调出(" + baseBean.getProductName() + "):" + name);

            itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

            FinanceHelper.copyFinanceItem(financeBean, itemInEach);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemInEach);

            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemInEach.setInmoney(FinanceHelper.doubleToLong(outMoney));

            itemInEach.setOutmoney(0);

            itemInEach.setDescription(itemInEach.getName());

            // 辅助核算 产品/仓库
            itemInEach.setProductId(baseBean.getProductId());
            itemInEach.setProductAmount(baseBean.getAmount());

            // 调入的仓库
            itemInEach.setDepotId(outBean.getLocation());

            itemList.add(itemInEach);
        }

        // 库存商品（成本价*数量）
        for (BaseBean baseBean : baseList)
        {
            FinanceItemBean itemOutEach = new FinanceItemBean();

            itemOutEach.setPareId(pare);

            itemOutEach.setName("库存商品调入(" + baseBean.getProductName() + "):" + name);

            itemOutEach.setForward(TaxConstanst.TAX_FORWARD_OUT);

            FinanceHelper.copyFinanceItem(financeBean, itemOutEach);

            // 库存商品
            String itemTaxIdOut1 = TaxItemConstanst.DEPOR_PRODUCT;

            TaxBean outTax = taxDAO.findByUnique(itemTaxIdOut1);

            if (outTax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 科目拷贝
            FinanceHelper.copyTax(outTax, itemOutEach);

            double outMoney = baseBean.getAmount() * baseBean.getCostPrice();

            itemOutEach.setInmoney(0);

            itemOutEach.setOutmoney(FinanceHelper.doubleToLong(outMoney));

            itemOutEach.setDescription(itemOutEach.getName());

            // 辅助核算 产品/仓库
            itemOutEach.setProductId(baseBean.getProductId());
            itemOutEach.setProductAmount(baseBean.getAmount());

            // 调出的仓库
            itemOutEach.setDepotId(moveOut.getLocation());

            itemList.add(itemOutEach);
        }
    }

    /**
     * 获取成本
     * 
     * @param outBean
     * @return
     */
    private double getOutCost(OutBean outBean)
    {
        double total = 0.0d;

        List<BaseBean> baseList = outBean.getBaseList();

        for (BaseBean baseBean : baseList)
        {
            total += baseBean.getAmount() * baseBean.getCostPrice();
        }

        return total;
    }

    /**
     * 委托成本
     * 
     * @param bean
     * @param outBean
     * @return
     * @throws MYException
     */
    private double getOutBalanceCost(OutBalanceBean bean, OutBean outBean)
        throws MYException
    {
        double total = 0.0d;

        List<BaseBalanceBean> baseBalanceList = bean.getBaseBalanceList();

        List<BaseBean> baseList = outBean.getBaseList();

        for (BaseBalanceBean baseBalanceBean : baseBalanceList)
        {
            BaseBean baseBean = getBaseBean(baseBalanceBean, baseList);

            if (baseBean == null)
            {
                throw new MYException("没有找到委托原单据产品,请确认操作");
            }

            total += baseBalanceBean.getAmount() * baseBean.getCostPrice();
        }

        return total;
    }

    private BaseBean getBaseBean(BaseBalanceBean baseBalanceBean, List<BaseBean> baseList)
    {
        for (BaseBean baseBean : baseList)
        {
            if (baseBalanceBean.getBaseId().equals(baseBean.getId()))
            {
                return baseBean;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onDelete(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onDelete(User user, OutBean bean)
        throws MYException
    {
        // 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onHadPay(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public ResultBean onHadPay(User user, OutBean bean)
    {
        // 
        return null;
    }

    /**
     * 委托销售-结算单/委托销售-结算退货单
     */
    public void onOutBalancePass(User user, OutBalanceBean bean)
        throws MYException
    {
        OutBean outBean = outDAO.find(bean.getOutId());

        if (outBean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(bean.getOutId());

        outBean.setBaseList(baseList);

        // 结算单
        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_SAIL)
        {
            processOutBalance(user, bean, outBean);
        }
        else
        {
            processOutBalanceBack(user, bean, outBean);
        }

    }

    private void processOutBalance(User user, OutBalanceBean bean, OutBean outBean)
        throws MYException
    {
        // 应收账款（销售金额，含税价）(1132) 主营业务收入：含税价
        // 主营业务成本(5401) 库存商品（成本价*数量）
        FinanceBean financeBean = new FinanceBean();

        String name = "委托销售-结算单:" + bean.getId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_CONSIGN_PAY);

        financeBean.setRefId(bean.getId());

        financeBean.setRefOut(bean.getOutId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 应收账款（销售金额，含税价）(1132)/主营业务收入：含税价
        createOutBalanceItem1(user, outBean, bean, financeBean, itemList);

        // 主营业务成本(5401)/委托代销商品（成本价*数量）
        createOutBalanceItem2(user, outBean, bean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onPass(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onPass(User user, OutBean bean)
        throws MYException
    {
        // 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onReject(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onReject(User user, OutBean bean)
        throws MYException
    {
        // 取消坏账
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#outNeedPayMoney(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    public double outNeedPayMoney(User user, String fullId)
    {
        return 0;
    }

    public void onConfirmBadDebts(User user, OutBean bean)
        throws MYException
    {
        // 这里需要注意因为坏账是覆盖模式的,所以先要查询是否存在此单据的坏账确认,然后本次的金额是总坏账金额-已经存在的
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "OutListener.TaxGlueImpl";
    }

    /**
     * @return the dutyDAO
     */
    public DutyDAO getDutyDAO()
    {
        return dutyDAO;
    }

    /**
     * @param dutyDAO
     *            the dutyDAO to set
     */
    public void setDutyDAO(DutyDAO dutyDAO)
    {
        this.dutyDAO = dutyDAO;
    }

    /**
     * @return the departmentDAO
     */
    public DepartmentDAO getDepartmentDAO()
    {
        return departmentDAO;
    }

    /**
     * @param departmentDAO
     *            the departmentDAO to set
     */
    public void setDepartmentDAO(DepartmentDAO departmentDAO)
    {
        this.departmentDAO = departmentDAO;
    }

    /**
     * @return the taxDAO
     */
    public TaxDAO getTaxDAO()
    {
        return taxDAO;
    }

    /**
     * @param taxDAO
     *            the taxDAO to set
     */
    public void setTaxDAO(TaxDAO taxDAO)
    {
        this.taxDAO = taxDAO;
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

    /**
     * @return the providerDAO
     */
    public ProviderDAO getProviderDAO()
    {
        return providerDAO;
    }

    /**
     * @param providerDAO
     *            the providerDAO to set
     */
    public void setProviderDAO(ProviderDAO providerDAO)
    {
        this.providerDAO = providerDAO;
    }

    /**
     * @return the financeManager
     */
    public FinanceManager getFinanceManager()
    {
        return financeManager;
    }

    /**
     * @param financeManager
     *            the financeManager to set
     */
    public void setFinanceManager(FinanceManager financeManager)
    {
        this.financeManager = financeManager;
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    /**
     * @return the baseDAO
     */
    public BaseDAO getBaseDAO()
    {
        return baseDAO;
    }

    /**
     * @param baseDAO
     *            the baseDAO to set
     */
    public void setBaseDAO(BaseDAO baseDAO)
    {
        this.baseDAO = baseDAO;
    }
}
