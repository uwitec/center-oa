/**
 * File Name: CommonFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.facade.impl;


import com.china.center.oa.publics.facade.CommonFacade;
import com.china.center.webportal.listener.MySessionListener;


/**
 * CommonFacadeImpl
 * 
 * @author ZHUZHU
 * @version 2010-7-18
 * @see CommonFacadeImpl
 * @since 1.0
 */
public class CommonFacadeImpl implements CommonFacade
{
    /**
     * auth
     */
    public boolean auth(String key, String value)
    {
        // /session/template
        boolean auth = MySessionListener.getSessionSet().contains(getSession(value));

        return auth;
    }

    private String getSession(String url)
    {
        String head = url.substring(0, url.indexOf("/", 1) + 1);

        // sessionId/template/TemplateFile.jsp
        String url2 = url.substring(head.length());

        String end = url2.substring(0, url2.indexOf("/"));

        return end;
    }
}
