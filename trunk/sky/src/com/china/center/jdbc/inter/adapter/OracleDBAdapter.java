/**
 *
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.annosql.AutoCreateSql;
import com.china.center.annosql.adapter.OracleAutoCreateSql;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;


/**
 * @author Administrator
 */
public class OracleDBAdapter extends BaseDBAdapter
{
    public Query getQuery()
    {
        return new OracleQueryImpl();
    }

    public AutoCreateSql getAutoCreateSql()
    {
        if (autoCreateSql == null)
        {
            this.autoCreateSql = new OracleAutoCreateSql();
        }

        return autoCreateSql;
    }

    public OtherProcess getOtherProcess()
    {
        return new OracleOtherProcess();
    }
}
