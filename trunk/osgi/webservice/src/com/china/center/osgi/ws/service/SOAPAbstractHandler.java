package com.china.center.osgi.ws.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Element;


/**
 * SOAPAbstractHandler
 * 
 * @author ZHUZHU
 * @version 2010-5-24
 * @see SOAPAbstractHandler
 * @since 1.0
 */
public interface SOAPAbstractHandler
{
    void invoke(Element head, HttpServletRequest request, HttpServletResponse response)
        throws Exception;
}
