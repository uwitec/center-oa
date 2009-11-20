/*
 * File Name: JMSSender.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-12-16 Grant: open source to
 * everybody
 */
package com.china.centet.yongyin.jms;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.JmsTemplate102;
import org.springframework.jms.core.MessageCreator;

import com.china.center.tools.TimeTools;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
public class JMSSender
{
    private JmsTemplate102 jmsTemplate102;

    private JmsTemplate jmsTemplate;

    private final Log _logger = LogFactory.getLog(getClass());

    private int i = 0;

    private int ii = 0;

    public JmsTemplate102 getJmsTemplate102()
    {
        return jmsTemplate102;
    }

    public void setJmsTemplate102(JmsTemplate102 jmsTemplate102)
    {
        this.jmsTemplate102 = jmsTemplate102;
    }

    public void init()
    {
        new Send().start();
        // new Send1().start();
    }

    class Send extends Thread
    {
        public void run()
        {
            System.out.println("------------------------------------------");
            try
            {
                Thread.sleep(15000);
            }
            catch (InterruptedException e1)
            {}

            while (true)
            {
                try
                {
                    // Thread.sleep(10);

                    if (i > 200100)
                    {
                        System.out.println("send:" + i);
                        break;
                    }

                    if (i % 500 == 0)
                    {
                        _logger.info(TimeTools.now() + " have send:" + i);
                        System.out.println(TimeTools.now() + " have send:" + i);
                    }

                    jmsTemplate.send("topic1", new MessageCreator()
                    {
                        public Message createMessage(Session session)
                            throws JMSException
                        {
                            String message = "ddd" + (i++ );
                            return session.createTextMessage(message);
                        }
                    });
                }
                catch (Throwable e)
                {
                    i-- ;
                    System.out.println("----------------------");
                }

            }
        }
    }

    class Send1 extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(5);

                    jmsTemplate102.send("queue1", new MessageCreator()
                    {
                        public Message createMessage(Session session)
                            throws JMSException
                        {
                            String message = "ddd" + (ii++ );
                            System.out.println("--------------send:" + message);
                            return session.createTextMessage(message);
                        }
                    });
                }
                catch (InterruptedException e)
                {
                    System.out.println("----------------------");
                }
                catch (Throwable e)
                {
                    System.out.println("----------------------");
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
