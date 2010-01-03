/*
 * 文件名：PageStart.java
 * 版权：Copyright 2002-2007 centerchina Tech. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：zhuzhu
 * 修改时间：2007-3-14
 * 跟踪单号：
 * 修改单号：
 * 修改内容：新增
 */
package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * 页面单元格
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageSetBeanClass
 * @since
 */

public class PageSetBeanClass extends BodyTagCenterSupport
{
    /**
     * 类名
     */
    private String value = "";

    /**
     * 默认构建器
     */
    public PageSetBeanClass()
    {}

    public int doStartTag()
        throws JspException
    {
        pageContext.setAttribute(TagLibConstant.CENTER_BEAN_CLASS, this.value);

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        return EVAL_PAGE;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }

}
