package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;

import com.url.ajax.json.JSONObject;


/**
 * 页面option
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageDefined
 * @since
 */

public class PageDefined extends BodyTagCenterSupport
{
    /**
     * 默认构建器
     */
    public PageDefined()
    {}

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writeDiv(buffer);

        this.writeContext(buffer.toString());
        return EVAL_BODY_INCLUDE;
    }

    private void writeDiv(StringBuffer buffer)
    {
        JSONObject object = new JSONObject();

        object.createMapList(PageSelectOption.optionMap, false);
        
        buffer.append(object.toString());
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }

}
