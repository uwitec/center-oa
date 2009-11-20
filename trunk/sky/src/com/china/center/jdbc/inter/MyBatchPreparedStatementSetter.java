package com.china.center.jdbc.inter;


import java.sql.SQLException;
import java.util.List;


/**
 * 〈一句话功能简述〉
 * 
 * @author zhuzhu
 * @version 2007-3-3
 * @see MyBatchPreparedStatementSetter
 * @since
 */

public interface MyBatchPreparedStatementSetter
{
    void setValues(List list, int i)
        throws SQLException;

    /**
     * Return the size of the batch.
     */
    int getBatchSize();
}
