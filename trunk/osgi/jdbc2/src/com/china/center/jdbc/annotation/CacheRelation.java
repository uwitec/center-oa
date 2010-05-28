/**
 * File Name: CacheRelation.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * CacheRelation<br>
 * defined the relation between class and class<br>
 * note:all class must user cache = true,or the application will error
 * 
 * @author ZHUZHU
 * @version 2008-11-8
 * @see CacheRelation
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CacheRelation
{
    Class[] relation() default {};
}
