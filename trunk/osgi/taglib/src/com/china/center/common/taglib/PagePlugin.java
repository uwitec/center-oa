package com.china.center.common.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;


/**
 * 页面标准控件
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PagePlugin
 * @since
 */

public class PagePlugin extends BodyTagCenterSupport
{
    /**
     * 0:日期控件
     */
    private int type = 0;

    private String innerString = "";

    private String name = "";

    private String value = "";

    private String size = "";

    private String oncheck = "";

    /**
     * 默认构建器
     */
    public PagePlugin()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writeStart(buffer);

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
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        // String line = "\r\n";

        String special = " readonly=readonly ";

        String sname = " name = '" + name + "' ";
        String check = " oncheck = \"" + oncheck + "\" ";
        String id = " id = '" + name + "' ";
        String svalue = " value = '" + value + "' ";

        String ssize = "";
        if ( !isNullOrNone(size))
        {
            ssize = " size = '" + size + "' ";
        }

        buffer.append("<input type=text").append(sname).append(id).append(check).append(
            innerString).append(svalue).append(special);
        buffer.append(ssize).append(">");

        String timeType = null;
        if (this.type == 1)
        {
            timeType = "calDateTime";
        }
        else
        {
            timeType = "calDate";
        }

        buffer.append("<img src='"
                      + request.getContextPath()
                      + TagLibConstant.DEST_FOLDER_NAME
                      + "calendar.gif' style='cursor: pointer' title='请选择时间' align='top' onclick='return "
                      + timeType + "(\"" + name + "\");' height='20px' width='20px'/>");

    }

    private void writeEnd(StringBuffer buffer)
    {}

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @return the innerString
     */
    public String getInnerString()
    {
        return innerString;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @param innerString
     *            the innerString to set
     */
    public void setInnerString(String innerString)
    {
        this.innerString = innerString;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @return the size
     */
    public String getSize()
    {
        return size;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(String size)
    {
        this.size = size;
    }

    /**
     * @return the oncheck
     */
    public String getOncheck()
    {
        return oncheck;
    }

    /**
     * @param oncheck
     *            the oncheck to set
     */
    public void setOncheck(String oncheck)
    {
        this.oncheck = oncheck;
    }

}
