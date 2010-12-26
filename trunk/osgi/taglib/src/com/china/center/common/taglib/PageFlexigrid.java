package com.china.center.common.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;


/**
 * 页面开始标签
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageFlexigrid
 * @since
 */

public class PageFlexigrid extends BodyTagCenterSupport
{
    /**
     * 0:memory 1:common
     */
    private String queryMode = "";

    private String auth = "$auth()";

    private String height = "'page'";

    private String rp = "20";

    private String def = "allDef";

    private String cache = "0";

    private String showTableToggleBtn = "true";

    private String callBack = "$callBack";

    /**
     * 默认构建器
     */
    public PageFlexigrid()
    {
    }

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

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        if (isNullOrNone(this.queryMode))
        {
            // 从session里面获取
            Object attribute = request.getSession().getAttribute("g_queryMode");

            if (attribute != null)
            {
                this.queryMode = attribute.toString();
            }
            else
            {
                // 默认是记忆查询
                this.queryMode = "0";
            }
        }

        buffer.append("usepager: true,").append(line);
        buffer.append("useRp: true,").append(line);
        buffer.append("queryMode: " + queryMode + ",").append(line);
        buffer.append("cache: " + cache + ",").append(line);
        buffer.append("height: " + height + ",").append(line);
        buffer.append("rp: " + rp + ",").append(line);
        buffer.append("showTableToggleBtn: " + showTableToggleBtn + ",").append(line);
        buffer.append("auth: " + auth + ",").append(line);
        buffer.append("def: " + def + ",").append(line);
        buffer.append("callBack: " + callBack + " //for firefox load ext att").append(line);
    }

    /**
     * @return the queryMode
     */
    public String getQueryMode()
    {
        return queryMode;
    }

    /**
     * @param queryMode
     *            the queryMode to set
     */
    public void setQueryMode(String queryMode)
    {
        this.queryMode = queryMode;
    }

    /**
     * @return the auth
     */
    public String getAuth()
    {
        return auth;
    }

    /**
     * @param auth
     *            the auth to set
     */
    public void setAuth(String auth)
    {
        this.auth = auth;
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

    /**
     * @return the rp
     */
    public String getRp()
    {
        return rp;
    }

    /**
     * @param rp
     *            the rp to set
     */
    public void setRp(String rp)
    {
        this.rp = rp;
    }

    /**
     * @return the def
     */
    public String getDef()
    {
        return def;
    }

    /**
     * @param def
     *            the def to set
     */
    public void setDef(String def)
    {
        this.def = def;
    }

    /**
     * @return the callBack
     */
    public String getCallBack()
    {
        return callBack;
    }

    /**
     * @param callBack
     *            the callBack to set
     */
    public void setCallBack(String callBack)
    {
        this.callBack = callBack;
    }

    /**
     * @return the cache
     */
    public String getCache()
    {
        return cache;
    }

    /**
     * @param cache
     *            the cache to set
     */
    public void setCache(String cache)
    {
        this.cache = cache;
    }

    /**
     * @return the showTableToggleBtn
     */
    public String getShowTableToggleBtn()
    {
        return showTableToggleBtn;
    }

    /**
     * @param showTableToggleBtn
     *            the showTableToggleBtn to set
     */
    public void setShowTableToggleBtn(String showTableToggleBtn)
    {
        this.showTableToggleBtn = showTableToggleBtn;
    }

}
