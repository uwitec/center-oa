package com.china.center.common.taglib;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;


/**
 * 页面option
 * 
 * @author zhuzhu
 * @version 2007-3-14
 * @see PageSelectOption
 * @since
 */

public class PageSelectOption extends BodyTagCenterSupport
{
    private String type = "101";

    public static Map<String, List<MapBean>> optionMap = new HashMap<String, List<MapBean>>();

    /**
     * 默认构建器
     */
    public PageSelectOption()
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
        String line = "\r\n";

        List<MapBean> list = optionMap.get(type);

        if (list != null)
        {
            for (MapBean mapBean : list)
            {
                buffer.append("<option value=").append(mapBean.getKey()).append(">").append(
                    mapBean.getValue()).append("</option>").append(line);
            }
        }
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
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

}
