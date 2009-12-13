/*
 * File Name: ListTools.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-5-27
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-5-27
 * @param <T>
 * @see
 * @since
 */
public abstract class ListTools
{
    public static List toList(Object[] o)
    {
        List result = new ArrayList();
        if (o == null)
        {
            return result;
        }

        for (int i = 0; i < o.length; i++ )
        {
            result.add(o[i]);
        }

        return result;
    }

    public static <T> List newList(Class<T> claz)
    {
        return new ArrayList<T>();
    }

    public static List newList()
    {
        return new ArrayList();
    }

    public static void flush(List<?> list)
    {
        if (list == null || list.size() == 0) return;
        for (int i = 0; i < list.size(); i++ )
            if (list.get(i) == null)
            {
                list.remove(i);
                i-- ;
            }

    }

    public static List<?> turnList(List<?> list)
    {
        if (list == null || list.size() == 0) return list;
        List<Object> list1 = new ArrayList<Object>(list.size());
        for (int i = list.size() - 1; i >= 0; i-- )
            list1.add(list.get(i));

        return list1;
    }

    public static String[] fill(String[] ss, int length)
    {
        if (ss == null)
        {
            return new String[length];
        }

        if (ss.length == length)
        {
            return ss;
        }

        String[] tem = new String[length];
        if (ss.length < length)
        {
            System.arraycopy(ss, 0, tem, 0, ss.length);

            for (int i = ss.length; i < length; i++ )
            {
                tem[i] = "";
            }
        }
        else
        {
            System.arraycopy(ss, 0, tem, 0, length);
        }

        return tem;

    }

    public static boolean isEmptyOrNull(List list)
    {
        if (list == null || list.size() == 0)
        {
            return true;
        }

        return false;
    }

    /**
     * 转换LIST
     * 
     * @param <T>
     * @param list
     * @param claz
     * @param dest
     * @return
     */
    public static <T> List<T> changeList(List<?> list, Class<T> claz, String clazName,
                                         String method)
    {
        Class dir = null;
        try
        {
            dir = Class.forName(clazName);
        }
        catch (ClassNotFoundException e)
        {
            return new ArrayList<T>();
        }

        return changeList(list, claz, dir, method);
    }

    /**
     * 转换LIST
     * 
     * @param list
     *            需要转换的List
     * @param claz
     *            目的转化的bean
     * @param changeClass
     *            转换提供方法的class
     * @param method
     *            具体的方法名称
     * @return
     */
    public static <T> List<T> changeList(List<?> list, Class<T> claz, Class changeClass,
                                         String method)
    {
        Class dir = changeClass;

        try
        {
            Method metf = MethodTools.getMethodByName(method, dir);

            List<T> result = new ArrayList<T>();

            for (Object t : list)
            {
                result.add((T)metf.invoke(null, t));
            }

            return result;
        }
        catch (SecurityException e)
        {}
        catch (IllegalArgumentException e)
        {}
        catch (IllegalAccessException e)
        {}
        catch (InvocationTargetException e)
        {}

        return new ArrayList<T>();
    }

    public static <T> List<T> distinct(List<T> inList)
    {
        Set<T> set = new HashSet();

        for (T t : inList)
        {
            set.add(t);
        }

        List<T> result = new ArrayList();

        for (T t2 : set)
        {
            result.add(t2);
        }

        return result;
    }

    public static void randomList(List list)
    {
        if (list == null || list.size() == 0)
        {
            return;
        }

        List<Integer> ranInt = randomInt(list.size());

        List temp = new ArrayList();

        for (Integer object : ranInt)
        {
            temp.add(list.get(object));
        }

        list.clear();

        for (Object object : temp)
        {
            list.add(object);
        }
    }

    public static List<Integer> randomInt(int length)
    {
        Random random = new Random(System.currentTimeMillis());

        List<Integer> list = new ArrayList<Integer>();

        Set<Integer> set = new HashSet<Integer>();

        while (set.size() < length)
        {
            int rad = random.nextInt(length);

            if ( !set.contains(rad))
            {
                set.add(rad);
                list.add(rad);
            }
        }

        return list;
    }

    public static boolean equals(Object[] par, Object[] args)
    {
        if ( ! (par == null && args == null))
        {
            if (par == null && args != null)
            {

                return false;
            }

            if (par != null && args == null)
            {
                return false;
            }

            if (par.length != args.length)
            {
                return false;
            }

            for (int i = 0; i < args.length; i++ )
            {
                if ( !par[i].equals(args[i]))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * hasEqualsObject
     * 
     * @param list
     * @return
     */
    public static boolean hasEqualsObject(List<?> list)
    {
        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Object object = (Object)iterator.next();

            for (Iterator iteratorInner = list.iterator(); iteratorInner.hasNext();)
            {
                Object objectInner = (Object)iteratorInner.next();

                if (objectInner != object)
                {
                    if (object.equals(objectInner))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
