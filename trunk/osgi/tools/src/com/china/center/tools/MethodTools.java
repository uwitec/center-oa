/*
 * File Name: MethodTools.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-13
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.lang.reflect.Method;


/**
 * <描述>
 * 
 * @author ZHUZHU
 * @version 2008-1-13
 * @see
 * @since
 */
public abstract class MethodTools
{
    public static Method getMethodByName(String name, Class claz)
    {
        Method[] mts = claz.getDeclaredMethods();

        for (Method method : mts)
        {
            if (method.getName().equalsIgnoreCase(name))
            {
                return method;
            }
        }

        return null;
    }
}
