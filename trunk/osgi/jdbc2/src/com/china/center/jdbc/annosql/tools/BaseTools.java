/**
 * File Name: BaseTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-7-11<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annosql.tools;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.china.center.jdbc.clone.DataClone;


/**
 * BaseTools
 * 
 * @author ZHUZHU
 * @version 2009-7-11
 * @see BaseTools
 * @since 1.0
 */
public abstract class BaseTools
{
    public static boolean isNullOrNone(String name)
    {
        if (name == null || "".equals(name.trim()))
        {
            return true;
        }

        return false;
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
     * 深度拷贝Serializable对象(函数不能保证正确copy一切Serializable对象)<br>
     * 但是对于数据对象则完全可以保证 对于集合类型是返回ArrayList
     * 
     * @param oldValue
     * @return
     */
    public static Object deepCopy(Object oldValue)
    {
        // 特殊处理DataClone
        if (oldValue instanceof DataClone)
        {
            return ((DataClone)oldValue).clones();
        }

        // 特殊处理DataClone
        if (oldValue instanceof Collection)
        {
            Collection vv = (Collection)oldValue;

            List newList = new ArrayList(vv.size());

            boolean isSupportClone = true;

            for (Object each : vv)
            {
                if ( ! (each instanceof DataClone))
                {
                    isSupportClone = false;

                    break;
                }

                newList.add( ((DataClone)each).clones());
            }

            if (isSupportClone)
            {
                return newList;
            }
        }

        Object newValue = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try
        {
            oos = new ObjectOutputStream(bout);
            oos.writeObject(oldValue);
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ois = new ObjectInputStream(bin);
            newValue = ois.readObject();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (oos != null)
                {
                    oos.close();
                }
                if (ois != null)
                {
                    ois.close();
                }
            }
            catch (Exception e)
            {

            }
        }
        return newValue;
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

    public static boolean isEmptyOrNull(List list)
    {
        if (list == null || list.size() == 0)
        {
            return true;
        }

        return false;
    }
}
