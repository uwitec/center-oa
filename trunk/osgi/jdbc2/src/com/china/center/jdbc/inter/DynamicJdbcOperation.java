package com.china.center.jdbc.inter;


import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;


/**
 * DynamicJdbcOperation
 * 
 * @author ZHUZHU
 * @version 2009-11-25
 * @see DynamicJdbcOperation
 * @since 1.0
 */
public interface DynamicJdbcOperation
{
    /**
     * get special JdbcOperation
     * 
     * @param key
     *            special key
     * @return
     */
    JdbcOperation getJdbcOperation(String key);

    /**
     * getDataSource
     * 
     * @param key
     * @return
     */
    DataSource getDataSource(String key);

    /**
     * getPlatformTransactionManager
     * 
     * @param key
     * @return
     */
    PlatformTransactionManager getPlatformTransactionManager(String key);
}
