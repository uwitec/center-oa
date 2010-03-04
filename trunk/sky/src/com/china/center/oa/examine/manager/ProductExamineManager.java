/**
 * File Name: ProductExamineManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;

import java.util.ArrayList;
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
import com.china.center.oa.examine.bean.CitySailBean;
import com.china.center.oa.examine.bean.ExamineBean;
import com.china.center.oa.examine.bean.ProductCityExamineItemBean;
import com.china.center.oa.examine.bean.ProductExamineBean;
import com.china.center.oa.examine.bean.ProductExamineItemBean;
import com.china.center.oa.examine.dao.CityConfigDAO;
import com.china.center.oa.examine.dao.CityProfitExamineDAO;
import com.china.center.oa.examine.dao.CitySailDAO;
import com.china.center.oa.examine.dao.ExamineDAO;
import com.china.center.oa.examine.dao.ProductCityExamineItemDAO;
import com.china.center.oa.examine.dao.ProductExamineDAO;
import com.china.center.oa.examine.helper.ExamineHelper;
import com.china.center.oa.examine.vo.CityConfigVO;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.dao.PlanDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.oa.tools.OATools;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
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
    
    private CommonDAO2 commonDAO2 = null;
    
    private CityProfitExamineDAO cityProfitExamineDAO = null;
    
    private ExamineDAO examineDAO = null;
    
    private CityConfigDAO cityConfigDAO = null;
    
    private CitySailDAO citySailDAO = null;
    
    private ProductCityExamineItemDAO productCityExamineItemDAO = null;
    
    private LogDAO logDAO = null;
    
    private PlanDAO planDAO = null;
    
    public ProductExamineManager()
    {
    }
    
    /**
     * 增加产品考核
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = { MYException.class })
    public boolean addBean(User user, ProductExamineBean bean,
            String[] stafferIdList) throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);
        
        checkAdd(user, bean, stafferIdList);
        
        bean.setId(commonDAO2.getSquenceString());
        
        bean.setStatus(ExamineConstant.EXAMINE_STATUS_INIT);
        
        bean.setLogTime(TimeTools.now());
        
        productExamineDAO.saveEntityBean(bean);
        
        // 增加默认的考核项
        for (ProductCityExamineItemBean item : bean.getItems())
        {
            item.setId(commonDAO2.getSquenceString());
            
            item.setPid(bean.getId());
            
            productCityExamineItemDAO.saveEntityBean(item);
        }
        
        return true;
    }
    
    /**
     * delBean
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = { MYException.class })
    public boolean delBean(User user, String id) throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);
        
        checkDel(id);
        
        productExamineDAO.deleteEntityBean(id);
        
        productCityExamineItemDAO.deleteEntityBeansByFK(id);
        
        return true;
    }
    
    /**
     * 更新所有的考评状态
     * 
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = { MYException.class })
    public int updateAllStatus()
    {
        // 先查询出结束时间小于今天的产品考评
        ConditionParse condition = new ConditionParse();
        
        condition.addCondition("endTime", "<=", TimeTools.now());
        
        // 运行中的
        condition.addCondition("status",
                "=",
                ExamineConstant.EXAMINE_STATUS_PASS);
        
        List<ProductExamineBean> list = productExamineDAO.queryEntityBeansByCondition(condition);
        
        int total = 0;
        
        // 循环处理
        for (ProductExamineBean productExamineBean : list)
        {
            // 看看下面是否都结束了
            List<ProductCityExamineItemBean> temp = productCityExamineItemDAO.queryEntityBeansByFK(productExamineBean.getId());
            
            boolean isEnd = true;
            
            for (ProductCityExamineItemBean productExamineItemBean : temp)
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
                
                total++;
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
    @Transactional(rollbackFor = { MYException.class })
    public boolean configProductExamine(User user, String parentId,
            List<ProductExamineItemBean> items) throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, items);
        
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
    @Transactional(rollbackFor = { MYException.class })
    public boolean submitBean(User user, String id) throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);
        
        checkSubmit(user, id);
        
        // 更新考核到运行
        productExamineDAO.updateStatus(id, ExamineConstant.EXAMINE_STATUS_PASS);
        
        // 生成执行计划(核心的一步)
        createPlans(id);
        
        saveApproveLog(user,
                id,
                ModuleConstant.MODULE_EXAMINE,
                PublicConstant.OPERATION_SUBMIT,
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
        
        List<ProductCityExamineItemBean> pList = productCityExamineItemDAO.queryEntityBeansByFK(id);
        
        for (ProductCityExamineItemBean item : pList)
        {
            createProductPlan(item, bean);
        }
    }
    
    /**
     * 产品考核的最后确定延时1天
     * 
     * @param item
     */
    private void createProductPlan(ProductCityExamineItemBean item,
            ProductExamineBean bean)
    {
        PlanBean plan = new PlanBean();
        
        plan.setId(commonDAO2.getSquenceString());
        
        plan.setFkId(item.getId());
        
        plan.setType(ExamineConstant.EXAMINE_ITEM_TYPE_PRODUCT2);
        
        plan.setCarryType(PlanConstant.PLAN_CARRYTYPES_EVERYDAY);
        
        plan.setStatus(PlanConstant.PLAN_STATUS_INIT);
        
        plan.setLogTime(TimeTools.now());
        
        plan.setBeginTime(bean.getBeginTime());
        
        plan.setEndTime(bean.getEndTime());
        
        plan.setCarryTime(TimeTools.getStringByOrgAndDays(bean.getEndTime(), 1));
        
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
    private void checkSubmit(User user, String id) throws MYException
    {
        ProductExamineBean old = productExamineDAO.find(id);
        
        if (old == null)
        {
            throw new MYException("请确认考核是否存在");
        }
        
        if (!user.getStafferId().equals(old.getCreaterId()))
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
    private void checkAdd(User user, ProductExamineBean bean,
            String[] stafferIdList) throws MYException
    {
        List<ProductCityExamineItemBean> itemList = new ArrayList();
        
        bean.setItems(itemList);
        
        //获取当前的财务年度
        int financeYear = OATools.getFinanceYear();
        
        //循环所有的考评职员
        for (String stafferId : stafferIdList)
        {
            ExamineBean examineBean = examineDAO.findByStafferIdAndYear(stafferId,
                    financeYear);
            
            if (examineBean == null)
            {
                throw new MYException("当前系统内没有职员[%s]的个人考评,或者考评没有批准",
                        user.getStafferName());
            }
            
            //查询是否存在当前财务年度的区域考核指标(考核指标在个人考核里面)
            List<String> list = cityProfitExamineDAO.queryDistinctCityByparentId(examineBean.getId());
            
            if (ListTools.isEmptyOrNull(list))
            {
                throw new MYException("当前系统内职员[%s]的个人考评没有选择区域",
                        user.getStafferName());
            }
            
            for (String cityId : list)
            {
                CityConfigVO cityConfig = cityConfigDAO.findVO(cityId);
                
                if (cityConfig == null)
                {
                    throw new MYException("区域缺少配置");
                }
                
                if (StringTools.isNullOrNone(cityConfig.getBespread()))
                {
                    throw new MYException("区域[%s]缺少铺样配置",
                            cityConfig.getCityName());
                }
                
                CitySailBean citySail = citySailDAO.find(cityConfig.getBespread());
                
                if (citySail == null)
                {
                    throw new MYException("区域[%d]铺样不存在",
                            cityConfig.getCityName());
                }
                
                ProductCityExamineItemBean item = new ProductCityExamineItemBean();
                
                item.setCityId(cityId);
                
                item.setStafferId(stafferId);
                
                item.setProductId(bean.getProductId());
                
                item.setBeginTime(bean.getBeginTime());
                
                item.setEndTime(bean.getEndTime());
                
                item.setPlanValue(citySail.getAmount() / citySail.getMonth()
                        * bean.getMonth());
                
                itemList.add(item);
            }
        }
    }
    
    /**
     * checkDel
     * 
     * @param id
     * @throws MYException
     */
    private void checkDel(String id) throws MYException
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
     * 写数据库日志
     * 
     * @param user
     * @param id
     */
    private void saveApproveLog(User user, String id, String module,
            String operation, String logs)
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
    
    public CityProfitExamineDAO getCityProfitExamineDAO()
    {
        return cityProfitExamineDAO;
    }
    
    public void setCityProfitExamineDAO(
            CityProfitExamineDAO cityProfitExamineDAO)
    {
        this.cityProfitExamineDAO = cityProfitExamineDAO;
    }
    
    public ExamineDAO getExamineDAO()
    {
        return examineDAO;
    }
    
    public void setExamineDAO(ExamineDAO examineDAO)
    {
        this.examineDAO = examineDAO;
    }
    
    public ProductCityExamineItemDAO getProductCityExamineItemDAO()
    {
        return productCityExamineItemDAO;
    }
    
    public void setProductCityExamineItemDAO(
            ProductCityExamineItemDAO productCityExamineItemDAO)
    {
        this.productCityExamineItemDAO = productCityExamineItemDAO;
    }
    
    public CityConfigDAO getCityConfigDAO()
    {
        return cityConfigDAO;
    }
    
    public void setCityConfigDAO(CityConfigDAO cityConfigDAO)
    {
        this.cityConfigDAO = cityConfigDAO;
    }
    
    public CitySailDAO getCitySailDAO()
    {
        return citySailDAO;
    }
    
    public void setCitySailDAO(CitySailDAO citySailDAO)
    {
        this.citySailDAO = citySailDAO;
    }
}
