/**
 *
 */
package com.china.centet.yongyin.wokflow.plugin.impl;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.MYException;
import com.china.centet.yongyin.bean.FlowBelongBean;
import com.china.centet.yongyin.bean.FlowInstanceViewBean;
import com.china.centet.yongyin.bean.FlowTokenBean;
import com.china.centet.yongyin.bean.FlowViewerBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.dao.FlowBelongDAO;
import com.china.centet.yongyin.dao.FlowInstanceDAO;
import com.china.centet.yongyin.dao.FlowInstanceViewDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.wokflow.plugin.CommonPlugin;


/**
 * ½ÇÉ«µÄPlgin
 * 
 * @author Administrator
 */
@Bean(name = "userPlugin")
public class UserPlugin implements CommonPlugin
{
    private UserDAO userDAO = null;

    private FlowInstanceDAO flowInstanceDAO = null;

    private FlowBelongDAO flowBelongDAO = null;

    private FlowInstanceViewDAO flowInstanceViewDAO = null;

    /**
     *
     */
    public UserPlugin()
    {}

    public void processFlowInstanceBelong(String instanceId, FlowTokenBean token)
        throws MYException
    {
        flowBelongDAO.deleteEntityBeansByFK(instanceId);

        FlowBelongBean bean = new FlowBelongBean();

        bean.setInstanceId(instanceId);

        bean.setFlowId(token.getFlowId());

        bean.setTokenId(token.getId());

        bean.setUserId(token.getProcesser());

        flowBelongDAO.saveEntityBean(bean);
    }

    public String getProcesserName(String processerId)
    {
        User user = userDAO.findUserById(processerId);

        if (user != null)
        {
            return user.getName();
        }

        return "";
    }

    public void processFlowInstanceViewer(String instanceId, FlowViewerBean viewer)
        throws MYException
    {
        FlowInstanceViewBean bean = new FlowInstanceViewBean();

        bean.setInstanceId(instanceId);

        bean.setUserId(viewer.getProcesser());

        if (flowInstanceViewDAO.countByUnique(instanceId, viewer.getProcesser()) < 1)
        {
            flowInstanceViewDAO.saveEntityBean(bean);
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
