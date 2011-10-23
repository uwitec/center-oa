/*
 * File Name: AutoCreateSql.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-28
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annosql;

/**
 * 自动生成sql的接口
 * 
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
public interface AutoCreateSql
{
    /**
     * sql最大缓冲
     */
    int MAX_BUFFER_SQL = 2000;

    String PREFIX = "anno_";

    /**
     * insertSql
     * 
     * @param claz
     * @return
     * @throws MYSqlException
     */
    String insertSql(Class claz)
        throws MYSqlException;

    /**
     * updateSql
     * 
     * @param claz
     * @return
     * @throws MYSqlException
     */
    String updateSql(Class claz)
        throws MYSqlException;

    /**
     * updateSql
     * 
     * @param claz
     * @param columnName
     * @return
     * @throws MYSqlException
     */
    String updateSql(Class claz, String columnName)
        throws MYSqlException;

    /**
     * updateFieldSql
     * 
     * @param claz
     * @param fieldName
     * @return
     * @throws MYSqlException
     */
    String updateFieldSql(Class claz, String fieldName)
        throws MYSqlException;

    /**
     * delSql
     * 
     * @param claz
     * @return
     * @throws MYSqlException
     */
    String delSql(Class claz)
        throws MYSqlException;

    /**
     * delSql<br>
     * MYSQL:delete t from student t where t.id in (1,2);
     * 
     * @param claz
     * @param columnName
     * @return
     * @throws MYSqlException
     */
    String delSql(Class claz, String columnName)
        throws MYSqlException;

    /**
     * 查询详细的
     * 
     * @param id
     * @param claz
     * @return
     * @throws MYSqlException
     */
    String querySql(Class<?> claz)
        throws MYSqlException;

    /**
     * 查询详细的
     * 
     * @param id
     * @param claz
     * @return
     * @throws MYSqlException
     */
    String querySql(String columnName, Class<?> claz)
        throws MYSqlException;

    /**
     * 查询详细的
     * 
     * @param id
     * @param claz
     * @return
     * @throws MYSqlException
     */
    String queryByCondtionSql(String condition, Class<?> claz)
        throws MYSqlException;

    /**
     * 生成sql的前缀
     * 
     * @param claz
     * @return
     */
    String prefix(Class<?> claz);
}
