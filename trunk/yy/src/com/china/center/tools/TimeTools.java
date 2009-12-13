/*
 * 文件名：TimeTools.java
 * 版权：Copyright by www.centerchina.com
 * 描述：
 * 修改人：zhu
 * 修改时间：2006-7-7
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.china.center.tools;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间的工具类
 * 
 * @author zhu
 * @version 2006-7-7
 * @see TimeTools
 * @since
 */
public abstract class TimeTools
{
    /**
     * 平年每月日期
     */
    private static int[] daysInMonth = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * 获得指定月份的天数
     * 
     * @param year
     *            年
     * @param month
     *            月份,从1开始到12(如果越界默认为1)
     * @return days
     */
    public static int getDaysOfMonth(int year, int month)
    {
        if (month < 1 || month > 12)
        {
            month = 1;
        }

        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
        {
            if (month == 2)
            {
                return 29;
            }
        }

        return daysInMonth[month - 1];
    }

    /**
     * Description: 通过"yyyy-MM-dd HH:mm:ss"字符串获得时间<br>
     * 
     * @param format
     * @return Date (异常返回null)
     */
    public static Date getDate(String format)
    {
        Date date = null;
        try
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = df.parse(format);
        }
        catch (Exception e)
        {
            date = null;
        }
        return date;
    }

    /**
     * 格式化
     * 
     * @param days
     * @param format
     * @return
     */
    public static String getSpecialDateString(int days, String format)
    {
        return getDateString(days, format);
    }

    /**
     * 获得指定的时间
     * 
     * @param days
     *            当前时间的前后多少天
     * @return 时间字符串
     */
    public static String getDateString(int days, String format)
    {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_YEAR, days);

        return getStringByFormat(new Date(cal.getTimeInMillis()), format);
    }

    /**
     * 获得指定的时间
     * 
     * @param days
     *            当前时间的前后多少天
     * @return 时间字符串
     */
    public static String getDateShortString(int days)
    {
        return getDateString(days, "yyyy-MM-dd");
    }

    /**
     * 获得指定的时间
     * 
     * @param days
     *            当前时间的前后多少天
     * @return 时间字符串
     */
    public static String getDateFullString(int days)
    {
        return getDateString(days, "yyyy-MM-dd HH:mm:ss");
    }

    public static int cdate(String compareA, String compareB)
    {
        Date d1 = getDateByFormat(compareA, "yyyy-MM-dd");
        Date d2 = getDateByFormat(compareB, "yyyy-MM-dd");

        return (int) ( (d1.getTime() - d2.getTime()) / (24 * 3600 * 1000));
    }

    /**
     * Description: 通过"yyyy-MM-dd HH:mm:ss"字符串获得时间<br>
     * 
     * @param format
     * @return Date (异常返回null)
     */
    public static java.sql.Date getSqlDate(String dateString)
    {
        Date date = null;
        try
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.parse(dateString);
        }
        catch (Exception e)
        {
            return null;
        }

        return new java.sql.Date(date.getTime());
    }

    /**
     * Description: 通过"yyyy-MM-dd HH:mm:ss"字符串获得时间<br>
     * 
     * @param format
     * @return Date (异常返回null)
     */
    public static String getStringBySqlDate(java.sql.Date date)
    {
        return getStringByFormat(new Date(date.getTime()), "yyyy-MM-dd");
    }

    /**
     * Description:通过制定的格式化方法获得date <br>
     * 
     * @param dateString
     *            日期的字符表现
     * @param format
     *            格式化的字符串 eg："yyyy-MM-dd HH:mm:ss"
     * @return Date (异常返回null)
     */
    public static Date getDateByFormat(String dateString, String format)
    {
        Date date = null;
        try
        {
            SimpleDateFormat df = new SimpleDateFormat(format);
            date = df.parse(dateString);
        }
        catch (Exception e)
        {
            date = null;
        }

        return date;
    }

    /**
     * Description:通过时间获得格式化的字符串 <br>
     * 
     * @param date
     *            时间
     * @return String 格式化后的字符串(异常返回空)
     */
    public static String getString(Date date)
    {
        if (date == null)
        {
            return "";
        }

        String dateString = "";
        try
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString = df.format(date);
        }
        catch (Exception e)
        {
            dateString = "";
        }
        return dateString;
    }

    /**
     * Description:通过时间获得格式化的字符串 <br>
     * 
     * @param addTime
     *            增量的时间
     * @return String 格式化后的字符串(异常返回空)
     */
    public static String getDateTimeString(long addTime)
    {
        return getString(new Date(new Date().getTime() + addTime));
    }

    public static String now()
    {
        return getString(new Date());
    }

    public static String now(String format)
    {
        return getStringByFormat(new Date(), format);
    }

    public static String now_short()
    {
        return getStringByFormat(new Date(), "yyyy-MM-dd");
    }

    /**
     * Description: 通过制定的格式化形式获得时间的字符串<br>
     * 
     * @param date
     *            需要格式化的时间
     * @param format
     *            格式化类型 eg:yyyy-MM-dd HH:mm:ss
     * @return String 格式化后的字符串(异常返回空)
     */
    public static String getStringByFormat(Date date, String format)
    {
        String dateString = "";
        try
        {
            SimpleDateFormat df = new SimpleDateFormat(format);
            dateString = df.format(date);
        }
        catch (Exception e)
        {
            dateString = "";
        }
        return dateString;
    }

    public static String getMonthBegin()
    {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);

        return getStringByFormat(new Date(cal.getTime().getTime()), "yyyy-MM-dd");
    }

    public static String getLastestMonthBegin()
    {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);

        cal.set(Calendar.DAY_OF_MONTH, 1);

        return getStringByFormat(new Date(cal.getTime().getTime()), "yyyy-MM-dd");
    }

    public static void main(String[] args)
    {
        System.out.println(getLastestMonthBegin());
    }

    /**
     * get current time(before or after days)
     * 
     * @param days
     * @return
     */
    public static String now(int days)
    {
        return getDateFullString(days);
    }

}
