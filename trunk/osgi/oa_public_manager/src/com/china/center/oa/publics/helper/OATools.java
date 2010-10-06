/**
 * File Name: OATools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.helper;


import java.util.Calendar;

import com.china.center.tools.TimeTools;


/**
 * OATools
 * 
 * @author ZHUZHU
 * @version 2009-12-8
 * @see OATools
 * @since 1.0
 */
public abstract class OATools
{
    /**
     * 获得财务年度的开始日期
     * 
     * @return
     */
    public static String getFinanceBeginDate()
    {
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH);

        int year = cal.get(Calendar.YEAR);

        // 上一年的
        if (month >= 0 && month <= 1)
        {
            return (year - 1) + "-03-01";
        }

        return year + "-03-01";
    }

    /**
     * 获得财务年度的开始日期
     * 
     * @return
     */
    public static int getFinanceYear()
    {
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH);

        int year = cal.get(Calendar.YEAR);

        // 上一年的
        if (month >= 0 && month <= 1)
        {
            return year - 1;
        }

        return year;
    }

    /**
     * 获得财务年度的结束日期
     * 
     * @return
     */
    public static String getFinanceEndDate()
    {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH);

        // 0-2月份
        if (month >= 0 && month <= 1)
        {
            int realYear = year;

            int daysOfMonth = TimeTools.getDaysOfMonth(realYear, 2);

            return realYear + "-02-" + daysOfMonth;
        }

        int realYear = year + 1;

        int daysOfMonth = TimeTools.getDaysOfMonth(realYear, 2);

        return realYear + "-02-" + daysOfMonth;
    }
}
