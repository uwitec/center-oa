/*
 * File Name: RandomTools.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.util.Random;


/**
 * Ëæ»ú¹¤¾ß
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public abstract class RandomTools
{
    private static Random random = new Random(System.currentTimeMillis());

    private static int inc = 0;

    private static char[] ranChar = new char[] {'8', '~', '0', '1', '!', '9', '@', '2', '#', '$',
        '4', '%', '5', '^', '6', '7', '&', '3', '*', '/', '?'};

    private static String getString(int n)
    {
        String s = "";
        char c = 'A';
        for (int i = 0; i < n; i++ )
        {
            // A 65
            if (random.nextInt(10) % 2 == 0)
            {
                c = (char) (65 + random.nextInt(25));
            }
            else
            {
                c = (char) (97 + random.nextInt(25));
            }

            s = s + String.valueOf(c);
        }

        return s;
    }

    public static String getRandomString(int length)
    {
        if (length <= 6)
        {
            return getString(length);
        }

        String result = "";

        int rl = ranChar.length;

        for (int i = 0; i < length; i++ )
        {
            Random random = new Random(System.currentTimeMillis() + i * i);

            int indx = random.nextInt(rl * 2);

            if (indx >= rl)
            {
                result += getString(1);
            }
            else
            {
                result += ranChar[indx];
            }
        }

        return result;
    }

    public static String getRandomMumber(int length)
    {
        String result = "";

        for (int i = 0; i < length; i++ )
        {
            Random random = new Random(System.currentTimeMillis() * (inc++ ) + i * i);

            int indx = (random.nextInt(1000) + random.nextInt(2000)) % 10;

            result += indx;
        }

        return result;
    }

    public static void main(String[] arg)
    {
        System.out.println(getRandomString(100));
    }
}
