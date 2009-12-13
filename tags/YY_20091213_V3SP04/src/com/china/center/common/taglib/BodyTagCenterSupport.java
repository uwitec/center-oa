/*
 * 文 件 名:  BodyTagCenterSupport.java
 * 版    权:  centerchina Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  admin
 * 修改时间:  2007-10-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
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
