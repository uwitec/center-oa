package com.china.center.common.taglib;


import java.io.IOException;

import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author admin
 * @version [版本号, 2007-10-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BodyTagCenterSupport extends BodyTagSupport
{
    protected String line = "\r\n";
    
    public void writeContext(String content)
    {
        try
        {
            this.pageContext.getOut().print(content);
        }
        catch (IOException e)
        {
            // e.printStackTrace();
        }
    }

    public boolean isNullOrNone(String name)
    {
        if (name == null || "".equals(name.trim()))
        {
            return true;
        }

        return false;
    }
}
