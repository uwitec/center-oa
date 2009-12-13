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
 * @see PageBodyCell
 * @since
 */

public class PageBodyCell extends BodyTagCenterSupport
{
    private int width = 15;

    private String align = "left";

    private String index = "";

    private String title = "";

    private String id = "";

    /**
     * 默认构建器
     */
    public PageBodyCell()
    {}

    public int getLastWidth()
    {
        int tatol = 100;
        Integer ints = (Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT
                                                         + this.index);

        int ll = tatol / ints.intValue() - this.width;

        return ll > 0 ? ll : 5;
    }

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        // HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        // 设置页面属性
        int allIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELL_INDEX
                                                          + this.index)).intValue();

        int cells = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT
                                                       + this.index)).intValue();

        int trIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_TRS_INDEX
                                                         + this.index)).intValue();

        // 需要折行了
        if (allIndex % cells == 0)
        {
            String clc = null;
            if (trIndex % 2 == 0)
            {
                clc = "content1";
            }
            else
            {
                clc = "content2";
            }

            buffer.append("<tr class='" + clc + "'>").append("\r\n");
        }

        writeStart(buffer);

        this.writeContext(buffer.toString());

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        int allIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELL_INDEX
                                                          + this.index)).intValue() + 1;

        int cells = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT
                                                       + this.index)).intValue();

        int trIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_TRS_INDEX
                                                         + this.index)).intValue();

        writeEnd(buffer);

        if (allIndex % cells == 0)
        {
            buffer.append("</tr>");

            pageContext.setAttribute(TagLibConstant.CENTER_TRS_INDEX + this.index, new Integer(
                trIndex + 1));
        }

        pageContext.setAttribute(TagLibConstant.CENTER_CELL_INDEX + this.index, new Integer(
            allIndex));

        this.writeContext(buffer.toString());

        return EVAL_PAGE;
    }

    private void writeStart(StringBuffer buffer)
    {
        String line = "\r\n";

        String temp = "";
        String temp1 = "";

        if (this.isNullOrNone(id))
        {
            temp = " id='" + id + "' ";
            temp1 = " id='" + id + "_SEC' ";
        }

        String ti = "";
        if ( !this.isNullOrNone(title))
        {
            ti = title + '：';
        }
        buffer.append(
            "<td width='" + width + "%' align='" + align + "'" + temp + ">" + ti + "</td>").append(
            line);

        buffer.append("<td width=" + getLastWidth() + "% " + temp1 + ">").append(line);

    }

    private void writeEnd(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("</td>").append(line);

    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getAlign()
    {
        return align;
    }

    public void setAlign(String align)
    {
        this.align = align;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

}
