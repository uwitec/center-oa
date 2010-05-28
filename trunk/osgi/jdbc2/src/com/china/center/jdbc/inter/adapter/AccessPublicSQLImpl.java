/*
 * File Name: AccessImpl.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.jdbc.inter.PublicSQL;


/**
 * <描述>
 * 
 * @author ZHUZHU
 * @version 2007-12-15
 * @see
 * @since
 */
public class AccessPublicSQLImpl implements PublicSQL
{
    /**
     * default constructor
     */
    public AccessPublicSQLImpl()
    {}

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.annosql.PublicSQL#to_date(java.lang.String, java.lang.String)
     */
    public String to_date(String sdate, String format)
    {
        return "CDATE('" + sdate + "')";
    }
}
