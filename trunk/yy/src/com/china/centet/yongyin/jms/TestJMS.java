/**
 * 文 件 名: TestJMS.java <br>
 * 版 权: centerchina Technologies Co., Ltd. Copyright YYYY-YYYY, All rights reserved
 * <br>
 * 描 述: <描述> <br>
 * 修 改 人: admin <br>
 * 修改时间: 2008-1-4 <br>
 * 跟踪单号: <跟踪单号> <br>
 * 修改单号: <修改单号> <br>
 * 修改内容: <修改内容> <br>
 */
package com.china.centet.yongyin.jms;


import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class TestJMS
{

    public static void main(String[] args)
    {
        try
        {
            // 取得JNDI上下文和连接
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.exolab.jms.jndi.InitialContextFactory");
            // openJms默认的端口是1099
            properties.put(Context.PROVIDER_URL, "rmi://10.40.9.229:1099/");
            Context context = new InitialContext(properties);
            // 获得JMS Topic连接队列工厂
            TopicConnectionFactory factory = (TopicConnectionFactory)context.lookup("JmsTopicConnectionFactory");

            // 创建一个Topic连接，并启动
            TopicConnection topicConnection = factory.createTopicConnection();
            topicConnection.start();

            // 创建一个Topic会话，并设置自动应答
            TopicSession topicSession = topicConnection.createTopicSession(false,
                Session.AUTO_ACKNOWLEDGE);

            // lookup 得到 topic1
            Topic topic = (Topic)context.lookup("topic1");
            // 用Topic会话生成Topic发布器
            TopicPublisher topicPublisher = topicSession.createPublisher(topic);

            // 发布消息到Topic
            System.out.println("消息发布到Topic");
            TextMessage message = topicSession.createTextMessage("你好，欢迎定购Topic类消息");
            topicPublisher.publish(message);

            // 资源清除，代码略 ... ...
        }
        catch (NamingException e)
        {
            e.printStackTrace();
        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }
    }

}
