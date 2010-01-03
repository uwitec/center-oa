package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * ҳ�濪ʼ��ǩ
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageSubBody
 * @since
 */

public class PageSubBody extends BodyTagCenterSupport
{
    private String width = "85%";

    /**
     * @return Returns the width.
     */
    public String getWidth()
    {
        return width;
    }

    /**
     * @param width
     *            The width to set.
     */
    public void setWidth(String width)
    {
        this.width = width;
    }

    /**
     * Ĭ�Ϲ�����
     */
    public PageSubBody()
    {}

    public int doStartTag()
        throws JspException
    {
        // ҳ����ʾ���ַ�
        StringBuffer buffer = new StringBuffer();
        writePageBodyStart(buffer);
        this.writeContext(buffer.toString());
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        // ҳ����ʾ���ַ�
        StringBuffer buffer = new StringBuffer();
        writePageBodyEnd(buffer);
        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }

    private void writePageBodyStart(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("<tr><td align='center' colspan='2'>").append(line);
        buffer.append("<table width='").append(width).append(
            " border='0' cellpadding='0' cellspacing='0' class='table1'>").append(line);
        buffer.append("<tr><td>").append(line);
    }

    private void writePageBodyEnd(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("</td></tr></table>").append(line);
        buffer.append("</td></tr>").append(line);
    }
}