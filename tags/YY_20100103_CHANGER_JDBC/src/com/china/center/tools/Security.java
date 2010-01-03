/*
 * 文件名：Security
 * 版权：Copyright by www.centerchina.com
 * 描述：
 * 修改人：public1247
 * 修改时间：2006-4-8
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.china.center.tools;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 加密类
 * 
 * @author public1247
 * @version
 * @see Securitys
 * @since
 */
public final class Security
{
    /**
     * Description: 得到一个字符串加密后的结果<br>
     * [参数列表，说明每个参数用途]
     * 
     * @param s
     * @return String
     */
    public static final String getSecurity(String s)
    {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f'};
        try
        {
            final byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            final byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j << 1];
            int k = 0;
            for (int i = 0; i < j; i++ )
            {
                byte byte0 = md[i];
                str[k++ ] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++ ] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }
    }

    /**
     * Automatically generated method: Security
     */
    private Security()
    {

    }

    public static void main(String[] args)
    {
        System.out.println(getSecurity("123456"));
    }

}
