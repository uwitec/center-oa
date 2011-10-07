/**
 * File Name: FinanceManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.manager.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.iaop.annotation.IntegrationAOP;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.IDPrefixConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.tax.bean.CheckViewBean;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.FinanceMonthBean;
import com.china.center.oa.tax.bean.FinanceTurnBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.constanst.CheckConstant;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.constanst.TaxItemConstanst;
import com.china.center.oa.tax.dao.CheckViewDAO;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.dao.FinanceMonthDAO;
import com.china.center.oa.tax.dao.FinanceTurnDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.helper.TaxHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.oa.tax.vo.FinanceTurnVO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * FinanceManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-2-7
 * @see FinanceManagerImpl
 * @since 1.0
 */
@IntegrationAOP
public class FinanceManagerImpl implements FinanceManager
{
    private final Log operationLog = LogFactory.getLog("opr");

    private FinanceDAO financeDAO = null;

    private CommonDAO commonDAO = null;

    private CheckViewDAO checkViewDAO = null;

    private FinanceItemDAO financeItemDAO = null;

    private TaxDAO taxDAO = null;

    private FinanceTurnDAO financeTurnDAO = null;

    private FinanceMonthDAO financeMonthDAO = null;

    private BillManager billManager = null;

    /**
     * 是否锁定凭证增加
     */
    private static boolean LOCK_FINANCE = false;

    /**
     * default constructor
     */
    public FinanceManagerImpl()
    {
    }

    public boolean addFinanceBeanWithoutTransactional(User user, FinanceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getItemList());

        bean.setId(commonDAO.getSquenceString20(IDPrefixConstant.ID_FINANCE_PREFIX));

        bean.setName(bean.getId());

        bean.setCreaterId(user.getStafferId());

        // 允许自己制定凭证日期
        if (StringTools.isNullOrNone(bean.getFinanceDate()))
        {
            bean.setFinanceDate(TimeTools.now_short());
        }

        checkTime(bean);

        // 入库时间
        bean.setLogTime(TimeTools.now());

        // 默认纳税实体
        if (bean.getType() == TaxConstanst.FINANCE_TYPE_MANAGER && StringTools.isNullOrNone(bean.getDutyId()))
        {
            bean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);
        }

        if (bean.getType() == TaxConstanst.FINANCE_TYPE_DUTY && StringTools.isNullOrNone(bean.getDutyId()))
        {
            throw new MYException("税务凭证必须有纳税实体的属性");
        }

        List<FinanceItemBean> itemList = bean.getItemList();

        boolean isTurn = FinanceHelper.isTurnFinance(itemList);

        if (isLOCK_FINANCE() && !isTurn)
        {
            throw new MYException("被锁定结转,不能增加凭证");
        }

        Map<String, List<FinanceItemBean>> pareMap = new HashMap<String, List<FinanceItemBean>>();

        long inTotal = 0;

        long outTotal = 0;

        // 整理出凭证对(且校验凭证的合法性)
        for (FinanceItemBean financeItemBean : itemList)
        {
            financeItemBean.setId(commonDAO.getSquenceString20());

            financeItemBean.setFinanceDate(bean.getFinanceDate());

            financeItemBean.setName(financeItemBean.getId());

            FinanceHelper.copyFinanceItem(bean, financeItemBean);

            financeItemBean.setPid(bean.getId());

            String taxId = financeItemBean.getTaxId();

            if (StringTools.isNullOrNone(taxId))
            {
                throw new MYException("缺少科目信息,请确认操作");
            }

            TaxBean tax = taxDAO.find(taxId);

            if (tax == null)
            {
                throw new MYException("科目不存在,请确认操作");
            }

            // 必须是最小科目哦
            if (tax.getBottomFlag() != TaxConstanst.TAX_BOTTOMFLAG_ITEM)
            {
                throw new MYException("[%s]科目必须是最小科目,请确认操作", tax.getName() + tax.getId());
            }

            // 不是结转需要检查辅助核算项
            if ( !isTurn)
            {
                checkItem(financeItemBean, tax);
            }

            // 拷贝凭证的父级ID
            TaxHelper.copyParent(financeItemBean, tax);

            String key = financeItemBean.getPareId();

            if (pareMap.get(key) == null)
            {
                pareMap.put(key, new ArrayList<FinanceItemBean>());
            }

            pareMap.get(key).add(financeItemBean);

            // 必须有一个为0
            if (financeItemBean.getInmoney() * financeItemBean.getOutmoney() != 0)
            {
                throw new MYException("借方金额或者贷方金额不能都不为0");
            }

            inTotal += financeItemBean.getInmoney();

            outTotal += financeItemBean.getOutmoney();
        }

        bean.setInmoney(inTotal);

        bean.setOutmoney(outTotal);

        if (inTotal != outTotal)
        {
            throw new MYException("总借[%s],总贷[%s]不等,凭证增加错误", FinanceHelper.longToString(inTotal),
                FinanceHelper.longToString(outTotal));
        }

        // CORE 核对借贷必相等的原则
        checkPare(pareMap);

        financeItemDAO.saveAllEntityBeans(itemList);

        financeDAO.saveEntityBean(bean);

        // 手工增加增加成功后需要更新
        if (bean.getCreateType() == TaxConstanst.FINANCE_CREATETYPE_HAND && !StringTools.isNullOrNone(bean.getRefId()))
        {
            billManager.updateBillBeanChecksWithoutTransactional(user, bean.getRefId(),
                "增加手工凭证自动更新收款单核对状态:" + FinanceHelper.createFinanceLink(bean.getId()));
        }

        return true;
    }

    /**
     * 结转
     */
    @IntegrationAOP(auth = AuthConstant.FINANCE_TURN, lock = TaxConstanst.FINANCETURN_OPR_LOCK)
    @Transactional(rollbackFor = MYException.class)
    public boolean addFinanceTurnBean(User user, FinanceTurnBean bean)
        throws MYException
    {
        try
        {
            JudgeTools.judgeParameterIsNull(user, bean);

            // 锁定
            setLOCK_FINANCE(true);

            // 时间校验
            checkTurnTime(bean);

            bean.setId(commonDAO.getSquenceString20());

            bean.setStafferId(user.getStafferId());

            String changeFormat = TimeTools.changeFormat(bean.getMonthKey(), "yyyyMM", "yyyy-MM");

            // 设置起止时间
            bean.setStartTime(changeFormat + "-01 00:00:00");
            bean.setEndTime(changeFormat + "-31 23:59:59");

            // 损益结转
            List<FinanceItemBean> itemList = createTurnFinance(user, bean, changeFormat);

            // 利润结转
            createProfitFinance(user, bean, changeFormat, itemList);

            // 产生月结数据(科目/借总额/贷总额/KEY/LOGTIME)
            createMonthData(user, bean, changeFormat);

            // 锁定凭证,不能修改和删除
            int amount = financeDAO.updateLockToEnd(changeFormat + "-01", changeFormat + "-31");

            bean.setAmount(amount);

            bean.setLogTime(TimeTools.now());

            // 保存后本月的凭证就不能增加了
            financeTurnDAO.saveEntityBean(bean);

            operationLog.info(user.getStafferName() + "进行了" + bean.getMonthKey() + "的结转(操作成功)");

            return true;
        }
        finally
        {
            // 解锁
            setLOCK_FINANCE(false);
        }
    }

    /**
     * 产生月结数据
     * 
     * @param user
     * @param bean
     * @param changeFormat
     */
    private void createMonthData(User user, FinanceTurnBean bean, String changeFormat)
    {
        List<TaxBean> taxList = taxDAO.listEntityBeans("order by id");

        for (TaxBean taxBean : taxList)
        {
            ConditionParse condition = new ConditionParse();

            condition.addWhereStr();

            condition.addCondition("financeDate", ">=", changeFormat + "-01");
            condition.addCondition("financeDate", "<=", changeFormat + "-31");

            // 所有的科目都月结
            condition.addCondition("taxId" + taxBean.getLevel(), "=", taxBean.getId());

            long inMonetTotal = financeItemDAO.sumInByCondition(condition);

            long outMonetTotal = financeItemDAO.sumOutByCondition(condition);

            FinanceMonthBean fmb = new FinanceMonthBean();

            fmb.setId(commonDAO.getSquenceString20());

            fmb.setStafferId(user.getStafferId());

            fmb.setMonthKey(bean.getMonthKey());

            FinanceHelper.copyTax(taxBean, fmb);

            fmb.setInmoneyTotal(inMonetTotal);

            fmb.setOutmoneyTotal(outMonetTotal);

            if (taxBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                fmb.setLastTotal(inMonetTotal - outMonetTotal);
            }
            else
            {
                fmb.setLastTotal(outMonetTotal - inMonetTotal);
            }

            // 获取最近的一个月
            FinanceTurnVO lastTurn = financeTurnDAO.findLastVO();

            FinanceMonthBean lastMonth = financeMonthDAO.findByUnique(taxBean.getId(), lastTurn.getMonthKey());

            if (lastMonth != null)
            {
                // 累加之前所有的值
                fmb.setInmoneyAllTotal(lastMonth.getInmoneyAllTotal() + inMonetTotal);

                fmb.setOutmoneyAllTotal(lastMonth.getOutmoneyAllTotal() + outMonetTotal);
            }
            else
            {
                // 没有作为初始值
                fmb.setInmoneyAllTotal(inMonetTotal);

                fmb.setOutmoneyAllTotal(outMonetTotal);
            }

            if (taxBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                fmb.setLastAllTotal(fmb.getInmoneyAllTotal() - fmb.getOutmoneyAllTotal());
            }
            else
            {
                fmb.setLastAllTotal(fmb.getOutmoneyAllTotal() - fmb.getInmoneyAllTotal());
            }

            fmb.setLogTime(TimeTools.now());

            financeMonthDAO.saveEntityBean(fmb);
        }
    }

    /**
     * checkTurnTime
     * 
     * @param bean
     * @throws MYException
     */
    private void checkTurnTime(FinanceTurnBean bean)
        throws MYException
    {
        FinanceTurnVO topTurn = financeTurnDAO.findLastVO();

        if (topTurn != null)
        {
            // 2011017
            String monthKey = topTurn.getMonthKey();

            String nextKey = TimeTools.getStringByOrgAndDaysAndFormat(monthKey, 32, "yyyyMM");

            if ( !nextKey.equals(bean.getMonthKey()))
            {
                throw new MYException("上次结转的是[%s],本次结转的是[%s],应该结转的是[%s],请确认操作", monthKey, bean.getMonthKey(), nextKey);
            }
        }

        // 结转时间不能小于当前时间
        String changeFormat = TimeTools.changeFormat(bean.getMonthKey(), "yyyyMM", "yyyy-MM");

        String monthEnd = TimeTools.getMonthEnd(changeFormat + "-01");

        if (TimeTools.now_short().compareTo(monthEnd) < 0)
        {
            throw new MYException("结转只能在月末或者下月发生,不能提前结转");
        }
    }

    /**
     * 结转凭证
     * 
     * @param user
     * @param bean
     * @param changeFormat
     * @throws MYException
     */
    private List<FinanceItemBean> createTurnFinance(User user, FinanceTurnBean bean, String changeFormat)
        throws MYException
    {
        // 产生凭证(结转/利润结转)
        List<TaxBean> taxList = taxDAO.queryEntityBeansByFK(TaxConstanst.TAX_PTYPE_LOSS);

        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "损益结转" + bean.getMonthKey();

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_TURN);

        financeBean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        // 这里的日期是本月最后一天哦
        financeBean.setFinanceDate(TimeTools.getMonthEnd(changeFormat + "-01"));

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        String pare = commonDAO.getSquenceString();

        // 本年利润
        String itemTaxId = TaxItemConstanst.YEAR_PROFIT;

        TaxBean yearTax = taxDAO.findByUnique(itemTaxId);

        if (yearTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        for (TaxBean taxBean : taxList)
        {
            if (taxBean.getBottomFlag() == TaxConstanst.TAX_BOTTOMFLAG_ROOT)
            {
                continue;
            }

            ConditionParse condition = new ConditionParse();

            condition.addWhereStr();

            condition.addCondition("financeDate", ">=", changeFormat + "-01");
            condition.addCondition("financeDate", "<=", changeFormat + "-31");

            condition.addCondition("taxId", "=", taxBean.getId());

            long inMonetTotal = financeItemDAO.sumInByCondition(condition);

            long outMonetTotal = financeItemDAO.sumOutByCondition(condition);

            if (inMonetTotal == 0 && outMonetTotal == 0)
            {
                continue;
            }

            // 空的删除
            if ( (inMonetTotal == outMonetTotal) && (itemList.size() != 0))
            {
                continue;
            }

            // 借方(损益每月都结转的,所以损益的期初都是0)(借:本年利润 贷损益,这里的辅助核算型可以为空的)
            if (taxBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                String eachName = "借:本年利润,贷:" + taxBean.getName();

                // 借:本年利润 贷:损益科目
                FinanceItemBean itemInEach = new FinanceItemBean();

                itemInEach.setPareId(pare);

                itemInEach.setName(eachName);

                itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

                FinanceHelper.copyFinanceItem(financeBean, itemInEach);

                // 科目拷贝
                FinanceHelper.copyTax(yearTax, itemInEach);

                itemInEach.setInmoney(inMonetTotal - outMonetTotal);

                itemInEach.setOutmoney(0);

                itemInEach.setDescription(eachName);

                itemList.add(itemInEach);

                FinanceItemBean itemOutEach = new FinanceItemBean();

                itemOutEach.setPareId(pare);

                itemOutEach.setName(eachName);

                itemOutEach.setForward(TaxConstanst.TAX_FORWARD_OUT);

                FinanceHelper.copyFinanceItem(financeBean, itemOutEach);

                // 科目拷贝
                FinanceHelper.copyTax(taxBean, itemOutEach);

                itemOutEach.setInmoney(0);

                itemOutEach.setOutmoney(inMonetTotal - outMonetTotal);

                itemOutEach.setDescription(eachName);

                itemList.add(itemOutEach);
            }
            else
            {
                String eachName = "借:" + taxBean.getName() + ",贷:本年利润";

                // 借:损益科目 贷:本年利润
                FinanceItemBean itemInEach = new FinanceItemBean();

                itemInEach.setPareId(pare);

                itemInEach.setName(eachName);

                itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

                FinanceHelper.copyFinanceItem(financeBean, itemInEach);

                // 科目拷贝
                FinanceHelper.copyTax(taxBean, itemInEach);

                itemInEach.setInmoney(outMonetTotal - inMonetTotal);

                itemInEach.setOutmoney(0);

                itemInEach.setDescription(eachName);

                itemList.add(itemInEach);

                // 贷方
                FinanceItemBean itemOutEach = new FinanceItemBean();

                itemOutEach.setPareId(pare);

                itemOutEach.setName(eachName);

                itemOutEach.setForward(TaxConstanst.TAX_FORWARD_OUT);

                FinanceHelper.copyFinanceItem(financeBean, itemOutEach);

                // 科目拷贝
                FinanceHelper.copyTax(yearTax, itemOutEach);

                itemOutEach.setInmoney(0);

                itemOutEach.setOutmoney(outMonetTotal - inMonetTotal);

                itemOutEach.setDescription(eachName);

                itemList.add(itemOutEach);
            }
        }

        financeBean.setItemList(itemList);

        // 入库
        addFinanceBeanWithoutTransactional(user, financeBean);

        return itemList;
    }

    /**
     * 利润结转
     * 
     * @param user
     * @param bean
     * @param changeFormat
     * @return
     * @throws MYException
     */
    private List<FinanceItemBean> createProfitFinance(User user, FinanceTurnBean bean, String changeFormat,
                                                      List<FinanceItemBean> itemNearList)
        throws MYException
    {
        // 利润结转
        long profit = 0L;

        for (FinanceItemBean financeItemBean : itemNearList)
        {
            if (financeItemBean.getTaxId().equals(TaxItemConstanst.YEAR_PROFIT)
                && financeItemBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                profit = profit - financeItemBean.getInmoney();
            }

            if (financeItemBean.getTaxId().equals(TaxItemConstanst.YEAR_PROFIT)
                && financeItemBean.getForward() == TaxConstanst.TAX_FORWARD_OUT)
            {
                profit = profit + financeItemBean.getInmoney();
            }
        }

        FinanceBean financeBean = new FinanceBean();

        String name = user.getStafferName() + "利润结转:" + bean.getMonthKey();

        financeBean.setName(name);

        financeBean.setType(TaxConstanst.FINANCE_TYPE_MANAGER);

        financeBean.setCreateType(TaxConstanst.FINANCE_CREATETYPE_PROFIT);

        financeBean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);

        financeBean.setCreaterId(user.getStafferId());

        financeBean.setDescription(financeBean.getName());

        // 这里的日期是本月最后一天哦
        financeBean.setFinanceDate(TimeTools.getMonthEnd(changeFormat + "-01"));

        financeBean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        // 凭证对
        String pare = commonDAO.getSquenceString();

        // 本年利润
        String itemTaxId = TaxItemConstanst.YEAR_PROFIT;

        TaxBean yearTax = taxDAO.findByUnique(itemTaxId);

        if (yearTax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 未分配利润
        TaxBean noProfit = taxDAO.findByUnique(TaxItemConstanst.NO_PROFIT);

        if (noProfit == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        String eachName = "借:本年利润,贷:未分配利润";

        // 借:本年利润 贷:损益科目
        FinanceItemBean itemInEach = new FinanceItemBean();

        itemInEach.setPareId(pare);

        itemInEach.setName(eachName);

        itemInEach.setForward(TaxConstanst.TAX_FORWARD_IN);

        FinanceHelper.copyFinanceItem(financeBean, itemInEach);

        // 科目拷贝
        FinanceHelper.copyTax(yearTax, itemInEach);

        itemInEach.setInmoney(profit);

        itemInEach.setOutmoney(0);

        itemInEach.setDescription(eachName);

        itemList.add(itemInEach);

        FinanceItemBean itemOutEach = new FinanceItemBean();

        itemOutEach.setPareId(pare);

        itemOutEach.setName(eachName);

        itemOutEach.setForward(TaxConstanst.TAX_FORWARD_OUT);

        FinanceHelper.copyFinanceItem(financeBean, itemOutEach);

        // 科目拷贝
        FinanceHelper.copyTax(noProfit, itemOutEach);

        itemOutEach.setInmoney(0);

        itemOutEach.setOutmoney(profit);

        itemOutEach.setDescription(eachName);

        itemList.add(itemOutEach);

        financeBean.setItemList(itemList);

        // 入库
        addFinanceBeanWithoutTransactional(user, financeBean);

        return itemList;
    }

    /**
     * checkItem
     * 
     * @param financeItemBean
     * @param tax
     * @throws MYException
     */
    private void checkItem(FinanceItemBean financeItemBean, TaxBean tax)
        throws MYException
    {
        if (tax.getUnit() == TaxConstanst.TAX_CHECK_YES && StringTools.isNullOrNone(financeItemBean.getUnitId()))
        {
            throw new MYException("科目[%s]下辅助核算型-单位必须存在,请确认操作", tax.getName());
        }

        if (tax.getDepartment() == TaxConstanst.TAX_CHECK_YES
            && StringTools.isNullOrNone(financeItemBean.getDepartmentId()))
        {
            throw new MYException("科目[%s]下辅助核算型-部门必须存在,请确认操作", tax.getName());
        }

        if (tax.getStaffer() == TaxConstanst.TAX_CHECK_YES && StringTools.isNullOrNone(financeItemBean.getStafferId()))
        {
            throw new MYException("科目[%s]下辅助核算型-职员必须存在,请确认操作", tax.getName());
        }

        if (tax.getProduct() == TaxConstanst.TAX_CHECK_YES && StringTools.isNullOrNone(financeItemBean.getProductId()))
        {
            throw new MYException("科目[%s]下辅助核算型-产品必须存在,请确认操作", tax.getName());
        }

        if (tax.getDepot() == TaxConstanst.TAX_CHECK_YES && StringTools.isNullOrNone(financeItemBean.getDepotId()))
        {
            throw new MYException("科目[%s]下辅助核算型-仓库必须存在,请确认操作", tax.getName());
        }

        if (tax.getDuty() == TaxConstanst.TAX_CHECK_YES && StringTools.isNullOrNone(financeItemBean.getDuty2Id()))
        {
            throw new MYException("科目[%s]下辅助核算型-纳税实体必须存在,请确认操作", tax.getName());
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addFinanceBean(User user, FinanceBean bean)
        throws MYException
    {
        return addFinanceBeanWithoutTransactional(user, bean);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateFinanceBean(User user, FinanceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getItemList());

        FinanceBean old = financeDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (old.getStatus() == TaxConstanst.FINANCE_STATUS_CHECK || old.getLocks() == TaxConstanst.FINANCE_LOCK_YES)
        {
            throw new MYException("已经被核对(锁定)不能修改,请重新操作");
        }

        bean.setType(old.getType());
        bean.setCreateType(old.getCreateType());
        bean.setStatus(old.getStatus());
        bean.setChecks(old.getChecks());
        bean.setLogTime(old.getLogTime());
        bean.setCreaterId(old.getCreaterId());
        bean.setName(old.getName());

        // 保存关联
        bean.setRefId(old.getRefId());
        bean.setRefOut(old.getRefOut());
        bean.setRefBill(old.getRefBill());
        bean.setRefStock(old.getRefStock());

        // 标识成更改
        bean.setUpdateFlag(TaxConstanst.FINANCE_UPDATEFLAG_YES);

        // 允许自己制定凭证日期
        if (StringTools.isNullOrNone(bean.getFinanceDate()))
        {
            bean.setFinanceDate(TimeTools.now_short());
        }

        checkTime(bean);

        // 默认纳税实体
        if (bean.getType() == TaxConstanst.FINANCE_TYPE_MANAGER && StringTools.isNullOrNone(bean.getDutyId()))
        {
            bean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);
        }

        if (bean.getType() == TaxConstanst.FINANCE_TYPE_DUTY && StringTools.isNullOrNone(bean.getDutyId()))
        {
            throw new MYException("税务凭证必须有纳税实体的属性");
        }

        List<FinanceItemBean> itemList = bean.getItemList();

        boolean isTurn = FinanceHelper.isTurnFinance(itemList);

        if (isLOCK_FINANCE() && !isTurn)
        {
            throw new MYException("被锁定结转,不能修改凭证");
        }

        Map<String, List<FinanceItemBean>> pareMap = new HashMap<String, List<FinanceItemBean>>();

        long inTotal = 0;

        long outTotal = 0;

        // 整理出凭证对(且校验凭证的合法性)
        for (FinanceItemBean financeItemBean : itemList)
        {
            financeItemBean.setId(commonDAO.getSquenceString20());

            financeItemBean.setName(financeItemBean.getId());

            financeItemBean.setPid(bean.getId());

            FinanceHelper.copyFinanceItem(bean, financeItemBean);

            financeItemBean.setLogTime(TimeTools.now());

            String taxId = financeItemBean.getTaxId();

            if (StringTools.isNullOrNone(taxId))
            {
                throw new MYException("缺少科目信息,请确认操作");
            }

            TaxBean tax = taxDAO.find(taxId);

            if (tax == null)
            {
                throw new MYException("科目不存在,请确认操作");
            }

            // 必须是最小科目哦
            if (tax.getBottomFlag() != TaxConstanst.TAX_BOTTOMFLAG_ITEM)
            {
                throw new MYException("[%s]科目必须是最小科目,请确认操作", tax.getName() + tax.getId());
            }

            // 不是结转需要检查辅助核算项
            if ( !isTurn)
            {
                checkItem(financeItemBean, tax);
            }

            // 拷贝凭证的父级ID
            TaxHelper.copyParent(financeItemBean, tax);

            String key = financeItemBean.getPareId();

            if (pareMap.get(key) == null)
            {
                pareMap.put(key, new ArrayList<FinanceItemBean>());
            }

            pareMap.get(key).add(financeItemBean);

            // 必须有一个为0
            if (financeItemBean.getInmoney() * financeItemBean.getOutmoney() != 0)
            {
                throw new MYException("借方金额或者贷方金额不能都不为0");
            }

            inTotal += financeItemBean.getInmoney();

            outTotal += financeItemBean.getOutmoney();
        }

        bean.setInmoney(inTotal);

        bean.setOutmoney(outTotal);

        if (inTotal != outTotal)
        {
            throw new MYException("总借[%s],总贷[%s]不等,凭证增加错误", FinanceHelper.longToString(inTotal),
                FinanceHelper.longToString(outTotal));
        }

        if (bean.getInmoney() != old.getInmoney())
        {
            throw new MYException("原单据金额[%s],当前金额[%s]不等,凭证增加错误", FinanceHelper.longToString(bean.getInmoney()),
                FinanceHelper.longToString(old.getInmoney()));
        }

        // CORE 核对借贷必相等的原则
        checkPare(pareMap);

        financeDAO.updateEntityBean(bean);

        // 先删除
        financeItemDAO.deleteEntityBeansByFK(bean.getId());

        financeItemDAO.saveAllEntityBeans(itemList);

        return true;
    }

    /**
     * 校验时间
     * 
     * @param bean
     * @throws MYException
     */
    private void checkTime(FinanceBean bean)
        throws MYException
    {
        // 校验凭证时间不能大于当前时间,也不能小于最近的结算时间
        if (bean.getFinanceDate().compareTo(TimeTools.now_short()) > 0)
        {
            throw new MYException("凭证时间不能大于[%s]", TimeTools.now_short());
        }

        String monthKey = TimeTools.changeFormat(bean.getFinanceDate(), TimeTools.SHORT_FORMAT, "yyyyMM");

        List<FinanceTurnBean> turnList = financeTurnDAO.listEntityBeans("order by monthKey desc");

        if (turnList.size() > 0)
        {
            FinanceTurnBean topTurn = turnList.get(0);

            if (monthKey.compareTo(topTurn.getMonthKey()) <= 0)
            {
                throw new MYException("[%s]已经结转,不能增加此月的凭证", topTurn.getMonthKey());
            }
        }
    }

    /**
     * checkPare
     * 
     * @param pareMap
     * @throws MYException
     */
    private void checkPare(Map<String, List<FinanceItemBean>> pareMap)
        throws MYException
    {
        // 核对借贷必相等的原则
        Set<String> keySet = pareMap.keySet();

        for (String key : keySet)
        {
            List<FinanceItemBean> pareList = pareMap.get(key);

            long inMoney = 0;

            long outMoney = 0;

            for (FinanceItemBean item : pareList)
            {
                inMoney += item.getInmoney();

                outMoney += item.getOutmoney();
            }

            if (inMoney != outMoney)
            {
                throw new MYException("借[%d],贷[%d]不等,凭证错误", inMoney, outMoney);
            }
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteFinanceBean(User user, String id)
        throws MYException
    {
        return deleteFinanceBeanWithoutTransactional(user, id);
    }

    public boolean deleteFinanceBeanWithoutTransactional(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        FinanceBean old = financeDAO.find(id);

        if (old == null)
        {
            throw new MYException("数据错误,请重新操作");
        }

        if (old.getStatus() == TaxConstanst.FINANCE_STATUS_CHECK)
        {
            throw new MYException("已经被核对,不能删除,请重新操作");
        }

        if (old.getLocks() == TaxConstanst.FINANCE_LOCK_YES)
        {
            throw new MYException("已经被锁定,不能删除,请重新操作");
        }

        // 结转的凭证不能删除
        if (old.getCreateType() == TaxConstanst.FINANCE_CREATETYPE_TURN
            || old.getCreateType() == TaxConstanst.FINANCE_CREATETYPE_PROFIT)
        {
            throw new MYException("结转的凭证不能被删除的,请重新操作");
        }

        // 获取凭证项
        old.setItemList(financeItemDAO.queryEntityBeansByFK(id));

        // 删除凭证
        financeDAO.deleteEntityBean(id);

        // 删除凭证项
        financeItemDAO.deleteEntityBeansByFK(id);

        // 删除需要记录操作日志
        operationLog.info(user.getStafferName() + "删除了凭证:" + old);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateFinanceCheck(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        financeDAO.updateCheck(id, reason);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checks2(User user, String id, int type, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        String tableName = "";

        if (type == CheckConstant.CHECK_TYPE_COMPOSE)
        {
            tableName = "T_CENTER_COMPOSE";
        }
        else if (type == CheckConstant.CHECK_TYPE_CHANGE)
        {
            tableName = "T_CENTER_PRICE_CHANGE";
        }
        else if (type == CheckConstant.CHECK_TYPE_INBILL)
        {
            tableName = "T_CENTER_INBILL";
        }
        else if (type == CheckConstant.CHECK_TYPE_OUTBILL)
        {
            tableName = "T_CENTER_OUTBILL";
        }
        else if (type == CheckConstant.CHECK_TYPE_STOCK)
        {
            tableName = "T_CENTER_STOCK";
        }
        else if (type == CheckConstant.CHECK_TYPE_BUY)
        {
            tableName = "T_CENTER_OUT";
        }
        else if (type == CheckConstant.CHECK_TYPE_CUSTOMER)
        {
            tableName = "T_CENTER_CUSTOMER_NOW";
        }
        else if (type == CheckConstant.CHECK_TYPE_BASEBALANCE)
        {
            tableName = "T_CENTER_OUTBALANCE";
        }
        else
        {
            throw new MYException("数据错误,请确认操作");
        }

        checkViewDAO.updateCheck(tableName, id, reason);

        checkViewDAO.deleteEntityBean(id);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checks(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        CheckViewBean bean = checkViewDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        return checks2(user, id, bean.getType(), reason);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteChecks(User user, String id)
        throws MYException
    {
        checkViewDAO.deleteEntityBean(id);
        return true;
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
     * @return the financeItemDAO
     */
    public FinanceItemDAO getFinanceItemDAO()
    {
        return financeItemDAO;
    }

    /**
     * @param financeItemDAO
     *            the financeItemDAO to set
     */
    public void setFinanceItemDAO(FinanceItemDAO financeItemDAO)
    {
        this.financeItemDAO = financeItemDAO;
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
     * @return the checkViewDAO
     */
    public CheckViewDAO getCheckViewDAO()
    {
        return checkViewDAO;
    }

    /**
     * @param checkViewDAO
     *            the checkViewDAO to set
     */
    public void setCheckViewDAO(CheckViewDAO checkViewDAO)
    {
        this.checkViewDAO = checkViewDAO;
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
     * @return the billManager
     */
    public BillManager getBillManager()
    {
        return billManager;
    }

    /**
     * @param billManager
     *            the billManager to set
     */
    public void setBillManager(BillManager billManager)
    {
        this.billManager = billManager;
    }

    /**
     * @return the financeTurnDAO
     */
    public FinanceTurnDAO getFinanceTurnDAO()
    {
        return financeTurnDAO;
    }

    /**
     * @param financeTurnDAO
     *            the financeTurnDAO to set
     */
    public void setFinanceTurnDAO(FinanceTurnDAO financeTurnDAO)
    {
        this.financeTurnDAO = financeTurnDAO;
    }

    /**
     * @return the financeMonthDAO
     */
    public FinanceMonthDAO getFinanceMonthDAO()
    {
        return financeMonthDAO;
    }

    /**
     * @param financeMonthDAO
     *            the financeMonthDAO to set
     */
    public void setFinanceMonthDAO(FinanceMonthDAO financeMonthDAO)
    {
        this.financeMonthDAO = financeMonthDAO;
    }

    /**
     * @return the lOCK_FINANCE
     */
    public synchronized static boolean isLOCK_FINANCE()
    {
        return LOCK_FINANCE;
    }

    /**
     * @param lock_finance
     *            the lOCK_FINANCE to set
     */
    public synchronized static void setLOCK_FINANCE(boolean lock_finance)
    {
        LOCK_FINANCE = lock_finance;
    }
}
