/*
 * File Name: Test.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-11-3
 * Grant: open source to everybody
 */
package com.china.center.tools;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * <ÃèÊö>
 *
 * @author zhuzhu
 * @version 2007-11-3
 * @see
 * @since
 */
public class Test
{
    public List<String> add(List<List<List>> flaots, Map<String, Integer> map)
    {
        return null;
    }

    /**
     * @param args
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static void main(String[] args) throws SecurityException,
            NoSuchMethodException
    {
        Method method = Test.class.getDeclaredMethod("add", List.class, Map.class);

        System.out.println(ParameterizedTools.getOnlyReturnType(method));

        System.out.println(ParameterizedTools.getOnlyParamterType(method, 0));

        System.out.println(TimeTools.cdate("2008-06-13", "1006-06-12"));
    }

}
