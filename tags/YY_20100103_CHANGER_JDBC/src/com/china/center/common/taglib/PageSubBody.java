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
 * 页面开始标签
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
     * 默认构建器
     */
    public PageSubBody()
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
