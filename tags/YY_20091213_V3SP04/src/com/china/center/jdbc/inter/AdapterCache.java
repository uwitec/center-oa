/**
 *
 */
package com.china.center.jdbc.inter;


import java.util.List;


/**
 * jdbc cache
 * 
 * @author Administrator
 */
public interface AdapterCache
{
    String SELECT = "select";

    String INSERT = "insert";

    String UPDATE = "update";

    String DELETE = "delete";

    String INTO = "into";

    String FROM = "from";

    void bootstrap();

    void insertNote(Class claz);

    void updateNote(Class claz);

    void deleteNote(Class claz);

    void cacheNote(String sql);

    <T> T find(Class<T> claz, Object id);

    <T> void addSingle(Class<T> claz, Object id, T object);

    <T> List<T> query(Class<T> claz, String condtion, Object... args);

    <T> void addMore(Class<T> claz, List<T> result, String condtion, Object... args);
}
