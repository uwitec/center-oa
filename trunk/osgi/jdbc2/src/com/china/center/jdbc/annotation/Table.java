/**
 * File Name: Table.java CopyRight: Copyright by www.center.china Description:
 * Creater: zhuAchen CreateTime: 2007-9-28 Grant: open source to everybody
 */
package com.china.center.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the table in db
 * 
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table
{
    /**
     * Description: 表名
     * 
     * @return
     */
    String name();
}
