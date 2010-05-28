package com.china.center.jdbc.inter.adapter;


import com.china.center.jdbc.annosql.AutoCreateSql;
import com.china.center.jdbc.annosql.adapter.HSqlAutoCreateSql;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;


/**
 * HSqlDBAdapter
 * 
 * @author ZHUZHU
 * @version 2010-3-25
 * @see HSqlDBAdapter
 * @since 1.0
 */
public class HSqlDBAdapter extends BaseDBAdapter
{
    public Query getQuery()
    {
        return new HSqlQueryImpl();
    }

    public AutoCreateSql getAutoCreateSql()
    {
        if (autoCreateSql == null)
        {
            this.autoCreateSql = new HSqlAutoCreateSql();
        }

        return autoCreateSql;
    }

    public OtherProcess getOtherProcess()
    {
        return new HSqlOtherProcess();
    }
}
