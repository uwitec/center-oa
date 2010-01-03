package com.china.center.common.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageBodyMessage
 * @since
 */

public class PageBodyMessage extends BodyTagCenterSupport
{
    private String scope = "session";

    private String message = "MESSAGE_INFO";

    private String color = "blue";

    /**
     * 默认构建器
     */
    public PageBodyMessage()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writeDiv(buffer);

        this.writeContext(buffer.toString());
        return EVAL_BODY_INCLUDE;
    }

    private void writeDiv(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append(
            "<div id='GG_MESSAGE_GG'><input type='hidden' value='0' id='GG_MESSAGE_GG_HIDDEN'>").append(
            line);

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        String messagese = null;
        if ("session".equals(scope.toLowerCase()))
        {
            messagese = (String)request.getSession().getAttribute(message);
            request.getSession().removeAttribute(message);
        }

        if ("request".equals(scope.toLowerCase()))
        {
            messagese = (String)request.getAttribute(message);

            request.removeAttribute(message);
        }

        if ("application".equals(scope.toLowerCase()))
        {
            messagese = pageContext.getServletConfig().getInitParameter(message);
        }

        if ("page".equals(scope.toLowerCase()))
        {
            messagese = (String)pageContext.getAttribute(message);
        }

        if (this.isNullOrNone(messagese))
        {
            messagese = "";
        }
        else
        {
            messagese = "提示:" + messagese;
        }

        buffer.append("<font color='" + color + "'><B id='GG_MESSAGE_GG_B'>").append(messagese).append(
            "</B></font></div>").append(line);
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message
     *            The message to set.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * @return Returns the scope.
     */
    public String getScope()
    {
        return scope;
    }

    /**
     * @param scope
     *            The scope to set.
     */
    public void setScope(String scope)
    {
        if (scope != null)
        {
            this.scope = scope;
        }
    }

    /**
     * @return Returns the color.
     */
    public String getColor()
    {
        return color;
    }

    /**
     * @param color
     *            The color to set.
     */
    public void setColor(String color)
    {
        if (color != null)
        {
            this.color = color;
        }
    }

}
