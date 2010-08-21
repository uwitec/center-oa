/**
 * File Name: DefinedCommon.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-2<br>
 * Grant: open source to everybody
 */
package com.china.center.common.taglib;


import java.util.List;
import java.util.Map;


/**
 * DefinedCommon
 * 
 * @author ZHUZHU
 * @version 2008-3-2
 * @see
 * @since
 */
public abstract class DefinedCommon
{
    private static Map<String, List<MapBean>> definedMap = PageSelectOption.optionMap;

    public static void addDefined(String key, List<MapBean> definedList)
    {
        definedMap.put(key, definedList);
    }

    public static void removeDefined(String key)
    {
        definedMap.remove(key);
    }

    public static void addDefinedClass(Class claz)
    {
        DefinedTools.parserConstant(definedMap, claz);
    }

    public static void reomoveConstant(Class claz)
    {
        DefinedTools.reomoveConstant(definedMap, claz);
    }

    public static String getValue(String key, int index)
    {
        List<MapBean> oo = definedMap.get(key);

        if (oo == null)
        {
            return "";
        }

        if (index < 0)
        {
            return "";
        }

        for (MapBean mapBean : oo)
        {
            if (mapBean.getKey().equals(String.valueOf(index)))
            {
                return mapBean.getValue();
            }
        }

        if (index >= oo.size())
        {
            return oo.get(oo.size() - 1).getValue();
        }

        return oo.get(index).getValue();
    }

    public static String getValue(String key, String indexKey)
    {
        List<MapBean> oo = definedMap.get(key);

        if (oo == null)
        {
            return "";
        }

        for (MapBean mapBean : oo)
        {
            if (mapBean.getKey().equals(indexKey))
            {
                return mapBean.getValue();
            }
        }

        return "";
    }

    /**
     * ҳ������Ⱦɫ�ɺ�ɫ
     * 
     * @param str
     * @return
     */
    public static String colorationToRed(String str)
    {
        return coloration(str, "red");
    }

    /**
     * ҳ������Ⱦɫ��6ɫ
     * 
     * @param str
     * @return
     */
    public static String colorationToBlue(String str)
    {
        return coloration(str, "blue");
    }

    /**
     * #FFD700 ��ɫ
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
