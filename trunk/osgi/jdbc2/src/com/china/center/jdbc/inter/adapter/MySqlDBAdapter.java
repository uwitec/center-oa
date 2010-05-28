/**
 *
 */
package com.china.center.jdbc.inter.adapter;

import com.china.center.jdbc.annosql.AutoCreateSql;
import com.china.center.jdbc.annosql.adapter.MySqlAutoCreateSql;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;

/**
 * @author Administrator
 *
 */
public class MySqlDBAdapter extends BaseDBAdapter
{
    public Query getQuery()
    {
        return new MySqlQueryImpl();
    }
    
    public AutoCreateSql getAutoCreateSql()
    {
        if (autoCreateSql == null)
        {
            this.autoCreateSql = new MySqlAutoCreateSql();
        }

        return autoCreateSql;
    }
    
    public OtherProcess getOtherProcess()
    {
        return new MySqlOtherProcess();
    }

}
