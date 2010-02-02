/**
 * File Name: DefinedCommon.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-2<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.constant;


import java.util.HashMap;
import java.util.Map;

import com.china.center.tools.TimeTools;


/**
 * 常量的定义和使用
 * 
 * @author ZHUZHU
 * @version 2008-3-2
 * @see
 * @since
 */
public abstract class DefinedCommon
{
    private static Map<String, Object[]> definedMap = new HashMap<String, Object[]>();

    public static String webBeginTime = TimeTools.now();

    static
    {
        definedMap.put("outType", new Object[] {"采购入库", "调出", "盘亏出库", "盘盈入库", "调入", "退换货入库",
            "报废出库", "采购退货", "调入"});

        definedMap.put("reprotType", new Object[] {"<font color=blue>无回复</font>", "正常收货",
            "<font color=red>异常收货</font>"});

        definedMap.put("memberGrade", new Object[] {"普通", "银卡", "金卡", "铂金卡"});

        definedMap.put("memberType", new Object[] {"普通会员", "永久会员"});

        definedMap.put("priceStatus", new Object[] {"发布", "驳回"});

        definedMap.put("priceAskStatus", new Object[] {"开始", colorationToBlue("询价中"),
            colorationToRed("驳回"), "结束"});

        definedMap.put("priceAskInstancy", new Object[] {"一般", "紧急", "非常紧急"});

        definedMap.put("stockItemPayStatus", new Object[] {colorationToRed("未汇总"),
            colorationToBlue("已汇总")});

        definedMap.put("priceAskType", new Object[] {"内部询价", colorationToBlue("外网询价"),
            colorationToBlue("内外网询价")});

        definedMap.put("stockStatus", new Object[] {colorationToGold("保存"), "提交",
            colorationToRed("驳回"), "区域经理通过", "核价员通过", "采购主管通过", "采购经理通过", colorationToBlue("采购中"),
            colorationToBlue("采购到货")});

        definedMap.put("stockPay", new Object[] {colorationToRed("未付款"), colorationToBlue("已付款"),
            colorationToGold("付款申请"), colorationToRed("申请驳回")});

        definedMap.put("stockExceptStatus", new Object[] {colorationToBlue("正常"),
            colorationToRed("产品价格非最小"), colorationToGold("产品价格总计过大")});

        definedMap.put("flowDefineStatus", new Object[] {"发布", "发布", colorationToRed("废弃")});

        definedMap.put("outCredit", new Object[] {"正常", "超支", "价格为0"});

        definedMap.put("tokenType", new Object[] {"人员处理", "角色处理", "全部", "职员处理", ""});
    }

    public static String getValue(String key, int index)
    {
        Object[] oo = definedMap.get(key);

        if (oo == null || oo.length == 0)
        {
            return "";
        }

        if (index < 0)
        {
            return "";
        }

        if (index >= oo.length)
        {
            return oo[oo.length - 1].toString();
        }

        return oo[index].toString();

    }

    /**
     * 页面字体染色成红色
     * 
     * @param str
     * @return
     */
    public static String colorationToRed(String str)
    {
        return coloration(str, "red");
    }

    /**
     * 页面字体染色成蓝色
     * 
     * @param str
     * @return
     */
    public static String colorationToBlue(String str)
    {
        return coloration(str, "blue");
    }

    /**
     * #FFD700 金色
     * 
     * @param str
     * @return
     */
    public static String colorationToGold(String str)
    {
        return coloration(str, "gold");
    }

    /**
     * #00FFFF
     * 
     * @param str
     * @return
     */
    public static String colorationToCyan(String str)
    {
        return coloration(str, "cyan");
    }

    public static String coloration(String str, String color)
    {
        return "<font color=" + color + ">" + str + "</font>";
    }
}
