/**
 * File Name: Note.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-10-7
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Defined
 * 
 * @author ZHUZHU
 * @version 2007-10-7
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Defined
{
    /**
     * 常量定义的key
     */
    String key();

    /**
     * 定义的现实名称
     */
    String value();
}
