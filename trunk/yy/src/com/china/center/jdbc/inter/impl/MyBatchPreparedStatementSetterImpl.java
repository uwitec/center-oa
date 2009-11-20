/*
 * File Name: MyBatchPreparedStatementSetterImpl.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-3-29
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.impl;


import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.china.center.jdbc.inter.Convert;


/**
 * 〈一句话功能简述〉
 * 
 * @version 2007-3-7
 * @see MyBatchPreparedStatementSetterImpl
 * @since
 */

public class MyBatchPreparedStatementSetterImpl implements BatchPreparedStatementSetter
{
    private BatchPreparedStatementSetter bss = null;

    private Convert convert = null;

    /**
     * 默认构建器
     */
    public MyBatchPreparedStatementSetterImpl(BatchPreparedStatementSetter bss, Convert convert)
    {
        this.bss = bss;
        this.convert = convert;
    }

    public int getBatchSize()
    {
        return bss.getBatchSize();
    }

    public void setValues(PreparedStatement ps, int i)
        throws SQLException
    {
        bss.setValues(new MyPreparedStatement(ps, convert), i);
    }

}
