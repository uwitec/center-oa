/**
 * 
 */
package com.china.center.tools;


import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 反射实现
 * 
 * @author ZHUZHU
 * @version 2007-7-30
 * @see InnerReflect
 * @since
 */

public abstract class InnerReflect
{
    public static Object get(Object oo, String name, Object[] args)
        throws ReflectException
    {
        if (oo == null)
        {
            throw new ReflectException("the invoke object is null");
        }

        if (oo instanceof Map)
        {
            Map tt = (Map)oo;
            return tt.get(name);
        }

        if ( ! (oo instanceof Serializable))
        {
            throw new ReflectException("the invoke object must implements serializable");
        }

        Method method = null;

        Class[] clasz = null;
        if (args != null)
        {
            clasz = new Class[args.length];
            for (int i = 0; i < args.length; i++ )
            {
                if (args[i] == null)
                {
                    throw new ReflectException("the parameter must is not null");
                }

                clasz[i] = args[i].getClass();
            }
        }

        try
        {
            method = oo.getClass().getMethod(getCommonMethodName(name), clasz);
        }
        catch (SecurityException e)
        {
            throw new ReflectException(e);
        }
        catch (NoSuchMethodException e)
        {
            try
            {
                method = oo.getClass().getMethod(getBooleanMethodName(name), clasz);
            }
            catch (SecurityException e1)
            {
                throw new ReflectException(e1);
            }
            catch (NoSuchMethodException e1)
            {
                throw new ReflectException(e1);
            }
        }

        try
        {
            return method.invoke(oo, args);
        }
        catch (IllegalArgumentException e)
        {
            throw new ReflectException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new ReflectException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new ReflectException(e);
        }
    }

    public static Object get(Object oo, int i)
        throws ReflectException
    {
        if (oo == null)
        {
            throw new ReflectException("the invoke object is null");
        }

        if (oo instanceof List)
        {
            List tt = (List)oo;

            if (i >= tt.size())
            {
                throw new ReflectException("index out of Bounds[index:" + i + ";size:" + tt.size()
                                           + ']');
            }
            return tt.get(i);
        }

        if (oo instanceof Collection)
        {
            Collection tt = (Collection)oo;

            oo = tt.toArray();
        }

        if (oo instanceof Set)
        {
            Set tt = (Set)oo;

            oo = tt.toArray();
        }

        if (oo.getClass().isArray())
        {
            Object[] tt = (Object[])oo;
            
            if (i >= tt.length)
            {
                throw new ReflectException("index out of Bounds[index:" + i + ";size:" + tt.length
                                           + ']');
            }
            return Array.get(oo, i);
        }

        throw new ReflectException("the invoke must be List or Array or Collection or Set");
    }

    private static String getCommonMethodName(String name)
    {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        return "get" + name;
    }

    private static String getBooleanMethodName(String name)
    {
        if (name.startsWith("is"))
        {
            if (name.length() > 2)
            {
                // a(大写字母)
                if (name.charAt(2) < 97)
                {
                    return name;
                }
            }
        }

        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        return "is" + name;
    }
}
