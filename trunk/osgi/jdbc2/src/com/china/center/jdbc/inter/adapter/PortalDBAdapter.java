/**
 *
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.jdbc.annosql.AutoCreateSql;
import com.china.center.jdbc.inter.DBAdapter;
import com.china.center.jdbc.inter.OtherProcess;
import com.china.center.jdbc.inter.Query;


/**
 * @author Administrator
 */
public class PortalDBAdapter extends BaseDBAdapter
{
    private DBAdapter adapter = new OracleDBAdapter();

    private String className = "";

    public PortalDBAdapter()
    {}

    public Query getQuery()
    {
        return adapter.getQuery();
    }

    public AutoCreateSql getAutoCreateSql()
    {
        return adapter.getAutoCreateSql();
    }

    public OtherProcess getOtherProcess()
    {
        return adapter.getOtherProcess();
    }

    /**
     * get className
     * 
     * @return className
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * set className
     * 
     * @param className
     *            the value of className
     */
    public void setClassName(String className)
    {
        this.className = className;

        try
        {
            Class forName = Class.forName(this.className);

            this.adapter = (DBAdapter)forName.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
