/**
 * 
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.jdbc.annosql.AutoCreateSql;
import com.china.center.jdbc.annosql.adapter.AccessAutoCreateSql;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;


/**
 * @author Administrator
 */
public class AccessDBAdapter extends BaseDBAdapter
{
    public Query getQuery()
    {
        return new AccessQueryImpl();
    }

    public AutoCreateSql getAutoCreateSql()
    {
        if (autoCreateSql == null)
        {
            this.autoCreateSql = new AccessAutoCreateSql();
        }

        return autoCreateSql;
    }

    public OtherProcess getOtherProcess()
    {
        return new AccessOtherProcess();
    }
}
