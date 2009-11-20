/*
 * 文件名：CenterServlet.java
 * 版权：Copyright by www.center.china
 * 描述：
 * 创建人：zhu
 * 创建时间：2007-1-11
 */
package com.china.center.oa.util;


import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-1-11
 * @see
 * @since
 */
public class CenterServlet extends HttpServlet
{
    public static String ROOTPATH = "";

    /**
     * 获得web的根路径
     */
    public void init(ServletConfig config)
        throws ServletException
    {
        ROOTPATH = config.getServletContext().getRealPath("/");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {}
}
