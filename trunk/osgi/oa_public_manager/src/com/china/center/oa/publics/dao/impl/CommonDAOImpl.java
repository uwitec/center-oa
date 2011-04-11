package com.china.center.oa.publics.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;


public class CommonDAOImpl implements CommonDAO
{
    private final Log _logger = LogFactory.getLog(getClass());

    private JdbcOperation jdbcOperation = null;

    private int squenceBegin = 0;

    private int squenceEnd = 0;

    private static Object LOCK = new Object();

    public int getSquence()
    {
        synchronized (LOCK)
        {
            if (squenceBegin != 0 && squenceBegin < squenceEnd)
            {
                return ++squenceBegin;
            }

            String sql = "select max(id) from T_CENTER_SEQUENCE";

            int tem = jdbcOperation.queryForInt(sql);

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
                con = jdbcOperation.getDataSource().getConnection();

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

            return (int)SequenceTools.getCurrentSequence();
        }
    }

    public synchronized String getSquenceString()
    {
        return String.valueOf(this.getSquence());
    }

    public synchronized String getSquenceString20()
    {
        return StringTools.formatString20(String.valueOf(this.getSquence()));
    }

    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation;
    }

    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }
}
