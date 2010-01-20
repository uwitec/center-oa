/*
 * File Name: SaveOutAction.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wokflow.sale.action;


import org.jbpm.JbpmConfiguration;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.china.centet.yongyin.manager.OutManager;
import com.china.centet.yongyin.wokflow.JbpmHelper;


/**
 * 提交销售单
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class DelOutAction implements ActionHandler
{
    private OutManager outManager = null;

    private JbpmConfiguration jbpmConfiguration = null;

    /**
     * 保存销售单
     */
    public void execute(ExecutionContext exc)
        throws Exception
    {
        JbpmHelper helper = new JbpmHelper(exc);

        // 获得request里面的数据
        String fullId = helper.getStringVariable("fullId");

        outManager.delOut(fullId);
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
