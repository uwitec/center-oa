/*
 * File Name: ParamterMap.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-8
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


/**
 * 参数类
 * 
 * @author ZHUZHU
 * @version 2007-12-8
 * @see
 * @since
 */
public class ParamterMap
{
    private Map<String, String> parameterMap = new HashMap<String, String>();

    /**
     * default constructor
     */
    public ParamterMap(HttpServletRequest request)
    {
        if (request != null)
        {
            saveParamersToMap(request);
        }
    }

    public ParamterMap(Map<String, String> parMap)
    {
        if (parMap != null)
        {
            this.parameterMap = parMap;
        }
    }

    public void saveParamersToRequest(HttpServletRequest request)
    {
        for (Map.Entry<String, String> entry : parameterMap.entrySet())
        {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public void saveParamersToSession(HttpServletRequest request)
    {
        for (Map.Entry<String, String> entry : parameterMap.entrySet())
        {
            request.getSession().setAttribute(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @return the parameterMap
     */
    public Map<String, String> getParameterMap()
    {
        return parameterMap;
    }

    public String getParameter(String key)
    {
        return parameterMap.get(key);
    }

    public void clearParamers()
    {
        parameterMap.clear();
    }

    private void saveParamersToMap(HttpServletRequest request)
    {
        Map map = request.getParameterMap();

        Set set = map.entrySet();

        Map.Entry element = null;
        String[] oo = null;

        for (Iterator iter = set.iterator(); iter.hasNext();)
        {
            element = (Map.Entry)iter.next();

            if (element.getValue() instanceof String[])
            {
                oo = (String[])element.getValue();

                if (oo.length == 1)
                {
                    parameterMap.put(element.getKey().toString(), oo[0].toString());
                }
            }
        }
    }
}
