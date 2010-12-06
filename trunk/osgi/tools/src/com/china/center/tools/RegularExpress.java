package com.china.center.tools;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegularExpress
{
    /**
     * Automatically generated method: RegularExpress
     */
    private RegularExpress()
    {

    }

    /**
     * Description: 检查是否为有效字符 <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param name
     * @return boolean
     */
    public static boolean isValidString(String name)
    {
        if (name == null || "".equals(name))
        {
            return false;
        }

        Pattern p = Pattern.compile("[ ,./<>?'\";:~!`#$%\\^&　*\\(\\)\\-=\\+\\\\|\\{\\}\\[\\]]");
        Matcher m = p.matcher(name);

        return !m.find();
    }

    /**
     * Description: 检查时间格式是否为年月日时分秒格式 <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param date
     * @return boolean
     */
    public static boolean isLongDate(String date)
    {
        if (date == null || "".equals(date))
        {
            return false;
        }

        Pattern p = Pattern
            .compile("^[0-9]{4}-[01][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]$");
        Matcher m = p.matcher(date);

        return m.find();
    }

    /**
     * Description: 检查时间格式是否为年月日格式 <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param date
     * @return boolean
     */
    public static boolean isShortDate(String date)
    {
        if (date == null || "".equals(date))
        {
            return false;
        }

        Pattern p = Pattern.compile("^[0-9]{4}-[01][0-9]-[0-3][0-9]$");
        Matcher m = p.matcher(date);

        return m.find();
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
     * Description: 是否是数字(包括负数) <br>
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

        // ^-?[1-9]\d*$
        Pattern p = Pattern.compile("^-?[0-9]+$");
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

    /**
     * Description: 是否是数字和字母组合 <br>
     * 
     * @param str
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNumberOrLetter(String str)
    {
        if (str == null || "".equals(str))
        {
            return false;
        }
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(str);

        return m.find();
    }

    /**
     * Description: 是否是数字和字符组合 <br>
     * 
     * @param str
     * @return boolean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNumOrLetter(String str)
    {
        if (str == null || "".equals(str))
        {
            return false;
        }
        Pattern p = Pattern.compile("^[A-Za-z.@0-9]+$");
        Matcher m = p.matcher(str);

        return m.find();
    }

    public static void main(String[] args)
    {
        System.out.println(isNumber("1"));
    }
}
