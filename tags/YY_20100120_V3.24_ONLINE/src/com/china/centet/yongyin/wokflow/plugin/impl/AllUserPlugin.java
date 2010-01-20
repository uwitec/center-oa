/**
 *
 */
package com.china.centet.yongyin.wokflow.plugin.impl;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.centet.yongyin.bean.BaseUser;
import com.china.centet.yongyin.bean.FlowBelongBean;
import com.china.centet.yongyin.bean.FlowInstanceViewBean;
import com.china.centet.yongyin.bean.FlowTokenBean;
import com.china.centet.yongyin.bean.FlowViewerBean;
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
@Bean(name = "allUserPlugin")
public class AllUserPlugin implements CommonPlugin
{
    private UserDAO userDAO = null;

    private FlowInstanceDAO flowInstanceDAO = null;

    private FlowBelongDAO flowBelongDAO = null;

    private FlowInstanceViewDAO flowInstanceViewDAO = null;

    public void processFlowInstanceBelong(String instanceId, FlowTokenBean token)
        throws MYException
    {
        flowBelongDAO.deleteEntityBeansByFK(instanceId);

        List<BaseUser> list = null;

        ConditionParse condtion = new ConditionParse();

        list = userDAO.queryEntityBeansBycondition(condtion);

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

    public String getProcesserName(String processerId)
    {
        return "全部人员";
    }

    public void processFlowInstanceViewer(String instanceId, FlowViewerBean viewer)
        throws MYException
    {
        List<BaseUser> list = null;

        ConditionParse condtion = new ConditionParse();

        list = userDAO.queryEntityBeansBycondition(condtion);

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
