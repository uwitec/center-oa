/**
 * File Name: UserManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.bean.BudgetLogBean;
import com.china.center.oa.budget.bean.FeeItemBean;
import com.china.center.oa.budget.dao.BudgetDAO;
import com.china.center.oa.budget.dao.BudgetItemDAO;
import com.china.center.oa.budget.dao.BudgetLogDAO;
import com.china.center.oa.budget.dao.FeeItemDAO;
import com.china.center.oa.budget.helper.BudgetHelper;
import com.china.center.oa.budget.vo.BudgetItemVO;
import com.china.center.oa.budget.vo.BudgetVO;
import com.china.center.oa.constant.BudgetConstant;
import com.china.center.oa.constant.ModuleConstant;
import com.china.center.oa.constant.OperationConstant;
import com.china.center.oa.constant.PlanConstant;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.ext.dao.BillDAO;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.dao.PlanDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.Bill;


/**
 * UserManager
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see BudgetManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "budgetManager")
public class BudgetManager
{
    private BudgetDAO budgetDAO = null;

    private BudgetItemDAO budgetItemDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private FeeItemDAO feeItemDAO = null;

    private BudgetLogDAO budgetLogDAO = null;

    private LogDAO logDAO = null;

    private BillDAO billDAO = null;

    private PlanDAO planDAO = null;

    public BudgetManager()
    {}

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
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO2.getSquenceString20());

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
    public synchronized boolean addBill(String stafferId, Bill bill, String budgetItemId,
                                        String budgetId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bill, budgetItemId, budgetId);

        BudgetItemBean itemBean = checkAddBill(bill, budgetItemId, budgetId);

        billDAO.saveEntityBean(bill);

        BudgetLogBean logBean = new BudgetLogBean();

        createLog(stafferId, bill, budgetItemId, budgetId, itemBean, logBean);

        // modify money
        itemBean.setRealMonery(itemBean.getRealMonery() + bill.getMoney());

        // NOTE recursive update all budget
        recursiveUpdate(itemBean, false);

        // log money
        budgetLogDAO.saveEntityBean(logBean);

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
            double sumRealTotalInSubBudget = budgetItemDAO.sumRealTotalInSubBudget(
                itemBean.getBudgetId(), itemBean.getFeeItemId());

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
    private void createLog(String stafferId, Bill bill, String budgetItemId, String budgetId,
                           BudgetItemBean itemBean, BudgetLogBean logBean)
    {
        logBean.setStafferId(stafferId);

        logBean.setLocationId(bill.getLocationId());

        logBean.setBillId(bill.getId());

        logBean.setBudgetId(budgetId);

        logBean.setMonery(bill.getMoney());

        logBean.setLog(bill.getDescription());

        logBean.setBudgetItemId(budgetItemId);

        logBean.setLogTime(TimeTools.now());

        logBean.setFeeItemId(itemBean.getFeeItemId());

        logBean.setBeforemonery(itemBean.getBudget() - itemBean.getRealMonery());

        logBean.setAftermonery(itemBean.getBudget() - itemBean.getRealMonery() - bill.getMoney());
    }

    /**
     * checkAddBill
     * 
     * @param bill
     * @param budgetItemId
     * @param budgetId
     * @throws MYException
     */
    private BudgetItemBean checkAddBill(Bill bill, String budgetItemId, String budgetId)
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

        if ( ( (budgetItem.getBudget() - budgetItem.getRealMonery()) - bill.getMoney()) < 0)
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
        if (bean.getStatus() == BudgetConstant.BUDGET_STATUS_SUBMIT)
        {
            if (ListTools.isEmptyOrNull(bean.getItems()))
            {
                throw new MYException("预算项不能为空");
            }

            // check subitem is in parent and budget is less than parent
            if (bean.getType() != BudgetConstant.BUDGET_TYPE_COMPANY)
            {
                compareSubItem(budgetItemDAO.queryEntityVOsByFK(bean.getParentId()),
                    bean.getItems());
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
        JudgeTools.judgeParameterIsNull(user, bean);

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

        checkTotal(bean);

        budgetDAO.updateStatus(id, BudgetConstant.BUDGET_STATUS_PASS);

        log(user, id, OperationConstant.OPERATION_PASS, "通过预算");

        // NOTE 需要注册任务执行回收事宜
        createPlan(bean);

        return true;
    }

    /**
     * 每个预算只有一个执行计划
     * 
     * @param item
     */
    private void createPlan(BudgetBean item)
    {
        PlanBean plan = new PlanBean();

        plan.setId(commonDAO2.getSquenceString());

        plan.setFkId(item.getId());

        // if the level is low ,it must carry priority
        plan.setOrderIndex(99 - item.getLevel());

        plan.setType(BudgetConstant.BUGFET_PLAN_TYPE);

        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);

        plan.setLogTime(TimeTools.now());

        plan.setBeginTime(item.getBeginDate() + " 00:00:00");

        plan.setEndTime(item.getEndDate() + " 23:59:59");

        plan.setCarryTime(plan.getEndTime());

        plan.setDescription("系统自动生成的任务");

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
     * @return the commonDAO2
     */
    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    /**
     * @param commonDAO2
     *            the commonDAO2 to set
     */
    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
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

    public PlanDAO getPlanDAO()
    {
        return planDAO;
    }

    public void setPlanDAO(PlanDAO planDAO)
    {
        this.planDAO = planDAO;
    }

    /**
     * @return the billDAO
     */
    public BillDAO getBillDAO()
    {
        return billDAO;
    }

    /**
     * @param billDAO
     *            the billDAO to set
     */
    public void setBillDAO(BillDAO billDAO)
    {
        this.billDAO = billDAO;
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
}
