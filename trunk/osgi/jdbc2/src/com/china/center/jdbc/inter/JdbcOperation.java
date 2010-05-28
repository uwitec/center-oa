package com.china.center.jdbc.inter;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.jdbc.util.PageSeparate;


/**
 * JdbcOperation<br>
 * 数据库底层的操作接口
 */

public interface JdbcOperation
{
    void query(String sql, PreparedStatementSetter pss, final RowCallbackHandler handler)
        throws DataAccessException;

    void query(String sql, Object[] arg, final RowCallbackHandler handler)
        throws DataAccessException;

    void query(String sql, final RowCallbackHandler handler)
        throws DataAccessException;

    Object queryForObject(String sql, Class requiredType)
        throws DataAccessException;

    Object queryForObject(String sql, Object[] args, Class requiredType)
        throws DataAccessException;

    Map queryForMap(String sql)
        throws DataAccessException;

    Map queryForMap(String sql, Object... args)
        throws DataAccessException;

    List queryForList(String sql)
        throws DataAccessException;

    List queryForList(String sql, Object... args)
        throws DataAccessException;

    /**
     * 条件查询
     * 
     * @param condtition
     *            查询条件从 WHERE 开始
     * @param claz
     *            实体对象
     * @param args
     *            参数
     * @return List<T>
     * @throws DataAccessException
     */
    <T> List<T> queryForList(String condtition, Class<T> claz, Object... args)
        throws DataAccessException;

    /**
     * 条件查询(根据申明的外键查询)
     * 
     * @param fk
     *            声明的外键值
     * @param claz
     *            实体对象
     * @return List<T>
     * @throws DataAccessException
     */
    <T> List<T> queryForListByFK(Object fk, Class<T> claz, int index)
        throws DataAccessException;

    /**
     * 条件查询
     * 
     * @param columnName
     *            查询feild
     * @param claz
     *            实体对象
     * @param args
     *            参数
     * @return List<T>
     * @throws DataAccessException
     */
    <T> List<T> queryForListByField(String fieldName, Class<T> claz, Object... args)
        throws DataAccessException;

    /**
     * 自定义sql条件查询
     * 
     * @param condtition
     *            查询条件从 WHERE 开始
     * @param claz
     *            实体对象
     * @param args
     *            参数
     * @return List<T>
     * @throws DataAccessException
     */
    <T> List<T> queryForListBySql(String sql, Class<T> claz, Object... args)
        throws DataAccessException;

    /**
     * 根据全sql查询实体
     * 
     * @param <T>
     * @param sql
     * @param claz
     * @param args
     * @return
     * @throws DataAccessException
     */
    <T> Query queryObjectsBySql(String sql, Object... args)
        throws DataAccessException;

    /**
     * 根据条件查询实体
     * 
     * @param <T>
     * @param condtition
     * @param claz
     * @param args
     * @return
     * @throws DataAccessException
     */
    <T> Query queryObjects(String condtition, Class<T> claz, Object... args)
        throws DataAccessException;

    /**
     * 根据Unique查询
     * 
     * @param <T>
     * @param condtition
     * @param claz
     * @param args
     * @return
     * @throws DataAccessException
     */
    <T> Query queryObjectsByUnique(Class<T> claz, Object... args)
        throws DataAccessException;

    /**
     * 根据id查询
     * 
     * @param <T>
     * @param claz
     * @param id
     * @return
     * @throws DataAccessException
     */
    <T> Query queryObjectById(Class<T> claz, Serializable id)
        throws DataAccessException;

    /**
     * 根据FK查询
     * 
     * @param <T>
     * @param condtition
     * @param claz
     * @param args
     * @return
     * @throws DataAccessException
     */
    <T> Query queryObjectsByFK(Class<T> claz, Object fk, int index)
        throws DataAccessException;

    /**
     * 根据分页类查询
     * 
     * @param condtition
     *            查询条件
     * @param page
     *            分页对象
     * @param claz
     *            类对象
     * @param args
     *            sql参数
     * @return 查询结果
     * @throws DataAccessException
     */
    <T> List<T> queryObjectsByPageSeparate(String condtition, PageSeparate page, Class<T> claz,
                                           Object... args)
        throws DataAccessException;

    <T> List<T> queryObjectsBySqlAndPageSeparate(String sql, PageSeparate page, Class<T> claz,
                                                 Object... args)
        throws DataAccessException;

    int update(final String sql)
        throws DataAccessException;

    /**
     * 更新一个对象
     * 
     * @param claz
     * @return
     * @throws DataAccessException
     */
    int update(Object obj)
        throws DataAccessException;

    /**
     * batch save entry
     * 
     * @param list
     * @return
     * @throws DataAccessException
     */
    <T> int[] updateAll(final Collection<T> list, Class<T> claz)
        throws DataAccessException;

    /**
     * 更新一个对象
     * 
     * @param claz
     * @param column
     *            主键
     * @return
     * @throws DataAccessException
     */
    int updateEntry(Object obj, String fieldName)
        throws DataAccessException;

    /**
     * 更新一个field，通过主键
     * 
     * @param fieldName
     * @param fieldValue
     * @param id
     * @param claz
     * @return
     * @throws DataAccessException
     */
    int updateField(String fieldName, Object fieldValue, Object id, Class claz)
        throws DataAccessException;

    int update(String sql, final PreparedStatementSetter myPss)
        throws DataAccessException;

    /**
     * 更新实体
     * 
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     */
    int update(String sql, final Object... args)
        throws DataAccessException;

    /**
     * 更新实体
     * 
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     */
    int update(String setSql, Class claz, final Object... args)
        throws DataAccessException;

    /**
     * 根据annotation保存对象到数据库
     * 
     * @param object
     * @return
     * @throws DataAccessException
     */
    int save(final Object object)
        throws DataAccessException;

    /**
     * batch save entry
     * 
     * @param list
     * @return
     * @throws DataAccessException
     */
    <T> int[] saveAll(final Collection<T> list, Class<T> claz)
        throws DataAccessException;

    /**
     * 根据主键删除一个对象
     * 
     * @param claz
     * @return
     * @throws DataAccessException
     */
    int delete(Object keyValue, Class claz)
        throws DataAccessException;

    /**
     * delete by ids
     * @param <T>
     * @param list
     * @param claz
     * @return
     * @throws DataAccessException
     */
    int[] deleteByIds(final Collection list, Class claz)
        throws DataAccessException;

    /**
     * delete by ids,but id is form bean
     * @param <T>
     * @param list
     * @param claz
     * @return
     * @throws DataAccessException
     */
    <T> int[] deleteByBeans(final Collection<T> list, Class<T> claz)
        throws DataAccessException;

    /**
     * 根据指定的主键删除对象
     * 
     * @param claz
     * @return
     * @throws DataAccessException
     */
    int delete(Object keyValue, String fieldName, Class claz)
        throws DataAccessException;

    /**
     * delete entry by sql
     * 
     * @param claz
     * @param sql
     * @param args
     * @return
     * @throws DataAccessException
     */
    int delete(String whereSql, Class claz, final Object... args)
        throws DataAccessException;

    /**
     * 根据主键查询单体
     * 
     * @param id
     *            主键值
     * @param claz
     *            目标对象
     * @return
     * @throws DataAccessException
     */
    <T> T find(Object id, Class<T> claz)
        throws DataAccessException;

    /**
     * 根据主键查询单体
     * 
     * @param claz
     *            目标对象
     * @param keys
     *            联合主键值
     * @return
     * @throws DataAccessException
     */
    <T> T findByUnique(Class<T> claz, Object... keys)
        throws DataAccessException;

    /**
     * 根据主键查询单体
     * 
     * @param id
     *            主键值
     * @param fieldName
     *            类里面的属性名(不是直接的列名)
     * @param claz
     *            目标对象
     * @return
     * @throws DataAccessException
     */
    <T> T find(Object id, String fieldName, Class<T> claz)
        throws DataAccessException;

    long queryForLong(String sql)
        throws DataAccessException;

    int queryForInt(String sql)
        throws DataAccessException;

    int queryForInt(String sql, Object... arg)
        throws DataAccessException;

    double queryForDouble(String sql, Object... arg)
        throws DataAccessException;

    int queryForInt(String sql, Class claz, Object... arg)
        throws DataAccessException;

    // boolean queryForUnique(Object object) throws DataAccessException;
    // boolean queryForExist(Object object, Strin) throws DataAccessException;

    int[] batchUpdate(final String... sql)
        throws DataAccessException;

    Object query(String sql, PreparedStatementSetter pss, final ResultSetExtractor rse)
        throws DataAccessException;

    int[] batchUpdate(String sql, BatchPreparedStatementSetter pss);

    void query(String sql, MyPreparedStatementSetter pss, final RowCallbackHandler handler)
        throws DataAccessException;

    int update(String sql, final MyPreparedStatementSetter myPss)
        throws DataAccessException;

    int update(String sql, final MyPreparedStatementSetter myPss, final List<Integer> columnType)
        throws DataAccessException;

    int[] batchUpdate(String sql, final MyBatchPreparedStatementSetter pss)
        throws DataAccessException;

    void execute(String sql)
        throws DataAccessException;

    DataSource getDataSource();

    /**
     * 获得缓存 适配类 进行手动通知
     * 
     * @return
     */
    AdapterCache getAdapterCache();

    /**
     * 获得Ibatis的接口实现
     * 
     * @return
     */
    IbatisDaoSupport getIbatisDaoSupport();
}
