package com.china.center.common.taglib;


import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageCache
 * @since
 */

public class PageCache extends BodyTagCenterSupport
{
    /**
     * 默认构建器
     */
    public PageCache()
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

        buffer.append("<input type=hidden name=cacheEle id=cacheEle value=''>").append(line);
        buffer.append("<input type=hidden name=cacheEle id=cacheRadio value=''>").append(line);
        buffer.append("<input type=hidden name=cacheFlag id=cacheFlag value='0'>").append(line);
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
