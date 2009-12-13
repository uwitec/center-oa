/*
 * File Name: ApplyManagerImpl.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-13
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.manager;


import java.io.FileNotFoundException;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;

import com.china.center.tools.ResourceLocator;


/**
 * 流程加载的manager
 * 
 * @author zhuzhu
 * @version 2007-12-13
 * @see
 * @since
 */
public class FlowManager
{
    private JbpmConfiguration jbpmConfiguration = null;

    private ProcessDefinition processDefinition = null;

    /**
     * default constructor
     */
    public FlowManager()
    {}

    public void inits()
        throws FileNotFoundException
    {
        JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();

        processDefinition = jbpmContext.getGraphSession().findLatestProcessDefinition("sale");

        if (processDefinition == null)
        {
            processDefinition = ProcessDefinition.parseXmlInputStream(ResourceLocator.getResource("classpath:config/workflow/sale.xml"));

            jbpmContext.getGraphSession().saveProcessDefinition(processDefinition);

            jbpmContext.close();

            System.out.println("加载销售单流程...");
        }
        else
        {
            System.out.println("已经加载销售单流程...");
        }
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

    /**
     * @return the processDefinition
     */
    public ProcessDefinition getProcessDefinition()
    {
        return processDefinition;
    }
}
