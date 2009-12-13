/*
 * File Name: JCheck.java CopyRight: Copyright by www.center.china Description:
 * Creater: zhuAchen CreateTime: 2007-12-13 Grant: open source to everybody
 */
package com.china.center.annotation;

/**
 * 自动校验
 * 
 * @author zhuzhu
 * @version 2007-12-13
 * @see
 * @since
 */
public interface JCheck
{
    /**
     * 仅仅数字 
     */
    String ONLY_NUMBER = "isNumber;";
    
    /**
     * 不能为空
     */
    String NOT_NONE = "notNone;";
    
    /**
     * 正常字符
     */
    String ONLY_COMMONCHAR = "isCommonChar;";
    
    /**
     * 字母/数字
     */
    String ONLY_NUMBER_OR_LETTER = "isNumberOrLetter;";
    
    /**
     * 仅仅是字母
     */
    String ONLY_LETTER = "isLetter;";
    
    /**
     * 浮点型
     */
    String ONLY_FLOAT = "isFloat;";
}
