package com.china.centet.yongyin.dao;


import junit.framework.TestCase;

import org.china.center.spring.ex.config.ExNamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.china.center.oa.publics.dao.LocationDAO;


public class CommonDAOTest extends TestCase
{
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        "classpath:application/total_applicationContextr.xml");

    public void testCountCode()
        throws Exception
    {
        LocationDAO dao = (LocationDAO)ctx.getBean("locationDAO");

        int countCode = dao.countCode("ZB");

        System.out.println(countCode);
    }

    public static void main(String[] args)
    {
        boolean is = NamespaceHandler.class.isAssignableFrom(ExNamespaceHandler.class);

        System.out.println(is);
    }
}
