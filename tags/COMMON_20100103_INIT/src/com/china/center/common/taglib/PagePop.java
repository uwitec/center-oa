package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PagePop
 * @since
 */

public class PagePop extends BodyTagCenterSupport
{
    private String id = "pop";

    private String title = "";

    private int type = 0;

    /**
     * 默认构建器
     */
    public PagePop()
    {}

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

        // <div id="rejectReson" style="display:none">
        // <p align='left'><label><font color=""><b>请输入驳回原因</b></font></label></p>
        // <p><label>&nbsp;</label></p>
        // <textarea name="reason" value="" rows="4" oncheck="notNone;maxLength(100)" style="width: 85%"></textarea>
        // <p><label>&nbsp;</label></p>
        // <p>
        // <input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok1' class='button_class'
        // onclick='$process()'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        // <input type='button' id='div_b_cancle' value='&nbsp;&nbsp;关 闭&nbsp;&nbsp;' class='button_class'
        // onclick='$close()'/>
        // </p>
        // <p><label>&nbsp;</label></p>
        // </div>

        buffer.append("<div id='" + this.id + "' style='display:none'>").append(line);
        buffer.append(
            "<p align='left'><label><font color=''><b>" + this.title + "</b></font></label></p>").append(
            line);
        buffer.append("<p><label>&nbsp;</label></p>").append(line);
        buffer.append(
            "<p><textarea name='preason' id='preason' rows=4 oncheck='notNone;maxLength(150)' style='width: 85%'></textarea></p>").append(
            line);
        buffer.append("<p><label>&nbsp;</label></p>").append(line);
        buffer.append(
            "<p><input type='button' onclick='' style='display:none'/><input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok' class='button_class' onclick='$ok()'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(
            line);
        buffer.append(
            "<input type='button' id='div_b_cancle' value='&nbsp;&nbsp;取 消&nbsp;&nbsp;' class='button_class' onclick='$cancle()'/></p>").append(
            line);
        buffer.append("<p><label>&nbsp;</label></p></div>").append(line);
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

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }
}
