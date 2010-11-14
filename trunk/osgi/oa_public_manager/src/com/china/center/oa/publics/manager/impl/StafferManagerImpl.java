/**
 * File Name: StafferManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-23<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager.impl;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.StafferVSPriDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.listener.StafferListener;
import com.china.center.oa.publics.manager.StafferManager;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.oa.publics.vs.StafferVSPriBean;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;


/**
 * StafferManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-6-23
 * @see StafferManagerImpl
 * @since 1.0
 */
@Exceptional
public class StafferManagerImpl extends AbstractListenerManager<StafferListener> implements StafferManager
{
    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private CommonDAO commonDAO = null;

    private StafferVSPriDAO stafferVSPriDAO = null;

    private PrincipalshipDAO principalshipDAO = null;

    public StafferManagerImpl()
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
    public synchronized boolean addBean(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO.getSquenceString());

        bean.setLever(StafferConstant.LEVER_DEFAULT);

        // save VS
        List<StafferVSPriBean> priList = bean.getPriList();

        if (priList.size() >= 1)
        {
            bean.setPrincipalshipId(priList.get(0).getPrincipalshipId());
        }

        stafferDAO.saveEntityBean(bean);

        for (StafferVSPriBean stafferVSPriBean : priList)
        {
            stafferVSPriBean.setStafferId(bean.getId());
        }

        stafferVSPriDAO.saveAllEntityBeans(priList);

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

        bean.setLever(oldBean.getLever());

        List<StafferVSPriBean> priList = bean.getPriList();

        if (priList.size() >= 1)
        {
            bean.setPrincipalshipId(priList.get(0).getPrincipalshipId());
        }

        stafferDAO.updateEntityBean(bean);

        // save VS
        stafferVSPriDAO.deleteEntityBeansByFK(bean.getId());

        for (StafferVSPriBean stafferVSPriBean : priList)
        {
            stafferVSPriBean.setStafferId(bean.getId());
        }

        stafferVSPriDAO.saveAllEntityBeans(priList);

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

    @Transactional(rollbackFor = {MYException.class})
    public boolean updateLever(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        stafferDAO.updateLever(bean.getId(), bean.getLever());

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

        stafferVSPriDAO.deleteEntityBeansByFK(stafferId);

        return true;
    }

    /**
     * 获得本职员的上级
     * 
     * @param stafferId
     * @return
     */
    public Collection<StafferVO> querySuperiorStaffer(String stafferId)
    {
        Set<StafferVO> set = new HashSet<StafferVO>();

        // 获得人员的组织结构
        List<StafferVSPriBean> vsList = stafferVSPriDAO.queryEntityBeansByFK(stafferId);

        // 循环获得所有可以操作的人员
        for (StafferVSPriBean stafferVSPriBean : vsList)
        {
            PrincipalshipBean pri = principalshipDAO.find(stafferVSPriBean.getPrincipalshipId());

            if (pri == null || StringTools.isNullOrNone(pri.getParentId()))
            {
                continue;
            }

            String parentId = pri.getParentId();

            List<StafferVSPriBean> svsp = stafferVSPriDAO.queryEntityBeansByFK(parentId,
                AnoConstant.FK_FIRST);

            for (StafferVSPriBean each : svsp)
            {
                set.add(stafferDAO.findVO(each.getStafferId()));
            }
        }

        return set;
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

        for (StafferListener listener : this.listenerMapValues())
        {
            listener.onDelete(stafferId);
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

    /**
     * @return the stafferVSPriDAO
     */
    public StafferVSPriDAO getStafferVSPriDAO()
    {
        return stafferVSPriDAO;
    }

    /**
     * @param stafferVSPriDAO
     *            the stafferVSPriDAO to set
     */
    public void setStafferVSPriDAO(StafferVSPriDAO stafferVSPriDAO)
    {
        this.stafferVSPriDAO = stafferVSPriDAO;
    }

    /**
     * @return the principalshipDAO
     */
    public PrincipalshipDAO getPrincipalshipDAO()
    {
        return principalshipDAO;
    }

    /**
     * @param principalshipDAO
     *            the principalshipDAO to set
     */
    public void setPrincipalshipDAO(PrincipalshipDAO principalshipDAO)
    {
        this.principalshipDAO = principalshipDAO;
    }
}
