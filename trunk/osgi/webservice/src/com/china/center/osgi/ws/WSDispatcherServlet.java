package com.china.center.osgi.ws;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.UrlPathHelper;

import com.china.center.osgi.ws.service.bean.WSServiceBean;
import com.china.center.osgi.ws.service.impl.ServerManagerImpl;
import com.china.center.osgi.ws.service.impl.ServerTools;


public class WSDispatcherServlet extends DispatcherServlet
{
    protected UrlPathHelper urlPathHelper = new UrlPathHelper();

    protected HandlerExecutionChain getHandler(HttpServletRequest request, boolean cache)
        throws Exception
    {
        HandlerExecutionChain handle = super.getHandler(request, cache);

        if (handle != null)
        {
            return handle;
        }
        // find dy reg service
        String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);

        WSServiceBean wsBean = ServerManagerImpl.wsMap.get(lookupPath);

        if (wsBean == null)
        {
            return null;
        }

        if ( !wsBean.isInit())
        {
            return null;
        }

        return ServerTools.getHandlerExecutionChain(lookupPath, wsBean.getHttpRequestHandler());
    }
}
