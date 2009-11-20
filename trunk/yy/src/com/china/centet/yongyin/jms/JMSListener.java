/*
 * 文 件 名:  TagServletContextListener.java
 * 版    权:  centerchina Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  admin
 * 修改时间:  2007-10-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.china.centet.yongyin.jms;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.exolab.jms.config.Configuration;
import org.exolab.jms.config.ConfigurationReader;
import org.exolab.jms.server.JmsServer;

import com.china.center.tools.FileTools;
import com.china.center.tools.ResourceLocator;


/**
 * JMS的起动器
 * 
 * @author admin
 * @version [版本号, 2007-10-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class JMSListener implements ServletContextListener
{

    public void contextDestroyed(ServletContextEvent arg0)
    {

    }

    public void contextInitialized(ServletContextEvent evt)
    {
        String rootPath = evt.getServletContext().getRealPath("/") + "WEB-INF/classes/";

        System.setProperty("openjms.home", FileTools.getCommonPath(rootPath));
        // org.exolab.jms.message.MessageImpl
        // org.exolab.jms.messagemgr.DefaultMessageCache

        Configuration config;
        try
        {
            config = ConfigurationReader.read(ResourceLocator.getResource("classpath:config/openjms.xml"));

            JmsServer service = new JmsServer(config);

            // 启动服务
            service.init();

            JmsServer.version();

            System.out.println("加载消息总线...");

        }
        catch (Throwable e)
        {
            System.out.println("加载消息总线失败");
            e.printStackTrace();
        }

    }
}
