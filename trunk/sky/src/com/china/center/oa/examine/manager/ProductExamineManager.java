/**
 * File Name: ProductExamineManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
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
import com.china.center.oa.examine.bean.ProductExamineBean;
import com.china.center.oa.examine.bean.ProductExamineItemBean;
import com.china.center.oa.examine.dao.ProductExamineDAO;
import com.china.center.oa.examine.dao.ProductExamineItemDAO;
import com.china.center.oa.examine.helper.ExamineHelper;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.dao.PlanDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;


/**
 * ProductExamineManager
 * 
 * @author zhuzhu
 * @version 2009-2-14
 * @see ProductExamineManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "productExamineManager")
public class ProductExamineManager
{
    private ProductExamineDAO productExamineDAO = null;

    private ProductExamineItemDAO productExamineItemDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private LogDAO logDAO = null;

    private PlanDAO planDAO = null;

    public ProductExamineManager()
    {}

    /**
     * 增加产品考核
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, ProductExamineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAdd(bean);

        bean.setId(commonDAO2.getSquenceString());

        bean.setStatus(ExamineConstant.EXAMINE_STATUS_INIT);

        bean.setLogTime(TimeTools.now());

        productExamineDAO.saveEntityBean(bean);

        // 增加默认的考核项
        for (ProductExamineItemBean item : bean.getItems())
        {
            item.setId(commonDAO2.getSquenceString());

            item.setPid(bean.getId());

            productExamineItemDAO.saveEntityBean(item);
        }

        return true;
    }

    /**
     * 修改产品考核
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, ProductExamineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkUpdate(bean);

        bean.setStatus(ExamineConstant.EXAMINE_STATUS_INIT);

        productExamineDAO.updateEntityBean(bean);

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDel(id);

        productExamineDAO.deleteEntityBean(id);

        productExamineItemDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /**
     * 更新所有的考评状态
     * 
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public int updateAllStatus()
    {
        // 先查询出结束时间小于今天的产品考评
        ConditionParse condition = new ConditionParse();

        condition.addCondition("endTime", "<=", TimeTools.now());

        // 运行中的
        condition.addCondition("status", "=", ExamineConstant.EXAMINE_STATUS_PASS);

        List<ProductExamineBean> list = productExamineDAO.queryEntityBeansByCondition(condition);

        int total = 0;
        
        // 循环处理
        for (ProductExamineBean productExamineBean : list)
        {
            // 看看下面是否都结束了
            List<ProductExamineItemBean> temp = productExamineItemDAO.queryEntityBeansByFK(productExamineBean.getId());

            boolean isEnd = true;

            for (ProductExamineItemBean productExamineItemBean : temp)
            {
                if (productExamineItemBean.getStatus() != ExamineConstant.EXAMINE_ITEM_STATUS_END)
                {
                    isEnd = false;

                    break;
                }
            }

            if (isEnd)
            {
                // 结束产品考核
                productExamineDAO.updateStatus(productExamineBean.getId(),
                    ExamineConstant.EXAMINE_STATUS_END);

                total++ ;
            }
        }

        return total;
    }

    /**
     * 配置产品考核
     * 
     * @param user
     * @param parentId
     * @param items
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean configProductExamine(User user, String parentId,
                                        List<ProductExamineItemBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, items);

        checkConfig(parentId);

        productExamineItemDAO.deleteEntityBeansByFK(parentId);

        for (ProductExamineItemBean item : items)
        {
            item.setId(commonDAO2.getSquenceString());

            item.setPid(parentId);

            productExamineItemDAO.saveEntityBean(item);
        }

        return true;
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

        checkSubmit(user, id);

        // 更新考核到运行
        productExamineDAO.updateStatus(id, ExamineConstant.EXAMINE_STATUS_PASS);

        // 生成执行计划(核心的一步)
        createPlans(id);

        saveApproveLog(user, id, ModuleConstant.MODULE_EXAMINE, PublicConstant.OPERATION_SUBMIT,
            "考核制定后提交确认,进入运行状态");

        return true;
    }

    /**
     * 生成考评执行计划--计划类型5
     * 
     * @param id
     */
    private void createPlans(String id)
    {
        ProductExamineBean bean = productExamineDAO.find(id);

        List<ProductExamineItemBean> pList = productExamineItemDAO.queryEntityBeansByFK(id);

        for (ProductExamineItemBean item : pList)
        {
            createProductPlan(item, bean);
        }
    }

    /**
     * 产品考核的最后确定延时5天
     * 
     * @param item
     */
    private void createProductPlan(ProductExamineItemBean item, ProductExamineBean bean)
    {
        PlanBean plan = new PlanBean();

        plan.setId(commonDAO2.getSquenceString());

        plan.setFkId(item.getId());

        plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_PRODUCT);

        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);

        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);

        plan.setLogTime(TimeTools.now());

        plan.setBeginTime(bean.getBeginTime());

        plan.setEndTime(bean.getEndTime());

        plan.setCarryTime(TimeTools.getStringByOrgAndDays(bean.getEndTime(), 5));

        plan.setDescription("系统自动生成的任务");

        planDAO.saveEntityBean(plan);
    }

    /**
     * checkSubmit
     * 
     * @param user
     * @param id
     * @throws MYException
     */
    private void checkSubmit(User user, String id)
        throws MYException
    {
        ProductExamineBean old = productExamineDAO.find(id);

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
            throw new MYException("只有初始态的可以提交");
        }
    }

    /**
     * 增加的业务逻辑检查
     * 
     * @param bean
     * @throws MYException
     */
    private void checkAdd(ProductExamineBean bean)
        throws MYException
    {}

    /**
     * checkUpdate
     * 
     * @param bean
     * @throws MYException
     */
    private void checkUpdate(ProductExamineBean bean)
        throws MYException
    {}

    /**
     * checkDel
     * 
     * @param id
     * @throws MYException
     */
    private void checkDel(String id)
        throws MYException
    {
        ProductExamineBean old = productExamineDAO.find(id);

        if (old == null)
        {
            throw new MYException("产品考核不存在,请确认");
        }

        if (ExamineHelper.isReadonly(old.getStatus()))
        {
            throw new MYException("只有初始态可以删除");
        }
    }

    /**
     * 配置检查
     * 
     * @param id
     * @throws MYException
     */
    private void checkConfig(String id)
        throws MYException
    {
        ProductExamineBean parent = productExamineDAO.find(id);

        if (parent == null)
        {
            throw new MYException("产品考核不存在,请确认");
        }

        if (ExamineHelper.isReadonly(parent.getStatus()))
        {
            throw new MYException("只有初始态可以配置");
        }
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
     * @return the productExamineDAO
     */
    public ProductExamineDAO getProductExamineDAO()
    {
        return productExamineDAO;
    }

    /**
     * @param productExamineDAO
     *            the productExamineDAO to set
     */
    public void setProductExamineDAO(ProductExamineDAO productExamineDAO)
    {
        this.productExamineDAO = productExamineDAO;
    }

    /**
     * @return the productExamineItemDAO
     */
    public ProductExamineItemDAO getProductExamineItemDAO()
    {
        return productExamineItemDAO;
    }

    /**
     * @param productExamineItemDAO
     *            the productExamineItemDAO to set
     */
    public void setProductExamineItemDAO(ProductExamineItemDAO productExamineItemDAO)
    {
        this.productExamineItemDAO = productExamineItemDAO;
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
}
