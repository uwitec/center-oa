package com.china.center.common.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;


/**
 * 值栈的实现
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageValue
 * @since
 */

public class PageValue extends BodyTagCenterSupport
{
    private String key = "";

    /**
     * 默认构建器
     */
    public PageValue()
    {
    }

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writeStart(buffer);

        this.writeContext(buffer.toString());

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writeEnd(buffer);

        this.writeContext(buffer.toString());

        return EVAL_PAGE;
    }

    private void writeStart(StringBuffer buffer)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        Object attribute = request.getParameter(this.key);

        if (attribute != null)
        {
            buffer.append(attribute);

            return;
        }

        attribute = request.getAttribute(this.key);

        if (attribute != null)
        {
            buffer.append(attribute);

            return;
        }

        attribute = request.getSession().getAttribute(this.key);

        if (attribute != null)
        {
            buffer.append(attribute);

            return;
        }

        attribute = pageContext.getServletContext().getAttribute(this.key);

        if (attribute != null)
        {
            buffer.append(attribute);

            return;
        }

        attribute = pageContext.getServletContext().getInitParameter(this.key);

        if (attribute != null)
        {
            buffer.append(attribute);

            return;
        }
    }

    private void writeEnd(StringBuffer buffer)
    {
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }
}
