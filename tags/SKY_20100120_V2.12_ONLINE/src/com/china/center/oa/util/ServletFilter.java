package com.china.center.oa.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.china.center.oa.publics.User;


/**
 * ServletFilter
 * 
 * @version
 * @author ZHUZHU
 */
public class ServletFilter implements Filter
{
    /**
     * 构造函数
     */
    public ServletFilter()
    {

    }

    private static List<String> filterPath = new ArrayList();

    private static List<String> filterExtPath = new ArrayList();

    static
    {
        filterPath.add("/adm");
        filterPath.add("/cus");
        filterPath.add("/sto");
        filterPath.add("/flo");
        filterPath.add("/pri");
        filterPath.add("/exa");
        filterPath.add("/tes");
        filterPath.add("/rpt");
        filterPath.add("/wor");
        filterPath.add("/gro");
        filterPath.add("/mai");
        filterPath.add("/per");
        filterPath.add("/bud");
        filterPath.add("/mak");
        filterPath.add("/cre");

        filterExtPath.add("/dow");
        filterExtPath.add("/doc");
        filterExtPath.add("/tem");
        filterExtPath.add("/sms");
        filterExtPath.add("/exc");
        filterExtPath.add("/err");

    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig)
        throws ServletException
    {
        String webAppRootKey = filterConfig.getServletContext().getInitParameter("webAppRootKey");

        System.out.println(webAppRootKey);
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

        if (filterPath.contains(path))
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
        else if (filterExtPath.contains(path))
        {
            chain.doFilter(request, response);

            return;
        }

        chain.doFilter(request, response);
    }

}
