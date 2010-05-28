/*
 * File Name: Column.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-28
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.china.center.jdbc.annotation.enums.ColumnType;


/**
 * definded the column in db
 * 
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column
{
    /**
     * column name
     * 
     * @return
     */
    String name();

    /**
     * is pk default false
     * 
     * @return
     */
    boolean pk() default false;

    /**
     * unite pk in table
     * 
     * @return
     */
    boolean unitePk() default false;

    /**
     * the type of column default -1
     * 
     * @return
     */
    ColumnType type() default ColumnType.DEFAULT;

    /**
     * length
     * 
     * @return
     */
    int length() default -1;

    /**
     * nullable
     * 
     * @return
     */
    boolean nullable() default true;
}
