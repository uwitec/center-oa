/**
 * File Name: StafferPlgin.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-24<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wokflow.plugin.impl;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.centet.yongyin.bean.BaseUser;
import com.china.centet.yongyin.bean.FlowBelongBean;
import com.china.centet.yongyin.bean.FlowInstanceBean;
import com.china.centet.yongyin.bean.FlowInstanceViewBean;
import com.china.centet.yongyin.bean.FlowTokenBean;
import com.china.centet.yongyin.bean.FlowViewerBean;
import com.china.centet.yongyin.bean.StafferBean;
import com.china.centet.yongyin.dao.FlowBelongDAO;
import com.china.centet.yongyin.dao.FlowInstanceDAO;
import com.china.centet.yongyin.dao.FlowInstanceViewDAO;
import com.china.centet.yongyin.dao.StafferDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.wokflow.plugin.CommonPlugin;


/**
 * StafferPlgin
 * 
 * @author zhuzhu
 * @version 2008-8-24
 * @see
 * @since
 */
@Bean(name = "stafferPlugin")
public class StafferPlugin implements CommonPlugin
{
    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private FlowInstanceDAO flowInstanceDAO = null;

    private FlowBelongDAO flowBelongDAO = null;

    private FlowInstanceViewDAO flowInstanceViewDAO = null;

    /**
     * default constructor
     */
    public StafferPlugin()
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
        List<BaseUser> list;
        ConditionParse condtion = new ConditionParse();

        setConstion(instanceId, processer, condtion);

        list = userDAO.queryEntityBeansBycondition(condtion);
        return list;
    }

    /**
     * @param instanceId
     * @param token
     * @param condtion
     */
    private void setConstion(String instanceId, String processer, ConditionParse condtion)
        throws MYException
    {
        String stafferId = processer;

        StafferBean staffer = stafferDAO.find(stafferId);

        if (staffer == null)
        {
            throw new MYException("职员不存在");
        }

        FlowInstanceBean bean = flowInstanceDAO.find(instanceId);

        if (bean == null)
        {
            throw new MYException("流程实例不存在");
        }

        condtion.addCondition("stafferName", "=", staffer.getName());
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
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
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
    }

    /**
     * @return the flowInstanceDAO
     */
    public FlowInstanceDAO getFlowInstanceDAO()
    {
        return flowInstanceDAO;
    }

    /**
     * @return the flowBelongDAO
     */
    public FlowBelongDAO getFlowBelongDAO()
    {
        return flowBelongDAO;
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
     * @param flowBelongDAO
     *            the flowBelongDAO to set
     */
    public void setFlowBelongDAO(FlowBelongDAO flowBelongDAO)
    {
        this.flowBelongDAO = flowBelongDAO;
    }

    public String getProcesserName(String processerId)
    {
        StafferBean stafferBean = stafferDAO.find(processerId);

        if (stafferBean != null)
        {
            return stafferBean.getName();
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
