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
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
public abstract class MathTools
{
    /**
     * ½âÎöÊý×Ö
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

        if (RegularExpress.isGuid(s))
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
}
