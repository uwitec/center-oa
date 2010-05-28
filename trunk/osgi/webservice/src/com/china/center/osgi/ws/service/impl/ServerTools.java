package com.china.center.osgi.ws.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.xfire.XFire;
import org.codehaus.xfire.service.ServiceFactory;
import org.codehaus.xfire.spring.remoting.XFireExporter;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.caucho.HessianExporter;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.china.center.osgi.ws.service.bean.WSServiceBean;


public abstract class ServerTools
{
    public static HessianExporter createHessianServiceExporter(WSServiceBean bean)
    {
        HessianServiceExporter exporter = new HessianServiceExporter();

        exporter.setBeanClassLoader(bean.getClassLoader());

        exporter.setServiceInterface(bean.getServiceClass());

        exporter.setService(bean.getServer());

        exporter.afterPropertiesSet();

        return exporter;
    }

    /**
     * createXFireExporter(修改了源代码XMLTypeCreator.java and XFireExporter.java)
     * 
     * @param bean
     * @param serviceFactory
     * @param xfire
     * @param applicationContext
     * @return XFireExporter [返回类型说明]
     */
    public static XFireExporter createXFireExporter(WSServiceBean bean,
                                                    ServiceFactory serviceFactory, XFire xfire,
                                                    ApplicationContext applicationContext)
    {
        XFireExporter exporter = new XFireExporter();

        exporter.setServiceFactory(serviceFactory);

        exporter.setServiceBean(bean.getServer());

        exporter.setApplicationContext(applicationContext);

        exporter.setXfire(xfire);

        // 设置鉴权的措施
        if (bean.getSoapHandler() != null)
        {
            SOAPHandlerImpl handler = new SOAPHandlerImpl();

            handler.setHandler(bean.getSoapHandler());

            List inList = new ArrayList();

            inList.add(handler);

            exporter.setInHandlers(inList);
        }

        exporter.setServiceInterface(bean.getServiceClass());

        try
        {
            exporter.afterPropertiesSet();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return exporter;
    }

    public static HandlerExecutionChain getHandlerExecutionChain(String path, Object handler)
    {
        HandlerExecutionChain chain = new HandlerExecutionChain(handler);
        chain.addInterceptor(new PathExposingHandlerInterceptor(path));
        return chain;
    }

    /**
     * Special interceptor for exposing the {@link AbstractUrlHandlerMapping#PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE}
     * attribute.
     * 
     * @link AbstractUrlHandlerMapping#exposePathWithinMapping
     */
    static private class PathExposingHandlerInterceptor extends HandlerInterceptorAdapter
    {

        private final String pathWithinMapping;

        public PathExposingHandlerInterceptor(String pathWithinMapping)
        {
            this.pathWithinMapping = pathWithinMapping;
        }

        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                 Object handler)
        {
            exposePathWithinMapping(this.pathWithinMapping, request);
            return true;
        }

        protected void exposePathWithinMapping(String pathWithinMapping, HttpServletRequest request)
        {
            request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE,
                pathWithinMapping);
        }
    }
}
