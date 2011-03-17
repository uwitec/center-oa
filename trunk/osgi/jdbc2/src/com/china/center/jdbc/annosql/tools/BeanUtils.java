/*
 * File Name: BeanUtils.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-29
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annosql.tools;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.jdbc.annosql.InnerBean;


/**
 * BeanUtils
 * 
 * @author ZHUZHU
 * @version 2007-9-29
 * @see
 * @since
 */
public abstract class BeanUtils extends org.apache.commons.beanutils.BeanUtils
{
    protected static final Log _logger = LogFactory.getLog(BeanUtils.class);

    /**
     * getPropertyValue
     * 
     * @param bean
     * @param name
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Object getPropertyValue(Object bean, String name)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        return BeanUtilsBean.getInstance().getPropertyUtils().getNestedProperty(bean, name);
    }

    /**
     * 属性拷贝(不区分大小写)
     * 
     * @param obj
     * @param map
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void populateIgnorCase(Object obj, Map map)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        // 把map里面的数据
        Map<String, Object> news = new HashMap<String, Object>(map.size());

        List<InnerBean> columns = BeanTools.getAllClassFieldsInner(obj.getClass());

        for (InnerBean field : columns)
        {
            Object temp = map.get(field.getColumnName().toUpperCase());

            if (obj != null)
            {
                if (temp != null && temp instanceof java.sql.Date)
                {
                    temp = new java.util.Date( ((java.sql.Date)temp).getTime());
                }

                news.put(field.getFieldName(), temp);
            }
        }

        try
        {
            org.apache.commons.beanutils.BeanUtils.populate(obj, news);
        }
        catch (IllegalArgumentException e)
        {
            _logger.error(e, e);
        }
    }

    /**
     * 属性拷贝(不区分大小写)
     * 
     * @param obj
     * @param map
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void populateSqlFieldIgnorCase(Object obj, Map map)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        // 把map里面的数据
        Map<String, Object> news = new HashMap<String, Object>(map.size());

        List<InnerBean> columns = BeanTools.getAllClassSqlFieldsInner(obj.getClass());

        for (InnerBean field : columns)
        {
            Object temp = map.get(field.getColumnName().toUpperCase());

            if (temp != null)
            {
                if (temp instanceof java.sql.Date)
                {
                    temp = new java.util.Date( ((java.sql.Date)temp).getTime());
                }

                news.put(field.getFieldName(), temp);
            }
        }

        org.apache.commons.beanutils.BeanUtils.populate(obj, news);
    }

    /**
     * 根据field反射
     * 
     * @param <T>
     * @param mapList
     * @param cla
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> populateListIgnoreByField(List<Map> mapList, Class<T> claz)
        throws IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        List<InnerBean> columns = BeanTools.getAllClassFieldsInner(claz);

        List<T> result = new LinkedList<T>();

        for (Map<?, ?> map : mapList)
        {
            T obj = reflectByFieldAndSqlColumn(claz, columns, map);

            result.add(obj);
        }

        return result;
    }

    /**
     * 依靠field进行反射,同时主键使用@Column
     * 
     * @param <T>
     * @param claz
     * @param columns
     * @param map
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static <T> T reflectByFieldAndSqlColumn(Class<T> claz, List<InnerBean> columns,
                                                    Map<?, ?> map)
        throws InstantiationException, IllegalAccessException
    {
        Map<String, Object> news = new HashMap<String, Object>();

        for (Map.Entry<?, ?> entry : map.entrySet())
        {
            news.put(entry.getKey().toString().toUpperCase(), entry.getValue());
        }

        T obj = claz.newInstance();

        for (InnerBean innerBean : columns)
        {
            Object oo = news.get(innerBean.getColumnName().toUpperCase());

            if (oo == null)
            {
                continue;
            }

            Field field = innerBean.getField();

            field.setAccessible(true);

            field.set(obj, BaseType.getValue(oo, field.getType()));
        }

        return obj;
    }

}