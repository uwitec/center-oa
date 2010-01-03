/**
 * File Name: CityConfigManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.constant.ModuleConstant;
import com.china.center.oa.constant.PlanConstant;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.examine.bean.CityProfitExamineBean;
import com.china.center.oa.examine.bean.ExamineBean;
import com.china.center.oa.examine.bean.NewCustomerExamineBean;
import com.china.center.oa.examine.bean.OldCustomerExamineBean;
import com.china.center.oa.examine.bean.ProfitExamineBean;
import com.china.center.oa.examine.dao.CityProfitExamineDAO;
import com.china.center.oa.examine.dao.CustomerExamineLogDAO;
import com.china.center.oa.examine.dao.ExamineDAO;
import com.china.center.oa.examine.dao.NewCustomerExamineDAO;
import com.china.center.oa.examine.dao.OldCustomerExamineDAO;
import com.china.center.oa.examine.dao.ProfitExamineDAO;
import com.china.center.oa.examine.helper.ExamineHelper;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.dao.PlanDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * CityConfigManager
 * 
 * @author zhuzhu
 * @version 2009-1-3
 * @see ExamineManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "examineManager")
public class ExamineManager
{
    private NewCustomerExamineDAO newCustomerExamineDAO = null;

    private OldCustomerExamineDAO oldCustomerExamineDAO = null;

    private ProfitExamineDAO profitExamineDAO = null;

    private CityProfitExamineDAO cityProfitExamineDAO = null;

    private CustomerExamineLogDAO customerExamineLogDAO = null;

    private ExamineDAO examineDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private StafferDAO stafferDAO = null;

    private LogDAO logDAO = null;

    private PlanDAO planDAO = null;

    public ExamineManager()
    {}

    /**
     * 增加考核
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean addBean(User user, ExamineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        // 抽象的不需要检查
        if (bean.getAbs() == ExamineConstant.EXAMINE_ABS_FALSE)
        {
            checkAdd(bean);
        }
        else
        {
            bean.setParentId("0");

            bean.setStafferId(user.getStafferId());

            bean.setCreaterId(user.getStafferId());
        }

        bean.setId(commonDAO2.getSquenceString());

        bean.setLogTime(TimeTools.now());

        examineDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * 配置新客户考核
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean configNewCustomerExamine(User user, String parentId,
                                            List<NewCustomerExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, items);

        checkConfig(parentId);

        newCustomerExamineDAO.deleteEntityBeansByFK(parentId);

        for (NewCustomerExamineBean newCustomerExamineBean : items)
        {
            newCustomerExamineBean.setId(commonDAO2.getSquenceString());
            newCustomerExamineDAO.saveEntityBean(newCustomerExamineBean);
        }

        return true;
    }

    /**
     * 配置老客户考核
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean configOldCustomerExamine(User user, String parentId,
                                            List<OldCustomerExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, items);

        checkConfig(parentId);

        oldCustomerExamineDAO.deleteEntityBeansByFK(parentId);

        for (OldCustomerExamineBean oldCustomerExamineBean : items)
        {
            oldCustomerExamineBean.setId(commonDAO2.getSquenceString());
            oldCustomerExamineDAO.saveEntityBean(oldCustomerExamineBean);
        }

        return true;
    }

    /**
     * 变更毛利率
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateProfitExamine(User user, String profitExamineId, String reason,
                                       double newProfit)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, profitExamineId);

        ProfitExamineBean bean = checkUpdateProfitExamine(profitExamineId);

        bean.setPlanValue(newProfit);

        // 写日志
        saveApproveLog(user, bean.getParentId(), ModuleConstant.MODULE_EXAMINE,
            PublicConstant.OPERATION_CHANGE, reason);

        profitExamineDAO.updateEntityBean(bean);

        ExamineBean examine = examineDAO.find(bean.getParentId());

        if (examine == null)
        {
            throw new MYException("数据不完备");
        }

        // 增加到系统后检查是否超支
        checkProfit(examine.getId(), examine.getParentId());

        List<ProfitExamineBean> items = profitExamineDAO.queryEntityBeansByFK(bean.getParentId());

        double total = 0.0d;

        for (ProfitExamineBean profitExamineBean2 : items)
        {
            total += profitExamineBean2.getPlanValue();
        }

        // 更新考核的业绩总额
        examineDAO.updateTotalProfit(bean.getParentId(), total);

        return true;
    }

    /**
     * checkUpdateP
     * 
     * @param profitExamineId
     * @return
     * @throws MYException
     */
    private ProfitExamineBean checkUpdateProfitExamine(String profitExamineId)
        throws MYException
    {
        ProfitExamineBean bean = profitExamineDAO.find(profitExamineId);

        if (bean == null)
        {
            throw new MYException("数据不完备");
        }

        if (bean.getStatus() == ExamineConstant.EXAMINE_ITEM_STATUS_END)
        {
            throw new MYException("此考评项已经结束,不能修改");
        }

        return bean;
    }

    /**
     * 配置毛利率(核心考核)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean configProfitExamine(User user, String parentId, List<ProfitExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, items);

        ExamineBean bean = checkConfig(parentId);

        profitExamineDAO.deleteEntityBeansByFK(parentId);

        for (ProfitExamineBean profitExamineBean : items)
        {
            profitExamineBean.setId(commonDAO2.getSquenceString());
            profitExamineDAO.saveEntityBean(profitExamineBean);
        }

        // 增加到系统后检查是否超支
        checkProfit(parentId, bean.getParentId());

        double total = 0.0d;

        for (ProfitExamineBean profitExamineBean2 : items)
        {
            total += profitExamineBean2.getPlanValue();
        }

        // 更新考核的业绩总额
        examineDAO.updateTotalProfit(parentId, total);

        return true;
    }

    /**
     * 配置区域毛利率
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean configCityProfitExamine(User user, String parentId,
                                           List<CityProfitExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, items);

        ExamineBean bean = checkConfig2(parentId);

        // handle readonly update
        if (ExamineHelper.isReadonly(bean.getStatus()))
        {
            // cancle task
            List<CityProfitExamineBean> cpList = cityProfitExamineDAO.queryEntityBeansByFK(bean.getId());

            for (CityProfitExamineBean item : cpList)
            {
                planDAO.deleteEntityBeansByFK(item.getId());
            }

            updateItemsInner(parentId, items);

            // add new task
            for (CityProfitExamineBean item : items)
            {
                createCPPlan(item);
            }

            saveApproveLog(user, parentId, ModuleConstant.MODULE_EXAMINE,
                PublicConstant.OPERATION_CHANGE, "【" + user.getStafferId() + "】更新区域客户成交考评");
        }
        else
        {
            updateItemsInner(parentId, items);
        }

        return true;
    }

    /**
     * @param parentId
     * @param items
     */
    private void updateItemsInner(String parentId, List<CityProfitExamineBean> items)
    {
        cityProfitExamineDAO.deleteEntityBeansByFK(parentId);

        for (CityProfitExamineBean item : items)
        {
            item.setId(commonDAO2.getSquenceString());

            cityProfitExamineDAO.saveEntityBean(item);
        }
    }

    /**
     * 检查修改配置
     * 
     * @param parentId
     * @throws MYException
     */
    private ExamineBean checkConfig(String parentId)
        throws MYException
    {
        ExamineBean bean = examineDAO.find(parentId);

        if (bean == null)
        {
            throw new MYException("考核不存在,请确认");
        }

        if (ExamineHelper.isReadonly(bean.getStatus()))
        {
            throw new MYException("只有初始和驳回的可以修改");
        }

        return bean;
    }

    private ExamineBean checkConfig2(String parentId)
        throws MYException
    {
        ExamineBean bean = examineDAO.find(parentId);

        if (bean == null)
        {
            throw new MYException("考核不存在,请确认");
        }

        return bean;
    }

    /**
     * 修改考核
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, ExamineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkUpdate(user, bean);

        bean.setStatus(ExamineConstant.EXAMINE_STATUS_INIT);

        examineDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * 删除考核(还没有运行的)
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDel(user, id);

        // 删除考核项
        delItems(id);

        examineDAO.deleteEntityBean(id);

        // 删除相关的日志
        logDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /**
     * 删除考核(运行的)
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean2(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDel2(user, id);

        // 删除考核项
        delItems2(id);

        examineDAO.deleteEntityBean(id);

        saveApproveLog(user, id, ModuleConstant.MODULE_EXAMINE, PublicConstant.OPERATION_DEL,
            user.getStafferId() + "删除考评");

        return true;
    }

    /**
     * 删除考核项
     * 
     * @param id
     */
    private void delItems(String id)
    {
        // 删除区域利润考核项
        cityProfitExamineDAO.deleteEntityBeansByFK(id);

        // 删除新客户考核项
        newCustomerExamineDAO.deleteEntityBeansByFK(id);

        // 删除老客户考核项
        oldCustomerExamineDAO.deleteEntityBeansByFK(id);

        // 删除利润考核项
        profitExamineDAO.deleteEntityBeansByFK(id);
    }

    /**
     * 删除考核项和计划
     * 
     * @param id
     */
    private void delItems2(String id)
    {
        // 区域考核
        List<CityProfitExamineBean> cList = cityProfitExamineDAO.queryEntityBeansByFK(id);
        for (CityProfitExamineBean cityProfitExamineBean : cList)
        {
            planDAO.deleteEntityBeansByFK(cityProfitExamineBean.getId());
        }
        // 删除区域利润考核项
        cityProfitExamineDAO.deleteEntityBeansByFK(id);

        List<NewCustomerExamineBean> nList = newCustomerExamineDAO.queryEntityBeansByFK(id);
        for (NewCustomerExamineBean newCustomerExamineBean : nList)
        {
            customerExamineLogDAO.deleteEntityBeansByFK(newCustomerExamineBean.getId());

            planDAO.deleteEntityBeansByFK(newCustomerExamineBean.getId());
        }
        // 删除新客户考核项
        newCustomerExamineDAO.deleteEntityBeansByFK(id);

        List<OldCustomerExamineBean> oList = oldCustomerExamineDAO.queryEntityBeansByFK(id);
        for (OldCustomerExamineBean oldCustomerExamineBean : oList)
        {
            customerExamineLogDAO.deleteEntityBeansByFK(oldCustomerExamineBean.getId());

            planDAO.deleteEntityBeansByFK(oldCustomerExamineBean.getId());
        }
        // 删除老客户考核项
        oldCustomerExamineDAO.deleteEntityBeansByFK(id);

        List<ProfitExamineBean> pList = profitExamineDAO.queryEntityBeansByFK(id);
        for (ProfitExamineBean profitExamineBean : pList)
        {
            planDAO.deleteEntityBeansByFK(profitExamineBean.getId());
        }
        // 删除利润考核项
        profitExamineDAO.deleteEntityBeansByFK(id);
    }

    /**
     * 提交考核(直接进入运行)
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean submitBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        ExamineBean bean = checkSubmit(user, id);

        // 更新考核到运行
        examineDAO.updateStatus(id, ExamineConstant.EXAMINE_STATUS_PASS);

        // 生成执行计划(核心的一步)
        createPlans(bean);

        saveApproveLog(user, id, ModuleConstant.MODULE_EXAMINE, PublicConstant.OPERATION_SUBMIT,
            "考核制定后提交确认,进入运行状态");

        return true;
    }

    /**
     * 写数据库日志
     * 
     * @param user
     * @param id
     */
    private void saveApproveLog(User user, String id, String module, String operation, String logs)
    {
        LogBean log = new LogBean();
        log.setFkId(id);
        log.setLocationId(user.getLocationId());
        log.setStafferId(user.getStafferId());
        log.setModule(module);
        log.setOperation(operation);
        log.setLogTime(TimeTools.now());
        log.setLog(logs);
        logDAO.saveEntityBean(log);
    }

    /**
     * 业务员通过
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean passBean(User user, String id)
        throws MYException
    {
        return true;
    }

    /**
     * 生成执行计划，注册定时任务
     * 
     * @param id
     */
    private void createPlans(ExamineBean bean)
    {
        // 新客户的任务
        List<NewCustomerExamineBean> newList = newCustomerExamineDAO.queryEntityBeansByFK(bean.getId());

        for (NewCustomerExamineBean newCustomerExamineBean : newList)
        {
            createNewPlan(newCustomerExamineBean, bean);
        }

        // 老客户的任务
        List<OldCustomerExamineBean> oldList = oldCustomerExamineDAO.queryEntityBeansByFK(bean.getId());

        for (OldCustomerExamineBean item : oldList)
        {
            createOldPlan(item, bean.getType());
        }

        // 纯利润的考核
        List<ProfitExamineBean> pList = profitExamineDAO.queryEntityBeansByFK(bean.getId());

        for (ProfitExamineBean item : pList)
        {
            createProfitPlan(item, bean);
        }

        // 区域利润的计划
        List<CityProfitExamineBean> cpList = cityProfitExamineDAO.queryEntityBeansByFK(bean.getId());

        for (CityProfitExamineBean item : cpList)
        {
            createCPPlan(item);
        }
    }

    /**
     * 利润的最终确认延时20天
     * 
     * @param item
     */
    private void createCPPlan(CityProfitExamineBean item)
    {
        PlanBean plan = new PlanBean();

        plan.setId(commonDAO2.getSquenceString());

        plan.setFkId(item.getId());

        plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_CPROFIT);

        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);

        plan.setLogTime(TimeTools.now());

        plan.setBeginTime(item.getBeginTime());

        plan.setEndTime(item.getEndTime());

        plan.setCarryTime(TimeTools.getStringByOrgAndDays(item.getEndTime(), 20));

        plan.setDescription("系统自动生成的任务");

        planDAO.saveEntityBean(plan);
    }

    /**
     * 延时20天最后确认
     * 
     * @param item
     */
    private void createProfitPlan(ProfitExamineBean item, ExamineBean bean)
    {
        PlanBean plan = new PlanBean();

        plan.setId(commonDAO2.getSquenceString());

        plan.setFkId(item.getId());

        plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_PROFIT);

        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);

        plan.setLogTime(TimeTools.now());

        plan.setBeginTime(item.getBeginTime());

        plan.setEndTime(item.getEndTime());

        if (bean.getAttType() == ExamineConstant.EXAMINE_ATTTYPE_PERSONAL)
        {
            // 首先执行
            plan.setOrderIndex(0);
        }

        if (bean.getAttType() == ExamineConstant.EXAMINE_ATTTYPE_DEPARTMENT)
        {
            // 其次执行
            plan.setOrderIndex(10);
        }

        if (bean.getAttType() == ExamineConstant.EXAMINE_ATTTYPE_LOCATION)
        {
            // 最后执行
            plan.setOrderIndex(100);
        }

        plan.setCarryTime(TimeTools.getStringByOrgAndDays(item.getEndTime(), 20));

        plan.setDescription("系统自动生成的任务");

        planDAO.saveEntityBean(plan);
    }

    /**
     * 老客户的考核同样是延期5天确认
     * 
     * @param item
     */
    private void createOldPlan(OldCustomerExamineBean item, int type)
    {
        PlanBean plan = new PlanBean();

        plan.setId(commonDAO2.getSquenceString());

        plan.setFkId(item.getId());

        if (type == ExamineConstant.EXAMINE_TYPE_EXPEND)
        {
            plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_OLDCUSTOMER);
        }
        else
        {
            plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_TER_OLDCUSTOMER);
        }

        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);

        plan.setLogTime(TimeTools.now());

        plan.setBeginTime(item.getBeginTime());

        plan.setEndTime(item.getEndTime());

        plan.setCarryTime(TimeTools.getStringByOrgAndDays(item.getEndTime(), 5));

        plan.setDescription("系统自动生成的任务");

        planDAO.saveEntityBean(plan);
    }

    /**
     * 新客户的最后确定延时5天
     * 
     * @param newCustomerExamineBean
     */
    private void createNewPlan(NewCustomerExamineBean newCustomerExamineBean, ExamineBean bean)
    {
        PlanBean plan = new PlanBean();

        plan.setId(commonDAO2.getSquenceString());

        plan.setFkId(newCustomerExamineBean.getId());

        if (bean.getType() == ExamineConstant.EXAMINE_TYPE_EXPEND)
        {
            plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_NEWCUSTOMER);
        }
        else
        {
            plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_TER_NEWCUSTOMER);
        }

        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);

        plan.setLogTime(TimeTools.now());

        plan.setBeginTime(newCustomerExamineBean.getBeginTime());

        plan.setEndTime(newCustomerExamineBean.getEndTime());

        plan.setCarryTime(TimeTools.getStringByOrgAndDays(newCustomerExamineBean.getEndTime(), 5));

        plan.setDescription("系统自动生成的任务");

        planDAO.saveEntityBean(plan);
    }

    /**
     * 业务员驳回(写日志的)
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectBean(User user, String id, String reason)
        throws MYException
    {
        // JudgeTools.judgeParameterIsNull(user, id);
        //
        // checkPassOrReject(user, id);
        //
        // examineDAO.updateStatus(id, ExamineConstant.EXAMINE_STATUS_REJECT);
        //
        // saveApproveLog(user, id, ModuleConstant.MODULE_EXAMINE, PublicConstant.OPERATION_REJECT,
        // reason);

        return true;
    }

    /**
     * 检查修改
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void checkUpdate(User user, ExamineBean bean)
        throws MYException
    {
        ExamineBean old = examineDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("考核不存在,请重新操作");
        }

        if (ExamineHelper.isReadonly(old.getStatus()))
        {
            throw new MYException("只有初始和驳回的可以修改");
        }

        if ( !user.getStafferId().equals(old.getCreaterId()))
        {
            throw new MYException("只有考核制定者可以修改");
        }

        // 信息是受到保护的
        bean.setStafferId(old.getStafferId());

        bean.setAttType(old.getAttType());

        bean.setLocationId(old.getLocationId());

        bean.setYear(old.getYear());
    }

    /**
     * 主要判断考核的状态和是否是制定者的
     * 
     * @param user
     * @param id
     * @throws MYException
     */
    private void checkDel(User user, String id)
        throws MYException
    {
        ExamineBean old = examineDAO.find(id);

        if (old == null)
        {
            throw new MYException("请确认考核是否存在");
        }

        if ( !user.getStafferId().equals(old.getCreaterId()))
        {
            throw new MYException("只有考核制定者可以删除");
        }

        if (ExamineHelper.isReadonly(old.getStatus()))
        {
            throw new MYException("只有初始和驳回的可以删除");
        }

        if (examineDAO.countByFK(old.getId()) > 0)
        {
            throw new MYException("仍有子考评,不能删除");
        }
    }

    /**
     * 主要判断考核的状态和是否是制定者的
     * 
     * @param user
     * @param id
     * @throws MYException
     */
    private void checkDel2(User user, String id)
        throws MYException
    {
        ExamineBean old = examineDAO.find(id);

        if (old == null)
        {
            throw new MYException("请确认考核是否存在");
        }

        if (old.getStatus() != ExamineConstant.EXAMINE_STATUS_PASS)
        {
            throw new MYException("只有运行的考核可以废弃");
        }

        if (old.getAttType() != ExamineConstant.EXAMINE_ATTTYPE_PERSONAL)
        {
            throw new MYException("只有个人的考核可以废弃");
        }
    }

    /**
     * checkSubmit
     * 
     * @param user
     * @param id
     * @throws MYException
     */
    private ExamineBean checkSubmit(User user, String id)
        throws MYException
    {
        ExamineBean old = examineDAO.find(id);

        if (old == null)
        {
            throw new MYException("请确认考核是否存在");
        }

        if ( !user.getStafferId().equals(old.getCreaterId()))
        {
            throw new MYException("只有考核制定者可以提交");
        }

        if (ExamineHelper.isReadonly(old.getStatus()))
        {
            throw new MYException("只有初始和驳回的可以提交");
        }

        if (old.getAbs() == ExamineConstant.EXAMINE_ABS_TRUE)
        {
            throw new MYException("抽象考核不需要提交");
        }

        List<ProfitExamineBean> pList = profitExamineDAO.queryEntityBeansByFK(id);

        // 只有拓展的有业绩考核
        if (ListTools.isEmptyOrNull(pList) && old.getType() == ExamineConstant.EXAMINE_TYPE_EXPEND)
        {
            throw new MYException("考核没有制定业绩,请补充");
        }

        checkPersonal(id, old);

        // 不是根考核
        if (old.getAttType() != ExamineConstant.EXAMINE_ATTTYPE_LOCATION)
        {
            ExamineBean parent = examineDAO.find(old.getParentId());

            if (parent == null)
            {
                throw new MYException("父级考核不存在");
            }

            checkProfit(id, old.getParentId());
        }

        return old;
    }

    /**
     * @param id
     * @param old
     * @throws MYException
     */
    private void checkPersonal(String id, ExamineBean old)
        throws MYException
    {
        if (old.getAttType() == ExamineConstant.EXAMINE_ATTTYPE_PERSONAL)
        {
            List<NewCustomerExamineBean> tem = newCustomerExamineDAO.queryEntityBeansByFK(id);

            if (ListTools.isEmptyOrNull(tem))
            {
                throw new MYException("个人考核没有制定新客户考核,请补充");
            }

            List<OldCustomerExamineBean> tem1 = oldCustomerExamineDAO.queryEntityBeansByFK(id);

            if (ListTools.isEmptyOrNull(tem1))
            {
                throw new MYException("个人考核没有制定老客户考核,请补充");
            }
        }
    }

    /**
     * 检查业绩，就是纯利是否从父级的分解出来的
     * 
     * @param id
     * @param rootExamineId
     * @throws MYException
     */
    private void checkProfit(String currentExamineId, String rootExamineId)
        throws MYException
    {
        // 根节点不做逻辑判断
        if ("0".equals(rootExamineId))
        {
            return;
        }

        // 检验提交中的业绩是否一致
        List<ProfitExamineBean> parentList = profitExamineDAO.queryEntityBeansByFK(rootExamineId);

        ConditionParse condition = new ConditionParse();

        condition.addCondition("PARENTID", "=", rootExamineId);

        // 查询子考核
        List<ExamineBean> subList = examineDAO.queryEntityBeansByCondition(condition);

        // 迭代父考核,获得相应的利润(检查利润是否下发)
        for (ProfitExamineBean profitExamineBean : parentList)
        {
            double total = profitExamineBean.getPlanValue();

            for (ExamineBean examineBean : subList)
            {
                ProfitExamineBean tem = profitExamineDAO.findByParentAndStep(examineBean.getId(),
                    profitExamineBean.getStep());

                if (tem != null)
                {
                    total = total - tem.getPlanValue();
                }
            }

            ProfitExamineBean cur = profitExamineDAO.findByParentAndStep(currentExamineId,
                profitExamineBean.getStep());

            if (cur == null)
            {
                throw new MYException("业绩考核数据不完备");
            }

            if (total < 0)
            {
                throw new MYException("子业绩考核之和在次序%d处大于父业绩考核", profitExamineBean.getStep());
            }

        }
    }

    /**
     * checkPass
     * 
     * @param user
     * @param id
     * @throws MYException
     */
    public void checkPassOrReject(User user, String id)
        throws MYException
    {
        ExamineBean old = examineDAO.find(id);

        if (old == null)
        {
            throw new MYException("请确认考核是否存在");
        }

        if ( !user.getStafferId().equals(old.getStafferId()))
        {
            throw new MYException("只有考核人可以操作");
        }

        if (old.getStatus() != ExamineConstant.EXAMINE_STATUS_SUBMIT)
        {
            throw new MYException("只有提交的考核可以操作");
        }
    }

    /**
     * 检查add是否成功
     * 
     * @param bean
     * @throws MYException
     */
    private void checkAdd(ExamineBean bean)
        throws MYException
    {
        StafferBean sb = stafferDAO.find(bean.getStafferId());

        if (sb == null)
        {
            throw new MYException("请确认考核的业务员是否存在");
        }

        if (StringTools.isNullOrNone(bean.getParentId()))
        {
            throw new MYException("请确认考核数据的完备");
        }

        if ( !sb.getLocationId().equals(bean.getLocationId()))
        {
            throw new MYException("考核分公司和职员分公司不一致,请核实");
        }

        // 在分公司考核允许一个人指定两份考核
        if (bean.getAttType() != ExamineConstant.EXAMINE_ATTTYPE_LOCATION)
        {
            if (examineDAO.countByUnique(bean.getStafferId(), bean.getYear(), bean.getAttType()) > 0)
            {
                throw new MYException("考核重复，请确定此%s是否已经制定%d年度考核", sb.getName(), bean.getYear());
            }
        }
        else
        {
            if (examineDAO.countInLocationType(bean.getStafferId(), bean.getYear(),
                bean.getAttType(), bean.getType()) > 0)
            {
                throw new MYException("考核重复2，请确定此%s是否已经制定%d年度考核", sb.getName(), bean.getYear());
            }
        }

        // 不是根考核(只是拓展的)
        checkNorRootExpend(bean);
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkNorRootExpend(ExamineBean bean)
        throws MYException
    {
        if (bean.getAttType() != ExamineConstant.EXAMINE_ATTTYPE_LOCATION
            && bean.getType() == ExamineConstant.EXAMINE_TYPE_EXPEND)
        {
            ExamineBean parent = examineDAO.find(bean.getParentId());

            if (parent == null)
            {
                throw new MYException("父级考核不存在");
            }

            // 校验父级考核的正确性
            if (bean.getAttType() == ExamineConstant.EXAMINE_ATTTYPE_DEPARTMENT
                && parent.getAttType() != ExamineConstant.EXAMINE_ATTTYPE_LOCATION)
            {
                throw new MYException("父级考核不是分公司经理考核");
            }

            if (bean.getAttType() == ExamineConstant.EXAMINE_ATTTYPE_PERSONAL
                && parent.getAttType() != ExamineConstant.EXAMINE_ATTTYPE_DEPARTMENT)
            {
                throw new MYException("父级考核不是部门经理考核");
            }

            if ( !bean.getLocationId().equals(parent.getLocationId()))
            {
                throw new MYException("父子考核不是同一分公司");
            }
        }
    }

    /**
     * @return the examineDAO
     */
    public ExamineDAO getExamineDAO()
    {
        return examineDAO;
    }

    /**
     * @param examineDAO
     *            the examineDAO to set
     */
    public void setExamineDAO(ExamineDAO examineDAO)
    {
        this.examineDAO = examineDAO;
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

    public NewCustomerExamineDAO getNewCustomerExamineDAO()
    {
        return newCustomerExamineDAO;
    }

    public void setNewCustomerExamineDAO(NewCustomerExamineDAO newCustomerExamineDAO)
    {
        this.newCustomerExamineDAO = newCustomerExamineDAO;
    }

    public OldCustomerExamineDAO getOldCustomerExamineDAO()
    {
        return oldCustomerExamineDAO;
    }

    public void setOldCustomerExamineDAO(OldCustomerExamineDAO oldCustomerExamineDAO)
    {
        this.oldCustomerExamineDAO = oldCustomerExamineDAO;
    }

    public ProfitExamineDAO getProfitExamineDAO()
    {
        return profitExamineDAO;
    }

    public void setProfitExamineDAO(ProfitExamineDAO profitExamineDAO)
    {
        this.profitExamineDAO = profitExamineDAO;
    }

    /**
     * @return the cityProfitExamineDAO
     */
    public CityProfitExamineDAO getCityProfitExamineDAO()
    {
        return cityProfitExamineDAO;
    }

    /**
     * @param cityProfitExamineDAO
     *            the cityProfitExamineDAO to set
     */
    public void setCityProfitExamineDAO(CityProfitExamineDAO cityProfitExamineDAO)
    {
        this.cityProfitExamineDAO = cityProfitExamineDAO;
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
     * @return the customerExamineLogDAO
     */
    public CustomerExamineLogDAO getCustomerExamineLogDAO()
    {
        return customerExamineLogDAO;
    }

    /**
     * @param customerExamineLogDAO
     *            the customerExamineLogDAO to set
     */
    public void setCustomerExamineLogDAO(CustomerExamineLogDAO customerExamineLogDAO)
    {
        this.customerExamineLogDAO = customerExamineLogDAO;
    }
}
