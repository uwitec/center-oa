/**
 * File Name: Unique.java
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


/**
 * the Unique KEY in table<br>
 * this is tell process the field is Unique KEY <br>
 * actually the field is Unique KEY ? I do not know<br>
 * support only one Unique in table
 * 
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Unique
{
    /**
     * 条件下唯一
     * 
     * @return
     */
    String[] dependFields() default {};
}
