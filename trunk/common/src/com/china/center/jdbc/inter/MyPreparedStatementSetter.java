package com.china.center.jdbc.inter;


import java.sql.SQLException;
import java.util.List;


/**
 * 〈一句话功能简述〉
 * 
 * @author zhuzhu
 * @version 2007-3-3
 * @see MyPreparedStatementSetter
 * @since
 */

public interface MyPreparedStatementSetter
{
    public void setValues(List list)
        throws SQLException;

}
