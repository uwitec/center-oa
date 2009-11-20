/**
 * File Name: BaseOtherProcess.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-9<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.adapter;


import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.inter.OtherProcess;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2008-8-9
 * @see
 * @since
 */
public abstract class BaseOtherProcess implements OtherProcess
{
    protected JdbcOperation jdbcOperation = null;

    protected long sequence = 1001L;

    public long getUniqueSequence()
    {
        return sequence++ ;
    }

    public void setJdbc(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }

}
