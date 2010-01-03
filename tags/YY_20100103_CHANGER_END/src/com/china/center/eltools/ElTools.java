/*
 * 文件名：ElTools.java 版权：Copyright by www.centerchina.com 描述： 修改人：zhuzhu
 * 修改时间：2006-9-29
 *
 */

package com.china.center.eltools;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.china.center.objecttoxml.ObjectToXml;
import com.china.center.tools.RegularExpress;
import com.china.center.tools.StringTools;
import com.china.centet.yongyin.bean.helper.OutBeanHelper;
import com.china.centet.yongyin.constant.DefinedCommon;


/**
 * 〈一句话功能简述〉
 * 
 * @author zhuzhu
 * @version 2006-9-29
 * @see ElTools
 * @since
 */

public final class ElTools
{
    /**
     * Description:获得对象的长度 <br>
     * 
     * @param o
     * @return int
     */
    public static int length(Object o)
    {
        if (o == null)
        {
            return 0;
        }

        if (o instanceof List)
        {
            return ((List)o).size();
        }

        if (o instanceof Map)
        {
            return ((Map)o).size();
        }

        if (o instanceof String)
        {
            return ((String)o).length();
        }

        return 0;
    }

    /**
     * Description: 获得数组的长度<br>
     * 
     * @param o
     * @return int
     */
    public static int lengthArray(Object[] o)
    {
        return o == null ? -1 : o.length;
    }

    /**
     * Description: 在界面显示的时候截断字符<br>
     * 
     * @param s
     * @param begin
     * @param end
     * @return String
     */
    public static String truncateString(String s, int begin, int end)
    {
        if (s == null)
        {
            return "";
        }

        if (begin < 0)
        {
            return "";
        }

        if (begin >= end)
        {
            return "";
        }

        s = s.trim();
        if (begin >= s.length())
        {
            return "";
        }

        if (end >= s.length())
        {
            return s;
        }
        else
        {
            return s.substring(begin, end) + "...";
        }
    }

    public static String toXML(Object o)
    {
        return ObjectToXml.toXML(o);
    }

    public static String status(int i)
    {
        return OutBeanHelper.getStatus(i);
    }

    public static String getValue(int i, Object... s)
    {
        if (s == null || s.length == 0)
        {
            return "";
        }

        if (s.length < i)
        {
            return s[s.length - 1].toString();
        }

        return s[i].toString();
    }

    public static String get(String key, int index)
    {
        return DefinedCommon.getValue(key, index);
    }

    public static String formatNum(double d)
    {
        DecimalFormat df = new DecimalFormat("####0.00");

        String result = df.format(d);

        if (".00".equals(result))
        {
            result = "0" + result;
        }

        return result;
    }

    public static int parserInt(String src)
    {
        if (StringTools.isNullOrNone(src))
        {
            return 0;
        }

        if ( !RegularExpress.isGuid(src))
        {
            return 0;
        }

        return Integer.parseInt(src);
    }

    public static String show(String str1, String str2)
    {
        if ( !StringTools.isNullOrNone(str1))
        {
            return str1;
        }

        if ( !StringTools.isNullOrNone(str2))
        {
            return str2;
        }

        return "";
    }
}
