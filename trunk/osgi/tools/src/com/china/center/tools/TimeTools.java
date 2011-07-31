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
public class TimeTools
{
    /**
     * 平年每月日期
     */
    private static int[] daysInMonth = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static String[] week = new String[] {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
        "星期六"};

    public static String LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String SHORT_FORMAT = "yyyy-MM-dd";

    /**
     * 每天的毫秒
     */
    private static long DAY_MS = 24 * 3600 * 1000;

    /**
     * 获得星期几
     * 
     * @param dayOfWeek
     * @return
     */
    public static String getWeekDay(int dayOfWeek)
    {
        if (dayOfWeek <= 0 || dayOfWeek > 7)
        {
            return "";
        }

        return week[dayOfWeek];
    }

    /**
     * getYeay
     * 
     * @return
     */
    public static int getYeay()
    {
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.YEAR);
    }

    /**
     * 获得星期几
     * 
     * @param dateStr
     * @return
     */
    public static String getWeekDay(String dateStr)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(TimeTools.getDateByFormat(dateStr, TimeTools.SHORT_FORMAT));

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        return getWeekDay(dayOfWeek);
    }

    /**
     * Description: 通过LONG_FORMAT字符串获得时间<br>
     * 
     * @param format
     * @return Date (异常返回null)
     */
    public static Date getDate(String format)
    {
        Date date = null;
        try
        {
            SimpleDateFormat df = new SimpleDateFormat(LONG_FORMAT);
            date = df.parse(format);
        }
        catch (Exception e)
        {
            date = null;
        }
        return date;
    }

    /**
     * 获得当前的季节数(从1开始 1就是春季)
     * 
     * @return
     */
    public int getCurrentSeason()
    {
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH) + 1;

        if (month <= 3)
        {
            return 1;
        }

        if (month <= 6)
        {
            return 2;
        }

        if (month <= 9)
        {
            return 3;
        }

        return 4;
    }

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
     * 从yyyy-MM-dd HH:mm:ss到yyyy-MM-dd
     * 
     * @param time
     * @return
     */
    public static String changeTimeToDate(String time)
    {
        return time.substring(0, SHORT_FORMAT.length());
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
     * 获得指定日期时间的前后天数
     * 
     * @param org
     *            yyyy-MM-dd HH:mm:ss
     * @param days
     *            天数
     * @return
     */
    public static String getStringByOrgAndDays(String org, int days)
    {
        return getStringByFormat(new Date(getDateByFormat(org, LONG_FORMAT).getTime() + days
                                          * DAY_MS), LONG_FORMAT);
    }

    /**
     * 获取指定时间后的时间(格式指定)
     * 
     * @param org
     * @param days
     * @param format
     * @return
     */
    public static String getStringByOrgAndDaysAndFormat(String org, int days, String format)
    {
        return getStringByFormat(new Date(getDateByFormat(org, format).getTime() + days * DAY_MS),
            format);
    }

    /**
     * 格式转
     * 
     * @param org
     * @param oldFormat
     * @param newFormat
     * @return
     */
    public static String changeFormat(String org, String oldFormat, String newFormat)
    {
        Date date = getDateByFormat(org, oldFormat);

        return getStringByFormat(date, newFormat);
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
        return getDateString(days, SHORT_FORMAT);
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
     * 根据指定的string时间获得N天后的时间
     * 
     * @param date(LONG_FORMAT)
     * @param days
     * @return
     */
    public static String getSpecialDateStringByDays(String date, int days)
    {
        Date dateByFormat = getDateByFormat(date, TimeTools.LONG_FORMAT);

        return getStringByFormat(new Date(dateByFormat.getTime() + DAY_MS * days),
            TimeTools.LONG_FORMAT);
    }

    public static String getSpecialDateStringByDays(String date, int days, String fomat)
    {
        Date dateByFormat = getDateByFormat(date, fomat);

        return getStringByFormat(new Date(dateByFormat.getTime() + DAY_MS * days), fomat);
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
        return getDateString(days, LONG_FORMAT);
    }

    /**
     * 获得指定的时间
     * 
     * @param days
     * @param format
     * @return
     */
    public static String getDateFullString(int days, String format)
    {
        return getDateString(days, format);
    }

    public static int cdate(String compareA, String compareB)
    {
        Date d1 = getDateByFormat(compareA, SHORT_FORMAT);
        Date d2 = getDateByFormat(compareB, SHORT_FORMAT);

        return (int) ( (d1.getTime() - d2.getTime()) / DAY_MS);
    }

    /**
     * Description: 通过LONG_FORMAT字符串获得时间<br>
     * 
     * @param format
     * @return Date (异常返回null)
     */
    public static java.sql.Date getSqlDate(String dateString)
    {
        Date date = null;
        try
        {
            SimpleDateFormat df = new SimpleDateFormat(SHORT_FORMAT);
            date = df.parse(dateString);
        }
        catch (Exception e)
        {
            return null;
        }

        return new java.sql.Date(date.getTime());
    }

    /**
     * Description: 通过LONG_FORMAT字符串获得时间<br>
     * 
     * @param format
     * @return Date (异常返回null)
     */
    public static String getStringBySqlDate(java.sql.Date date)
    {
        return getStringByFormat(new Date(date.getTime()), SHORT_FORMAT);
    }

    /**
     * Description:通过制定的格式化方法获得date <br>
     * 
     * @param dateString
     *            日期的字符表现
     * @param format
     *            格式化的字符串 eg：LONG_FORMAT
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
     * 获得格式化的日期(yyyy-MM-dd)
     * 
     * @param str
     * @return
     */
    public static String getFormatDateStr(String str)
    {
        Date tem = getDateByFormat(str, SHORT_FORMAT);

        if (tem == null)
        {
            return "";
        }

        return getStringByFormat(tem, SHORT_FORMAT);
    }

    /**
     * 获得格式化的日期+时间(yyyy-MM-dd HH:mm:ss)
     * 
     * @param str
     * @return
     */
    public static String getFormatDateTimeStr(String str)
    {
        Date tem = getDateByFormat(str, LONG_FORMAT);

        if (tem == null)
        {
            return "";
        }

        return getStringByFormat(tem, LONG_FORMAT);
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
            SimpleDateFormat df = new SimpleDateFormat(LONG_FORMAT);
            dateString = df.format(date);
        }
        catch (Exception e)
        {
            dateString = "";
        }
        return dateString;
    }

    public static String getString(long time)
    {
        return getStringByFormat(new Date(time), LONG_FORMAT);
    }

    public static String getString(long time, String foramt)
    {
        return getStringByFormat(new Date(time), foramt);
    }

    public static String getShortString(long time)
    {
        return getStringByFormat(new Date(time), SHORT_FORMAT);
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

    public static String now_short(int days)
    {
        return getDateString(days, SHORT_FORMAT);
    }

    public static String now(String format)
    {
        return getStringByFormat(new Date(), format);
    }

    public static String now_short()
    {
        return getStringByFormat(new Date(), SHORT_FORMAT);
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

        return getStringByFormat(new Date(cal.getTime().getTime()), SHORT_FORMAT);
    }

    /**
     * 给出月初的日期获得月末日期
     * 
     * @param monthBeginDateString
     * @return
     */
    public static String getMonthEnd(String monthBeginDateString)
    {
        Calendar cal = Calendar.getInstance();

        // 本月时间
        cal.setTime(TimeTools.getDateByFormat(monthBeginDateString, SHORT_FORMAT));

        // 下个月的1号
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);

        return getStringByFormat(new Date(cal.getTime().getTime() - DAY_MS), SHORT_FORMAT);
    }

    public static String getLastestMonthBegin()
    {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);

        cal.set(Calendar.DAY_OF_MONTH, 1);

        return getStringByFormat(new Date(cal.getTime().getTime()), SHORT_FORMAT);
    }

    public static void main(String[] args)
    {
        System.out.println(getMonthEnd("2011-07-01"));
    }
}
