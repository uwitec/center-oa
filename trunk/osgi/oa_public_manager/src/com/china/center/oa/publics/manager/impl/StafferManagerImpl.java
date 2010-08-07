/**
 * File Name: StafferManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-23<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager.impl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.ParentListener;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.listener.StafferListener;
import com.china.center.oa.publics.manager.StafferManager;
import com.china.center.tools.JudgeTools;


/**
 * StafferManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-6-23
 * @see StafferManagerImpl
 * @since 1.0
 */
@Exceptional
public class StafferManagerImpl implements StafferManager
{
    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private CommonDAO commonDAO = null;

    private List<StafferListener> listenerList = new ArrayList();

    public StafferManagerImpl()
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
    public synchronized boolean addBean(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO.getSquenceString());

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

        // 职员下存在客户 TODO_OSGI 这里需要stafferVSCustomerDAO注入countByFK监听
        for (StafferListener listener : listenerList)
        {
            listener.onDelete(stafferId);
        }

        // if (stafferVSCustomerDAO.countByFK(stafferId) > 0)
        // {
        // throw new MYException("职员下面还有客户,不能删除");
        // }
    }

    public void putListener(StafferListener listener)
    {
        listenerList.add(listener);
    }

    public void removeListener(String listener)
    {
        for (Iterator iterator = listenerList.iterator(); iterator.hasNext();)
        {
            ParentListener each = (ParentListener)iterator.next();

            if (each.getListenerType().equals(listener))
            {
                iterator.remove();
            }
        }
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
}
