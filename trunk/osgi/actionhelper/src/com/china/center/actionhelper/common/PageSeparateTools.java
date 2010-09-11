/**
 * File Name: PageSeparateTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-4<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.common;


import javax.servlet.http.HttpServletRequest;

import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.StringTools;


/**
 * 数据库分页工具
 * 
 * @author ZHUZHU
 * @version 2008-3-4
 * @see
 * @since
 */
public abstract class PageSeparateTools extends AbstractPage
{
    /**
     * 初始化分页
     * 
     * @param condtion
     * @param page
     * @param request
     */
    public static void initPageSeparate(ConditionParse condition, PageSeparate page, HttpServletRequest request,
                                        String key)
    {
        initPageSeparate(condition, page, request, key, isFirstLoad(request, key));
    }

    /**
     * 初始化分页
     * 
     * @param condtion
     * @param page
     * @param request
     */
    public static void initPageSeparate(ConditionParse condition, PageSeparate page, HttpServletRequest request,
                                        String key, boolean isFirst)
    {
        if (isFirst)
        {
            request.getSession().setAttribute(getPageAttributeNameInSession(request, key), page);

            request.getSession().setAttribute(PAGE_ATTRIBUTE_NAME, page);

            initParameterMap(request, key);

            request.getSession().setAttribute(getConditionAttributeNameInSession(request, key), condition);

            request.getSession().setAttribute(CONDITION_ATTRIBUTE_NAME, condition);

            request.setAttribute("next", page.hasNextPage());

            request.setAttribute("pre", page.hasPrevPage());

            request.getSession().setAttribute("TOTAL", page.getRowCount());
        }
        else
        {
            processSeparate(request, key);
        }
    }

    /**
     * 是否第一次加载
     * 
     * @param request
     * @return
     */
    public static boolean isFirstLoad(HttpServletRequest request)
    {
        String load = request.getParameter(LOAD);

        String old_load = request.getParameter(OLD_LOAD);

        // 从一个action到另一个action使用forward
        Object oo = request.getAttribute(FORWARD);

        String forward = null;

        if (oo != null)
        {
            forward = oo.toString();
        }

        // 具备优先性
        String page = request.getParameter(PAGE);

        String memory = request.getParameter(MEMORY);

        Object o1 = request.getAttribute(MEMORY);

        if (o1 != null)
        {
            return false;
        }

        if ( !StringTools.isNullOrNone(page) || !StringTools.isNullOrNone(memory))
        {
            return false;
        }

        return !StringTools.isNullOrNone(load) || !StringTools.isNullOrNone(old_load)
               || !StringTools.isNullOrNone(forward);
    }

    /**
     * 是否第一次加载
     * 
     * @param request
     * @return
     */
    public static boolean isFirstLoad(HttpServletRequest request, String key)
    {
        // 具备最高优先性
        String page = request.getParameter(PAGE);

        if ( !StringTools.isNullOrNone(page))
        {
            return false;
        }

        String memory = request.getParameter(MEMORY);

        // 如果是记忆方式的话,没有初始化也认为是第一次进入
        if ( !StringTools.isNullOrNone(memory))
        {
            if (PageSeparateTools.getPageSeparate(request, key) == null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        // 一般链接使用firstLoad 或者load
        String load = request.getParameter(LOAD);

        // 从一个action到另一个action使用forward
        Object oo = request.getAttribute(FORWARD);

        String forward = null;

        if (oo != null)
        {
            forward = oo.toString();
        }

        return !StringTools.isNullOrNone(load) || !StringTools.isNullOrNone(forward);
    }

    /**
     * 是否记忆
     * 
     * @param request
     * @return
     */
    public static boolean isMemory(HttpServletRequest request)
    {
        String memory = request.getParameter(MEMORY);

        Object o1 = request.getAttribute(MEMORY);

        if (o1 != null)
        {
            return true;
        }

        o1 = request.getAttribute(MEMORY);

        if (o1 != null)
        {
            return true;
        }

        if ( !StringTools.isNullOrNone(memory))
        {
            return true;
        }

        return false;
    }

    /**
     * 是否记忆
     * 
     * @param request
     * @return
     */
    public static boolean isJust(HttpServletRequest request)
    {
        String turn = request.getParameter(JUST);

        String memory = request.getParameter(JUST);

        Object o1 = request.getAttribute(JUST);

        if (o1 != null)
        {
            return true;
        }

        o1 = request.getAttribute(JUST);

        if (o1 != null)
        {
            return true;
        }

        if ( !StringTools.isNullOrNone(turn) || !StringTools.isNullOrNone(memory))
        {
            return true;
        }

        return false;
    }

    /**
     * 初始化记忆分页
     * 
     * @param condtion
     * @param page
     * @param request
     */
    public static void processMemory(int total, HttpServletRequest request, String key)
    {
        updateParameterMap(request, key);

        PageSeparate page = getPageSeparate(request, key);

        int nowPage = page.getNowPage();

        int pageSize = page.getPageSize();

        page.reset(total, pageSize);

        page.setNowPage(nowPage);
    }

    /**
     * 处理just
     * 
     * @param condtion
     * @param page
     * @param request
     */
    public static void processJust(HttpServletRequest request, String key)
    {
        updateParameterMap(request, key);
    }

    /**
     * 处理分页
     * 
     * @param request
     * @return
     */
    public static boolean processSeparate(HttpServletRequest request, String key)
    {
        updateParameterMap(request, key);

        PageSeparate pageS = (PageSeparate)request.getSession().getAttribute(
            getPageAttributeNameInSession(request, key));

        request.getSession().setAttribute(PAGE_ATTRIBUTE_NAME, pageS);

        if (pageS == null)
        {
            return false;
        }

        String page = request.getParameter(PAGE);

        if ("next".equals(page))
        {
            pageS.nextPage();
            request.setAttribute("next", pageS.hasNextPage());
            request.setAttribute("pre", pageS.hasPrevPage());
            return true;
        }
        else if ("previous".equals(page))
        {
            pageS.prevPage();

            request.setAttribute("next", pageS.hasNextPage());
            request.setAttribute("pre", pageS.hasPrevPage());
            return true;
        }

        return false;
    }
}
