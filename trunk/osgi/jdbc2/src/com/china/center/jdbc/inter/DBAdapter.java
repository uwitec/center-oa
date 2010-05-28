/**
 *
 */
package com.china.center.jdbc.inter;

import com.china.center.jdbc.annosql.AutoCreateSql;

/**
 * 数据库特殊的适用类
 * 
 * @author Administrator
 */
public interface DBAdapter
{
    /**
     * 查询的class
     * 
     * @return
     */
    Query getQuery();
    
    AutoCreateSql getAutoCreateSql();
    
    /**
     * get OtherProcess
     * @return
     */
    OtherProcess getOtherProcess();
}
