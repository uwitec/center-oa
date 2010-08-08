package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageBody
 * @since
 */
public class PageBody extends BodyTagCenterSupport
{
    private String width = "85%";

    /**
     * @return Returns the bodyWidth.
     */
    public String getWidth()
    {
        return width;
    }

    /**
     * @param bodyWidth
     *            The bodyWidth to set.
     */
    public void setWidth(String bodyWidth)
    {
        this.width = bodyWidth;
    }

    /**
     * 默认构建器
     */
    public PageBody()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writePageBodyStart(buffer);

        this.writeContext(buffer.toString());
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();
        writePageBodyEnd(buffer);
        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }

    private void writePageBodyStart(StringBuffer buffer)
    {
        buffer.append("<table width='").append(width).append(
            "' border='0' cellpadding='0' cellspacing='0' align='center'> ");
    }

    private void writePageBodyEnd(StringBuffer buffer)
    {
        buffer.append("</table>");
    }
}
