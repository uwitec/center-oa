/**
 *
 */
package com.china.centet.yongyin.wokflow.plugin.impl;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.center.tools.CommonTools;
import com.china.centet.yongyin.bean.BaseUser;
import com.china.centet.yongyin.bean.FlowBelongBean;
import com.china.centet.yongyin.bean.FlowInstanceBean;
import com.china.centet.yongyin.bean.FlowInstanceViewBean;
import com.china.centet.yongyin.bean.FlowViewerBean;
import com.china.centet.yongyin.bean.FlowTokenBean;
import com.china.centet.yongyin.bean.RoleBean;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.FlowBelongDAO;
import com.china.centet.yongyin.dao.FlowInstanceDAO;
import com.china.centet.yongyin.dao.FlowInstanceViewDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.wokflow.plugin.CommonPlugin;


/**
 * 角色的Plgin
 * 
 * @author Administrator
 */
@Bean(name = "rolePlugin")
public class RolePlugin implements CommonPlugin
{
    private UserDAO userDAO = null;

    private FlowInstanceDAO flowInstanceDAO = null;

    private CommonDAO commonDAO = null;

    private FlowBelongDAO flowBelongDAO = null;

    private FlowInstanceViewDAO flowInstanceViewDAO = null;

    /**
     *
     */
    public RolePlugin()
    {}

    public void processFlowInstanceBelong(String instanceId, FlowTokenBean token)
        throws MYException
    {
        flowBelongDAO.deleteEntityBeansByFK(instanceId);

        List<BaseUser> list = getUsers(instanceId, token.getProcesser());

        for (BaseUser baseUser : list)
        {
            FlowBelongBean bean = new FlowBelongBean();

            bean.setInstanceId(instanceId);

            bean.setFlowId(token.getFlowId());

            bean.setTokenId(token.getId());

            bean.setUserId(baseUser.getId());

            flowBelongDAO.saveEntityBean(bean);
        }
    }

    /**
     * @param instanceId
     * @param token
     * @return
     * @throws MYException
     */
    private List<BaseUser> getUsers(String instanceId, String processer)
        throws MYException
    {
        ConditionParse condtion = new ConditionParse();

        setConstion(instanceId, processer, condtion);

        return userDAO.queryEntityBeansBycondition(condtion);
    }

    /**
     * @param instanceId
     * @param token
     * @param condtion
     */
    private void setConstion(String instanceId, String processer, ConditionParse condtion)
        throws MYException
    {
        int role = CommonTools.parseInt(processer);

        if (role == Constant.ROLE_TOP)
        {
            condtion.addIntCondition("role", "=", role);
        }
        else
        {
            FlowInstanceBean bean = flowInstanceDAO.find(instanceId);

            if (bean == null)
            {
                throw new MYException("流程实例不存在");
            }

            condtion.addIntCondition("role", "=", role);

            condtion.addIntCondition("locationId", "=", bean.getLocationId());
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
     * @return the flowBelongDAO
     */
    public FlowBelongDAO getFlowBelongDAO()
    {
        return flowBelongDAO;
    }

    /**
     * @param flowBelongDAO
     *            the flowBelongDAO to set
     */
    public void setFlowBelongDAO(FlowBelongDAO flowBelongDAO)
    {
        this.flowBelongDAO = flowBelongDAO;
    }

    /**
     * @return the flowInstanceDAO
     */
    public FlowInstanceDAO getFlowInstanceDAO()
    {
        return flowInstanceDAO;
    }

    /**
     * @param flowInstanceDAO
     *            the flowInstanceDAO to set
     */
    public void setFlowInstanceDAO(FlowInstanceDAO flowInstanceDAO)
    {
        this.flowInstanceDAO = flowInstanceDAO;
    }

    public String getProcesserName(String processerId)
    {
        RoleBean roleBean = commonDAO.findRoleById(processerId);

        if (roleBean != null)
        {
            return roleBean.getRoleName();
        }

        return "";
    }

    public void processFlowInstanceViewer(String instanceId, FlowViewerBean viewer)
        throws MYException
    {
        List<BaseUser> list = getUsers(instanceId, viewer.getProcesser());

        for (BaseUser baseUser : list)
        {
            FlowInstanceViewBean bean = new FlowInstanceViewBean();

            bean.setInstanceId(instanceId);

            bean.setUserId(baseUser.getId());

            if (flowInstanceViewDAO.countByUnique(instanceId, baseUser.getId()) < 1)
            {
                flowInstanceViewDAO.saveEntityBean(bean);
            }
        }
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
     * @return the flowInstanceViewDAO
     */
    public FlowInstanceViewDAO getFlowInstanceViewDAO()
    {
        return flowInstanceViewDAO;
    }

    /**
     * @param flowInstanceViewDAO
     *            the flowInstanceViewDAO to set
     */
    public void setFlowInstanceViewDAO(FlowInstanceViewDAO flowInstanceViewDAO)
    {
        this.flowInstanceViewDAO = flowInstanceViewDAO;
    }
}
