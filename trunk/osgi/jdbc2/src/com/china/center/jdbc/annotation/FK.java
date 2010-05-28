/*
 * File Name: Table.java
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
 * the FOREIGN KEY in table<br>
 * this is tell process the field is FOREIGN KEY <br>
 * actually the field is FOREIGN KEY ? I do not know<br>
 * support more PKs in table
 *
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FK
{
	/**
	 * more pk in a table
	 * @return
	 */
	int index() default 0;
}
