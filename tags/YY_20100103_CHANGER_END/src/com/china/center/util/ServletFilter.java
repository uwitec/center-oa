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

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,javax.servlet.ServletResponse,javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException
    {
        // cast to the types I want to use
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        request.setCharacterEncoding("GBK");

        HttpSession session = request.getSession();

        User user = (User)session.getAttribute("user");

        RequestDispatcher dispatch = request.getRequestDispatcher("timeout.jsp");

        String path = request.getServletPath().substring(0, 4);

        if ("/index.jsp".equals(request.getServletPath()))
        {
            chain.doFilter(request, response);
            return;
        }

        if ("/adm".equals(path) || "/mem".equals(path) || "/sto".equals(path)
            || "/flo".equals(path) || "/pri".equals(path))
        {
            if ( (user == null) && ! ("/admin/index.jsp".equals(request.getServletPath()))
                && ! ("/admin/checkuser.do".equals(request.getServletPath()))
                && ! ("/admin/image.jsp".equals(request.getServletPath()))
                && ! ("/admin/logout.do".equals(request.getServletPath())))
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

        // 去掉try {} finally{}
        chain.doFilter(request, response);
    }

}
