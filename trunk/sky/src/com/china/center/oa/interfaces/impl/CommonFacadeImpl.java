/**
 * File Name: CommonFacadeImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-23<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.interfaces.impl;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.oa.interfaces.CommonFacade;
import com.china.center.oa.util.MySessionListener;


/**
 * CommonFacadeImpl
 * 
 * @author zhuzhu
 * @version 2009-4-23
 * @see CommonFacadeImpl
 * @since 1.0
 */
@Bean(name = "commonFacade")
public class CommonFacadeImpl implements CommonFacade
{
    /**
     * auth
     */
    public boolean auth(String key, String value)
    {
        // /session/template
        boolean auth = MySessionListener.sessionSet.contains(getSession(value));

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
