/*
 * 文件名：ISPDataSource.java
 * 版权：Copyright 2002-2007 centerchina Tech. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：zhuzhu
 * 修改时间：2007-3-5
 * 跟踪单号：
 * 修改单号：
 * 修改内容：新增
 */
package com.china.center.jdbc.inter;


import javax.sql.DataSource;


/**
 * 封装的数据库源接口
 * 
 * @author zhuzhu
 * @version 2007-3-5
 * @see MyDataSource
 * @since
 */

public interface MyDataSource extends DataSource
{
    Convert getConvertEncode();
}
