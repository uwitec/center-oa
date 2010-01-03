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
 * 页面单元行
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageBodyCells
 * @since
 */
public class PageBodyCells extends BodyTagCenterSupport
{
    private int width = 15;

    private String align = "left";

    private String index = "";

    private String title = "";

    /**
     * 打通的CELL数 0:全部打通<br>
     */
    private int celspan = 1;

    private String id = "";

    /**
     * 默认构建器
     */
    public PageBodyCells()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        // HttpServletRequest request =
        // (HttpServletRequest)pageContext.getRequest();

        // CELL的索引值
        int allIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELL_INDEX
                                                          + this.index)).intValue();

        // 定义的每行CELL的数量
        int cells = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT
                                                       + this.index)).intValue();

        // TR的索引值
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

            String al = "";

            if (celspan == 0)
            {
                if ( !this.isNullOrNone(this.align))
                {
                    al = " align = '" + this.align + "' ";
                }
            }

            String idTR = id + "_TR";

            buffer.append("<tr id='" + idTR + "' class='" + clc + "' " + al + ">").append("\r\n");
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

        // 这里必须加上colspan 作为索引值

        int cells = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT
                                                       + this.index)).intValue();

        int add = this.celspan == 0 ? cells : this.celspan;

        int allIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELL_INDEX
                                                          + this.index)).intValue()
                       + add;

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

        int col = 0;

        int cells = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT
                                                       + this.index)).intValue();
        if (celspan == 0)
        {
            col = cells * 2;
        }
        else
        {
            col = this.celspan * 2 - 1;
        }

        String colspanString = " colspan = '" + col + "' ";

        if ( !this.isNullOrNone(id))
        {
            temp = " id='" + id + "' ";
            temp1 = " id='" + id + "_SEC' ";
        }

        if (celspan != 0)
        {
            buffer.append(
                "<td width='" + width + "%' align='" + align + "'" + temp + ">" + title + "：</td>").append(
                line);

            buffer.append("<td " + temp1 + colspanString + ">").append(line);
        }
        else
        {
            // 没有标题TD了
            buffer.append("<td " + temp + colspanString + ">").append(line);
        }

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

    public int getCelspan()
    {
        return celspan;
    }

    public void setCelspan(int celspan)
    {
        if (celspan > -1)
        {
            this.celspan = celspan;
        }
    }
}
