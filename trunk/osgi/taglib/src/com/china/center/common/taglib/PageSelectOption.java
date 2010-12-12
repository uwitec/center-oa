package com.china.center.common.taglib;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.china.center.tools.InnerReflect;
import com.china.center.tools.ReflectException;


/**
 * 页面option
 * 
 * @author ZHUZHU
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
    {
    }

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

        if (type.startsWith("[") && type.endsWith("]"))
        {
            String[] splits = type.substring(1, type.length() - 1).split(",");

            int begin = Integer.parseInt(splits[0].trim());
            int end = Integer.parseInt(splits[1].trim());
            for (int i = begin; i <= end; i++ )
            {
                buffer
                    .append("<option value=")
                    .append(i)
                    .append(">")
                    .append(i)
                    .append("</option>")
                    .append(line);
            }
        }
        else
        {
            List<MapBean> list = optionMap.get(type);

            if (list != null)
            {
                for (MapBean mapBean : list)
                {
                    buffer.append("<option value=").append(mapBean.getKey()).append(">").append(
                        mapBean.getValue()).append("</option>").append(line);
                }
            }
            else
            {
                // 从request里面获取
                HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

                Object attribute = request.getAttribute(type);

                if (attribute == null)
                {
                    attribute = request.getSession().getAttribute(type);
                }

                if (attribute != null)
                {
                    // 肯定是list
                    Collection optionList = (Collection)attribute;

                    for (Object object : optionList)
                    {
                        try
                        {
                            buffer
                                .append("<option value=")
                                .append(InnerReflect.get(object, "id"))
                                .append(">")
                                .append(InnerReflect.get(object, "name"))
                                .append("</option>")
                                .append(line);
                        }
                        catch (ReflectException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
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
