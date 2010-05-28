package com.china.center.jdbc.inter.impl;


import java.util.Map;

import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;

import com.china.center.jdbc.inter.DynamicJdbcOperation;
import com.china.center.jdbc.inter.JdbcOperation;


/**
 * MyDynamicJdbcOperationImpl
 * 
 * @author ZHUZHU
 * @version 2009-11-25
 * @see MyDynamicJdbcOperationImpl
 * @since 1.0
 */
public class MyDynamicJdbcOperationImpl implements DynamicJdbcOperation
{
    /**
     * jdbcOperationMap
     */
    private Map<String, JdbcOperation> jdbcOperationMap = null;

    private Map<String, PlatformTransactionManager> platformTransactionManagerMap = null;

    private JdbcOperation defaultJdbcOperation = null;

    private PlatformTransactionManager defaultPlatformTransactionManager = null;

    /**
     * default constructor
     */
    public MyDynamicJdbcOperationImpl()
    {}

    /**
     * @param key
     * @return
     */
    public JdbcOperation getJdbcOperation(String key)
    {
        JdbcOperation jdbcOperation = jdbcOperationMap.get(key);

        if (jdbcOperation != null)
        {
            return jdbcOperation;
        }

        return defaultJdbcOperation;
    }

    /**
     * @param key
     * @return
     */
    public DataSource getDataSource(String key)
    {
        return getJdbcOperation(key).getDataSource();
    }

    /**
     * @param key
     * @return
     */
    public PlatformTransactionManager getPlatformTransactionManager(String key)
    {
        PlatformTransactionManager platformTransactionManager = platformTransactionManagerMap.get(key);

        if (platformTransactionManager != null)
        {
            return platformTransactionManager;
        }

        return defaultPlatformTransactionManager;
    }

    /**
     * get jdbcOperationMap
     * 
     * @return jdbcOperationMap
     */
    public Map<String, JdbcOperation> getJdbcOperationMap()
    {
        return jdbcOperationMap;
    }

    /**
     * set jdbcOperationMap
     * 
     * @param jdbcOperationMap
     *            the value of jdbcOperationMap
     */
    public void setJdbcOperationMap(Map<String, JdbcOperation> jdbcOperationMap)
    {
        this.jdbcOperationMap = jdbcOperationMap;
    }

    /**
     * get defaultJdbcOperation
     * 
     * @return defaultJdbcOperation
     */
    public JdbcOperation getDefaultJdbcOperation()
    {
        return defaultJdbcOperation;
    }

    /**
     * set defaultJdbcOperation
     * 
     * @param defaultJdbcOperation
     *            the value of defaultJdbcOperation
     */
    public void setDefaultJdbcOperation(JdbcOperation defaultJdbcOperation)
    {
        this.defaultJdbcOperation = defaultJdbcOperation;
    }

    /**
     * get platformTransactionManagerMap
     * 
     * @return platformTransactionManagerMap
     */
    public Map<String, PlatformTransactionManager> getPlatformTransactionManagerMap()
    {
        return platformTransactionManagerMap;
    }

    /**
     * set platformTransactionManagerMap
     * 
     * @param platformTransactionManagerMap
     *            the value of platformTransactionManagerMap
     */
    public void setPlatformTransactionManagerMap(
                                                 Map<String, PlatformTransactionManager> platformTransactionManagerMap)
    {
        this.platformTransactionManagerMap = platformTransactionManagerMap;
    }

    /**
     * get defaultPlatformTransactionManager
     * 
     * @return defaultPlatformTransactionManager
     */
    public PlatformTransactionManager getDefaultPlatformTransactionManager()
    {
        return defaultPlatformTransactionManager;
    }

    /**
     * set defaultPlatformTransactionManager
     * 
     * @param defaultPlatformTransactionManager
     *            the value of defaultPlatformTransactionManager
     */
    public void setDefaultPlatformTransactionManager(
                                                     PlatformTransactionManager defaultPlatformTransactionManager)
    {
        this.defaultPlatformTransactionManager = defaultPlatformTransactionManager;
    }
}
