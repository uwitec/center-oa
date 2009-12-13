/**
 * File Name: Test.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-12<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.test;


import java.net.URISyntaxException;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2008-8-12
 * @see
 * @since
 */
public class Test
{
    public static String getBASE64(String s)
    {
        if (s == null) return null;
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
    }

    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String[] args)
        throws URISyntaxException
    {
        String path = "file:/E:/workspace/yongyin v3.1/yongyin";

        System.out.println(getBASE64(path));

    }

}
