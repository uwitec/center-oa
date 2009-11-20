/*
 * 文件名：ISPPreparedStatementSetter.java
 * 版权：Copyright 2002-2007 centerchina Tech. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：zhuzhu
 * 修改时间：2007-3-7
 * 跟踪单号：
 * 修改单号：
 * 修改内容：新增
 */
package com.china.center.jdbc.inter.impl;


import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementSetter;

import com.china.center.jdbc.inter.Convert;


/**
 * MyPreparedStatementSetterImpl的借用实现
 * 
 * @author zhuzhu
 * @version 2007-3-7
 * @see MyPreparedStatementSetterImpl
 * @since
 */

public class MyPreparedStatementSetterImpl implements PreparedStatementSetter
{
    private PreparedStatementSetter pss = null;

    private Convert convert = null;

    /**
     * 默认构建器
     */
    public MyPreparedStatementSetterImpl(PreparedStatementSetter pss, Convert convert)
    {
        this.pss = pss;
        this.convert = convert;
    }

    public void setValues(PreparedStatement ps)
        throws SQLException
    {
        pss.setValues(new MyPreparedStatement(ps, convert));
    }

}
