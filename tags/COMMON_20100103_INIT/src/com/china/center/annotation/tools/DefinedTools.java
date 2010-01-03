/**
 * File Name: DefinedTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-2<br>
 * Grant: open source to everybody
 */
package com.china.center.annotation.tools;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.annotation.Defined;
import com.china.center.common.taglib.MapBean;
import com.china.center.oa.constant.BudgetConstant;
import com.china.center.tools.CommonTools;


/**
 * 利用Defined解析常量定义
 * 
 * @author zhuzhu
 * @version 2008-12-2
 * @see DefinedTools
 * @since 1.0
 */
public abstract class DefinedTools
{
    /**
     * 解析constant获得
     * 
     * @param map
     * @param claz
     */
    public static void parserConstant(Map<String, List<MapBean>> map, Class claz)
    {
        Field[] ff = claz.getDeclaredFields();

        for (Field field : ff)
        {
            Defined defined = field.getAnnotation(Defined.class);

            if (defined == null)
            {
                continue;
            }

            // 必须是public static的field才可以
            if ( ! (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())))
            {
                continue;
            }

            String key = defined.key();

            String value = defined.value();

            int index = 0;

            try
            {
                Object obj = field.get(null);

                if (obj == null)
                {
                    continue;
                }

                if (obj instanceof Integer || obj instanceof Long || obj instanceof String)
                {
                    index = CommonTools.parseInt(obj.toString());
                }

                List<MapBean> list = map.get(key);

                if (list == null)
                {
                    list = new ArrayList<MapBean>();

                    map.put(key, list);
                }

                list.add(new MapBean(index, value));
            }
            catch (Exception e)
            {}
        }
    }

    public static void main(String[] args)
    {
        Map<String, List<MapBean>> map = new HashMap();

        DefinedTools.parserConstant(map, BudgetConstant.class);

        System.out.println(map);
    }
}
