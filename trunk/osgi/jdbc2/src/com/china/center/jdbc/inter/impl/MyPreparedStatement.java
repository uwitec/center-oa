package com.china.center.jdbc.inter.impl;


import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.china.center.jdbc.inter.Convert;



/**
 * ISPPreparedStatement的借用实现
 *
 * @author ZHUZHU
 * @version 2007-3-7
 * @see MyPreparedStatement
 * @since
 */

public class MyPreparedStatement implements PreparedStatement
{
    private PreparedStatement ps = null;

    private Convert convert = null;

    /**
     * 默认构建器
     */
    public MyPreparedStatement(PreparedStatement ps, Convert convert)
    {
        this.ps = ps;
        this.convert = convert;
    }

    public void addBatch()
        throws SQLException
    {
        ps.addBatch();
    }

    public void clearParameters()
        throws SQLException
    {
        ps.clearParameters();

    }

    public boolean execute()
        throws SQLException
    {
        return ps.execute();
    }

    public ResultSet executeQuery()
        throws SQLException
    {
        return ps.executeQuery();
    }

    public int executeUpdate()
        throws SQLException
    {
        return ps.executeUpdate();
    }

    public ResultSetMetaData getMetaData()
        throws SQLException
    {
        return ps.getMetaData();
    }

    public ParameterMetaData getParameterMetaData()
        throws SQLException
    {

        return ps.getParameterMetaData();
    }

    public void setArray(int i, Array x)
        throws SQLException
    {
        ps.setArray(i, x);
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length)
        throws SQLException
    {
        ps.setAsciiStream(parameterIndex, x, length);
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x)
        throws SQLException
    {
        ps.setBigDecimal(parameterIndex, x);
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length)
        throws SQLException
    {
        ps.setBinaryStream(parameterIndex, x, length);
    }

    public void setBlob(int i, Blob x)
        throws SQLException
    {
        ps.setBlob(i, x);
    }

    public void setBoolean(int parameterIndex, boolean x)
        throws SQLException
    {
        ps.setBoolean(parameterIndex, x);
    }

    public void setByte(int parameterIndex, byte x)
        throws SQLException
    {
        ps.setByte(parameterIndex, x);
    }

    public void setBytes(int parameterIndex, byte[] x)
        throws SQLException
    {
        ps.setBytes(parameterIndex, x);
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length)
        throws SQLException
    {
        ps.setCharacterStream(parameterIndex, reader, length);
    }

    public void setClob(int i, Clob x)
        throws SQLException
    {
        ps.setClob(i, x);

    }

    public void setDate(int parameterIndex, Date x)
        throws SQLException
    {
        ps.setDate(parameterIndex, x);
    }

    public void setDate(int parameterIndex, Date x, Calendar cal)
        throws SQLException
    {
        ps.setDate(parameterIndex, x, cal);
    }

    public void setDouble(int parameterIndex, double x)
        throws SQLException
    {
        ps.setDouble(parameterIndex, x);
    }

    public void setFloat(int parameterIndex, float x)
        throws SQLException
    {
        ps.setFloat(parameterIndex, x);
    }

    public void setInt(int parameterIndex, int x)
        throws SQLException
    {
        ps.setInt(parameterIndex, x);
    }

    public void setLong(int parameterIndex, long x)
        throws SQLException
    {
        ps.setLong(parameterIndex, x);
    }

    public void setNull(int parameterIndex, int sqlType)
        throws SQLException
    {
        ps.setNull(parameterIndex, sqlType);
    }

    public void setNull(int paramIndex, int sqlType, String typeName)
        throws SQLException
    {
        ps.setNull(paramIndex, sqlType, typeName);
    }

    public void setObject(int parameterIndex, Object x)
        throws SQLException
    {
        ps.setObject(parameterIndex, x);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType)
        throws SQLException
    {
        ps.setObject(parameterIndex, x, targetSqlType);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
        throws SQLException
    {
        ps.setObject(parameterIndex, x, targetSqlType, scale);
    }

    public void setRef(int i, Ref x)
        throws SQLException
    {
        ps.setRef(i, x);
    }

    public void setShort(int parameterIndex, short x)
        throws SQLException
    {
        ps.setShort(parameterIndex, x);

    }

    public void setString(int parameterIndex, String x)
        throws SQLException
    {
        ps.setString(parameterIndex, convert.encode(x));
    }

    public void setTime(int parameterIndex, Time x)
        throws SQLException
    {
        ps.setTime(parameterIndex, x);
    }

    public void setTime(int parameterIndex, Time x, Calendar cal)
        throws SQLException
    {
        ps.setTime(parameterIndex, x);
    }

    public void setTimestamp(int parameterIndex, Timestamp x)
        throws SQLException
    {
        ps.setTimestamp(parameterIndex, x);
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
        throws SQLException
    {
        ps.setTimestamp(parameterIndex, x, cal);
    }

    public void setURL(int parameterIndex, URL x)
        throws SQLException
    {
        ps.setURL(parameterIndex, x);
    }

    /**
     * @deprecated
     */
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
        throws SQLException
    {
        ps.setUnicodeStream(parameterIndex, x, length);
    }

    public void addBatch(String sql)
        throws SQLException
    {
        ps.addBatch(sql);
    }

    public void cancel()
        throws SQLException
    {
        ps.cancel();
    }

    public void clearBatch()
        throws SQLException
    {
        ps.clearBatch();
    }

    public void clearWarnings()
        throws SQLException
    {
        ps.clearWarnings();

    }

    public void close()
        throws SQLException
    {
        ps.close();
    }

    public boolean execute(String sql)
        throws SQLException
    {
        return ps.execute(sql);
    }

    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException
    {
        return ps.execute(sql, autoGeneratedKeys);
    }

    public boolean execute(String sql, int[] columnIndexes)
        throws SQLException
    {
        return ps.execute(sql, columnIndexes);
    }

    public boolean execute(String sql, String[] columnNames)
        throws SQLException
    {
        return ps.execute(sql, columnNames);
    }

    public int[] executeBatch()
        throws SQLException
    {

        return ps.executeBatch();
    }

    public ResultSet executeQuery(String sql)
        throws SQLException
    {
        return ps.executeQuery(sql);
    }

    public int executeUpdate(String sql)
        throws SQLException
    {

        return ps.executeUpdate(sql);
    }

    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException
    {
        return ps.executeUpdate(sql, autoGeneratedKeys);
    }

    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException
    {
        return ps.executeUpdate(sql, columnIndexes);
    }

    public int executeUpdate(String sql, String[] columnNames)
        throws SQLException
    {

        return ps.executeUpdate(sql, columnNames);
    }

    public Connection getConnection()
        throws SQLException
    {

        return ps.getConnection();
    }

    public int getFetchDirection()
        throws SQLException
    {
        return ps.getFetchDirection();
    }

    public int getFetchSize()
        throws SQLException
    {
        return ps.getFetchSize();
    }

    public ResultSet getGeneratedKeys()
        throws SQLException
    {
        return ps.getGeneratedKeys();
    }

    public int getMaxFieldSize()
        throws SQLException
    {
        return ps.getMaxFieldSize();
    }

    public int getMaxRows()
        throws SQLException
    {
        return ps.getMaxRows();
    }

    public boolean getMoreResults()
        throws SQLException
    {
        return ps.getMoreResults();
    }

    public boolean getMoreResults(int current)
        throws SQLException
    {
        return ps.getMoreResults(current);
    }

    public int getQueryTimeout()
        throws SQLException
    {
        return ps.getQueryTimeout();
    }

    public ResultSet getResultSet()
        throws SQLException
    {
        return ps.getResultSet();
    }

    public int getResultSetConcurrency()
        throws SQLException
    {
        return ps.getResultSetConcurrency();
    }

    public int getResultSetHoldability()
        throws SQLException
    {
        return ps.getResultSetHoldability();
    }

    public int getResultSetType()
        throws SQLException
    {
        return ps.getResultSetType();
    }

    public int getUpdateCount()
        throws SQLException
    {
        return ps.getUpdateCount();
    }

    public SQLWarning getWarnings()
        throws SQLException
    {
        return ps.getWarnings();
    }

    public void setCursorName(String name)
        throws SQLException
    {
        ps.setCursorName(name);
    }

    public void setEscapeProcessing(boolean enable)
        throws SQLException
    {
        ps.setEscapeProcessing(enable);
    }

    public void setFetchDirection(int direction)
        throws SQLException
    {
        ps.setFetchDirection(direction);
    }

    public void setFetchSize(int rows)
        throws SQLException
    {
        ps.setFetchSize(rows);
    }

    public void setMaxFieldSize(int max)
        throws SQLException
    {
        ps.setMaxFieldSize(max);
    }

    public void setMaxRows(int max)
        throws SQLException
    {
        ps.setMaxRows(max);
    }

    public void setQueryTimeout(int seconds)
        throws SQLException
    {
        ps.setQueryTimeout(seconds);
    }
}
