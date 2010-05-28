/**
 * File Name: AbstractPage.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-3<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.common;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;


/**
 * AbstractPage
 * 
 * @author ZHUZHU
 * @version 2008-11-3
 * @see AbstractPage
 * @since 1.0
 */
public abstract class AbstractPage implements PageConstant
{
    public static String getPageAttributeNameInSession(HttpServletRequest request, String key)
    {
        if (StringTools.isNullOrNone(key))
        {
            return PAGE_ATTRIBUTE_NAME;
        }

        return PAGE_ATTRIBUTE_NAME + "_" + key;
    }

    public static String getConditionAttributeNameInSession(HttpServletRequest request, String key)
    {
        if (StringTools.isNullOrNone(key))
        {
            return CONDITION_ATTRIBUTE_NAME;
        }

        return CONDITION_ATTRIBUTE_NAME + "_" + key;
    }

    public static PageSeparate getPageSeparate(HttpServletRequest request, String key)
    {
        PageSeparate result = (PageSeparate)request.getSession().getAttribute(
            getPageAttributeNameInSession(request, key));

        request.getSession().setAttribute(PAGE_ATTRIBUTE_NAME, result);

        return result;
    }

    public static ConditionParse getCondition(HttpServletRequest request, String key)
    {
        ConditionParse result = (ConditionParse)request.getSession().getAttribute(
            getConditionAttributeNameInSession(request, key));

        request.getSession().setAttribute(CONDITION_ATTRIBUTE_NAME, result);

        return result;
    }

    public static void initParameterMap(HttpServletRequest request, String key)
    {
        Map<String, String> parameterMap = CommonTools.saveParamersToMap(request);

        request.getSession().setAttribute(key + "_" + PARAMETER_MAP, parameterMap);

        request.getSession().setAttribute(PARAMETER_MAP, parameterMap);

        Enumeration enums = request.getAttributeNames();

        while (enums.hasMoreElements())
        {
            String name = (String)enums.nextElement();

            if (name.startsWith(PAREXT))
            {
                Object ox = request.getAttribute(name);

                if (ox != null)
                {
                    parameterMap.put(name.substring(PAREXT.length()), ox.toString());
                }
            }
        }
    }

    public static void updateParameterMap(HttpServletRequest request, String key)
    {
        Map parameterMap = (Map)request.getSession().getAttribute(key + "_" + PARAMETER_MAP);

        if (parameterMap == null)
        {
            parameterMap = (Map)request.getSession().getAttribute(PARAMETER_MAP);
        }

        request.getSession().setAttribute(PARAMETER_MAP, parameterMap);
    }

    public static Map getParameterMap(HttpServletRequest request, String key)
    {
        Map parameterMap = (Map)request.getSession().getAttribute(key + "_" + PARAMETER_MAP);

        if (parameterMap == null)
        {
            parameterMap = (Map)request.getSession().getAttribute(PARAMETER_MAP);
        }

        if (parameterMap == null)
        {
            parameterMap = new HashMap();
        }

        return parameterMap;
    }
}
