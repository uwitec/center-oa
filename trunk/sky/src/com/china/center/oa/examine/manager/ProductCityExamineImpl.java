/**
 * File Name: NewCustomerExamineImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;

import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.examine.bean.CustomerExamineLogBean;
import com.china.center.oa.examine.bean.ProductCityExamineItemBean;
import com.china.center.oa.examine.bean.ProductExamineBean;
import com.china.center.oa.examine.dao.CustomerExamineLogDAO;
import com.china.center.oa.examine.dao.ProductCityExamineItemDAO;
import com.china.center.oa.examine.dao.ProductExamineDAO;
import com.china.center.oa.examine.wrap.CustomerWrap;
import com.china.center.oa.plan.bean.PlanBean;
import com.china.center.oa.plan.manager.CarryPlan;
import com.china.center.oa.plan.manager.PlanManager;
import com.china.center.tools.StringTools;

/**
 * 产品区域铺样的实现
 * 
 * @author ZHUZHU
 * @version 2009-1-11
 * @see ProductCityExamineImpl
 * @since 1.0
 */
@Exceptional
@Bean(name = "productCityExamineImpl", initMethod = "init")
public class ProductCityExamineImpl implements CarryPlan
{
    private ProductExamineDAO productExamineDAO = null;
    
    private ProductCityExamineItemDAO productCityExamineItemDAO = null;
    
    private CustomerDAO customerDAO = null;
    
    private CustomerExamineLogDAO customerExamineLogDAO = null;
    
    /**
     * default constructor
     */
    public ProductCityExamineImpl()
    {
    }
    
    /**
     * 初始化注入
     */
    public void init()
    {
        PlanManager.addCarryPlan("productCityExamineImpl");
    }
    
    /**
     * 实施计划
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean carryPlan(PlanBean plan, boolean end)
    {
        String itemId = plan.getFkId();
        
        if (StringTools.isNullOrNone(itemId))
        {
            return true;
        }
        
        ProductCityExamineItemBean item = productCityExamineItemDAO.find(itemId);
        
        if (item == null)
        {
            return true;
        }
        
        // 状态如果已经结束,则不执行
        if (item.getStatus() == ExamineConstant.EXAMINE_ITEM_STATUS_END)
        {
            return true;
        }
        
        ProductExamineBean examine = productExamineDAO.find(item.getPid());
        
        if (examine == null)
        {
            return true;
        }
        
        // 获得考核的职员和时间，在out里面查询到符合记录的数据
        List<CustomerWrap> clist = productExamineDAO.queryProductOutCustomerByCondition(examine.getProductId(),
                item.getStafferId(),
                examine.getBeginTime(),
                examine.getEndTime(), item.getCityId());
        
        // 删除临时log
        customerExamineLogDAO.deleteEntityBeansByFK(itemId);
        
        //产品区域的成交总数
        int total = 0;
        
        for (CustomerWrap customerBean : clist)
        {
            total += customerBean.getAmount();
            
            saveLog(plan, itemId, customerBean);
        }
        
        item.setRealValue(total);
        
        if (end)
        {
            processResult(item);
            
            // 对于产品考核来说所有的考核时间一致(直接把主考评结束掉)
            if (examine.getStatus() != ExamineConstant.EXAMINE_STATUS_END)
            {
                productExamineDAO.updateStatus(examine.getId(),
                        ExamineConstant.EXAMINE_STATUS_END);
            }
        }
        else
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_INIT);
        }
        
        // 修改
        productCityExamineItemDAO.updateEntityBean(item);
        
        return true;
    }
    
    /**
     * @param item
     */
    private void processResult(ProductCityExamineItemBean item)
    {
        // 超过预期
        if (item.getRealValue() > item.getPlanValue())
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_GOOD);
        }
        
        if (item.getRealValue() == item.getPlanValue())
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_OK);
        }
        
        if (item.getRealValue() < item.getPlanValue())
        {
            item.setResult(ExamineConstant.EXAMINE_RESULT_LESS);
        }
        
        item.setStatus(ExamineConstant.EXAMINE_ITEM_STATUS_END);
    }
    
    /**
     * 记入日志，方便考核查询
     * 
     * @param plan
     * @param item
     * @param customerBean
     */
    private void saveLog(PlanBean plan, String itemId, CustomerWrap customerBean)
    {
        CustomerExamineLogBean log = new CustomerExamineLogBean();
        
        log.setPlanId(plan.getId());
        
        log.setItemId(itemId);
        
        log.setCustomerId(customerBean.getId());
        
        log.setCustomerName(customerBean.getName());
        
        log.setOutId(customerBean.getOutId());
        
        log.setLogTime(customerBean.getLogTime());
        
        log.setAmount(customerBean.getAmount());
        
        customerExamineLogDAO.saveEntityBean(log);
    }
    
    /**
     * 产品考核的标志
     */
    public int getPlanType()
    {
        return ExamineConstant.EXAMINE_ITEM_TYPE_PRODUCT2;
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
    public void setCustomerExamineLogDAO(
            CustomerExamineLogDAO customerExamineLogDAO)
    {
        this.customerExamineLogDAO = customerExamineLogDAO;
    }
    
    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }
    
    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
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
}
