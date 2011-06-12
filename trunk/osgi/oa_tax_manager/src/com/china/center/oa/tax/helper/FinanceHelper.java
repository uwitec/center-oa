/**
 * File Name: FinanceHelper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.helper;


import java.text.DecimalFormat;

import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.tools.MathTools;


/**
 * FinanceHelper
 * 
 * @author ZHUZHU
 * @version 2011-6-12
 * @see FinanceHelper
 * @since 3.0
 */
public abstract class FinanceHelper
{
    /**
     * 字符串数字转换成long(准确到微分,就是小数点后四位)
     * 
     * @param val
     * @return
     */
    public static long doubleToLong(String val)
    {
        // 先格式转成double
        double parseDouble = MathTools.parseDouble(val);

        return (long)Math.round(MathTools.parseDouble(formatNum2(parseDouble))
                                * TaxConstanst.DOUBLE_TO_INT);
    }

    /**
     * 数字转换成long(准确到微分,就是小数点后四位)
     * 
     * @param val
     * @return
     */
    public static long doubleToLong(double val)
    {
        String formatNum2 = formatNum2(val);

        return (long)Math.round(MathTools.parseDouble(formatNum2) * TaxConstanst.DOUBLE_TO_INT);
    }

    public static String formatNum2(double d)
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
     * 变成展现的string
     * 
     * @param val
     * @return
     */
    public static String longToString(long val)
    {
        return MathTools.formatNum(val / (TaxConstanst.DOUBLE_TO_INT + 0.0d));
    }
}
