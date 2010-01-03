/**
 *
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.annosql.AutoCreateSql;
import com.china.center.annosql.adapter.MSAutoCreateSql;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;


/**
 * @author Administrator
 */
public class MSDBAdapter extends BaseDBAdapter
{
    public Query getQuery()
    {
        return new MSQueryImpl();
    }

    public AutoCreateSql getAutoCreateSql()
    {
        if (autoCreateSql == null)
        {
            this.autoCreateSql = new MSAutoCreateSql();
        }

        return autoCreateSql;
    }

    public OtherProcess getOtherProcess()
    {
        return new MSOtherProcess();
    }
}
