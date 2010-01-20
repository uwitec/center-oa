package com.china.center.oa.publics.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.JdbcOperation;


/**
 * @author zhu
 * @version 2006-7-16
 * @see CommonDAO
 * @since
 */
@Bean(name = "commonDAO")
public class CommonDAO
{
    private JdbcOperation jdbcOperation = null;

    public synchronized int getSquence()
    {
        String sql = "select max(id) from T_CENTER_SEQUENCE";

        int tem = jdbcOperation.queryForInt(sql);

        int kk = tem;

        if (tem > (Integer.MAX_VALUE - 1000))
        {
            kk = 0;
        }

        sql = "update T_CENTER_SEQUENCE set id = ?  where id = ?";

        jdbcOperation.update(sql, new Object[] {kk + 1, tem});

        return tem + 1;
    }

    public String getSquenceString()
    {
        return String.valueOf(this.getSquence());
    }

    /**
     * @return the jdbcOperation
     */
    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation;
    }

    /**
     * @param jdbcOperation
     *            the jdbcOperation to set
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }

}
