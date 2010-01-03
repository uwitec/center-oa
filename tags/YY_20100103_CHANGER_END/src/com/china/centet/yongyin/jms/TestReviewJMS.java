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
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * @author Liang.xf 2004-12-27 openJms 非持久订阅异步接收演示 www.javayou.com
 */
public class TestReviewJMS implements MessageListener
{
    private TopicConnection topicConnection;

    private TopicSession topicSession;

    private Topic topic;

    private TopicSubscriber topicSubscriber;

    private int ii = 0;

    TestReviewJMS()
    {
        try
        {
            // 取得JNDI上下文和连接
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.exolab.jms.jndi.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, "rmi://10.40.9.229:1099/");
            Context context = new InitialContext(properties);

            // 取得Topic的连接工厂和连接
            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)context.lookup("JmsTopicConnectionFactory");
            topicConnection = topicConnectionFactory.createTopicConnection();

            // 创建Topic的会话，用于接收信息
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = (Topic)context.lookup("topic1");

            // 创建Topic subscriber
            topicSubscriber = topicSession.createSubscriber(topic);
            // 设置订阅监听
            topicSubscriber.setMessageListener(this);

            // 启动信息接收
            topicConnection.start();
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

    public static void main(String[] args)
    {
        System.out.println("非同步定购消息的接收：");
        try
        {
            new TestReviewJMS();
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    // 收到订阅信息后自动调用此方法
    public void onMessage(Message message)
    {
        try
        {
            String messageText = null;
            if (message instanceof TextMessage) messageText = ((TextMessage)message).getText();
            System.out.println("002" + messageText);
            ii++ ;

            if (ii > 200000)
            {
                System.out.println("002:" + ii);
            }
        }
        catch (JMSException e)
        {
            System.out.println("002:" + "ERROR");
        }
    }
}
