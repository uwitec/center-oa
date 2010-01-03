/*
 * File Name: SaveOutAction.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wokflow.sale.action;


import java.util.Map;

import org.jbpm.JbpmConfiguration;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;

import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.manager.OutManager;
import com.china.centet.yongyin.wokflow.JbpmHelper;


/**
 * 保存销售单
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class SaveOutAction implements ActionHandler
{
    private OutManager outManager = null;

    private JbpmConfiguration jbpmConfiguration = null;

    /**
     * 保存销售单
     */
    public void execute(ExecutionContext exc)
        throws Exception
    {
        ProcessInstance processInstance = exc.getProcessInstance();

        Map dataMap = null;

        OutBean outBean = null;

        User user = null;

        JbpmHelper helper = new JbpmHelper(exc);

        // 获得request里面的数据
        dataMap = helper.get("dataMap", Map.class);

        outBean = helper.get("outBean", OutBean.class);

        user = helper.get("user", User.class);

        String fullId = outManager.addOut(outBean, dataMap, user);

        processInstance.getContextInstance().setVariable("fullId", fullId);
    }

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }

    /**
     * @return the jbpmConfiguration
     */
    public JbpmConfiguration getJbpmConfiguration()
    {
        return jbpmConfiguration;
    }

    /**
     * @param jbpmConfiguration
     *            the jbpmConfiguration to set
     */
    public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration)
    {
        this.jbpmConfiguration = jbpmConfiguration;
    }
}
