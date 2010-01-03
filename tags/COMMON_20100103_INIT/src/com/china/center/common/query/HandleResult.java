/**
 * File Name: HandleResult.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-10<br>
 * Grant: open source to everybody
 */
package com.china.center.common.query;

/**
 * HandleResult
 * 
 * @author zhuzhu
 * @version 2009-5-10
 * @see HandleResult
 * @since 1.0
 */
public interface HandleResult<T>
{
    /**
     * handle each item
     * 
     * @param obj
     */
    void handle(T obj);
}
