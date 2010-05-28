/**
 * File Name: QueryConfig.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.query;


import java.util.Collection;


/**
 * QueryConfig
 * 
 * @author ZHUZHU
 * @version 2008-11-8
 * @see QueryConfig
 * @since 1.0
 */
public interface QueryConfig
{
    /**
     * findQueryCondition
     * @param name
     * @return
     */
    QueryItemBean findQueryCondition(String name);

    /**
     * listQueryCondition
     * @return
     */
    Collection<QueryItemBean> listQueryCondition();
}
