/**
 * File Name: Note.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-10-7
 * Grant: open source to everybody
 */
package com.china.center.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ×¢ÊÍ
 * 
 * @author zhuzhu
 * @version 2007-10-7
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR})
public @interface Note
{
    /**
     * ¾ßÌåµÄ×¢ÊÍ
     */
    String value() default "";
}
