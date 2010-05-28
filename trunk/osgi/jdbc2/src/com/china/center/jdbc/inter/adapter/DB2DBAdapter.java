/**
 *
 */
package com.china.center.jdbc.inter.adapter;

import com.china.center.jdbc.annosql.AutoCreateSql;
import com.china.center.jdbc.annosql.adapter.DB2AutoCreateSql;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;

/**
 * @author Administrator
 *
 */
public class DB2DBAdapter extends BaseDBAdapter
{
    public Query getQuery()
    {
        return new DB2QueryImpl();
    }
    
    public AutoCreateSql getAutoCreateSql()
    {
        if (autoCreateSql == null)
        {
            this.autoCreateSql = new DB2AutoCreateSql();
        }

        return autoCreateSql;
    }
    
    public OtherProcess getOtherProcess()
    {
        return new DB2OtherProcess();
    }
}
