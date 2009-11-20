/*
 * File Name: OutBeanHelper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-14
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean.helper;


import java.util.List;

import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.centet.yongyin.vo.StockItemBeanVO;


/**
 * @author zhuzhu
 * @version 2007-8-14
 * @see
 * @since
 */
public abstract class StockHelper
{
    public static String createTable(List<StockItemBeanVO> list)
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<table width='100%' border='0' cellspacing='1'>");
        buffer.append("<tr align='center' class='content0'>");
        buffer.append("<td width='30%' align='center'>产品</td>");
        buffer.append("<td width='10%' align='center'>数量</td>");
        buffer.append("<td width='10%' align='center'>报价</td>");
        buffer.append("<td width='10%' align='center'>预期价格</td>");
        buffer.append("<td width='30%' align='center'>供应商</td>");
        buffer.append("<td width='10%' align='center'>合计</td>");

        buffer.append("</tr>");

        int index = 0;
        String cls = null;
        for (StockItemBeanVO bean : list)
        {
            if (index % 2 == 0)
            {
                cls = "content1";
            }
            else
            {
                cls = "content2";
            }

            buffer.append("<tr class='" + cls + "'>");

            buffer.append("<td  align='center'>" + bean.getProductName() + "</td>");
            buffer.append("<td  align='center'>" + bean.getAmount() + "</td>");
            buffer.append("<td  align='center'>" + MathTools.formatNum(bean.getPrice()) + "</td>");
            buffer.append("<td  align='center'>" + MathTools.formatNum(bean.getPrePrice())
                          + "</td>");
            buffer.append("<td  align='center'>" + StringTools.print(bean.getProviderName())
                          + "</td>");
            buffer.append("<td  align='center'>" + MathTools.formatNum(bean.getTotal()) + "</td>");

            buffer.append("</tr>");
            index++ ;
        }

        buffer.append("</table>");

        return buffer.toString();
    }

    public static String getStatus(int i)
    {
        if (i == 0)
        {
            return "保存";
        }

        if (i == 1)
        {
            return "提交";
        }

        if (i == 2)
        {
            return "<font color=red>驳回</font>";
        }

        if (i == 3)
        {
            return "区域经理通过";
        }

        if (i == 4)
        {
            return "核价员通过";
        }

        if (i == 5)
        {
            return "采购主管通过";
        }

        if (i == 6)
        {
            return "采购经理通过";
        }

        if (i == 7)
        {
            return "结束进入采购中";
        }

        if (i == 8)
        {
            return "采购结束";
        }

        return "";
    }
}
