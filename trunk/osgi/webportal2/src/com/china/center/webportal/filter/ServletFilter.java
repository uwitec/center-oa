package com.china.center.webportal.filter;


import java.io.IOException;
import java.util.Collection;

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

import com.center.china.osgi.publics.User;
import com.china.center.webplugin.inter.FilterLoad;


/**
 * ServletFilter
 * 
 * @version 2.0
 */
public class ServletFilter implements Filter
{
    /**
     * INIT
     * 
     * @param filterConfig
     *            filterConfig
     * @throws ServletException
     *             ServletException
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
     * doFilter
     * 
     * @param req
     *            ServletRequest
     * @param resp
     *            ServletResponse
     * @param chain
     *            FilterChain
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)req;

        HttpServletResponse response = (HttpServletResponse)resp;

        request.setCharacterEncoding("UTF-8");

        final String servletPath = request.getServletPath();

        HttpSession session = request.getSession();

        User user = (User)session.getAttribute("user");

        RequestDispatcher timeoutDispatch = request.getRequestDispatcher("/common/timeout.jsp");

        // handle IGNORE_FILTER_MATCH
        for (String eachItem : FilterLoad.getIGNORE_FILTER_MATCH())
        {
            if (servletPath.startsWith(eachItem))
            {
                chain.doFilter(request, response);

                return;
            }
        }

        if (user == null && !FilterLoad.getIGNORE_FILTER().contains(servletPath))
        {
            timeoutDispatch.forward(req, resp);

            return;
        }

        if (user != null)
        {
            Collection<FilterListener> listeners = FilterListenerService.getListenerMap().values();

            for (FilterListener filterListener : listeners)
            {
                boolean result = filterListener.onDoFilterAfterCheckUser(req, resp, chain);

                if (result)
                {
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
