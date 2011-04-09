/**
 * File Name: JSONPageSeparateTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-3<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.common;


import javax.servlet.http.HttpServletRequest;

import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.StringTools;


/**
 * JSONPageSeparateTools
 * 
 * @author ZHUZHU
 * @version 2008-11-3
 * @see JSONPageSeparateTools
 * @since 1.0
 */
public abstract class JSONPageSeparateTools extends AbstractPage
{
    /**
     * 初始化JSON分页
     * 
     * @param condtion
     * @param page
     * @param request
     */
    public static void initPageSeparate(ConditionParse condition, PageSeparate page,
                                        HttpServletRequest request, String key)
    {
        if (isLoad(request))
        {
            initParameterMap(request, key);
        }

        if ( !isMemeryMode(request) && isCommonInit(request, key))
        {
            initParameterMap(request, key);
        }

        // 内存分页
        if (isMemeryMode(request) && isMemeryInit(request, key))
        {
            initParameterMap(request, key);
        }

        request.getSession().setAttribute(getPageAttributeNameInSession(request, key), page);

        request.getSession().setAttribute(PAGE_ATTRIBUTE_NAME, page);

        request.getSession().setAttribute(getConditionAttributeNameInSession(request, key),
            condition);

        request.getSession().setAttribute(CONDITION_ATTRIBUTE_NAME, condition);

        updateParameterMap(request, key);
    }

    public static boolean isMemeryMode(HttpServletRequest request)
    {
        // load 最优先
        String queryMode = request.getParameter(JSON_QUERYMODE);

        return "0".equals(queryMode);
    }

    /**
     * @param request
     * @param key
     * @return
     */
    public static boolean isMemeryInit(HttpServletRequest request, String key)
    {
        // load 最优先
        String load = request.getParameter(LOAD);

        if ( !StringTools.isNullOrNone(load))
        {
            return true;
        }

        String oprAction = request.getParameter(JSON_OPRACTION);

        if ("0".equals(oprAction))
        {
            if (getPageSeparate(request, key) == null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    public static boolean isLoad(HttpServletRequest request)
    {
        String load = request.getParameter(LOAD);

        return !StringTools.isNullOrNone(load);
    }

    /**
     * @param request
     * @param key
     * @return
     */
    public static boolean isCommonInit(HttpServletRequest request, String key)
    {
        // load 最优先
        String load = request.getParameter(LOAD);

        if ( !StringTools.isNullOrNone(load))
        {
            return true;
        }

        String oprAction = request.getParameter(JSON_OPRACTION);

        return "0".equals(oprAction);
    }

    /**
     * 是否页面第一次加载(load=null and oprAction=0 menu=1)
     * 
     * @param request
     * @param key
     * @return
     */
    public static boolean isPageFirstInit(HttpServletRequest request, String key)
    {
        // load 最优先
        String load = request.getParameter(LOAD);

        Object menu = request.getSession().getAttribute(GMENU);

        if ( !StringTools.isNullOrNone(load))
        {
            return false;
        }

        if (menu == null)
        {
            return false;
        }

        // 使用一次就删除
        request.getSession().removeAttribute(GMENU);

        String oprAction = request.getParameter(JSON_OPRACTION);

        return "1".equals(menu.toString()) && "0".equals(oprAction);
    }

    /**
     * @param request
     * @param key
     * @return
     */
    public static boolean isMemery(HttpServletRequest request, String key)
    {
        String oprAction = request.getParameter(JSON_OPRACTION);

        if ("0".equals(oprAction))
        {
            if (getPageSeparate(request, key) != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    /**
     * @param request
     * @param key
     * @return
     */
    public static boolean isChangePageSize(HttpServletRequest request, String key)
    {
        String oprAction = request.getParameter(JSON_OPRACTION);

        return "1".equals(oprAction);
    }

    /**
     * @param request
     * @param key
     * @return
     */
    public static boolean isPageTurning(HttpServletRequest request, String key)
    {
        String oprAction = request.getParameter(JSON_OPRACTION);

        return "2".equals(oprAction);
    }

    /**
     * @param request
     * @param key
     * @return
     */
    public static boolean isFlush(HttpServletRequest request, String key)
    {
        String oprAction = request.getParameter(JSON_OPRACTION);

        return "3".equals(oprAction);
    }

    /**
     * @param request
     * @param key
     * @return
     */
    public static boolean isSort(HttpServletRequest request, String key)
    {
        String oprAction = request.getParameter(JSON_OPRACTION);

        return "4".equals(oprAction);
    }
}
