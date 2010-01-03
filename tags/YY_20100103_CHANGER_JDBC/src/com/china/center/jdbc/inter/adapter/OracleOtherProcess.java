/**
 * File Name: PracleOtherProcess.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-9<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.adapter;

/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2008-8-9
 * @see
 * @since
 */
public class OracleOtherProcess extends BaseOtherProcess
{
    /**
     * 通过oracle的sequence获得id
     */
    public long getUniqueSequence()
    {
        return this.jdbcOperation.queryForLong("SELECT SEQ_CENTER_ANNOSQL_UNION.NEXTVAL FROM DUAL");
    }
}
