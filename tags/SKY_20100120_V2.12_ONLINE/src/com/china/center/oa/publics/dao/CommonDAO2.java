package com.china.center.oa.publics.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;

import net.sourceforge.sannotations.annotation.Bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.tools.StringTools;


/**
 * 必须是单立的类
 * 
 * @author zhu
 * @version 2006-7-16
 * @see CommonDAO2
 * @since
 */
@Bean(name = "commonDAO2")
public class CommonDAO2
{
    private final Log _logger = LogFactory.getLog(getClass());

    private JdbcOperation jdbcOperation2 = null;

    private int squenceBegin = 0;

    private int squenceEnd = 0;

    public synchronized int getSquence()
    {
        if (squenceBegin != 0 && squenceBegin < squenceEnd)
        {
            return ++squenceBegin ;
        }

        String sql = "select max(id) from T_CENTER_SEQUENCE";

        int tem = jdbcOperation2.queryForInt(sql);

        int kk = tem;

        if (tem > (Integer.MAX_VALUE - 101000))
        {
            kk = 0;
        }

        squenceBegin = kk + 1;

        squenceEnd = kk + 1000;

        // kk+1 to kk+1000

        sql = "update T_CENTER_SEQUENCE set id = ? where id = ?";

        Connection con = null;

        PreparedStatement ps = null;
        try
        {
            con = jdbcOperation2.getDataSource().getConnection();

            con.setAutoCommit(false);

            ps = con.prepareStatement(sql);

            ps.setInt(1, squenceEnd);

            ps.setInt(2, tem);
            
            ps.execute();

            con.commit();
        }
        catch (Throwable e)
        {
            _logger.error(e, e);
        }
        finally
        {
            if (ps != null)
            {
                try
                {
                    ps.close();
                }
                catch (Throwable e)
                {
                    _logger.error(e, e);
                }
            }

            if (con != null)
            {
                try
                {
                    con.close();
                }
                catch (Throwable e)
                {
                    _logger.error(e, e);
                }
            }
        }
        // jdbcOperation2.update(sql, new Object[] {kk + 1, tem});

        return tem + 1;
    }

    public String getSquenceString()
    {
        return String.valueOf(this.getSquence());
    }
    
    public String getSquenceString20()
    {
        return StringTools.formatString20(String.valueOf(this.getSquence()));
    }

    /**
     * @return the jdbcOperation
     */
    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation2;
    }

    /**
     * @param jdbcOperation
     *            the jdbcOperation to set
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation2 = jdbcOperation;
    }
}
