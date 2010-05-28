package com.china.center.jdbc.inter;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ibatis.sqlmap.client.event.RowHandler;


/**
 *
 * @see IbatisDaoSupport
 * @since
 */

public interface IbatisDaoSupport
{
    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(String)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    Object queryForObject(String statementName);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(String, Object)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    Object queryForObject(String statementName, Object parameterObject);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(String, Object, Object)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    Object queryForObject(String statementName, Object parameterObject, Object resultObject);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(String)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    List queryForList(String statementName);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(String, Object)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    List queryForList(String statementName, Object parameterObject);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(String, int, int)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    List queryForList(String statementName, int skipResults, int maxResults);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(String, Object, int, int)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    List queryForList(String statementName, Object parameterObject, int skipResults, int maxResults);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryWithRowHandler(String, RowHandler)
     * @throws org.springframework.dao.DataAccessException in case of errors
     */
    void queryWithRowHandler(String statementName, RowHandler rowHandler);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryWithRowHandler(String, Object, RowHandler)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    void queryWithRowHandler(String statementName, Object parameterObject, RowHandler rowHandler);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForMap(String, Object, String)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    Map queryForMap(String statementName, Object parameterObject, String keyProperty);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForMap(String, Object, String, String)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    Map queryForMap(String statementName, Object parameterObject, String keyProperty,
                    String valueProperty);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(String)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    Object insert(String statementName);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(String, Object)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    Object insert(String statementName, Object parameterObject);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(String, Object)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    int update(String statementName, Object parameterObject);

    /**
     * Convenience method provided by Spring: execute an update operation
     * with an automatic check that the update affected the given required
     * number of rows.
     * @param statementName the name of the mapped statement
     * @param parameterObject the parameter object
     * @param requiredRowsAffected the number of rows that the update is
     * required to affect
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    void update(String statementName, Object parameterObject, int requiredRowsAffected);

    /**
     * @see com.ibatis.sqlmap.client.SqlMapExecutor#delete(String, Object)
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    int delete(String statementName, Object parameterObject);

    /**
     * Convenience method provided by Spring: execute a delete operation
     * with an automatic check that the delete affected the given required
     * number of rows.
     * @param statementName the name of the mapped statement
     * @param parameterObject the parameter object
     * @param requiredRowsAffected the number of rows that the delete is
     * required to affect
     * @throws org.springframework.dao.DataAccessException in case of errors
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    void delete(String statementName, Object parameterObject, int requiredRowsAffected);
    
    void setDataSource(DataSource dataSource);
}
