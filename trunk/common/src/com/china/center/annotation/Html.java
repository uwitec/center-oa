/*
 * File Name: Html.java
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

import com.china.center.annotation.enums.Element;


/**
 * jsp展示的annotation
 * 
 * @author zhuzhu
 * @version 2007-10-7
 * @see
 * @since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Html
{
    /**
     * 页面展现类型
     * 
     * @return
     */
    Element type() default Element.INPUT;

    /**
     * Element的name
     * 
     * @return
     */
    String name() default "";

    /**
     * 展现的标题
     * 
     * @return String
     */
    String title();

    /**
     * 页面的提示
     * 
     * @return
     */
    String tip() default "";

    /**
     * 抽象的(在界面上占位，通过js动态显示)(暂时不使用)
     * 
     * @return
     */
    boolean abstracts() default false;

    /**
     * 展现的顺序(暂时不使用)
     * 
     * @return
     */
    int order() default 0;

    /**
     * 自动校验的规则
     * 
     * @return
     */
    String[] oncheck() default {};

    /**
     * 是否必填
     * 
     * @return
     */
    boolean must() default false;

    /**
     * 只读
     * 
     * @return
     */
    boolean readonly() default false;

    /**
     * 最大长度(仅仅对input生效)
     * 
     * @return
     */
    int maxLength() default 0;
}
