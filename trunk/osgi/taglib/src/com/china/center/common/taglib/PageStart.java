package com.china.center.common.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;


/**
 * 页面的导航标签
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageStart
 * @since
 */

public class PageStart extends BodyTagCenterSupport
{
    private String height = "22";

    private String url = "";

    /**
     * 默认构建器
     */
    public PageStart()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        this.url = request.getContextPath() + TagLibConstant.DEST_FOLDER_NAME;

        writePageStart(buffer);

        this.writeContext(buffer.toString());

        // this.writeContext(buffer.toString());
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writePageEnd(buffer);

        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }

    private void writePageStart(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("<table width='100%' border='0' cellpadding='0' cellspacing='0'>").append(
            line);

        buffer.append("<tr>").append(line);

        buffer.append("<td height='" + height + "' valign='bottom'>").append(line);

        buffer.append(
            "<table width='100%' height='" + height
                + "' border='0' cellpadding='0' cellspacing='0'>").append(line);

        buffer.append(
            "<tr valign='middle'><td width='8'></td><td width='30'><div align='center'>"
                + "<img src='" + this.url + "dot_a.gif' width='9' height='9'></div></td>").append(
            line);
    }

    private void writePageEnd(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("</tr></table></td></tr>").append(line);

        buffer.append(
            "<tr><td height='6' valign='top'><table width='100%' border='0' cellpadding='0' cellspacing='0'>  ").append(
            line);
        buffer.append("<tr><td width='8' height='6' background='" + this.url + "center_10.gif'>").append(
            line);
        buffer.append("<img src='" + this.url + "center_07.gif' width='8' height='6'></td>").append(
            line);

        buffer.append("<td width='190' background='" + this.url + "center_08.gif'></td>").append(
            line);

        buffer.append("<td width='486' background='" + this.url + "center_10.gif'></td>").append(
            line);

        buffer.append("<td align='right' background='" + this.url + "center_10.gif'>").append(line);

        buffer.append(
            "<div align='right'><img src='" + this.url
                + "center_12.gif' width='23' height='6'></div>").append(line);

        buffer.append("</td></tr></table></td></tr></table>").append(line);

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

}
