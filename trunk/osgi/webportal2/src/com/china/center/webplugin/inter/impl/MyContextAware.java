package com.china.center.webplugin.inter.impl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class MyContextAware implements ApplicationContextAware
{
    private final Log _logger = LogFactory.getLog(getClass());

    /**
     * set DelegatingRequestProcessor.ac
     * 
     * @param arg0
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext arg0)
        throws BeansException
    {
        _logger.info("set ApplicationContextAware to DelegatingRequestProcessor ----------------");

        DelegatingRequestProcessor.ac = arg0;
    }
}
