/**
 * File Name: LocationManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.StafferConstant;
import com.china.center.oa.customer.dao.StafferVSCustomerDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.tools.JudgeTools;


/**
 * StafferManager
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see StafferManager
 * @since 1.0
 */
@Bean(name = "stafferManager")
public class StafferManager
{
    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private StafferVSCustomerDAO stafferVSCustomerDAO = null;

    public StafferManager()
    {}

    /**
     * addBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean addBean(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO2.getSquenceString());

        stafferDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * updateBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        StafferBean oldBean = stafferDAO.find(bean.getId());

        if (oldBean == null)
        {
            throw new MYException("人员不存在");
        }

        bean.setName(oldBean.getName());

        bean.setCode(oldBean.getCode());

        bean.setPwkey(oldBean.getPwkey());

        stafferDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * updateBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updatePwkey(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        stafferDAO.updatePwkey(bean.getId(), bean.getPwkey());

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
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String stafferId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, stafferId);

        StafferBean bean = stafferDAO.find(stafferId);

        if (bean == null)
        {
            throw new MYException("人员不存在");
        }

        if (bean.getStatus() != StafferConstant.STATUS_COMMON)
        {
            throw new MYException("拒绝操作");
        }

        checkDelBean(stafferId);

        bean.setStatus(StafferConstant.STATUS_DROP);

        stafferDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(StafferBean bean)
        throws MYException
    {
        if (stafferDAO.countByUnique(bean.getName()) > 0)
        {
            throw new MYException("职员名称不能重复");
        }

        if (stafferDAO.countByCode(bean.getCode()) > 0)
        {
            throw new MYException("职员工号不能重复");
        }
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkDelBean(String stafferId)
        throws MYException
    {
        if ("0".equals(stafferId) || "1".equals(stafferId))
        {
            throw new MYException("拒绝操作");
        }

        // 存在人员或者user
        if (userDAO.countByStafferId(stafferId) > 0)
        {
            throw new MYException("人员下存在注册登录用户,请先删除登录用户");
        }

        // 职员下存在客户
        if (stafferVSCustomerDAO.countByFK(stafferId) > 0)
        {
            throw new MYException("职员下面还有客户,不能删除");
        }
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
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
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
     * @return the stafferVSCustomerDAO
     */
    public StafferVSCustomerDAO getStafferVSCustomerDAO()
    {
        return stafferVSCustomerDAO;
    }

    /**
     * @param stafferVSCustomerDAO
     *            the stafferVSCustomerDAO to set
     */
    public void setStafferVSCustomerDAO(StafferVSCustomerDAO stafferVSCustomerDAO)
    {
        this.stafferVSCustomerDAO = stafferVSCustomerDAO;
    }
}
