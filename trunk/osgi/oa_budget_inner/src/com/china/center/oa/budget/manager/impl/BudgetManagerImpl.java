/**
 * File Name: UserManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.manager.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.bean.BudgetLogBean;
import com.china.center.oa.budget.bean.FeeItemBean;
import com.china.center.oa.budget.constant.BudgetConstant;
import com.china.center.oa.budget.dao.BudgetDAO;
import com.china.center.oa.budget.dao.BudgetItemDAO;
import com.china.center.oa.budget.dao.BudgetLogDAO;
import com.china.center.oa.budget.dao.FeeItemDAO;
import com.china.center.oa.budget.helper.BudgetHelper;
import com.china.center.oa.budget.manager.BudgetManager;
import com.china.center.oa.budget.vo.BudgetItemVO;
import com.china.center.oa.budget.vo.BudgetVO;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.bean.PlanBean;
import com.china.center.oa.publics.constant.IDPrefixConstant;
import com.china.center.oa.publics.constant.ModuleConstant;
import com.china.center.oa.publics.constant.OperationConstant;
import com.china.center.oa.publics.constant.PlanConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.oa.publics.dao.PlanDAO;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.TimeTools;


/**
 * UserManager
 * 
 * @author ZHUZHU
 * @version 2008-11-9
 * @see BudgetManagerImpl
 * @since 1.0
 */
@Exceptional
public class BudgetManagerImpl implements BudgetManager
{
    private final Log triggerLog = LogFactory.getLog("trigger");

    private BudgetDAO budgetDAO = null;

    private BudgetItemDAO budgetItemDAO = null;

    private CommonDAO commonDAO = null;

    private FeeItemDAO feeItemDAO = null;

    private BudgetLogDAO budgetLogDAO = null;

    private LogDAO logDAO = null;

    private OrgManager orgManager = null;

    private OutBillDAO outBillDAO = null;

    private PlanDAO planDAO = null;

    public BudgetManagerImpl()
    {
    }

    /**
     * addBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, BudgetBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getSigner());

        checkAddBean(bean);

        // 如果是月度预算需要校验时间(不能重合)
        checkContain(bean);

        bean.setId(commonDAO.getSquenceString20(IDPrefixConstant.ID_BUDGET_PREFIX));

        setTotalAndSaveSubItem(bean);

        budgetDAO.saveEntityBean(bean);

        handleSumbit(user, bean);

        return true;
    }

    /**
     * add bill in budget
     * 
     * @param bill
     * @param budgetItemId
     * @param budgetId
     * @return boolean
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean addBill(String stafferId, OutBillBean bill, String budgetItemId,
                                        String budgetId)
        throws MYException
    {
        // TODO
        checkAddBill(bill, budgetItemId, budgetId);

        createLog(stafferId, bill, budgetItemId, budgetId, null, null);

        recursiveUpdate(null, false);

        return true;
    }

    /**
     * recursive update budget
     * 
     * @param itemBean
     * @param hasSub
     * @throws MYException
     */
    private void recursiveUpdate(BudgetItemBean itemBean, boolean hasSub)
        throws MYException
    {
        if (hasSub)
        {
            // if has subItem, count sub
            double sumRealTotalInSubBudget = budgetItemDAO.sumRealTotalInSubBudget(itemBean
                .getBudgetId(), itemBean.getFeeItemId());

            itemBean.setRealMonery(sumRealTotalInSubBudget);
        }

        budgetItemDAO.updateEntityBean(itemBean);

        // update budget realmoney
        double realMoney = budgetItemDAO.sumRealTotal(itemBean.getBudgetId());

        budgetDAO.updateRealMoney(itemBean.getBudgetId(), realMoney);

        BudgetBean budget = budgetDAO.find(itemBean.getBudgetId());

        if (budget == null)
        {
            throw new MYException("数据不完备");
        }

        // top budget
        if (budget.getParentId().equals(BudgetConstant.BUDGET_ROOT))
        {
            return;
        }

        BudgetBean parent = budgetDAO.find(budget.getParentId());

        if (parent == null)
        {
            throw new MYException("数据不完备");
        }

        // parent item
        BudgetItemBean parentItem = budgetItemDAO.findByBudgetIdAndFeeItemId(parent.getId(),
            itemBean.getFeeItemId());

        if (parentItem == null)
        {
            throw new MYException("数据不完备");
        }

        recursiveUpdate(parentItem, true);
    }

    /**
     * @param stafferId
     * @param bill
     * @param budgetItemId
     * @param budgetId
     * @param itemBean
     * @param logBean
     */
    private void createLog(String stafferId, OutBillBean bill, String budgetItemId,
                           String budgetId, BudgetItemBean itemBean, BudgetLogBean logBean)
    {
        logBean.setStafferId(stafferId);

        logBean.setLocationId(bill.getLocationId());

        logBean.setBillId(bill.getId());

        logBean.setBudgetId(budgetId);

        logBean.setMonery(bill.getMoneys());

        logBean.setLog(bill.getDescription());

        logBean.setBudgetItemId(budgetItemId);

        logBean.setLogTime(TimeTools.now());

        logBean.setFeeItemId(itemBean.getFeeItemId());

        logBean.setBeforemonery(itemBean.getBudget() - itemBean.getRealMonery());

        logBean.setAftermonery(itemBean.getBudget() - itemBean.getRealMonery() - bill.getMoneys());
    }

    /**
     * checkAddBill
     * 
     * @param bill
     * @param budgetItemId
     * @param budgetId
     * @throws MYException
     */
    private BudgetItemBean checkAddBill(OutBillBean bill, String budgetItemId, String budgetId)
        throws MYException
    {
        BudgetBean budget = budgetDAO.find(budgetId);

        if (budget == null)
        {
            throw new MYException("预算不存在");
        }

        if (budget.getLevel() != BudgetConstant.BUDGET_LEVEL_MONTH)
        {
            throw new MYException("不是月度预算,不能报销");
        }

        BudgetItemBean budgetItem = budgetItemDAO.find(budgetItemId);

        if (budgetItem == null)
        {
            throw new MYException("预算项不存在");
        }

        if ( !budgetItem.getBudgetId().equals(budgetId))
        {
            throw new MYException("数据不匹配,请重新操作");
        }

        if ( ( (budgetItem.getBudget() - budgetItem.getRealMonery()) - bill.getMoneys()) < 0)
        {
            throw new MYException("预算项金额不足,无法报销");
        }

        return budgetItem;
    }

    /**
     * setTotalAndSaveSubItem
     * 
     * @param bean
     */
    private void setTotalAndSaveSubItem(BudgetBean bean)
    {
        List<BudgetItemBean> items = bean.getItems();

        double total = 0.0d;

        if ( !ListTools.isEmptyOrNull(items))
        {
            for (BudgetItemBean budgetItemBean : items)
            {
                budgetItemBean.setId(commonDAO.getSquenceString20());

                budgetItemBean.setBudgetId(bean.getId());

                total += budgetItemBean.getBudget();

                budgetItemDAO.saveEntityBean(budgetItemBean);
            }
        }

        // 预算总额
        bean.setTotal(total);
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(BudgetBean bean)
        throws MYException
    {
        // if COMPANY budget,the year must unqiue
        if (bean.getType() == BudgetConstant.BUDGET_TYPE_COMPANY)
        {
            if (budgetDAO.countByYearAndType(bean.getYear(), bean.getType()) > 0)
            {
                throw new MYException("[%s]年度的公司预算已经存在,请核实", bean.getYear());
            }

            bean.setLocationId(PublicConstant.CENTER_LOCATION);
        }

        if ( !budgetDAO.isExist(bean.getParentId()))
        {
            throw new MYException("父预算不存在");
        }

        checkSubmit(bean);
    }

    /**
     * checkSubmit(bean);
     * 
     * @param bean
     * @throws MYException
     */
    private void checkSubmit(BudgetBean bean)
        throws MYException
    {
        if (bean.getBeginDate().compareTo(bean.getEndDate()) >= 0)
        {
            throw new MYException("预算起始日期不符合要求");
        }

        // 预算项不能重复
        List<BudgetItemBean> items = bean.getItems();

        Set<String> itemSet = new HashSet();

        for (BudgetItemBean each : items)
        {
            if (itemSet.contains(each.getFeeItemId()))
            {
                throw new MYException("预算项不能相同");
            }

            itemSet.add(each.getFeeItemId());
        }

        // 检查子预算不能和其他的子预算部门重复
        if (bean.getType() == BudgetConstant.BUDGET_TYPE_LOCATION
            || bean.getType() == BudgetConstant.BUDGET_TYPE_DEPARTMENT)
        {
            if (bean.getLevel() == BudgetConstant.BUDGET_LEVEL_YEAR)
            {
                List<BudgetBean> subList = budgetDAO.queryEntityBeansByFK(bean.getParentId());

                Set<String> set = new HashSet();

                set.add(bean.getBudgetDepartment());

                for (BudgetBean each : subList)
                {
                    if ( !each.getId().equals(bean.getId()))
                    {
                        if (set.contains(each.getBudgetDepartment()))
                        {
                            throw new MYException("子预算的部门不能相同");
                        }

                        set.add(each.getBudgetDepartment());
                    }
                }
            }

            // 检查递归级别orgManager
            BudgetBean parentBudget = budgetDAO.find(bean.getParentId());

            if (parentBudget == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            // 预算选择的组织不是上下级别的关系
            if (bean.getLevel() == BudgetConstant.BUDGET_LEVEL_YEAR)
            {
                if ( !orgManager.isSubPrincipalship(parentBudget.getBudgetDepartment(), bean
                    .getBudgetDepartment()))
                {
                    throw new MYException("预算选择的组织不是上下级别的关系,请确认操作");
                }
            }

            // 年度预算的时间继承
            if (bean.getLevel() == BudgetConstant.BUDGET_LEVEL_YEAR)
            {
                bean.setBeginDate(parentBudget.getBeginDate());

                bean.setEndDate(parentBudget.getEndDate());
            }

            // 月度预算继承年度预算的部门
            if (bean.getLevel() == BudgetConstant.BUDGET_LEVEL_MONTH)
            {
                if (parentBudget.getLevel() != BudgetConstant.BUDGET_LEVEL_YEAR
                    || parentBudget.getType() != BudgetConstant.BUDGET_TYPE_DEPARTMENT)
                {
                    throw new MYException("月度预算的父级必须是部门的年度预算,请确认操作");
                }

                bean.setBudgetDepartment(parentBudget.getBudgetDepartment());
            }

            // 校验权签人
            if ( !orgManager.isStafferBelongOrg(bean.getSigner(), bean.getBudgetDepartment()))
            {
                throw new MYException("权签人不属于选择的部门,请确认操作");
            }
        }

        // 预算项的角和
        if (bean.getStatus() == BudgetConstant.BUDGET_STATUS_SUBMIT)
        {
            if (ListTools.isEmptyOrNull(bean.getItems()))
            {
                throw new MYException("预算项不能为空");
            }

            // check subitem is in parent and budget is less than parent
            if (bean.getType() != BudgetConstant.BUDGET_TYPE_COMPANY)
            {
                compareSubItem(budgetItemDAO.queryEntityVOsByFK(bean.getParentId()), bean
                    .getItems());
            }
        }
    }

    /**
     * compareSubItem
     * 
     * @param parentSubItem
     * @param subItem
     * @throws MYException
     */
    private void compareSubItem(List<BudgetItemVO> parentSubItem, List<BudgetItemBean> subItem)
        throws MYException
    {
        for (BudgetItemBean item : subItem)
        {
            boolean isOK = false;

            for (BudgetItemVO pitem : parentSubItem)
            {
                if (pitem.getFeeItemId().equals(item.getFeeItemId()))
                {
                    // total money,less than current
                    if (compareMoney(pitem, item.getBudget()))
                    {
                        isOK = true;
                    }
                    else
                    {
                        throw new MYException("[%s]的预算和超出父预算", pitem.getFeeItemName());
                    }
                }
            }

            if ( !isOK)
            {
                FeeItemBean feeItem = feeItemDAO.find(item.getFeeItemId());

                if (feeItem == null)
                {
                    throw new MYException("预算项不存在");
                }

                throw new MYException("父预算缺少提交的预算项[%s]", feeItem.getName());
            }
        }
    }

    /**
     * compareMoney
     * 
     * @param pitem
     * @param currentMoney
     * @return
     */
    private boolean compareMoney(BudgetItemBean pitem, double currentMoney)
    {
        List<BudgetBean> allSubBudget = budgetDAO.querySubmitBudgetByParentId(pitem.getBudgetId());

        double total = currentMoney;

        for (BudgetBean budgetBean : allSubBudget)
        {
            BudgetItemBean subFeeItem = budgetItemDAO.findByBudgetIdAndFeeItemId(
                budgetBean.getId(), pitem.getFeeItemId());

            if (subFeeItem != null)
            {
                total += subFeeItem.getUseMonery();
            }
        }

        return pitem.getBudget() >= total;
    }

    /**
     * 修改预算
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, BudgetBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getSigner());

        checkupdateBean(bean);

        budgetItemDAO.deleteEntityBeansByFK(bean.getId());

        setTotalAndSaveSubItem(bean);

        budgetDAO.updateEntityBean(bean);

        // log
        handleSumbit(user, bean);

        return true;
    }

    /**
     * @param user
     * @param bean
     */
    private void handleSumbit(User user, BudgetBean bean)
    {
        if (bean.getStatus() == BudgetConstant.BUDGET_STATUS_SUBMIT)
        {
            log(user, bean.getId(), OperationConstant.OPERATION_SUBMIT, "提交预算");
        }
    }

    /**
     * checkupdateBean
     * 
     * @param bean
     * @throws MYException
     */
    private void checkupdateBean(BudgetBean bean)
        throws MYException
    {
        BudgetBean oldBean = budgetDAO.find(bean.getId());

        if (oldBean == null)
        {
            throw new MYException("预算不存在");
        }

        if ( ! (oldBean.getStatus() == BudgetConstant.BUDGET_STATUS_INIT || oldBean.getStatus() == BudgetConstant.BUDGET_STATUS_REJECT))
        {
            throw new MYException("只有初始和驳回的预算才可以修改");
        }

        if (bean.getType() == BudgetConstant.BUDGET_TYPE_COMPANY)
        {
            bean.setLocationId(PublicConstant.CENTER_LOCATION);
        }

        checkSubmit(bean);
    }

    /**
     * 预算通过后对预算项的修正
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateItemBean(User user, BudgetItemBean bean, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        budgetItemDAO.updateEntityBean(bean);

        updateTotal(bean);

        log(user, bean.getBudgetId(), OperationConstant.OPERATION_UPDATE, reason);

        return true;
    }

    /**
     * 修改预算金额
     * 
     * @param bean
     */
    private void updateTotal(BudgetItemBean bean)
    {
        double total = budgetItemDAO.sumBudgetTotal(bean.getBudgetId());

        budgetDAO.updateTotal(bean.getBudgetId(), total);
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean delItemBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        BudgetItemBean item = budgetItemDAO.find(id);

        checkDelItemBean(item);

        budgetItemDAO.deleteEntityBean(id);

        updateTotal(item);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public void initCarryStatus()
    {
        triggerLog.info("开始执行预算的执行状态的变更...");

        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addIntCondition("carryStatus", "=", BudgetConstant.BUDGET_CARRY_INIT);

        condition.addCondition("beginDate", "<=", TimeTools.now_short());

        condition.addIntCondition("status", "=", BudgetConstant.BUDGET_STATUS_PASS);

        List<BudgetBean> budgetList = budgetDAO.queryEntityBeansByCondition(condition);

        for (BudgetBean budgetBean : budgetList)
        {
            budgetDAO.updateCarryStatus(budgetBean.getId(), BudgetConstant.BUDGET_CARRY_DOING);

            triggerLog.info("预算变更为执行:" + budgetBean);
        }

        triggerLog.info("结束执行预算的执行状态的变更...");
    }

    /**
     * delBean
     * 
     * @param user
     * @param stafferId
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDelBean(user, id);

        budgetDAO.deleteEntityBean(id);

        budgetItemDAO.deleteEntityBeansByFK(id);

        logDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /**
     * 通过
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean passBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        BudgetBean bean = checkPassBean(id);

        // 校验是否有权限审批部门预算
        if (bean.getType() == BudgetConstant.BUDGET_TYPE_DEPARTMENT)
        {
            if ( !orgManager.isOrgBelongStaffer(user.getStafferId(), bean.getBudgetDepartment()))
            {
                throw new MYException("部门预算必须都是所属事业部经理审批");
            }
        }

        checkTotal(bean);

        // 如果是月度预算需要校验时间(不能重合)
        checkContain(bean);

        budgetDAO.updateStatus(id, BudgetConstant.BUDGET_STATUS_PASS);

        log(user, id, OperationConstant.OPERATION_PASS, "通过预算");

        // NOTE 需要注册任务执行回收事宜
        createPlan(bean);

        return true;
    }

    /**
     * checkContain
     * 
     * @param bean
     * @throws MYException
     */
    private void checkContain(BudgetBean bean)
        throws MYException
    {
        if (bean.getLevel() == BudgetConstant.BUDGET_LEVEL_MONTH)
        {
            BudgetBean parent = budgetDAO.find(bean.getParentId());

            if (parent == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            int b = bean.getBeginDate().compareTo(parent.getBeginDate());

            if (b < 0)
            {
                throw new MYException("月份和年度预算有冲突,年度预算[%s]-[%s]", parent.getBeginDate(), parent
                    .getEndDate());
            }

            int e = bean.getEndDate().compareTo(parent.getEndDate());

            if (e > 0)
            {
                throw new MYException("月份和年度预算有冲突,年度预算[%s]-[%s]", parent.getBeginDate(), parent
                    .getEndDate());
            }

            List<BudgetBean> subList = budgetDAO.queryEntityBeansByFK(bean.getParentId());

            for (BudgetBean each : subList)
            {
                if (each.getId().equals(bean.getId()))
                {
                    continue;
                }

                if (each.getStatus() != BudgetConstant.BUDGET_STATUS_PASS)
                {
                    continue;
                }

                int bcompare = each.getBeginDate().compareTo(bean.getBeginDate());

                int ecompare = each.getEndDate().compareTo(bean.getBeginDate());

                if (bcompare * ecompare <= 0)
                {
                    throw new MYException("月份有重叠处,已经存在[%s]-[%s]", each.getBeginDate(), each
                        .getEndDate());
                }

                int bcompare1 = each.getBeginDate().compareTo(bean.getEndDate());

                int ecompare1 = each.getEndDate().compareTo(bean.getEndDate());

                if (bcompare1 * ecompare1 <= 0)
                {
                    throw new MYException("月份有重叠处,已经存在[%s]-[%s]", each.getBeginDate(), each
                        .getEndDate());
                }

                // 包含关系的
                if (bcompare * bcompare1 < 0)
                {
                    throw new MYException("月份有重叠处,已经存在[%s]-[%s]", each.getBeginDate(), each
                        .getEndDate());
                }

            }
        }
    }

    /**
     * 每个预算只有一个执行计划
     * 
     * @param item
     */
    private void createPlan(BudgetBean item)
    {
        PlanBean plan = new PlanBean();

        plan.setId(commonDAO.getSquenceString());

        plan.setFkId(item.getId());

        // if the level is low ,it must carry priority
        plan.setOrderIndex(99 - item.getLevel());

        plan.setType(BudgetConstant.BUGFET_PLAN_TYPE);

        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);

        plan.setLogTime(TimeTools.now());

        plan.setBeginTime(item.getBeginDate() + " 00:00:00");

        // 推后15天
        String endTime = TimeTools.getSpecialDateStringByDays(item.getEndDate() + " 23:59:59", 15);

        // 结束应该是推后
        plan.setEndTime(endTime);

        plan.setCarryTime(plan.getEndTime());

        plan.setDescription("系统自动生成的预算任务:" + item.getName());

        planDAO.saveEntityBean(plan);
    }

    /**
     * log in db
     * 
     * @param user
     * @param id
     */
    private void log(User user, String id, String operation, String reson)
    {
        // 记录审批日志
        LogBean log = new LogBean();

        log.setFkId(id);

        log.setLocationId(user.getLocationId());
        log.setStafferId(user.getStafferId());
        log.setLogTime(TimeTools.now());
        log.setModule(ModuleConstant.MODULE_BUDGET);
        log.setOperation(operation);
        log.setLog(reson);

        logDAO.saveEntityBean(log);
    }

    /**
     * 检查预算总额
     * 
     * @param bean
     * @throws MYException
     */
    private void checkTotal(BudgetBean bean)
        throws MYException
    {
        // 验证预算是否超过父级预算
        if ( !"0".equals(bean.getParentId()))
        {
            BudgetBean parentBean = budgetDAO.find(bean.getParentId());

            if (parentBean == null)
            {
                throw new MYException("父级预算不存在");
            }

            double hasUsed = budgetDAO.countParentBudgetTotal(bean.getParentId());

            if ( (bean.getTotal() + hasUsed) > parentBean.getTotal())
            {
                throw new MYException("总体预算超过父级预算,请核实");
            }
        }
    }

    /**
     * 驳回
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectBean(User user, String id, String reson)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkRejectBean(id);

        budgetDAO.updateStatus(id, BudgetConstant.BUDGET_STATUS_REJECT);

        log(user, id, OperationConstant.OPERATION_REJECT, reson);

        return true;
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkDelBean(User user, String id)
        throws MYException
    {
        BudgetBean oldBean = budgetDAO.find(id);

        if (oldBean == null)
        {
            throw new MYException("预算不存在");
        }

        if ( ! (oldBean.getStatus() == BudgetConstant.BUDGET_STATUS_INIT || oldBean.getStatus() == BudgetConstant.BUDGET_STATUS_REJECT))
        {
            throw new MYException("只有初始和驳回的预算才可以删除");
        }

        if ( !user.getStafferId().equals(oldBean.getStafferId()))
        {
            throw new MYException("不能删除他人提交的预算");
        }
    }

    private BudgetBean checkPassBean(String id)
        throws MYException
    {
        BudgetBean oldBean = budgetDAO.find(id);

        if (oldBean == null)
        {
            throw new MYException("预算不存在");
        }

        if (oldBean.getStatus() != BudgetConstant.BUDGET_STATUS_SUBMIT)
        {
            throw new MYException("只有提交的预算可以通过");
        }

        return oldBean;
    }

    private void checkRejectBean(String id)
        throws MYException
    {
        BudgetBean oldBean = budgetDAO.find(id);

        if (oldBean == null)
        {
            throw new MYException("预算不存在");
        }

        if (oldBean.getStatus() != BudgetConstant.BUDGET_STATUS_SUBMIT)
        {
            throw new MYException("只有提交的预算可以驳回");
        }
    }

    private void checkDelItemBean(BudgetItemBean item)
        throws MYException
    {
        if (item == null)
        {
            throw new MYException("预算项不存在");
        }

        BudgetBean oldBean = budgetDAO.find(item.getBudgetId());

        if (oldBean == null)
        {
            throw new MYException("预算不存在");
        }

        if ( ! (oldBean.getStatus() == BudgetConstant.BUDGET_STATUS_INIT || oldBean.getStatus() == BudgetConstant.BUDGET_STATUS_REJECT))
        {
            throw new MYException("只有初始和驳回的预算项才可以删除");
        }
    }

    /**
     * 查询单个预算
     * 
     * @param id
     * @return
     * @throws MYException
     */
    public BudgetVO findBudgetVO(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        BudgetVO vo = budgetDAO.findVO(id);

        if (vo == null)
        {
            return null;
        }

        List<BudgetItemVO> itemVOs = budgetItemDAO.queryEntityVOsByFK(id);

        vo.setItemVOs(itemVOs);

        // change vo
        for (BudgetItemVO budgetItemVO : itemVOs)
        {
            BudgetHelper.formatBudgetItem(budgetItemVO);
        }

        return vo;
    }

    /**
     * 查询预算的基本信息
     * 
     * @param id
     * @return
     * @throws MYException
     */
    public BudgetBean findBudget(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        BudgetBean vo = budgetDAO.find(id);

        if (vo == null)
        {
            return null;
        }

        return vo;
    }

    /**
     * @return the budgetDAO
     */
    public BudgetDAO getBudgetDAO()
    {
        return budgetDAO;
    }

    /**
     * @param budgetDAO
     *            the budgetDAO to set
     */
    public void setBudgetDAO(BudgetDAO budgetDAO)
    {
        this.budgetDAO = budgetDAO;
    }

    /**
     * @return the budgetItemDAO
     */
    public BudgetItemDAO getBudgetItemDAO()
    {
        return budgetItemDAO;
    }

    /**
     * @param budgetItemDAO
     *            the budgetItemDAO to set
     */
    public void setBudgetItemDAO(BudgetItemDAO budgetItemDAO)
    {
        this.budgetItemDAO = budgetItemDAO;
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
     * @return the feeItemDAO
     */
    public FeeItemDAO getFeeItemDAO()
    {
        return feeItemDAO;
    }

    /**
     * @param feeItemDAO
     *            the feeItemDAO to set
     */
    public void setFeeItemDAO(FeeItemDAO feeItemDAO)
    {
        this.feeItemDAO = feeItemDAO;
    }

    /**
     * @return the budgetLogDAO
     */
    public BudgetLogDAO getBudgetLogDAO()
    {
        return budgetLogDAO;
    }

    /**
     * @param budgetLogDAO
     *            the budgetLogDAO to set
     */
    public void setBudgetLogDAO(BudgetLogDAO budgetLogDAO)
    {
        this.budgetLogDAO = budgetLogDAO;
    }

    /**
     * @return the logDAO
     */
    public LogDAO getLogDAO()
    {
        return logDAO;
    }

    /**
     * @param logDAO
     *            the logDAO to set
     */
    public void setLogDAO(LogDAO logDAO)
    {
        this.logDAO = logDAO;
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
     * @return the planDAO
     */
    public PlanDAO getPlanDAO()
    {
        return planDAO;
    }

    /**
     * @param planDAO
     *            the planDAO to set
     */
    public void setPlanDAO(PlanDAO planDAO)
    {
        this.planDAO = planDAO;
    }

    /**
     * @return the orgManager
     */
    public OrgManager getOrgManager()
    {
        return orgManager;
    }

    /**
     * @param orgManager
     *            the orgManager to set
     */
    public void setOrgManager(OrgManager orgManager)
    {
        this.orgManager = orgManager;
    }

}
