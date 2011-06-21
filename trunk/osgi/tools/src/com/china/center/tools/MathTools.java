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
        int aa = (int)Math.round(a * 1000);
        int bb = (int)Math.round(b * 1000);

        return Math.abs(aa - bb) <= 5;
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
}
