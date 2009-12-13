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
