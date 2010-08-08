/**
 * File Name: ObjectTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-18<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.tools;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * ObjectTools
 * 
 * @author ZHUZHU
 * @version 2010-7-18
 * @see ObjectTools
 * @since 1.0
 */
public abstract class ObjectTools
{
    /**
     * 深度拷贝Serializable对象(函数不能保证正确copy一切Serializable对象)<br>
     * 但是对于数据对象则完全可以保证
     * 
     * @param oldValue
     * @return
     */
    public static Object deepCopy(Object oldValue)
    {
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

    public static Object[] deepCopy(Object oldValue, int count)
    {
        Object org = deepCopy(oldValue);

        Object[] each = null;

        if (count == 1)
        {
            return new Object[] {org};
        }

        List<Integer> nList = twon(count);

        Object[] result = new Object[count];

        int max = nList.get(0);

        Object[] temp = null;

        int index = 0;

        for (int i = 0; i <= max; i++ )
        {
            Object[] tempo = new Object[(int)Math.pow(2, i)];

            if (i == 0)
            {
                tempo[0] = org;

                each = tempo;
            }
            else
            {
                temp = (Object[])deepCopy(each);

                System.arraycopy(each, 0, tempo, 0, each.length);

                System.arraycopy(temp, 0, tempo, each.length, temp.length);

                each = tempo;
            }

            if (nList.contains(i))
            {
                System.arraycopy(each, 0, result, index, each.length);

                index += each.length;
            }

            temp = null;

            tempo = null;
        }

        return result;
    }

    private static int twonInner(int num)
    {
        int tem = num;

        int count = 0;

        while (tem >= 2)
        {
            count++ ;

            tem = tem >> 1;
        }

        return count;
    }

    private static void twon(int num, List<Integer> nList)
    {
        if (num <= 0)
        {
            return;
        }

        int n = twonInner(num);

        nList.add(n);

        int dex = num - (int)Math.pow(2, n);

        twon(dex, nList);
    }

    private static List<Integer> twon(int num)
    {
        List<Integer> nList = new ArrayList<Integer>();

        twon(num, nList);

        return nList;
    }
}
