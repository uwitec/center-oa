package com.china.center.webplugin.inter.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;
import org.springframework.context.ApplicationContext;


/**
 * @author ZHUZHU
 * @version [版本号, 2009-6-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DelegatingRequestProcessor extends RequestProcessor
{
    public static ApplicationContext ac;

    public static Map<String, Object> extContext = new HashMap();;

    protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response,
                                         ActionMapping mapping)
        throws IOException
    {
        String prefix = mapping.getModuleConfig().getPrefix();

        String path = mapping.getPath();

        String beanName = prefix + path;

        Action action = (Action)getAction(beanName);

        action.setServlet(this.servlet);

        return action;
    }

    private static Action getAction(String beanName)
    {
        Object bean = extContext.get(beanName);

        if (bean != null)
        {
            return (Action)bean;
        }

        return (Action)ac.getBean(beanName);
    }

}
