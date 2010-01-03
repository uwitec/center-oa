/**
 * File Name: Column.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-28
 * Grant: open source to everybody
 */
package com.china.center.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * definded the Relationship between tables
 * 
 * @author zhuzhu
 * @version 2007-9-28
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Relationship
{
    /**
     * 目标的字段(类定义字段)
     * 
     * @return
     */
    String tagField() default "name";

    /**
     * join field
     * 
     * @return
     */
    String relationField();
}
