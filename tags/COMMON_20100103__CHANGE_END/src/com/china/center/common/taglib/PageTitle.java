package com.china.center.common.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * 页面开始标签
 *
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageTitle
 * @since
 */

public class PageTitle extends BodyTagCenterSupport
{
    private String url = "";
    
    /**
     * 默认构建器
     */
    public PageTitle()
    {
    }
    
    public int doStartTag() throws JspException
    {
        //页面显示的字符
        StringBuffer buffer = new StringBuffer();
        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        this.url = request.getContextPath() + TagLibConstant.DEST_FOLDER_NAME;
        
        writePageStart(buffer);
        
        this.writeContext(buffer.toString());
        return EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws JspException
    {
        //页面显示的字符
        StringBuffer buffer = new StringBuffer();
        writePageEnd(buffer);
        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }
    
    private void writePageStart(StringBuffer buffer)
    {
        String line = "\r\n";
        
        buffer.append("<tr><td valign='top' colspan='2'>").append(line);
        buffer.append("<table width='100%' border='0' cellpadding='0' cellspacing='0'>")
                .append(line);
        buffer.append("<tr><td width='784' height='6'></td></tr><tr><td align='center' valign='top'>")
                .append(line);
        buffer.append("<div align='left'><table width='90%' border='0' cellspacing='2'>")
                .append(line);
        buffer.append("<tr><td><table width='100%' border='0' cellpadding='0' cellspacing='10'>")
                .append(line);
        buffer.append("<tr><td width='35'>&nbsp;</td><td width='6'>")
                .append(line);
        buffer.append("<img src='" + this.url
                + "dot_r.gif' width='6' height='6'></td>").append(line);
    }
    
    private void writePageEnd(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append("</tr></table></td></tr></table></div></td></tr></table></td></tr>")
                .append(line);
    }
}
