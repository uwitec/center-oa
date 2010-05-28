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

import com.china.center.jdbc.annotation.enums.JoinType;

/**
 * definded the join in db
 *
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Join
{
	/**
	 * 连接方式
	 *
	 * @return
	 */
	JoinType type() default JoinType.EQUAL;

	/**
	 * 目标bean
	 *
	 * @return
	 */
	Class tagClass();

	/**
	 * 目标的字段(类定义字段)
	 *
	 * @return
	 */
	String tagField() default "";

	/**
	 * 自定义连接的表的别名
	 *
	 * @return
	 */
	String alias() default "";

}
