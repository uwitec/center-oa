package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * 页面table
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageBodyTable
 * @since
 */

public class PageBodyTable extends BodyTagCenterSupport
{
    private String width = "100%";

    private String tableClass = "table0";

    private int cells = 2;

    private String index = "";

    private String id = "default_table";

    private String clasz = "";

    /**
     * 默认构建器
     */
    public PageBodyTable()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writeStart(buffer);

        // HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        // 设置页面属性
        pageContext.setAttribute(TagLibConstant.CENTER_CELL_INDEX + this.index, new Integer(0));

        pageContext.setAttribute(TagLibConstant.CENTER_CELLS_INIT + this.index, new Integer(
            this.cells));

        pageContext.setAttribute(TagLibConstant.CENTER_TRS_INDEX + this.index, new Integer(0));

        // 设置class
        if ( !this.isNullOrNone(clasz))
        {
            pageContext.setAttribute(TagLibConstant.CENTER_BEAN_CLASS, clasz);
        }

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
        String line = "\r\n";

        if (isNullOrNone(id))
        {
            id = "default_table";
        }

        buffer.append(
            "<table align='center' width='" + width + "' cellpadding='0' id='" + id
                + "' cellspacing='1' class='" + tableClass + "'>").append(line);

    }

    private void writeEnd(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("</table>").append(line);

    }

    public String getTableClass()
    {
        return tableClass;
    }

    public void setTableClass(String tableClass)
    {
        this.tableClass = tableClass;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public int getCells()
    {
        return cells;
    }

    public void setCells(int cells)
    {
        this.cells = cells;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    /**
     * @return 返回 clasz
     */
    public String getClasz()
    {
        return clasz;
    }

    /**
     * @param 对clasz进行赋值
     */
    public void setClasz(String clasz)
    {
        this.clasz = clasz;
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

}
