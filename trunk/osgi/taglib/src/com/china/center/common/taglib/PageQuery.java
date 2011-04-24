package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageQuery
 * @since
 */

public class PageQuery extends BodyTagCenterSupport
{
    private String width = "500px";

    private String height = "200px";

    /**
     * 默认构建器
     */
    public PageQuery()
    {
    }

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writeCache(buffer);

        this.writeContext(buffer.toString());

        return EVAL_BODY_INCLUDE;
    }

    private void writeCache(StringBuffer buffer)
    {

        String line = "\r\n";

        // <input type=hidden name=cacheEle id=cacheEle value=''/>
        // <input type=hidden name=cacheFlag id=cacheFlag value="0"/>

        buffer.append("<div id='modalQuery' title='查询条件设置' style='width:" + width + ";'>").append(
            line);

        buffer
            .append("<div style='padding:20px;height:" + height + ";' id='dialog_inner' title=''/>")
            .append(line);

        buffer.append("</div>").append(line);
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
     * @return the width
     */
    public String getWidth()
    {
        return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(String width)
    {
        this.width = width;
    }

    /**
     * @return the height
     */
    public String getHeight()
    {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(String height)
    {
        this.height = height;
    }
}
