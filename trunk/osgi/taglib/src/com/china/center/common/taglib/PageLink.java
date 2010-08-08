package com.china.center.common.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageLink
 * @since
 */

public class PageLink extends BodyTagCenterSupport
{
    private String title = "";

    private boolean cal = true;

    private boolean link = true;

    private boolean guid = false;

    private boolean dialog = false;

    /**
     * 默认构建器
     */
    public PageLink()
    {}

    public int doStartTag()
        throws JspException
    {
        return 2;
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        writePageLine(buffer);

        this.writeContext(buffer.toString());
        return EVAL_PAGE;
    }

    private void writePageLine(StringBuffer buffer)
    {
        String line = "\r\n";

        buffer.append("<title>" + title + "</title>").append(line);

        // <base
        // href='${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.requestURI}'/>

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        String serverPort = String.valueOf(request.getServerPort());
        String requestURI = request.getRequestURI();

        buffer.append("<base href='" + scheme + "://" + serverName + ':');
        buffer.append(serverPort + requestURI + "' />").append(line);

        String cssStyle = (String)request.getSession().getAttribute(TagLibConstant.CSS_STYLE_LOADING);

        if (cssStyle == null || "".equals(cssStyle))
        {
            cssStyle = request.getContextPath() + TagLibConstant.CSS_FOLDER_NAME + TagLibConstant.CSS_FILE_NAME;
        }

        // <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />").append(line);

        // buffer.append("<!-- saved from url=(0028)http://www.center.china.com/ -->").append(line);

        if (this.guid)
        {
            String guidStyle = request.getContextPath() + TagLibConstant.CSS_FOLDER_NAME + "flexigrid/"
                               + TagLibConstant.CSS_GUID_NAME;

            buffer.append("<link href='" + guidStyle + "' type=text/css rel=stylesheet>").append(line);
        }

        if (this.link)
        {
            buffer.append("<link href='" + cssStyle + "' type=text/css rel=stylesheet>").append(line);
        }

        if (this.dialog)
        {
            buffer.append(
                "<link href='" + request.getContextPath()
                    + "/js/plugin/dialog/css/dialog.css' type=text/css rel=stylesheet>").append(line);
        }

        if (cal)
        {
            buffer.append(
                "<link rel=stylesheet type='text/css' href='" + request.getContextPath()
                    + TagLibConstant.JS_FOLDER_NAME + TagLibConstant.CAL_JS_FOLDER_NAME + "skins/aqua/theme.css'/>").append(
                line);

            buffer.append(
                "<script type=\"text/javascript\" src=\"" + request.getContextPath() + TagLibConstant.JS_FOLDER_NAME
                    + TagLibConstant.CAL_JS_FOLDER_NAME + "calendar.js\"></script>").append(line);
            // + "calendar_debug.js\"></script>").append(line);

            buffer.append(
                "<script type=\"text/javascript\" src=\"" + request.getContextPath() + TagLibConstant.JS_FOLDER_NAME
                    + TagLibConstant.CAL_JS_FOLDER_NAME + "lang/cn_utf8.js\"></script>").append(line);
        }

        String lang = (String)request.getSession().getAttribute(TagLibConstant.CENTER_GOBAL_LANG_SETTING);

        if (lang == null)
        {
            lang = "zh";
        }

        buffer.append(
            "<script type=\"text/javascript\" src=\"" + request.getContextPath() + TagLibConstant.JS_LANG_FOLDER_NAME
                + lang + "/" + TagLibConstant.JS_RESOURCES_NAME + "\"></script>").append(line);

        if (this.guid)
        {
            buffer.append(
                "<script type=\"text/javascript\" src=\"" + request.getContextPath() + TagLibConstant.JS_FOLDER_NAME
                    + TagLibConstant.JQUERY_JS_LANG_FOLDER_NAME + TagLibConstant.JS_QUERY_NAME + "\"></script>").append(
                line);
            // + "calendar_debug.js\"></script>").append(line);

            buffer.append(
                "<script type=\"text/javascript\" src=\"" + request.getContextPath() + TagLibConstant.JS_FOLDER_NAME
                    + TagLibConstant.JS_FLEXIGRID_NAME + "\"></script>").append(line);
        }

        if (this.dialog)
        {
            buffer.append(
                "<script type=\"text/javascript\" src='" + request.getContextPath() + "/js/json.js'></script>").append(
                line);
            buffer.append(
                "<script type=\"text/javascript\" src='" + request.getContextPath() + "/js/cnchina.js'></script>").append(
                line);
            buffer.append(
                "<script type=\"text/javascript\" src='" + request.getContextPath()
                    + "/js/plugin/dialog/jquery.dialog.js'></script>").append(line);
            buffer.append(
                "<script type=\"text/javascript\" src='" + request.getContextPath() + "/js/adapter.js'></script>").append(
                line);
        }

    }

    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the cal
     */
    public boolean isCal()
    {
        return cal;
    }

    /**
     * @param cal
     *            the cal to set
     */
    public void setCal(boolean cal)
    {
        this.cal = cal;
    }

    /**
     * @return the link
     */
    public boolean isLink()
    {
        return link;
    }

    /**
     * @param link
     *            the link to set
     */
    public void setLink(boolean link)
    {
        this.link = link;
    }

    /**
     * @return the guid
     */
    public boolean isGuid()
    {
        return guid;
    }

    /**
     * @param guid
     *            the guid to set
     */
    public void setGuid(boolean guid)
    {
        this.guid = guid;
    }

    /**
     * @return the dialog
     */
    public boolean isDialog()
    {
        return dialog;
    }

    /**
     * @param dialog
     *            the dialog to set
     */
    public void setDialog(boolean dialog)
    {
        this.dialog = dialog;
    }

}
