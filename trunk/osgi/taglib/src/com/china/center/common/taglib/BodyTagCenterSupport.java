package com.china.center.common.taglib;


import java.io.IOException;

import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * BodyTagCenterSupport
 * 
 * @author ZHUZHU
 * @version 2010-5-24
 * @see BodyTagCenterSupport
 * @since 1.0
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
