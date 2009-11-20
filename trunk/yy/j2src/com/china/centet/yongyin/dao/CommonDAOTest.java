package com.china.centet.yongyin.dao;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.china.centet.yongyin.manager.NetAccessManager;


public class CommonDAOTest
{
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "classpath:application/total_applicationContextr.xml");

    @Before
    public void init()
    {
        System.out.println("init");
    }

    @After
    public void destroy()
    {
        System.out.println("destroy");
    }

    @Test
    public void “Ï≥£—π¡¶≤‚ ‘()
        throws Exception
    {
        NetAccessManager manager = (NetAccessManager)ctx.getBean("netAccessManager");
        
        manager.access();
    }
}
