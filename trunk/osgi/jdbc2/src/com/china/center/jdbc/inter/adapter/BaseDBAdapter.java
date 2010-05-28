/**
 *
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.jdbc.annosql.AutoCreateSql;
import com.china.center.jdbc.inter.DBAdapter;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;


/**
 * @author Administrator
 */
public abstract class BaseDBAdapter implements DBAdapter
{
    protected JdbcOperation jdbcOperation = null;

    protected AutoCreateSql autoCreateSql = null;
    
    public abstract Query getQuery();

    public abstract AutoCreateSql getAutoCreateSql();
    
    public abstract OtherProcess getOtherProcess();
}
