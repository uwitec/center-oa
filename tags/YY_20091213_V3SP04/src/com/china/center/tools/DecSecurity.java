/**
 * 文件名：DecSecurity.java
 * 版权：Copyright by www.centerchina.com
 * 描述：
 * 修改人：zhuzhu
 * 修改时间：2006-6-17
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.china.center.tools;


import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * 用des加密用户的id，获得操作码
 * 
 * @author zhuzhu
 * @version 2006-6-17
 * @see DecSecurity
 * @since
 */

public class DecSecurity
{
    private static String algorithm = "DES"; // 定义 加密算法,可用 DES,DESede,Blowfish

    private static String key = "A68-q%k)"; // 密钥字符串,必须是8字节,如果是DESede必须是24字节

    private static final char[] hexDigits = {'m', 'k', 'p', '3', '4', '5', 'a', '7', '8', 'f',
        '6', 'b', 'c', 'd', 'x', '9'};

    // 单实例的模式
    public static final DecSecurity INSTANCE = new DecSecurity();

    // 构建器
    private DecSecurity()
    {}

    private static boolean needSecurity = true;

    /**
     * 用DES加密 String 要加密的字符串
     */
    public static String encrypt(int num)
    {
        return encrypt(String.valueOf(num));
    }

    public static String encrypt(String enStr)
    {
        return encrypt(enStr, DecSecurity.key);
    }

    /**
     * 用DES加密 String 要加密的字符串
     */
    public static String encrypt(String enStr, String keygen)
    {
        if (StringTools.isNullOrNone(enStr))
        {
            return "";
        }

        if ( !needSecurity)
        {
            return enStr;
        }

        // 添加新安全算法,如果用JCE就要把它添加进去
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        String myinfo = "要加密的信息";
        byte[] cipherByte = null; // 加密后的密文
        if (enStr != null)
        {
            myinfo = enStr;
        }
        try
        {
            SecretKey deskey = new SecretKeySpec(keygen.getBytes(), algorithm);
            // 加密
            Cipher c1 = Cipher.getInstance(algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            cipherByte = c1.doFinal(myinfo.getBytes());

        }
        catch (java.security.NoSuchAlgorithmException e1)
        {
            e1.printStackTrace();
        }
        catch (javax.crypto.NoSuchPaddingException e2)
        {
            e2.printStackTrace();
        }
        catch (java.lang.Exception e3)
        {
            e3.printStackTrace();
        }

        return getString(cipherByte);
    }

    public static String decrypt(String str)
    {
        return decrypt(str, DecSecurity.key);
    }

    /**
     * 用DES解密 String 要解密的密文 SecretKey 密钥
     */
    public static String decrypt(String str, String keygen)
    {
        if (StringTools.isNullOrNone(str))
        {
            return "";
        }

        if ( !needSecurity)
        {
            return str;
        }

        String result = "";
        try
        {
            SecretKey deskey = new SecretKeySpec(keygen.getBytes(), algorithm);

            byte[] deStr = getbytes(str);
            // 添加新安全算法,如果用JCE就要把它添加进去
            Security.addProvider(new com.sun.crypto.provider.SunJCE());

            // 解密
            Cipher c1 = Cipher.getInstance(algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] clearByte = c1.doFinal(deStr);
            result = new String(clearByte);
        }
        catch (Exception e)
        {}

        return result;
    }

    /**
     * Description:通过散列char加密bytes <br>
     * 
     * @param b
     * @return String
     */
    private static String getString(byte[] b)
    {
        char[] str = new char[b.length << 1];
        int k = 0;
        for (int i = 0; i < b.length; i++ )
        {
            byte bb = b[i];
            str[k++ ] = hexDigits[bb >>> 4 & 0xf];
            str[k++ ] = hexDigits[bb & 0xf];
        }
        return new String(str);
    }

    /**
     * Description:通过加密的String获得bytes <br>
     * 
     * @param s
     * @return byte[]
     */
    private static byte[] getbytes(String s)
    {
        byte[] b = new byte[s.length() >> 1];
        int j = 0;
        for (int i = 0; i < s.length(); i = i + 2)
        {
            b[j++ ] = (byte) ( (getPos(s.charAt(i)) << 4) | (getPos(s.charAt(i + 1))));
        }
        return b;
    }

    private static int getPos(char c)
    {
        for (int i = 0; i < hexDigits.length; i++ )
        {
            if (hexDigits[i] == c)
            {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args)
        throws Exception
    {
        String ss = DecSecurity.encrypt("123456789012345678901234567890AP", "12345678");
        System.out.println(ss);
        System.out.println(DecSecurity.decrypt(ss, "12345678"));
    }
}