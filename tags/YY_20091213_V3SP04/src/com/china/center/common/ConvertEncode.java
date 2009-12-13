/*
 * Created on 2005-5-27
 * 文件名：ConvertEncode.java
 * 版权：Copyright 2003-2004 centerchina Tech. Co. Ltd. All Rights Reserved.
 * 描述：
 */
package com.china.center.common;


import java.io.UnsupportedEncodingException;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: centerchina
 * </p>
 * 
 * @author c36091
 * @version 1.0
 */
public class ConvertEncode
{
    private String databaseEncoding = "ISO8859-1";

    private String systemEncoding = "GBK";

    /**
     * 
     *
     */
    public ConvertEncode()
    {

    }

    /**
     * 在从数据库取出字符串的时候需要按数据库的编码方式进行decode以便正确显示
     * 
     * @param originStr
     * @return
     */
    public String decode(String originStr)
    {
        String s = originStr;
        if (originStr != null)
        {
            try
            {
                s = new String(originStr.getBytes(databaseEncoding), systemEncoding);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            s = "";
        }

        return s;
    }

    /**
     * 在插入数据库的时候需要将字符串按数据库编码方式进行encode
     * 
     * @param originStr
     * @return
     */
    public String encode(String originStr)
    {
        String s = originStr;
        if (originStr != null)
        {
            try
            {
                s = new String(originStr.getBytes(systemEncoding), databaseEncoding);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            s = "";
        }

        return s;
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