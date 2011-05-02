/**
 * File Name: JSONTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.common;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.jsonimpl.JSONObject;
import com.china.center.jdbc.annosql.InnerBean;
import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;


/**
 * JSONTools
 * 
 * @author ZHUZHU
 * @version 2008-11-2
 * @see JSONTools
 * @since 1.0
 */
public abstract class JSONTools
{
    private static String DEFAULT_ENCODING = "UTF-8";

    public static void setEncoding(String encoding)
    {
        DEFAULT_ENCODING = encoding;
    }

    public static int getNowPage(HttpServletRequest request)
    {
        // 得到当前页数
        String nowPage = request.getParameter("nowPage");

        int nowPage1 = CommonTools.parseInt(nowPage);

        if (nowPage1 == 0)
        {
            nowPage1 = 1;
        }

        return nowPage1;
    }

    public static int getPageSize(HttpServletRequest request)
    {
        // 得到当前页数
        String pageSize = request.getParameter("pageSize");

        int nowPage1 = CommonTools.parseInt(pageSize);

        if (nowPage1 == 0)
        {
            nowPage1 = 15;
        }

        return nowPage1;
    }

    public static void setPageSeparate(HttpServletRequest request, PageSeparate page)
    {
        page.setNowPage(getNowPage(request));

        page.setPageSize(getPageSize(request));
    }

    /**
     * write json str to response
     * 
     * @param response
     * @param json
     */
    public static ActionForward writeResponse(HttpServletResponse response, String json)
    {
        response.setContentType("application/json");
        response.setCharacterEncoding(DEFAULT_ENCODING);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        try
        {
            response.getWriter().write(json);
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (IOException e)
        {
        }

        return null;
    }

    /**
     * 返回错误
     * 
     * @param response
     * @param errorMessage
     * @return
     */
    public static ActionForward writeErrorResponse(HttpServletResponse response, String errorMessage)
    {
        response.setContentType("html/txt");
        response.setStatus(500);
        response.setCharacterEncoding(DEFAULT_ENCODING);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        try
        {
            response.getWriter().write(errorMessage);
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (IOException e)
        {
        }

        return null;
    }

    /**
     * write AjaxResult to response
     * 
     * @param response
     * @param ajax
     */
    public static ActionForward writeResponse(HttpServletResponse response, AjaxResult ajax)
    {
        JSONObject object = new JSONObject(ajax, true);

        return writeResponse(response, object.toString());
    }

    /**
     * getJSONString
     * 
     * @param list
     * @return
     */
    public static String getJSONString(List list)
    {
        PageSeparate page = new PageSeparate();

        if (list.size() > 0)
        {
            page.setPageSize(list.size());
        }

        page.setRowCount(list.size());

        page.setNowPage(1);

        return getJSONString(list, page);
    }

    public static String getJSONString(List list, PageSeparate page, String hint)
    {

        Map pageInfo = new HashMap();

        if (StringTools.isNullOrNone(hint))
        {
            pageInfo.put("hint", "");
        }
        else
        {
            pageInfo.put("hint", hint);
        }

        pageInfo.put("nowPage", page.getNowPage());

        pageInfo.put("total", page.getRowCount());

        pageInfo.put("pageSize", page.getPageSize());

        List mapList = new ArrayList();

        Object oo = null;

        for (int i = 0; i < list.size(); i++ )
        {
            Map<String, Map> cellMap = new HashMap<String, Map>();

            Map<String, String> map = new HashMap<String, String>();

            oo = list.get(i);

            cellMap.put("cell", map);

            createMapByObject(oo, map);

            mapList.add(cellMap);
        }

        pageInfo.put("rows", mapList);
        JSONObject object = new JSONObject(pageInfo);
        return object.toString();

    }

    public static String getJSONString(List list, PageSeparate page)
    {
        return getJSONString(list, page, "");
    }

    /**
     * createMapByObject
     * 
     * @param oo
     * @param map
     */
    private static void createMapByObject(Object oo, Map<String, String> map)
    {
        List<InnerBean> fields = BeanTools.getAllClassFieldsInner(oo.getClass());

        Field field = null;

        Object fvalue = null;

        for (InnerBean innerBean : fields)
        {
            field = innerBean.getField();

            field.setAccessible(true);

            try
            {
                fvalue = field.get(oo);
            }
            catch (IllegalArgumentException e)
            {
            }
            catch (IllegalAccessException e)
            {
            }

            if (fvalue != null)
            {
                map.put(innerBean.getFieldName(), fvalue.toString());
            }
            else
            {
                map.put(innerBean.getFieldName(), "");
            }
        }
    }

    /**
     * getMapListJSON(map里面是string:List[Bean])
     * 
     * @param map
     * @return
     */
    public static String getMapListJSON(Map map)
    {
        Map result = new HashMap();

        Collection<String> values = map.keySet();

        for (String string : values)
        {
            List mapList = new ArrayList();

            List each = (List)map.get(string);

            for (Object object : each)
            {
                Map<String, String> vmap = new HashMap<String, String>();

                createMapByObject(object, vmap);

                mapList.add(vmap);
            }

            result.put(string, mapList);

        }

        JSONObject object = new JSONObject(result);

        return object.toString();
    }
}
