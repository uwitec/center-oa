package com.china.center.osgi.jsp;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.center.china.osgi.publics.User;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.osgi.dym.DynamicBundleTools;
import com.china.center.tools.RegularExpress;
import com.china.center.tools.StringTools;


/**
 * ElTools
 * 
 * @author ZHUZHU
 * @version 2006-9-29
 * @see ElTools
 * @since
 */

public final class ElTools
{
    /**
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
     * @param o
     * @return int
     */
    public static int lengthArray(Object[] o)
    {
        return o == null ? -1 : o.length;
    }

    /**
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

    public static boolean auth(User user, String authId)
    {
        if (authId.equals("0000"))
        {
            return true;
        }

        List<String> authList = user.getAuthIdList();

        for (String auth : authList)
        {
            if (auth.equals(authId))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean dym(String bundleName)
    {
        return DynamicBundleTools.isBundleActive(bundleName);
    }

    /**
     * formatNum
     * 
     * @param d
     * @return
     */
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
