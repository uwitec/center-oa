/**
 * File Name: QueryTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.query;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.china.center.actionhelper.common.OldPageSeparateTools;
import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;


/**
 * QueryTools
 * 
 * @author ZHUZHU
 * @version 2008-10-2
 * @see
 * @since
 */
public abstract class QueryTools
{
    /**
     * 设置记忆分页
     * 
     * @param request
     */
    public static void setMemoryQuery(HttpServletRequest request)
    {
        request.setAttribute(OldPageSeparateTools.TURN, 1);
    }

    public static void setForwardQuery(HttpServletRequest request)
    {
        request.setAttribute(OldPageSeparateTools.FORWARD, 1);
    }

    public static void setJustQuery(HttpServletRequest request)
    {
        request.setAttribute(OldPageSeparateTools.JUST, 1);
    }

    /**
     * 设置页面参数
     * 
     * @param request
     * @param key
     * @param value
     */
    public static void setParMapAttribute(HttpServletRequest request, String key, Object value)
    {
        request.setAttribute(OldPageSeparateTools.PAREXT + key, value);
    }

    /**
     * 常见的查询方法
     * 
     * @param request
     *            请求
     * @param list
     *            存放结果的列表
     * @param condtion
     *            条件
     * @param dao
     *            使用的dao
     * @param pagesize
     *            获取的数量
     */
    public static void commonQueryVO(String key, HttpServletRequest request, List list, ConditionParse condtion,
                                     DAO dao, int... pagesize)
    {
        int size = getPageSize(request);

        if (pagesize == null || pagesize.length == 0)
        {
            size = getPageSize(request);
        }
        else
        {
            size = pagesize[0];
        }

        if (size <= 0)
        {
            size = 10;
        }

        list.addAll(commonQueryVOInner(key, request, condtion, dao, size));
    }

    private static int getPageSize(HttpServletRequest request)
    {
        Object attribute = request.getSession().getAttribute("g_page");

        if (attribute == null)
        {
            return 10;
        }

        return (Integer)attribute;
    }

    /**
     * 常见的查询方法
     * 
     * @param request
     *            请求
     * @param list
     *            存放结果的列表
     * @param condtion
     *            条件
     * @param dao
     *            使用的dao
     * @param pagesize
     *            获取的数量
     */
    private static List<?> commonQueryVOInner(String key, HttpServletRequest request, ConditionParse condtion, DAO dao,
                                              int pagesize)
    {
        int size = pagesize;

        if (OldPageSeparateTools.isFirstLoad(request))
        {
            int total = dao.countVOByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, size);

            OldPageSeparateTools.initPageSeparate(condtion, page, request, key);
        }
        else if (OldPageSeparateTools.isMemory(request))
        {
            int total = dao.countVOByCondition(OldPageSeparateTools.getCondition(request, key).toString());

            OldPageSeparateTools.processMemory(total, request, key);
        }
        else
        {
            OldPageSeparateTools.processSeparate(request, key);
        }

        return dao.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(request, key), OldPageSeparateTools
            .getPageSeparate(request, key));
    }

    public static void commonQueryBean(String key, HttpServletRequest request, List list, ConditionParse condtion,
                                       DAO dao, int... pagesize)
    {
        int size = 10;

        if (pagesize == null || pagesize.length == 0)
        {
            size = 10;
        }
        else
        {
            size = pagesize[0];
        }

        list.addAll(commonQueryBeanInner(key, request, condtion, dao, size));
    }

    private static List<?> commonQueryBeanInner(String key, HttpServletRequest request, ConditionParse condtion,
                                                DAO dao, int pagesize)
    {
        int size = pagesize;

        if (OldPageSeparateTools.isFirstLoad(request))
        {
            int total = dao.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, size);

            OldPageSeparateTools.initPageSeparate(condtion, page, request, key);
        }
        else if (OldPageSeparateTools.isMemory(request))
        {
            int total = dao.countByCondition(OldPageSeparateTools.getCondition(request, key).toString());

            OldPageSeparateTools.processMemory(total, request, key);
        }
        else
        {
            OldPageSeparateTools.processSeparate(request, key);
        }

        return dao.queryEntityBeansByCondition(OldPageSeparateTools.getCondition(request, key), OldPageSeparateTools
            .getPageSeparate(request, key));
    }
}
