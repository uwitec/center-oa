/*
 * File Name: ConvertEncode.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-29
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.impl;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.china.center.jdbc.inter.Convert;

/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-9-30
 * @see 
 * @since
 */
public class ConvertEncode implements Convert
{
    private final Logger logger = Logger.getLogger(getClass());

    private String databaseEncoding = "";

    private String systemEncoding = "";

    /**
     * 
     * 
     */
    public ConvertEncode()
    {

    }

    /**
     * 
     * 
     */
    public ConvertEncode(String databaseEncoding, String systemEncoding)
    {
        this.databaseEncoding = databaseEncoding;
        this.systemEncoding = systemEncoding;
    }

    /**
     * 
     * @param originStr
     * @return
     */
    public String decode(String originStr)
    {
        if (originStr == null)
        {
            return null;
        }

        String s = originStr;

        if (databaseEncoding.toUpperCase().equals(systemEncoding.toUpperCase()))
        {
            return s;
        }

        try
        {
            s = new String(originStr.getBytes(databaseEncoding), systemEncoding);
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error(e, e);
        }

        return s;
    }

    /**
     * 
     * @param originStr
     * @return
     */
    public String encode(String originStr)
    {
        if (originStr == null)
        {
            return null;
        }

        String s = originStr;

        if (databaseEncoding.toUpperCase().equals(systemEncoding.toUpperCase()))
        {
            return s;
        }

        try
        {
            s = new String(originStr.getBytes(systemEncoding), databaseEncoding);
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("encode exception:", e);
        }

        return s;
    }

    /**
     * 
     * @param List
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List decodeMapInList(List list)
    {
        List templist = new ArrayList();

        if (list == null || list.size() == 0)
        {
            return list;
        }

        if (databaseEncoding.toUpperCase().equals(systemEncoding.toUpperCase()))
        {
            return list;
        }

        Map m = null;

        for (Iterator it = list.iterator(); it.hasNext();)
        {
            m = (Map)it.next();
            Map mm = decodeMap(m);
            templist.add(mm);
        }

        return templist;
    }

    /**
     * 
     * @param Map
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public Map decodeMap(Map map)
    {

        Map tempmap = new HashMap();

        if (map == null || map.size() == 0)
        {
            return map;
        }

        if (databaseEncoding.toUpperCase().equals(systemEncoding.toUpperCase()))
        {
            return map;
        }

        for (Iterator it = map.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry e = (Map.Entry)it.next();
            Object valueTemp = null;
            Object keyTemp = null;
            if (e.getValue() != null)
            {
                if (e.getValue() instanceof String)
                {
                    valueTemp = decode(e.getValue().toString());
                }
                else
                {
                    valueTemp = e.getValue();
                }
            }

            if (e.getKey() != null)
            {
                if (e.getKey() instanceof String)
                {
                    keyTemp = decode(e.getKey().toString());
                }
                else
                {
                    keyTemp = e.getKey();
                }
            }

            tempmap.put(keyTemp, valueTemp);
        }

        return tempmap;
    }

    /**
     */
    public Object decodeObject(Object obj)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if (obj == null)
        {
            return null;
        }
        if (obj instanceof String)
        {
            return decode((String)obj);
        }
        else if (obj instanceof List)
        {
            return decodeList((List)obj);
        }
        else if (obj instanceof Map)
        {
            return decodeMap((Map)obj);
        }
        return dealObjectCoding(obj, 2);
    }

    public Object encodeObject(Object obj)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return dealObjectCoding(obj, 1);
    }

    public List decodeList(List srcList)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if (srcList == null || srcList.size() == 0)
        {
            return srcList;
        }
        List list = srcList;
        ListIterator listIterator = list.listIterator();
        Object obj = null;
        while (listIterator.hasNext())
        {
            obj = listIterator.next();
            dealObjectCoding(obj, 2);
        }
        return list;
    }

    // 对一个对象的所有String类型的属性进行编解码 type:1:编码;2:解码
    private Object dealObjectCoding(Object srcObj, int type)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if (srcObj == null)
        {
            return null;
        }
        Object obj = srcObj;
        Field[] fs = obj.getClass().getDeclaredFields();
        Method[] methods = obj.getClass().getDeclaredMethods();
        String propertyName = null;
        Object[] params = new Object[1];
        String value = null;
        for (int i = 0; i < fs.length; i++ )
        {
            if (fs[i].getType().equals(String.class))
            {
                propertyName = fs[i].getName();

                // 取值
                for (int j1 = 0; j1 < methods.length; j1++ )
                {
                    if (methods[j1].getName().equalsIgnoreCase("get" + propertyName))
                    {
                        value = StringUtils.defaultIfEmpty((String)methods[j1].invoke(obj), "");
                        if (type == 1)
                        {
                            value = encode(value);
                        }
                        else
                        {
                            value = decode(value);
                        }
                        params[0] = value;
                        break; // 匹配到就退出该层循环，提高效率
                    }
                }

                // 设值
                for (int j2 = 0; j2 < methods.length; j2++ )
                {
                    if (methods[j2].getName().equalsIgnoreCase("set" + propertyName))
                    {
                        methods[j2].invoke(obj, params);
                        break; // 匹配到就退出该层循环，提高效率
                    }
                }
            }
        }
        return obj;
    }

    /**
     * @return Returns the destEncoding.
     */
    public String getSystemEncoding()
    {
        return systemEncoding;
    }

    /**
     * @param destEncoding
     *            The destEncoding to set.
     */
    public void setSystemEncoding(String destEncoding)
    {
        this.systemEncoding = destEncoding;
    }

    /**
     * @return Returns the originEncoding.
     */
    public String getDatabaseEncoding()
    {
        return databaseEncoding;
    }

    /**
     * @param originEncoding
     *            The originEncoding to set.
     */
    public void setDatabaseEncoding(String originEncoding)
    {
        this.databaseEncoding = originEncoding;
    }
}
