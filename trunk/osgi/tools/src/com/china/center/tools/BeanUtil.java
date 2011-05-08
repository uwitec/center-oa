/*
 * File Name: BeanUtil.java CopyRight: Copyright by www.center.china
 * Description: Creater: zhuAchen CreateTime: 2007-3-29 Grant: open source to
 * everybody
 */
package com.china.center.tools;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * BeanUtil
 * 
 * @author ZHUZHU
 * @version 2007-3-29
 * @see
 * @since
 */
public class BeanUtil extends BeanUtils
{
    private static final Log _logger = LogFactory.getLog(BeanUtil.class);

    private BeanUtil()
    {
    }

    /**
     * getBean
     * 
     * @param obj
     * @param request
     */
    public static void getBean(Object obj, HttpServletRequest request)
    {
        try
        {
            Map pMap = request.getParameterMap();

            Map newMap = new HashMap();

            Set<Map.Entry<Object, Object>> entrySet = pMap.entrySet();

            for (Map.Entry<Object, Object> entry : entrySet)
            {
                Object value = entry.getValue();

                if (value != null && value instanceof String)
                {
                    newMap.put(entry.getKey(), value.toString().trim());
                }
                else if (value != null && value instanceof String[])
                {
                    String[] ss = (String[])value;

                    for (int i = 0; i < ss.length; i++ )
                    {
                        if (ss[i] != null)
                        {
                            ss[i] = ss[i].trim();
                        }
                    }

                    newMap.put(entry.getKey(), ss);
                }
                else
                {
                    newMap.put(entry.getKey(), value);
                }
            }

            BeanUtils.populate(obj, newMap);
        }
        catch (IllegalAccessException e)
        {
            _logger.error(e, e);
        }
        catch (InvocationTargetException e)
        {
            _logger.error(e, e);
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
    }

    public static void getBean(Object obj, Map map)
    {
        try
        {
            BeanUtils.populate(obj, map);
        }
        catch (IllegalAccessException e)
        {
        }
        catch (InvocationTargetException e)
        {
        }
        catch (Exception e)
        {
        }
    }

    public static String getProperty(Object obj, String name)
    {
        try
        {
            return BeanUtils.getProperty(obj, name);
        }
        catch (IllegalAccessException e)
        {
            _logger.error(e, e);
            return "";
        }
        catch (InvocationTargetException e)
        {
            _logger.error(e, e);
            return "";
        }
        catch (NoSuchMethodException e)
        {
            _logger.error(e, e);
            return "";
        }
    }

    /**
     * Description: bean的属性拷贝
     * 
     * @param dest
     * @param orig
     */
    public static void copyProperties(Object dest, Object orig)
    {
        try
        {
            BeanUtils.copyProperties(dest, orig);
        }
        catch (IllegalAccessException e)
        {
        }
        catch (InvocationTargetException e)
        {
        }
    }

    public static void copyPropertyWithoutException(Object bean, String name, Object value)
    {
        try
        {
            BeanUtils.copyProperty(bean, name, value);
        }
        catch (IllegalAccessException e)
        {
        }
        catch (InvocationTargetException e)
        {
        }
    }

    public static void getBeanInner(Object obj, Map<?, ?> properties)
    {
        // Loop through the property name/value pairs to be set
        Iterator<?> names = properties.keySet().iterator();
        Method method = null;
        while (names.hasNext())
        {
            String name = (String)names.next();
            if (name == null)
            {
                continue;
            }

            Object value = properties.get(name);

            method = getMethod(obj, name, true);

            if (method == null)
            {
                continue;
            }

            try
            {
                Class[] clazs = method.getParameterTypes();

                if (clazs.length == 1)
                {
                    if (String.class.equals(clazs[0]))
                    {
                        method.invoke(obj, value.toString());
                    }
                    else if (Integer.class.equals(clazs[0]))
                    {
                        method.invoke(obj, new Integer(value.toString()));
                    }
                    else
                    {
                        method.invoke(obj, value);
                    }
                }
            }
            catch (IllegalArgumentException e)
            {
            }
            catch (IllegalAccessException e)
            {
            }
            catch (InvocationTargetException e)
            {
            }
            catch (Exception ee)
            {
            }

        }
    }

    public static <T> List<T> getListBean(List<Map> list, Class<T> claz)
    {
        List<T> result = new ArrayList();

        for (Map map : list)
        {
            try
            {
                T o = claz.newInstance();

                getBeanInner(o, map);

                result.add(o);
            }
            catch (InstantiationException e)
            {
                _logger.error(e, e);
            }
            catch (IllegalAccessException e)
            {
                _logger.error(e, e);
            }
        }

        return result;
    }

    private static Method getMethod(Object obj, String methodName, boolean ignore)
    {
        Method[] methods = obj.getClass().getMethods();

        // 先过滤set方法
        for (int i = 0; i < methods.length; i++ )
        {
            if (methods[i].getName().startsWith("set"))
            {
                if (ignore)
                {
                    if (methodName.toLowerCase().equals(
                        methods[i].getName().substring(3).toLowerCase()))
                    {
                        return methods[i];
                    }
                }
                else
                {
                    String tem = methods[i].getName().substring(3);

                    tem = String.valueOf(tem.charAt(0)).toUpperCase() + tem.substring(1);

                    if (methodName.equals(tem))
                    {
                        return methods[i];
                    }
                }
            }
        }

        return null;
    }

    /**
     * Description: 对象toString<br>
     * 
     * @param obj
     * @return String
     */
    public static String toStrings(Object obj)
    {
        if (obj == null)
        {
            return "null";
        }

        if (obj instanceof String || obj instanceof Boolean || obj instanceof Integer)
        {
            return obj.toString();
        }

        if (obj instanceof List || obj instanceof Map)
        {
            return ToStringBuilder.reflectionToString(obj) + '(' + obj.toString() + ')';
        }

        return ToStringBuilder.reflectionToString(obj);
    }

    /**
     * Description: 部打印list里面的东西,避免日志冗长<br>
     * 
     * @param obj
     * @param ignal
     * @return String
     */
    public static String toStrings(Object obj, boolean ignal)
    {
        if (obj == null)
        {
            return "null";
        }

        if (obj instanceof String || obj instanceof Boolean || obj instanceof Integer)
        {
            return obj.toString();
        }

        if (obj instanceof List || obj instanceof Map)
        {
            if (ignal)
            {
                return ToStringBuilder.reflectionToString(obj);
            }

            return ToStringBuilder.reflectionToString(obj) + '(' + obj.toString() + ')';
        }

        return ToStringBuilder.reflectionToString(obj);
    }

}
