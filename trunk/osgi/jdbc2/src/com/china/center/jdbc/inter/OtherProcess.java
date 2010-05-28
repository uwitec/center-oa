/**
 * File Name: OtherProcess.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-9<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter;

/**
 * some db special process in auto create sql
 * 
 * @author ZHUZHU
 * @version 2008-8-9
 * @see
 * @since
 */
public interface OtherProcess
{
    void setJdbc(JdbcOperation jdbcOperation);

    /**
     * 获得唯一的sequence
     * 
     * @return
     */
    long getUniqueSequence();
}
