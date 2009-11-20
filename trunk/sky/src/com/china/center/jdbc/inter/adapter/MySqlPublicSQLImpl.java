/*
 * File Name: MySqlImp.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.jdbc.inter.PublicSQL;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class MySqlPublicSQLImpl implements PublicSQL
{
    /**
     * default constructor
     */
    public MySqlPublicSQLImpl()
    {}

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.PublicSQL#to_date(java.lang.String, java.lang.String)
     */
    public String to_date(String sdate, String format)
    {
        return "DATE_FORMAT('" + sdate + "', '" + parserFormat(format) + "')";
    }

    private String parserFormat(String format)
    {
        // '%Y-%m-%d %H:%i:%s'
        if (format.equals("yyyy-MM-dd HH:mm:ss"))
        {
            return "%Y-%m-%d %H:%i:%s";
        }

        if (format.equals("yyyy-MM-dd"))
        {
            return "%Y-%m-%d";
        }

        if (format.equals("yy-MM-dd"))
        {
            return "%y-%m-%d";
        }

        if (format.equals("yy-MM-dd HH:mm:ss"))
        {
            return "%y-%m-%d %H:%i:%s";
        }

        return "%Y-%m-%d %H:%i:%s";
    }

}
