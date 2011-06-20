/**
 * File Name: PaymentApplyListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import java.util.ArrayList;
import java.util.List;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.listener.PaymentApplyListener;
import com.china.center.oa.finance.vs.PaymentVSOutBean;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.constanst.TaxItemConstanst;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * TODO_OSGI 回款转预收/销售单绑定(预收转应收)/预收转费用
 * 
 * @author ZHUZHU
 * @version 2011-6-11
 * @see PaymentApplyListenerTaxGlueImpl
 * @since 3.0
 */
public class PaymentApplyListenerTaxGlueImpl implements PaymentApplyListener
{
    private DutyDAO dutyDAO = null;

    private DepartmentDAO departmentDAO = null;

    private TaxDAO taxDAO = null;

    private BankDAO bankDAO = null;

    private CommonDAO commonDAO = null;

    private ProviderDAO providerDAO = null;

    private StafferDAO stafferDAO = null;

    private FinanceManager financeManager = null;

    private FinanceDAO financeDAO = null;

    private PaymentDAO paymentDAO = null;

    private ParameterDAO parameterDAO = null;

    private InBillDAO inBillDAO = null;

    private OutBillDAO outBillDAO = null;

    private OutDAO outDAO = null;

    /**
     * default constructor
     */
    public PaymentApplyListenerTaxGlueImpl()
    {
    }

    /**
     * 回款转预收/销售单绑定(预收转应收)/坏账/预收转费用
     */
    public void onPassBean(User user, PaymentApplyBean apply)
        throws MYException
    {
        // 回款转预收 && 销售单和回款单关联
        if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_PAYMENT)
        {
            List<PaymentVSOutBean> vsList = apply.getVsList();

            // 这里是迭代循环,每一个单独生成凭证
            for (PaymentVSOutBean item : vsList)
            {
                // 回款转预收
                if (StringTools.isNullOrNone(item.getOutId()))
                {
                    // 每个预收都生成一个凭证,当然一般只有一个预收
                    paymentToPre(user, apply, item);
                }
                else
                {
                    // 回款转应收
                    paymentToPay(user, apply, item);
                }
            }

            PaymentBean payment = paymentDAO.find(apply.getPaymentId());

            if (payment == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            if ( !TaxGlueHelper.bankGoon(payment.getBankId(), this.taxDAO))
            {
                return;
            }

            // 手续费是一次性的
            if (payment.getHandling() > 0)
            {
                BankBean bank = bankDAO.find(payment.getBankId());

                if (bank == null)
                {
                    throw new MYException("银行不存在,请确认操作");
                }

                List<OutBillBean> outBillList = outBillDAO.queryEntityBeansByFK(payment.getId());

                for (OutBillBean outBillBean : outBillList)
                {
                    if (outBillBean.getType() == FinanceConstant.OUTBILL_TYPE_HANDLING)
                    {
                        // 第二个全凭证
                        secondFinance(user, apply, bank, payment, outBillBean);

                        return;
                    }
                }
            }
        }
        // 预收转费用
        else if (apply.getType() == FinanceConstant.PAYAPPLY_TYPE_CHANGEFEE)
        {
            List<PaymentVSOutBean> vsList = apply.getVsList();

            // 营业费用-运输费（负数）（5501-04）/预收账款
            for (PaymentVSOutBean item : vsList)
            {
                preToFee(user, apply, item);
            }
        }
        // 绑定销售单
        else
        {
            List<PaymentVSOutBean> vsList = apply.getVsList();

            // 这里是迭代循环,每一个单独生成凭证 预收账款/应收账款
            for (PaymentVSOutBean item : vsList)
            {
                preToPay(user, apply, item);
            }

            // 存在坏账
            if (apply.getBadMoney() > 0 && vsList.size() > 0)
            {
                OutBean outBean = outDAO.find(vsList.get(0).getOutId());

                if (outBean == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                mainFinanceInBadPay(user, apply, outBean);
            }
        }
    }

    /**
     * 回款转预(生成收款单和付款单)
     * 
     * @param user
     * @param apply
     * @param item
     * @throws MYException
     */
    private void paymentToPre(User user, PaymentApplyBean apply, PaymentVSOutBean item)
        throws MYException
    {
        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !TaxGlueHelper.bankGoon(payment.getBankId(), this.taxDAO))
        {
            return;
        }

        BankBean bank = bankDAO.find(payment.getBankId());

        if (bank == null)
        {
            throw new MYException("银行不存在,请确认操作");
        }

        List<InBillBean> inList = inBillDAO.queryEntityBeansByFK(payment.getId(),
            AnoConstant.FK_FIRST);

        if (ListTools.isEmptyOrNull(inList))
        {
            throw new MYException("缺少对应的收款单,请确认操作");
        }

        for (InBillBean inBillBean : inList)
        {
            // 预收
            if (inBillBean.getStatus() == FinanceConstant.INBILL_STATUS_NOREF)
            {
                // 第一个全凭证
                mainFinance(user, apply, item, payment, bank, inBillBean);

                return;
            }
        }

        throw new MYException("没有执行生成回款认领(转预收)凭证");
    }

    /**
     * 预收转应收（勾款/销售单绑定）-会计审核(预收账款/应收账款)
     * 
     * @param user
     * @param apply
     * @param item
     * @throws MYException
     */
    private void preToPay(User user, PaymentApplyBean apply, PaymentVSOutBean item)
        throws MYException
    {
        PaymentBean payment = paymentDAO.find(item.getPaymentId());

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !TaxGlueHelper.bankGoon(payment.getBankId(), this.taxDAO))
        {
            return;
        }

        BankBean bank = bankDAO.find(payment.getBankId());

        if (bank == null)
        {
            throw new MYException("银行不存在,请确认操作");
        }

        mainFinanceInPreToPay(user, apply, item, payment, bank);
    }

    /**
     * 预收转费用
     * 
     * @param user
     * @param apply
     * @param item
     * @throws MYException
     */
    private void preToFee(User user, PaymentApplyBean apply, PaymentVSOutBean item)
        throws MYException
    {
        String billId = item.getBillId();

        InBillBean bill = inBillDAO.find(billId);

        if (bill == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 这里的预收都是从回款单中出来的
        PaymentBean payment = paymentDAO.find(bill.getPaymentId());

        if (payment == null)
        {
            throw new MYException("预收不是从回款中来,数据错误,请确认操作");
        }

        if ( !TaxGlueHelper.bankGoon(bill.getBankId(), this.taxDAO))
        {
            return;
        }

        BankBean bank = bankDAO.find(bill.getBankId());

        if (bank == null)
        {
            throw new MYException("银行不存在,请确认操作");
        }

        mainFinanceInPreToFee(user, payment, apply, item, bill, bank);
    }

    /**
     * 回款转应收
     * 
     * @param user
     * @param apply
     * @param item
     * @throws MYException
     */
    private void paymentToPay(User user, PaymentApplyBean apply, PaymentVSOutBean item)
        throws MYException
    {
        PaymentBean payment = paymentDAO.find(apply.getPaymentId());

        if (payment == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !TaxGlueHelper.bankGoon(payment.getBankId(), this.taxDAO))
        {
            return;
        }

        BankBean bank = bankDAO.find(payment.getBankId());

        if (bank == null)
        {
            throw new MYException("银行不存在,请确认操作");
        }

        List<InBillBean> inList = inBillDAO.queryEntityBeansByFK(payment.getId(),
            AnoConstant.FK_FIRST);

        if (ListTools.isEmptyOrNull(inList))
        {
            throw new MYException("缺少对应的收款单,请确认操作");
        }

        for (InBillBean inBillBean : inList)
        {
            // 销售单已收(包括委托代销的也在内啊)
            if (inBillBean.getStatus() == FinanceConstant.INBILL_STATUS_PAYMENTS
                && item.getOutId().equals(inBillBean.getOutId()))
            {
                // 全凭证
                mainFinanceInOut(user, apply, item, payment, bank, inBillBean);

                return;
            }
        }

        throw new MYException("没有执行生成回款认领(销售单关联)凭证");
    }

    /**
     * 第一个全凭证
     * 
     * @param user
     * @param apply
     * @param item
     * @param payment
     * @param bank
     * @param inBillBean
     * @throws MYException
     */
    private void mainFinance(User user, PaymentApplyBean apply, PaymentVSOutBean item,
                             PaymentBean payment, BankBean bank, InBillBean inBillBean)
        throws MYException
    {
        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "通过回款认领(转预收):" + apply.getPaymentId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BILL_GETPAY);

        // 这里也是关联的回款单号
        financeBean.setRefId(apply.getPaymentId());

        financeBean.setRefBill(inBillBean.getId());

        financeBean.setDutyId(bank.getDutyId());

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 借:银行科目 贷:银行对应的暂记户科目
        createAddItem1(user, payment, bank, apply, inBillBean, item, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 和销售单关联生成的凭证(银行对应的暂记户科目（没有手续费）/应收账款)
     * 
     * @param user
     * @param apply
     * @param item
     * @param payment
     * @param bank
     * @param inBillBean
     * @throws MYException
     */
    private void mainFinanceInOut(User user, PaymentApplyBean apply, PaymentVSOutBean item,
                                  PaymentBean payment, BankBean bank, InBillBean inBillBean)
        throws MYException
    {
        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "通过回款认领(销售单关联):" + apply.getPaymentId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BILL_GETPAY);

        // 这里也是关联的回款单号
        financeBean.setRefId(apply.getPaymentId());

        financeBean.setRefBill(inBillBean.getId());

        financeBean.setRefOut(item.getOutId());

        financeBean.setDutyId(bank.getDutyId());

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 银行对应的暂记户科目/应收账款
        createAddItem3(user, payment, bank, apply, inBillBean, item, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 预收转应收（勾款/销售单绑定）-会计审核
     * 
     * @param user
     * @param apply
     * @param item
     * @param payment
     * @param bank
     * @throws MYException
     */
    private void mainFinanceInPreToPay(User user, PaymentApplyBean apply, PaymentVSOutBean item,
                                       PaymentBean payment, BankBean bank)
        throws MYException
    {
        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "通过预收转应收(销售单关联):" + item.getPaymentId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BILL_MAYTOREAL);

        // 这里也是关联的回款单号
        financeBean.setRefId(item.getPaymentId());

        financeBean.setRefBill(item.getBillId());

        financeBean.setRefOut(item.getOutId());

        financeBean.setDutyId(bank.getDutyId());

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 预收账款/应收账款
        createAddItem4(user, payment, bank, apply, item, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 预收转费用(此单据全转)
     * 
     * @param user
     * @param apply
     * @param item
     * @param bill
     * @param bank
     * @throws MYException
     */
    private void mainFinanceInPreToFee(User user, PaymentBean payment, PaymentApplyBean apply,
                                       PaymentVSOutBean item, InBillBean bill, BankBean bank)
        throws MYException
    {
        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "通过预收转费用:" + item.getPaymentId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BILL_TOFEE);

        // 这里也是关联的回款单号
        financeBean.setRefId(payment.getId());

        financeBean.setRefBill(item.getBillId());

        financeBean.setDutyId(bank.getDutyId());

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 营业费用-运输费（负数）（5501-04）/预收账款
        createAddItem7(user, payment, bank, bill, apply, item, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 其它应收-坏账(1133-16)/应收账款
     * 
     * @param user
     * @param apply
     * @param item
     * @param payment
     * @param bank
     * @throws MYException
     */
    private void mainFinanceInBadPay(User user, PaymentApplyBean apply, OutBean outBean)
        throws MYException
    {
        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "通过预收转应收中确认坏账(销售单关联):" + outBean.getFullId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_SAIL_BADMONEY);

        // 这里也是关联的回款单号
        financeBean.setRefId(outBean.getFullId());

        financeBean.setRefOut(outBean.getFullId());

        financeBean.setDutyId(outBean.getDutyId());

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 其它应收-坏账(1133-16)/应收账款
        createAddItem5(user, apply, outBean, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 第二个全凭证
     * 
     * @param user
     * @param apply
     * @param item
     * @param payment
     * @param bank
     * @param inBillBean
     * @throws MYException
     */
    private void secondFinance(User user, PaymentApplyBean apply, BankBean bank,
                               PaymentBean payment, OutBillBean outBillBean)
        throws MYException
    {
        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "通过回款认领(手续费):" + apply.getPaymentId() + '.';

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_BILL_GETPAY);

        // 这里也是关联的回款单号
        financeBean.setRefId(apply.getPaymentId());

        // 手续费的ID
        financeBean.setRefBill(outBillBean.getId());

        financeBean.setDutyId(bank.getDutyId());

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        financeBean.setFinanceDate(TimeTools.now_short());

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        createAddItem2(user, payment, bank, apply, financeBean, itemList);

        financeBean.setItemList(itemList);

        financeManager.addFinanceBeanWithoutTransactional(user, financeBean);
    }

    /**
     * 银行对应的暂记户科目（没有手续费）/(1)应收账款(2)预收账款
     * 
     * @param user
     * @param bean
     * @param bank
     * @param apply
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem1(User user, PaymentBean bean, BankBean bank, PaymentApplyBean apply,
                                InBillBean inBillBean, PaymentVSOutBean item,
                                FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = user.getStafferName() + "通过回款认领:" + apply.getPaymentId() + '.';

        // 银行对应的暂记户科目（没有手续费）/(1)应收账款(2)预收账款
        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setName("银行暂记户:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        TaxBean inTax = taxDAO.findTempByBankId(bank.getId());

        if (inTax == null)
        {
            throw new MYException("银行[%s]缺少对应的暂记户科目,请确认操作", bank.getName());
        }

        // 科目拷贝
        FinanceHelper.copyTax(inTax, itemIn);

        // 当前发生额
        double inMoney = item.getMoneys();

        itemIn.setInmoney(FinanceHelper.doubleToLong(inMoney));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 NA
        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("预收账款:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        // 预收账款(客户/职员/部门)
        TaxBean outTax = taxDAO.findByUnique(TaxItemConstanst.PREREVEIVE_PRODUCT);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 申请人
        StafferBean staffer = stafferDAO.find(apply.getStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        double outMoney = item.getMoneys();

        itemOut.setInmoney(0);

        itemOut.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut.setDescription(itemOut.getName());

        // 辅助核算 客户/职员/部门
        itemOut.setDepartmentId(staffer.getPrincipalshipId());
        itemOut.setStafferId(apply.getStafferId());
        itemOut.setUnitId(apply.getCustomerId());
        itemOut.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemOut);
    }

    /**
     * 过回款认领(销售单关联):银行对应的暂记户科目（没有手续费）/应收账款
     * 
     * @param user
     * @param bean
     * @param bank
     * @param apply
     * @param inBillBean
     * @param item
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem3(User user, PaymentBean bean, BankBean bank, PaymentApplyBean apply,
                                InBillBean inBillBean, PaymentVSOutBean item,
                                FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {
        String name = user.getStafferName() + "通过回款认领(销售单关联):" + apply.getPaymentId() + '.';

        // 银行对应的暂记户科目（没有手续费）/应收账款
        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setName("银行暂记户:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        TaxBean inTax = taxDAO.findTempByBankId(bank.getId());

        if (inTax == null)
        {
            throw new MYException("银行[%s]缺少对应的暂记户科目,请确认操作", bank.getName());
        }

        // 科目拷贝
        FinanceHelper.copyTax(inTax, itemIn);

        // 当前发生额
        double inMoney = item.getMoneys();

        itemIn.setInmoney(FinanceHelper.doubleToLong(inMoney));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 NA
        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("应收账款:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        // 应收账款(客户/职员/部门)
        TaxBean outTax = taxDAO.findByUnique(TaxItemConstanst.REVEIVE_PRODUCT);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 申请人
        StafferBean staffer = stafferDAO.find(apply.getStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        double outMoney = item.getMoneys();

        itemOut.setInmoney(0);

        itemOut.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut.setDescription(itemOut.getName());

        // 辅助核算 客户/职员/部门
        itemOut.setDepartmentId(staffer.getPrincipalshipId());
        itemOut.setStafferId(apply.getStafferId());
        itemOut.setUnitId(apply.getCustomerId());
        itemOut.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemOut);
    }

    /**
     * 预收账款/应收账款
     * 
     * @param user
     * @param bean
     * @param bank
     * @param apply
     * @param item
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem4(User user, PaymentBean bean, BankBean bank, PaymentApplyBean apply,
                                PaymentVSOutBean item, FinanceBean financeBean,
                                List<FinanceItemBean> itemList)
        throws MYException
    {
        // 申请人
        StafferBean staffer = stafferDAO.find(apply.getStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        OutBean outBean = outDAO.find(item.getOutId());

        if (outBean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        String name = user.getStafferName() + "通过预收转应收(销售单关联):" + item.getPaymentId() + '.';

        // 银行对应的暂记户科目（没有手续费）/应收账款
        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setName("预收账款:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        TaxBean inTax = taxDAO.findByUnique(TaxItemConstanst.PREREVEIVE_PRODUCT);

        if (inTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(inTax, itemIn);

        // 当前发生额
        double inMoney = item.getMoneys();

        itemIn.setInmoney(FinanceHelper.doubleToLong(inMoney));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 客户/职员/部门
        itemIn.setDepartmentId(staffer.getPrincipalshipId());
        itemIn.setStafferId(apply.getStafferId());
        itemIn.setUnitId(outBean.getCustomerId());
        itemIn.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("应收账款:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        // 应收账款(客户/职员/部门)
        TaxBean outTax = taxDAO.findByUnique(TaxItemConstanst.REVEIVE_PRODUCT);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        double outMoney = item.getMoneys();

        itemOut.setInmoney(0);

        itemOut.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut.setDescription(itemOut.getName());

        // 辅助核算 客户/职员/部门
        itemOut.setDepartmentId(staffer.getPrincipalshipId());
        itemOut.setStafferId(apply.getStafferId());
        itemOut.setUnitId(outBean.getCustomerId());
        itemOut.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemOut);
    }

    /**
     * 其它应收-坏账(1133-16)/应收账款
     * 
     * @param user
     * @param bean
     * @param bank
     * @param apply
     * @param item
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem5(User user, PaymentApplyBean apply, OutBean outBean,
                                FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {
        // 申请人
        StafferBean staffer = stafferDAO.find(apply.getStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addIntCondition("createType", "=", TaxConstanst.FINANCE_CREATETYPE_SAIL_BADMONEY);

        condition.addCondition("refId", "=", outBean.getFullId());

        List<FinanceBean> oldFinanceList = financeDAO.queryEntityBeansByCondition(condition);

        // 当天已经存在的坏账
        long bad = 0L;

        for (FinanceBean each : oldFinanceList)
        {
            bad += each.getInmoney();
        }

        if (bad != 0)
        {
            // 其它应收-坏账（负数）/应收账款（负数）
            createAddItem6(user, apply, outBean, financeBean, itemList, bad);
        }

        String name = user.getStafferName() + "通过预收转应收中确认坏账(销售单关联):" + outBean.getFullId() + '.';

        // 借方
        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setName("其它应收-坏账:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        TaxBean inTax = taxDAO.findByUnique(TaxItemConstanst.BAD_REVEIVE_PRODUCT);

        if (inTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(inTax, itemIn);

        // 当前发生额
        double inMoney = apply.getBadMoney();

        itemIn.setInmoney(FinanceHelper.doubleToLong(inMoney));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 客户/职员/部门
        itemIn.setDepartmentId(staffer.getPrincipalshipId());
        itemIn.setStafferId(apply.getStafferId());
        itemIn.setUnitId(outBean.getCustomerId());
        itemIn.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("应收账款:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        // 应收账款(客户/职员/部门)
        TaxBean outTax = taxDAO.findByUnique(TaxItemConstanst.REVEIVE_PRODUCT);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        double outMoney = apply.getBadMoney();

        itemOut.setInmoney(0);

        itemOut.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut.setDescription(itemOut.getName());

        // 辅助核算 客户/职员/部门
        itemOut.setDepartmentId(staffer.getPrincipalshipId());
        itemOut.setStafferId(apply.getStafferId());
        itemOut.setUnitId(outBean.getCustomerId());
        itemOut.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemOut);
    }

    /**
     * 先取消坏账
     * 
     * @param user
     * @param apply
     * @param outBean
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem6(User user, PaymentApplyBean apply, OutBean outBean,
                                FinanceBean financeBean, List<FinanceItemBean> itemList, long bad)
        throws MYException
    {
        // 申请人
        StafferBean staffer = stafferDAO.find(apply.getStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        String name = "坏账生成前先取消已经存在的坏账:" + outBean.getFullId() + '.';

        // 借方
        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setName("其它应收-坏账:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        TaxBean inTax = taxDAO.findByUnique(TaxItemConstanst.BAD_REVEIVE_PRODUCT);

        if (inTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(inTax, itemIn);

        itemIn.setInmoney( -bad);

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 客户/职员/部门
        itemIn.setDepartmentId(staffer.getPrincipalshipId());
        itemIn.setStafferId(apply.getStafferId());
        itemIn.setUnitId(outBean.getCustomerId());
        itemIn.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("应收账款:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        // 应收账款(客户/职员/部门)
        TaxBean outTax = taxDAO.findByUnique(TaxItemConstanst.REVEIVE_PRODUCT);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        itemOut.setInmoney(0);

        itemOut.setOutmoney( -bad);

        itemOut.setDescription(itemOut.getName());

        // 辅助核算 客户/职员/部门
        itemOut.setDepartmentId(staffer.getPrincipalshipId());
        itemOut.setStafferId(apply.getStafferId());
        itemOut.setUnitId(outBean.getCustomerId());
        itemOut.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemOut);
    }

    /**
     * 营业费用-运输费（负数）（5501-04）/预收账款
     * 
     * @param user
     * @param bean
     * @param bank
     * @param apply
     * @param item
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem7(User user, PaymentBean bean, BankBean bank, InBillBean bill,
                                PaymentApplyBean apply, PaymentVSOutBean item,
                                FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {
        // 申请人
        StafferBean staffer = stafferDAO.find(apply.getStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        String name = user.getStafferName() + "通过预收转费用:" + bean.getId() + '.';

        // 银行对应的暂记户科目（没有手续费）/应收账款
        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setName("营业费用-运输费:" + name);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        TaxBean inTax = taxDAO.findByUnique(TaxItemConstanst.TRAN_FEE);

        if (inTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(inTax, itemIn);

        // 当前发生额
        double inMoney = -bill.getMoneys();

        itemIn.setInmoney(FinanceHelper.doubleToLong(inMoney));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 部门/职员
        itemIn.setDepartmentId(staffer.getPrincipalshipId());
        itemIn.setStafferId(apply.getStafferId());

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("预收账款:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        // 应收账款(客户/职员/部门)
        TaxBean outTax = taxDAO.findByUnique(TaxItemConstanst.PREREVEIVE_PRODUCT);

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        double outMoney = -bill.getMoneys();

        itemOut.setInmoney(0);

        itemOut.setOutmoney(FinanceHelper.doubleToLong(outMoney));

        itemOut.setDescription(itemOut.getName());

        // 辅助核算 客户/职员/部门
        itemOut.setDepartmentId(staffer.getPrincipalshipId());
        itemOut.setStafferId(apply.getStafferId());
        itemOut.setUnitId(bill.getCustomerId());
        itemOut.setUnitType(TaxConstanst.UNIT_TYPE_CUSTOMER);

        itemList.add(itemOut);
    }

    /**
     * 营业费用-部门手续费（回款超过1万）/银行对应的暂记户科目（手续费）
     * 
     * @param user
     * @param bean
     * @param bank
     * @param apply
     * @param financeBean
     * @param itemList
     * @throws MYException
     */
    private void createAddItem2(User user, PaymentBean bean, BankBean bank, PaymentApplyBean apply,
                                FinanceBean financeBean, List<FinanceItemBean> itemList)
        throws MYException
    {
        // 申请人
        StafferBean staffer = stafferDAO.find(apply.getStafferId());

        if (staffer == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        String name = user.getStafferName() + "通过回款认领(手续费):" + apply.getPaymentId() + '.';

        // 临界值
        int maxFee = parameterDAO.getInt(SysConfigConstant.MAX_RECVIVE_FEE);

        // 银行对应的暂记户科目（没有手续费）/(1)应收账款(2)预收账款
        FinanceItemBean itemIn = new FinanceItemBean();

        String pareId = commonDAO.getSquenceString();

        itemIn.setPareId(pareId);

        itemIn.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemIn);

        String itemInTaxId = "";

        if (bean.getMoney() >= maxFee)
        {
            itemIn.setName("部门手续费:" + name);
            itemInTaxId = TaxItemConstanst.HAND_BANK_DEPARTMENT;
        }
        else
        {
            itemIn.setName("个人手续费:" + name);
            itemInTaxId = TaxItemConstanst.HAND_BANK_PERSON;
        }

        TaxBean itemInTax = taxDAO.findByUnique(itemInTaxId);

        if (itemInTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(itemInTax, itemIn);

        double inMoney = bean.getHandling();

        itemIn.setInmoney(FinanceHelper.doubleToLong(inMoney));

        itemIn.setOutmoney(0);

        itemIn.setDescription(itemIn.getName());

        // 辅助核算 职员/部门
        itemIn.setDepartmentId(staffer.getPrincipalshipId());
        itemIn.setStafferId(apply.getStafferId());

        itemList.add(itemIn);

        // 贷方
        FinanceItemBean itemOut = new FinanceItemBean();

        itemOut.setPareId(pareId);

        itemOut.setName("银行暂记户:" + name);

        itemOut.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOut);

        TaxBean outTax = taxDAO.findTempByBankId(bank.getId());

        if (outTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 科目拷贝
        FinanceHelper.copyTax(outTax, itemOut);

        double outMoney = bean.getHandling();

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
        return "PaymentApplyListener.TaxGlueImpl";
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

}
