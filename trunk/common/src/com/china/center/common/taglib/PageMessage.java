package com.china.center.common.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageMessage
 * @since
 */

public class PageMessage extends BodyTagCenterSupport
{
    private String scope = "request";

    private String successMessage = "MESSAGE_INFO";

    private String errorMessage = "errorInfo";

    /**
     * 默认构建器
     */
    public PageMessage()
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

        // <div id=g_center_tip><h1><a href='javascript:void(0)' onclick='tip_hiden()'>×</a>系统提示</h1><p>zhuzhu</p></div>
        String line = "\r\n";
        buffer.append(
            "<div id=g_center_tip><h1><a href='javascript:void(0)' onclick='tip_hiden()'>×</a>系统提示</h1><p>").append(
            line);

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        String succMessagese = getMessage(request, scope, successMessage);

        String errMessagese = getMessage(request, scope, errorMessage);

        buffer.append("<FONT color='blue' id='g_tip_content1'>").append(succMessagese).append(
            "</FONT><FONT color='red' id='g_tip_content2'>").append(errMessagese).append("</FONT></p></div><input type=hidden id=g_tip_flag value='0'>");
    }

    private String getMessage(HttpServletRequest request, String scope, String successMessage)
    {
        String messagese = null;

        if (this.isNullOrNone(scope))
        {
            messagese = (String)request.getAttribute(successMessage);

            request.removeAttribute(successMessage);
        }

        if ("session".equals(scope.toLowerCase()))
        {
            messagese = (String)request.getSession().getAttribute(successMessage);
            request.getSession().removeAttribute(successMessage);
        }

        if ("request".equals(scope.toLowerCase()))
        {
            messagese = (String)request.getAttribute(successMessage);

            request.removeAttribute(successMessage);
        }

        if ("application".equals(scope.toLowerCase()))
        {
            messagese = pageContext.getServletConfig().getInitParameter(successMessage);
        }

        if ("page".equals(scope.toLowerCase()))
        {
            messagese = (String)pageContext.getAttribute(successMessage);
        }

        if (this.isNullOrNone(messagese))
        {
            messagese = "";
        }

        return messagese;
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
     * @return the successMessage
     */
    public String getSuccessMessage()
    {
        return successMessage;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * @param successMessage
     *            the successMessage to set
     */
    public void setSuccessMessage(String successMessage)
    {
        this.successMessage = successMessage;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

}
