package com.china.center.util;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.china.centet.yongyin.bean.User;


/**
 * @version
 * @author ccc
 */
public class ServletFilter implements Filter
{
    /**
     * 构造函数
     */
    public ServletFilter()
    {

    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig)
        throws ServletException
    {

    }

    /**
     * Destroys the filter.
     */
    public void destroy()
    {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException
    {
        // cast to the types I want to use
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        request.setCharacterEncoding("GBK");

        HttpSession session = request.getSession();

        User user = (User)session.getAttribute("user");

        Object wapUser = session.getAttribute("gwap");

        RequestDispatcher dispatch = request.getRequestDispatcher("timeout.jsp");

        RequestDispatcher wapdispatch = request.getRequestDispatcher("../wap/index.jsp");

        String path = request.getServletPath().substring(0, 4);

        if (wapUser != null)
        {
            if (request.getServletPath().indexOf("/wap/") == -1)
            {
                dispatch.forward(request, response);
                return;
            }
        }

        if ("/index.jsp".equals(request.getServletPath())
            || "/wap/index.jsp".equals(request.getServletPath())
            || "/wap/image.jsp".equals(request.getServletPath())
            || ("/wap/checkuser.do".equals(request.getServletPath())))
        {
            chain.doFilter(request, response);
            return;
        }

        if (wapUser == null && user == null)
        {
            if (request.getServletPath().indexOf("/wap/") != -1)
            {
                request.getSession().setAttribute("errorInfo", "请重新登录");

                wapdispatch.forward(request, response);

                return;
            }
        }

        if ("/adm".equals(path) || "/mem".equals(path) || "/hel".equals(path)
            || "/sto".equals(path) || "/flo".equals(path) || "/pri".equals(path)
            || "/wap".equals(path))
        {
            if ( (user == null) && ! ("/admin/index.jsp".equals(request.getServletPath()))
                && ! ("/admin/checkuser.do".equals(request.getServletPath()))
                && ! ("/wap/checkuser.do".equals(request.getServletPath()))
                && ! ("/admin/image.jsp".equals(request.getServletPath()))
                && ! ("/admin/logout.do".equals(request.getServletPath()))
                && ! ("/admin/ask.jsp".equals(request.getServletPath())))
            {
                dispatch.forward(request, response);
                return;
            }
        }
        else if ("/js/".equals(path))
        {
            String all = request.getServletPath();

            if (all.startsWith("/js/center_cal") || all.startsWith("/js/lang"))
            {
                // 去掉try {} finally{}
                chain.doFilter(request, response);
                return;
            }
        }
        else if ("/doc".equals(path))
        {
            chain.doFilter(request, response);

            return;
        }
        else if ( ! ("/OutXml.do".equals(request.getServletPath()))
                 && ! ("/spOutXml.do".equals(request.getServletPath()))
                 && ! ("/agentXml.do".equals(request.getServletPath())))
        {
            dispatch.forward(request, response);
            return;
        }

        // 限制wap使用其他的
        if (request.getServletPath() != null && request.getServletPath().length() > 0)
        {
            if (request.getServletPath().indexOf("/wap/") != -1
                && ! ("/wap/checkuser.do".equals(request.getServletPath())))
            {
                if (wapUser == null)
                {
                    dispatch.forward(request, response);

                    return;
                }
            }
        }

        // 去掉try {} finally{}
        chain.doFilter(request, response);
    }
}
