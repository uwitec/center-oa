/*
 * File Name: MathTools.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.text.DecimalFormat;


/**
 * @author ZHUZHU
 * @version 2007-12-16
 * @see
 * @since
 */
public abstract class MathTools
{
    /**
     * 解析数字
     * 
     * @param s
     * @return
     */
    public static int parseInt(String s)
    {
        if (StringTools.isNullOrNone(s))
        {
            return 0;
        }

        if (RegularExpress.isNumber(s))
        {
            return Integer.parseInt(s);
        }

        return 0;
    }

    public static double parseDouble(String s)
    {
        if (StringTools.isNullOrNone(s))
        {
            return 0.0d;
        }

        if (RegularExpress.isDouble(s))
        {
            return Double.parseDouble(s);
        }

        return 0.0d;
    }

    /**
     * equal(解决java里面浮点数的问题)
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean equal(double a, double b)
    {
        String astr = formatNum(a);

        String bstr = formatNum(b);

        return parseDouble(astr) - parseDouble(bstr) == 0;
    }

    public static boolean equal2(double a, double b)
    {
        long aa = Math.round(a * 1000);
        long bb = Math.round(b * 1000);

        return Math.abs(aa - bb) <= 10;
    }

    public static int compare(double a, double b)
    {
        long aa = Math.round(a * 1000);
        long bb = Math.round(b * 1000);

        if (Math.abs(aa - bb) <= 10)
        {
            return 0;
        }

        if (aa > bb)
        {
            return 1;
        }

        return -1;
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

    /**
     * 格式化
     * 
     * @param d
     * @return
     */
    public static double round2(double d)
    {
        DecimalFormat df = new DecimalFormat("####0.00");

        String result = df.format(d);

        if (".00".equals(result))
        {
            result = "0" + result;
        }

        return Double.parseDouble(result);
    }

    public static long doubleToLong2(String value)
    {
        // 先格式转成double
        double parseDouble = MathTools.parseDouble(value);

        return Math.round(MathTools.parseDouble(formatNum(parseDouble)) * 100);
    }

    public static double longToDouble2(long value)
    {
        return value / 100.0d;
    }

    public static String longToDoubleStr2(long value)
    {
        return formatNum(value / 100.0d);
    }

    public static long doubleToLong2(double value)
    {
        return Math.round(MathTools.parseDouble(formatNum(value)) * 100);
    }
}
