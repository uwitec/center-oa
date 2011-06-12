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
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.listener.OutListener;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.constanst.TaxItemConstanst;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.manager.FinanceManager;
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

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onConfirmOutOrBuy(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onConfirmOutOrBuy(User user, OutBean outBean)
        throws MYException
    {
        // 销售-销售出库
        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_OUT_COMMON)
        {
            processOutCommon(user, outBean);

            return;
        }
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
     * 应收账款（销售金额，含税价）(1132)/主营业务收入：含税价
     * 
     * @param user
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
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

        DepartmentBean department = departmentDAO.findByUnique(outBean.getDepartment());

        if (department == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 辅助核算 客户，职员，部门
        itemIn1.setDepartmentId(department.getId());
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
        itemOut1.setDepartmentId(itemIn1.getDepartmentId());
        itemOut1.setStafferId(itemIn1.getStafferId());

        itemList.add(itemOut1);
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

        DepartmentBean department = departmentDAO.findByUnique(outBean.getDepartment());

        if (department == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 辅助核算 客户，职员，部门
        itemIn1.setDepartmentId(department.getId());
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
            itemOut1.setDepotId(outBean.getLocation());

            itemList.add(itemOut1);
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

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onOutBalancePass(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBalanceBean)
     */
    public void onOutBalancePass(User user, OutBalanceBean bean)
        throws MYException
    {
        // 

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
}
