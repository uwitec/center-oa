/**
 * File Name: Query.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-2<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter;


import java.util.List;
import java.util.Map;



/**
 * 查询结果
 *
 * @author zhuzhu
 * @version 2008-3-2
 * @see
 * @since
 */
public interface Query
{
    String FROM = " from ";
    
    String LIMIT = " limit ";
    
    /**
     * 获得查询结果
     *
     * @return
     */
    <T> List<T> list(Class<T> claz);

    List list();

    /**
     * get unique result
     *
     * @param <T>
     * @param claz
     * @return
     */
    <T> T uniqueResult(Class<T> claz);

    Map uniqueResult();

    /**
     * set max results in query
     *
     * @param maxResults
     * @return
     */
    Query setMaxResults(int maxResults);

    /**
     * set range results in query
     *
     * @param begin
     * @param end
     * @return
     */
    Query setResultsRange(int begin, int end);

    /**
     * set the first which begin in query
     *
     * @param firstResult
     * @return
     */
    Query setFirstResult(int firstResult);

    void setSqlString(String sql);

    void setParamters(Object... pars);

    void setPrefix(String prefix);

    void setIdColumn(String idColumn);

    /**
     * 获得操作
     *
     * @param jdbcOperation
     */
    void setJdbcOperation(JdbcOperation jdbcOperation);

    String getLastSql();

    int getCount();

    void setAdapterCache(AdapterCache adapterCache);

    void setCache(boolean cache);
}
