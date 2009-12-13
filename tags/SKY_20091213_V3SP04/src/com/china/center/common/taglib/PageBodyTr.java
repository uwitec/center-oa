package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageBodyTr
 * @since
 */

public class PageBodyTr extends BodyTagCenterSupport
{
    private String height = "10";

    private String align = "right";

    /**
     * 默认构建器
     */
    public PageBodyTr()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        write(buffer);
        this.writeContext(buffer.toString());

        return EVAL_BODY_INCLUDE;
    }

    private void write(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("<tr height='" + height + "'>").append(line);
        buffer.append("<td height='" + height + "' colspan='2' align='" + align + "'>").append(
            line);
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writePageLine(buffer);

        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }

    private void writePageLine(StringBuffer buffer)
    {
        buffer.append("</td></tr>");

    }

    /**
     * @return Returns the height.
     */
    public String getHeight()
    {
        return height;
    }

    /**
     * @param height
     *            The height to set.
     */
    public void setHeight(String height)
    {
        this.height = height;
    }

    /**
     * @return Returns the align.
     */
    public String getAlign()
    {
        return align;
    }

    /**
     * @param align
     *            The align to set.
     */
    public void setAlign(String align)
    {
        this.align = align;
    }

}
