package com.china.center.jdbc.util;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class JDBCCommonTools
{
    public static boolean isEmptyOrNull(List list)
    {
        if (list == null || list.size() == 0)
        {
            return true;
        }

        return false;
    }

    /**
     * isNullOrNone
     * 
     * @param name
     * @return
     */
    public static boolean isNullOrNone(String name)
    {
        if (name == null || "".equals(name.trim()))
        {
            return true;
        }

        return false;
    }

    public static boolean isNullOrNone(Object name)
    {
        if (name == null)
        {
            return true;
        }

        if (name instanceof String)
        {
            String temp = (String)name;

            if ("".equals(temp.trim()))
            {
                return true;
            }
        }

        return false;
    }

    public static int parseInt(String s)
    {
        if (isNullOrNone(s))
        {
            return 0;
        }

        if (isGuid(s.trim()))
        {
            return Integer.parseInt(s);
        }

        return 0;
    }

    public static float parseFloat(String s)
    {
        if (isNullOrNone(s))
        {
            return 0.0f;
        }

        if (isDouble(s.trim()))
        {
            return Float.parseFloat(s);
        }

        return 0.0f;
    }

    /**
     * Description:检查GUID是否为全数字类型 <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param id
     * @return boolean
     */
    public static boolean isGuid(String id)
    {
        if (id == null || "".equals(id))
        {
            return false;
        }

        Pattern p = Pattern.compile("^[0-9]+$");
        Matcher m = p.matcher(id);

        return m.find();
    }

    /**
     * Description: 是否是数字 <br>
     * 
     * @param id
     * @return boolean
     */
    public static boolean isNumber(String id)
    {
        if (id == null || "".equals(id))
        {
            return false;
        }

        Pattern p = Pattern.compile("^[0-9]+$");
        Matcher m = p.matcher(id);

        return m.find();
    }

    public static boolean isDouble(String id)
    {
        if (id == null || "".equals(id))
        {
            return false;
        }

        Pattern p = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");

        Matcher m = p.matcher(id);

        return m.find();
    }
}
