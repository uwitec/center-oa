/*
 * File Name: JMSReceiver.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-12-16 Grant: open source to
 * everybody
 */
package com.china.centet.yongyin.jms;


import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.JmsTemplate102;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
public class JMSReceiver
{
    private JmsTemplate102 jmsTemplate102;

    private JmsTemplate jmsTemplate;

    private int ii = 0;

    public JmsTemplate102 getJmsTemplate102()
    {
        return jmsTemplate102;
    }

    public void setJmsTemplate102(JmsTemplate102 jmsTemplate102)
    {
        this.jmsTemplate102 = jmsTemplate102;
    }

    public void inits()
    {
        new Recevie().start();
        // new Recevie1().start();
    }

    class Recevie extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Message msg = jmsTemplate.receive("topic1");

                    TextMessage textMessage = (TextMessage)msg;

                    if (msg != null)
                    {
                        System.out.println("xxxxOfJMSReceiver sub1 Received -->"
                                           + textMessage.getText());
                    }

                    if (ii > 1800)
                    {
                        System.out.println("xxxxOfJMSReceiver sub1 Received -->" + ii);
                    }
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    class Recevie1 extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Message msg = jmsTemplate102.receive("queue1");

                    TextMessage textMessage = (TextMessage)msg;

                    if (msg != null)
                    {
                        System.out.println("--------------xxxxOfJMSReceiver queue1 Received -->"
                                           + textMessage.getText());
                    }
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return 返回 jmsTemplate
     */
    public JmsTemplate getJmsTemplate()
    {
        return jmsTemplate;
    }

    /**
     * @param 对jmsTemplate进行赋值
     */
    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }
}
