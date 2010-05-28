/*
 * File Name: MyDataSourceImpl.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-29
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.impl;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.china.center.jdbc.inter.Convert;
import com.china.center.jdbc.inter.MyDataSource;



/**
 * 〈一句话功能简述〉
 *
 * @author ZHUZHU
 * @version 2007-3-5
 * @see MyDataSourceImpl
 * @since
 */

public class MyDataSourceImpl implements MyDataSource
{
    private Convert convertEncode = null;

    private DataSource dataSource = null;

    /**
     * 默认构建器
     */
    public MyDataSourceImpl()
    {
        super();
    }

    public Connection getConnection()
        throws SQLException
    {
        return dataSource.getConnection();
    }

    public Connection getConnection(String username, String password)
        throws SQLException
    {
        return dataSource.getConnection(username, password);
    }

    public PrintWriter getLogWriter()
        throws SQLException
    {
        return dataSource.getLogWriter();
    }

    public int getLoginTimeout()
        throws SQLException
    {
        return dataSource.getLoginTimeout();
    }

    public void setLogWriter(PrintWriter out)
        throws SQLException
    {
        dataSource.setLogWriter(out);
    }

    public void setLoginTimeout(int seconds)
        throws SQLException
    {
        dataSource.setLoginTimeout(seconds);
    }

    /**
     * Description: 取得转码的类<br>
     */
    public Convert getConvertEncode()
    {
        return convertEncode;
    }

    /**
     * @param convertEncode The convertEncode to set.
     */
    public void setConvertEncode(Convert convertEncode)
    {
        this.convertEncode = convertEncode;
    }

    /**
     * @return Returns the dataSource.
     */
    public DataSource getDataSource()
    {
        return dataSource;
    }

    /**
     * @param dataSource The dataSource to set.
     */
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public boolean isWrapperFor(Class<?> arg0)
        throws SQLException
    {
        return false;
    }

    public <T> T unwrap(Class<T> arg0)
        throws SQLException
    {
        return null;
    }

  

}
