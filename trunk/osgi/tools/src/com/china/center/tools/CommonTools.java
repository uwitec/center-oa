/*
 * File Name: CommonTools.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-4-10
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


/**
 * 公用函数
 * 
 * @author ZHUZHU
 * @version 2007-4-10
 * @see
 * @since
 */
public class CommonTools
{
    private CommonTools()
    {
    }

    /**
     * getParamerFromAll
     * 
     * @param request
     * @param key
     * @return
     */
    public static String getParamerFromAll(HttpServletRequest request, String key)
    {
        String keyValue = request.getParameter(key);

        if ( !StringTools.isNullOrNone(keyValue))
        {
            keyValue = (String)request.getAttribute(key);
        }

        return keyValue;
    }

    /**
     * 自动保存paramers到Attribute里面
     */
    public static void saveParamers(HttpServletRequest request)
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
                    request.setAttribute(element.getKey().toString(), oo[0]);
                }
                else
                {
                    request.setAttribute(element.getKey().toString(), oo);
                }
            }

        }
    }

    /**
     * 自动保存paramers到Attribute里面
     */
    public static void removeParamers(HttpServletRequest request)
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
                    oo[0] = null;
                }
                else
                {
                    oo = null;
                }
            }

        }
    }

    public static Map<String, String> saveParamersToMap(HttpServletRequest request)
    {
        Map map = request.getParameterMap();

        Map<String, String> result = new HashMap();

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
                    if (oo[0] != null)
                    {
                        result.put(element.getKey().toString(), oo[0].toString().trim());

                        request.setAttribute(element.getKey().toString(), oo[0]);
                    }
                    else
                    {
                        // System.out.println(element.getKey().toString());
                    }
                }
                else
                {
                    for (int i = 0; i < oo.length; i++ )
                    {
                        if (oo[i] != null)
                        {
                            oo[i] = oo[i].trim();
                        }
                    }

                    request.setAttribute(element.getKey().toString(), oo);
                }
            }

        }

        return result;
    }

    public static int parseInt(String s)
    {
        if (StringTools.isNullOrNone(s))
        {
            return 0;
        }

        if (RegularExpress.isGuid(s.trim()))
        {
            return Integer.parseInt(s);
        }

        return 0;
    }

    public static float parseFloat(String s)
    {
        if (StringTools.isNullOrNone(s))
        {
            return 0.0f;
        }

        if (RegularExpress.isDouble(s.trim()))
        {
            return Float.parseFloat(s);
        }

        return 0.0f;
    }

    public static void closeDatatStream(Map<String, InputStream> datatStream)
    {
        for (Map.Entry<String, InputStream> entry : datatStream.entrySet())
        {
            InputStream in = entry.getValue();

            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }
}
