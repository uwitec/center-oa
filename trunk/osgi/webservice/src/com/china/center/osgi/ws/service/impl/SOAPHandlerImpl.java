package com.china.center.osgi.ws.service.impl;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.transport.http.XFireServletController;

import com.china.center.osgi.ws.service.SOAPAbstractHandler;


public class SOAPHandlerImpl extends org.codehaus.xfire.handler.AbstractHandler
{
    private SOAPAbstractHandler handler = null;

    public void invoke(MessageContext cfx)
        throws Exception
    {
        HttpServletResponse response = XFireServletController.getResponse();

        HttpServletRequest request = XFireServletController.getRequest();

        handler.invoke(cfx.getInMessage().getHeader(), request, response);
    }

    /**
     * get handler
     * 
     * @return handler
     */
    public SOAPAbstractHandler getHandler()
    {
        return handler;
    }

    /**
     * set handler
     * 
     * @param handler
     *            the value of handler
     */
    public void setHandler(SOAPAbstractHandler handler)
    {
        this.handler = handler;
    }

}
