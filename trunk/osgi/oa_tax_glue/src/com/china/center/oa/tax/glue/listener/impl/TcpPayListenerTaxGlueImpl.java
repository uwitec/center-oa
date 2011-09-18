/**
 * File Name: TcpPayListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-9-13<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import java.util.ArrayList;
import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.constanst.TaxItemConstanst;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.oa.tcp.bean.TravelApplyBean;
import com.china.center.oa.tcp.constanst.TcpConstanst;
import com.china.center.oa.tcp.listener.TcpPayListener;
import com.china.center.tools.TimeTools;


/**
 * 报销生成凭证的地方
 * 
 * @author ZHUZHU
 * @version 2011-9-13
 * @see TcpPayListenerTaxGlueImpl
 * @since 1.0
 */
public class TcpPayListenerTaxGlueImpl implements TcpPayListener
{
    private DutyDAO dutyDAO = null;

    private DepartmentDAO departmentDAO = null;

    private TaxDAO taxDAO = null;

    private BankDAO bankDAO = null;

    private CommonDAO commonDAO = null;

    private StafferDAO stafferDAO = null;

    private FinanceManager financeManager = null;

    private FinanceDAO financeDAO = null;

    private PaymentDAO paymentDAO = null;

    private ParameterDAO parameterDAO = null;

    private InBillDAO inBillDAO = null;

    private OutBillDAO outBillDAO = null;

    /**
     * default constructor
     */
    public TcpPayListenerTaxGlueImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tcp.listener.TcpPayListener#onPayTravelApply(com.center.china.osgi.publics.User,
     *      com.china.center.oa.tcp.bean.TravelApplyBean, java.util.List)
     */
    public void onPayTravelApply(User user, TravelApplyBean bean, List<OutBillBean> outBillList)
        throws MYException
    {
        for (OutBillBean outBillBean : outBillList)
        {
            // 兼容性
            if ( !TaxGlueHelper.bankGoon(outBillBean.getBankId(), this.taxDAO))
            {
                continue;
            }

            BankBean bank = bankDAO.find(outBillBean.getBankId());

            if (bank == null)
            {
                throw new MYException("银行不存在,请确认操作");
            }

            FinanceBean financeBean = new FinanceBean();

            String name = DefinedCommon.getValue("tcpApplyType", bean.getType()) + "申请通过:"
                          + bean.getId() + '.';

            financeBean.setName(name);

            financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

            if (bean.getType() == TcpConstanst.TCP_APPLYTYPE_TRAVEL)
            {
                financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_TCP_BORROW);
            }
            else if (bean.getType() == TcpConstanst.TCP_APPLYTYPE_ENTERTAIN)
            {
                financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_TCP_ENTERTAIN);
            }
            else if (bean.getType() == TcpConstanst.TCP_APPLYTYPE_STOCK)
            {
                financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_TCP_STOCK);
            }
            else if (bean.getType() == TcpConstanst.TCP_APPLYTYPE_PUBLIC)
            {
                financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_TCP_PUBLIC);
            }
            else
            {
                financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_TCP_BORROW);
            }

            // 付款单申请
            financeBean.setRefId(bean.getId());

            financeBean.setRefBill(outBillBean.getId());

            financeBean.setDutyId(bank.getDutyId());

            financeBean.setCreaterId(user.getStafferId());

            financeBean.setDescription(financeBean.getName());

            financeBean.setFinanceDate(TimeTools.now_short());

            financeBean.setLogTime(TimeTools.now());

            List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

            // 应付账款-供应商/银行科目
            createAddItem1(user, bean, bank, outBillBean, financeBean, itemList);

            financeBean.setItemList(itemList);

            financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
        }

    }

    /**
     * 其他应收款-借款/银行科目
     * 
     * @param user
     * @param bean
     * @param bank
     * @param outBillBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem1(User user, TravelApplyBean bean, BankBean bank,
                                OutBillBean outBillBean, FinanceBean financeBean,
                                List<FinanceItemBean> itemList)
        throws MYException
    {
        // 借款人
        StafferBean staffer = stafferDAO.find(bean.getBorrowStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        String name = "出差申请借款:" + bean.getId() + '.';

        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setName("其他应收款_备用金:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        // 其他应收款_备用金(部门/职员)
        TaxBean inTax = taxDAO.findByUnique(TaxItemConstanst.OTHER_RECEIVE_BORROW);

        if (inTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(inTax, itemIn);

        // 当前发生额
        double inMoney = outBillBean.getMoneys();

        itemIn.setInmoney(FinanceHelper.doubleToLong(inMoney));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 部门和职员
        itemIn.setDepartmentId(staffer.getPrincipalshipId());
        itemIn.setStafferId(staffer.getId());

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("银行科目:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        // 银行科目
        TaxBean outTax = taxDAO.findByBankId(outBillBean.getBankId());

        if (outTax == null)
        {
            throw new MYException("银行[%s]缺少对应的科目,请确认操作", bank.getName());
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        double outMoney = outBillBean.getMoneys();

        itemOut.setInmoney(0);

        itemOut.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut.setDescription(itemOut.getName());

        // 辅助核算 NA
        itemList.add(itemOut);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "TcpPayListener.TaxGlueImpl";
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
     * @return the bankDAO
     */
    public BankDAO getBankDAO()
    {
        return bankDAO;
    }

    /**
     * @param bankDAO
     *            the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO)
    {
        this.bankDAO = bankDAO;
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
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
    }

    /**
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
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
     * @return the financeDAO
     */
    public FinanceDAO getFinanceDAO()
    {
        return financeDAO;
    }

    /**
     * @param financeDAO
     *            the financeDAO to set
     */
    public void setFinanceDAO(FinanceDAO financeDAO)
    {
        this.financeDAO = financeDAO;
    }

    /**
     * @return the paymentDAO
     */
    public PaymentDAO getPaymentDAO()
    {
        return paymentDAO;
    }

    /**
     * @param paymentDAO
     *            the paymentDAO to set
     */
    public void setPaymentDAO(PaymentDAO paymentDAO)
    {
        this.paymentDAO = paymentDAO;
    }

    /**
     * @return the parameterDAO
     */
    public ParameterDAO getParameterDAO()
    {
        return parameterDAO;
    }

    /**
     * @param parameterDAO
     *            the parameterDAO to set
     */
    public void setParameterDAO(ParameterDAO parameterDAO)
    {
        this.parameterDAO = parameterDAO;
    }

    /**
     * @return the inBillDAO
     */
    public InBillDAO getInBillDAO()
    {
        return inBillDAO;
    }

    /**
     * @param inBillDAO
     *            the inBillDAO to set
     */
    public void setInBillDAO(InBillDAO inBillDAO)
    {
        this.inBillDAO = inBillDAO;
    }

    /**
     * @return the outBillDAO
     */
    public OutBillDAO getOutBillDAO()
    {
        return outBillDAO;
    }

    /**
     * @param outBillDAO
     *            the outBillDAO to set
     */
    public void setOutBillDAO(OutBillDAO outBillDAO)
    {
        this.outBillDAO = outBillDAO;
    }

}
