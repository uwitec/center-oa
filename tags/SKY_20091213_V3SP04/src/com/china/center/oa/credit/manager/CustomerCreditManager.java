/**
 * File Name: CustomerCreditManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.manager;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.CreditConstant;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.credit.bean.CreditlogBean;
import com.china.center.oa.credit.dao.CreditItemThrDAO;
import com.china.center.oa.credit.dao.CreditLevelDAO;
import com.china.center.oa.credit.dao.CreditlogDAO;
import com.china.center.oa.credit.dao.CustomerCreditApplyDAO;
import com.china.center.oa.credit.dao.CustomerCreditDAO;
import com.china.center.oa.credit.vs.CustomerCreditApplyBean;
import com.china.center.oa.credit.vs.CustomerCreditBean;
import com.china.center.oa.customer.bean.CustomerApplyBean;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.dao.CustomerApplyDAO;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.publics.User;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.TimeTools;


/**
 * CustomerCreditManager
 * 
 * @author ZHUZHU
 * @version 2009-11-8
 * @see CustomerCreditManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "customerCreditManager")
public class CustomerCreditManager
{
    private CustomerCreditDAO customerCreditDAO = null;

    private CustomerCreditApplyDAO customerCreditApplyDAO = null;

    private CreditLevelDAO creditLevelDAO = null;

    private CustomerDAO customerDAO = null;

    private CreditlogDAO creditlogDAO = null;

    private CreditItemThrDAO creditItemThrDAO = null;

    private CustomerApplyDAO customerApplyDAO = null;

    /**
     * default constructor
     */
    public CustomerCreditManager()
    {}

    /**
     * 设置客户等级
     * 
     * @param user
     * @param cid
     * @param creditList
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean configSpecialCredit(User user, String ccode, List<CustomerCreditBean> creditList)
        throws MYException
    {
        return false;
    }

    /**
     * configCustomerCredit
     * 
     * @param user
     * @param data
     * @return boolean
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean configCustomerCredit(User user, String cid, List<CustomerCreditBean> creditList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, creditList);

        for (CustomerCreditBean customerCreditBean : creditList)
        {
            customerCreditBean.setCid(cid);

            CustomerCreditBean old = customerCreditDAO.findByUnique(customerCreditBean.getCid(),
                customerCreditBean.getItemId());

            customerCreditBean.setLogTime(TimeTools.now());

            if (old == null)
            {
                customerCreditDAO.saveEntityBean(customerCreditBean);
            }
            else
            {
                customerCreditBean.setId(old.getId());

                // update
                customerCreditDAO.updateEntityBean(customerCreditBean);
            }

            // dynamic add log
            saveLog(user, customerCreditBean);
        }

        updateLevel(cid);

        return true;
    }

    /**
     * updateLevel
     * 
     * @param cid
     */
    private void updateLevel(String cid)
    {
        // sum value
        int sum = (int)Math.ceil(customerCreditDAO.sumValByFK(cid));

        // 不能是负数
        if (sum < 0)
        {
            sum = 0;
        }

        // 不能超过100
        if (sum >= 100)
        {
            sum = 100;
        }

        CreditLevelBean findByVal = creditLevelDAO.findByVal(sum);

        String level = CustomerConstant.CREDITLEVELID_DEFAULT;

        if (findByVal != null)
        {
            level = findByVal.getId();
        }

        // update customer
        customerDAO.updateCustomerCredit(cid, level, sum);
    }

    /**
     * interposeCredit
     * 
     * @param user
     * @param cid
     * @param creditList
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean interposeCredit(User user, String cid, double newCreditVal)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        // less than 100
        newCreditVal = Math.min(100.0d, newCreditVal);

        CustomerCreditBean customerCreditBean = new CustomerCreditBean();

        // sum value
        double sum = Math.ceil(customerCreditDAO.sumValExceptPersonByFK(cid));

        customerCreditBean.setCid(cid);

        customerCreditBean.setVal(newCreditVal - sum);

        customerCreditBean.setLogTime(TimeTools.now());

        customerCreditBean.setPtype(CreditConstant.CREDIT_TYPE_DYNAMIC);

        customerCreditBean.setItemId(CreditConstant.SET_DRECT);

        customerCreditBean.setLog(user.getStafferName() + "人为干预等级,直接加分到:"
                                  + MathTools.formatNum(newCreditVal));

        customerCreditBean.setPitemId("0");

        customerCreditBean.setValueId("0");

        return interposeCredit(user, cid, customerCreditBean);
    }

    /**
     * interposeCredit
     * 
     * @param user
     * @param cid
     * @param customerCreditBean
     * @return boolean
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean interposeCredit(User user, String cid, CustomerCreditBean customerCreditBean)
        throws MYException
    {
        return interposeCreditInner(user, cid, customerCreditBean);
    }

    /**
     * interposeCreditInner(None Transaction)
     * 
     * @param user
     * @param cid
     * @param customerCreditBean
     * @return
     * @throws MYException
     */
    public boolean interposeCreditInner(User user, String cid,
                                        CustomerCreditBean customerCreditBean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid, customerCreditBean);

        CustomerCreditBean drectBean = customerCreditDAO.findByUnique(cid,
            customerCreditBean.getItemId());

        double sumVal = customerCreditDAO.sumValExceptionByFK(cid, customerCreditBean.getItemId());

        double total = sumVal + customerCreditBean.getVal();

        // 越界了
        if (total < 0.0)
        {
            // 防止越界
            customerCreditBean.setVal( -sumVal);
        }

        if (total > 100.0)
        {
            double minus = 100.0d - sumVal;

            // 防止越界
            customerCreditBean.setVal(minus);
        }

        if (drectBean == null)
        {
            customerCreditDAO.saveEntityBean(customerCreditBean);
        }
        else
        {
            customerCreditBean.setId(drectBean.getId());

            customerCreditDAO.updateEntityBean(customerCreditBean);
        }

        // dynamic add log
        saveLog(user, customerCreditBean);

        updateLevel(cid);

        return true;
    }

    /**
     * saveLog
     * 
     * @param user
     * @param customerCreditBean
     */
    private void saveLog(User user, CustomerCreditBean customerCreditBean)
    {
        if (customerCreditBean.getPtype() == CreditConstant.CREDIT_TYPE_DYNAMIC)
        {
            CreditlogBean log = new CreditlogBean();

            log.setStafferId(user.getStafferId());
            log.setLocationId(user.getLocationId());
            log.setLog(customerCreditBean.getLog());
            log.setCid(customerCreditBean.getCid());
            log.setLogTime(TimeTools.now());
            log.setVal(customerCreditBean.getVal());

            creditlogDAO.saveEntityBean(log);
        }
    }

    /**
     * applyConfigStaticCustomerCredit
     * 
     * @param user
     * @param cid
     * @param creditList
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean applyConfigStaticCustomerCredit(User user, String cid,
                                                   List<CustomerCreditApplyBean> creditList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, creditList);

        CustomerBean bean = checkConfig(cid);

        for (CustomerCreditApplyBean customerCreditBean : creditList)
        {
            customerCreditBean.setCid(cid);

            customerCreditBean.setLogTime(TimeTools.now());
        }

        customerCreditApplyDAO.saveAllEntityBeans(creditList);

        saveApply(user, bean);

        return true;
    }

    /**
     * doPassApplyConfigStaticCustomerCredit
     * 
     * @param user
     * @param cid
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean doPassApplyConfigStaticCustomerCredit(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        List<CustomerCreditApplyBean> creditApplyList = customerCreditApplyDAO.queryEntityBeansByFK(cid);

        List<CustomerCreditBean> creditList = new ArrayList();

        for (CustomerCreditApplyBean customerCreditApplyBean : creditApplyList)
        {
            CustomerCreditBean bean = new CustomerCreditBean();

            BeanUtil.copyProperties(bean, customerCreditApplyBean);

            creditList.add(bean);
        }

        configCustomerCredit(user, cid, creditList);

        customerCreditApplyDAO.deleteEntityBeansByFK(cid);

        customerApplyDAO.deleteEntityBean(cid);

        return true;
    }

    /**
     * doRejectApplyConfigStaticCustomerCredit
     * 
     * @param user
     * @param cid
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean doRejectApplyConfigStaticCustomerCredit(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        customerCreditApplyDAO.deleteEntityBeansByFK(cid);

        customerApplyDAO.deleteEntityBean(cid);

        return true;
    }

    /**
     * saveApply
     * 
     * @param user
     * @param bean
     */
    private void saveApply(User user, CustomerBean bean)
    {
        CustomerApplyBean apply = new CustomerApplyBean();

        BeanUtil.copyProperties(apply, bean);

        apply.setStatus(CustomerConstant.STATUS_APPLY);

        apply.setOpr(CustomerConstant.OPR_UPATE_CREDIT);

        apply.setLocationId(user.getLocationId());

        apply.setLoginTime(TimeTools.now());

        apply.setUpdaterId(user.getStafferId());

        customerApplyDAO.saveEntityBean(apply);
    }

    /**
     * checkConfig
     * 
     * @param cid
     * @return
     * @throws MYException
     */
    private CustomerBean checkConfig(String cid)
        throws MYException
    {
        if (customerCreditApplyDAO.hasUpdate(cid))
        {
            throw new MYException("此客户的静态属性已经在申请中");
        }

        CustomerBean bean = customerDAO.find(cid);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }
        return bean;
    }

    /**
     * @return the customerCreditDAO
     */
    public CustomerCreditDAO getCustomerCreditDAO()
    {
        return customerCreditDAO;
    }

    /**
     * @param customerCreditDAO
     *            the customerCreditDAO to set
     */
    public void setCustomerCreditDAO(CustomerCreditDAO customerCreditDAO)
    {
        this.customerCreditDAO = customerCreditDAO;
    }

    /**
     * @return the creditLevelDAO
     */
    public CreditLevelDAO getCreditLevelDAO()
    {
        return creditLevelDAO;
    }

    /**
     * @param creditLevelDAO
     *            the creditLevelDAO to set
     */
    public void setCreditLevelDAO(CreditLevelDAO creditLevelDAO)
    {
        this.creditLevelDAO = creditLevelDAO;
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

    /**
     * @return the creditItemThrDAO
     */
    public CreditItemThrDAO getCreditItemThrDAO()
    {
        return creditItemThrDAO;
    }

    /**
     * @param creditItemThrDAO
     *            the creditItemThrDAO to set
     */
    public void setCreditItemThrDAO(CreditItemThrDAO creditItemThrDAO)
    {
        this.creditItemThrDAO = creditItemThrDAO;
    }

    /**
     * @return the customerCreditApplyDAO
     */
    public CustomerCreditApplyDAO getCustomerCreditApplyDAO()
    {
        return customerCreditApplyDAO;
    }

    /**
     * @param customerCreditApplyDAO
     *            the customerCreditApplyDAO to set
     */
    public void setCustomerCreditApplyDAO(CustomerCreditApplyDAO customerCreditApplyDAO)
    {
        this.customerCreditApplyDAO = customerCreditApplyDAO;
    }

    /**
     * @return the customerApplyDAO
     */
    public CustomerApplyDAO getCustomerApplyDAO()
    {
        return customerApplyDAO;
    }

    /**
     * @param customerApplyDAO
     *            the customerApplyDAO to set
     */
    public void setCustomerApplyDAO(CustomerApplyDAO customerApplyDAO)
    {
        this.customerApplyDAO = customerApplyDAO;
    }

    /**
     * @return the creditlogDAO
     */
    public CreditlogDAO getCreditlogDAO()
    {
        return creditlogDAO;
    }

    /**
     * @param creditlogDAO
     *            the creditlogDAO to set
     */
    public void setCreditlogDAO(CreditlogDAO creditlogDAO)
    {
        this.creditlogDAO = creditlogDAO;
    }
}
